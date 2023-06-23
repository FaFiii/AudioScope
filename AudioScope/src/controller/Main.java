package controller;

import java.nio.ByteBuffer;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import acquisition.UsbConnection;
import acquisition.FileIO;
import acquisition.Time;
import model.Settings;

import org.usb4java.DeviceHandle;
import org.usb4java.LibUsbException;


public class Main {

	private volatile static int timeForOneSampleNs=0;
	private volatile static Thread usbThread;
	private volatile static LinkedBlockingQueue<ByteBuffer> rawDataQueue = new LinkedBlockingQueue<ByteBuffer>();
	private volatile static int timesReceived =0;
	//private volatile static boolean keyNotPressed = true;
	private volatile static JKeyListener keyListener = new JKeyListener();
	private volatile static JFrame frame =new JFrame();
	
	/**
	 * Main function !!!
	 * 
	 * @param args is ignored
	 */
	
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		//crate connection and read data
		Settings conf = new Settings(); 
		FileIO.readConfig(conf);
		conf.setInternalGainCH1(Util.sendByteGain(conf.getGainCH1()));
		conf.setInternalGainCH2(Util.sendByteGain(conf.getGainCH2()));
		
		byte[] periodArray = Util.devicePeriod(conf.getFrequency());
		conf.setMSBSampelperiode(periodArray[0]);
		conf.setLSBSampelperiode(periodArray[1]);
		byte[] ControllComand = Util.createControlCommand(conf.getInternalGainCH1(), conf.getInternalGainCH2(), conf.isResetCaputer(), conf.getMSBSampelperiode(), conf.getLSBSampelperiode());
		String timeString = Time.getMostAcurateTime(conf.getAtomicTimeServer(), conf.getAtomicTimePort(),conf.getTimeZone());
		try {
			// 
			timeForOneSampleNs =(int) Math.round((((double)1/conf.getFrequency())*(conf.getNUMOFBYTE()/4)*1000000));
//------------------------------------------------------------------------------------------------------------------------------------			
			//creates Thread with window that has focus for key KeyListener 
			final int finalSampleTime = conf.getSampleTime();
			Thread keyListenerThread = new Thread(){
				public void run(){
					if(finalSampleTime<=0) {
						keyListener.setKeyNotPressed(true);
					}else {
						keyListener.setKeyNotPressed(false);
					}
					if(keyListener.isKeyNotPressed()) {
						JPanel jPanel = new JPanel();
						JLabel label = new JLabel("Space to STOP!!");
						jPanel.add(label);
						frame.add(jPanel);
						frame.addKeyListener(keyListener);
						frame.setSize(200, 100);
						frame.setVisible(true);
					}
					//System.out.println("keyListenerThread end");//debug
				}
			};
//-------------------------------------------------------------------------------------------------------------------------------------------------
			//Thread that creates the timing for every read sends an interrupt to usbThread 
			Thread timingThread = new Thread(){
				public void run(){
					while(true) {
						try {
							Thread.sleep(0,timeForOneSampleNs);
						} catch (InterruptedException e) {
							//System.out.println("timingThread stoped");//debug
							return;
						}
						usbThread.interrupt();
					}
				}
			}; 
//-----------------------------------------------------------------------------------------------------------------------------------------------------------
			//gets an packet from USB device when it is interrupted 
			//raw data is added as complete packet to an LinkedBlockingQueue 
			final int finalNUMOFBYTE = conf.getNUMOFBYTE();
			final byte finalInEndpoint = conf.getInEndpoint();
			final DeviceHandle handle = UsbConnection.setupUSB(conf.getVendorID(), conf.getProductID(), conf.getmInterface());
			if(handle ==null) {
				return;
			}
			UsbConnection.sendConfigArray(ControllComand, handle, conf.getOutEndpoint());
			final Thread finalTimingThread =timingThread;
			usbThread = new Thread(){
				public void run(){
					ByteBuffer rawData; //= BufferUtils.allocateByteBuffer(finalNUMOFBYTE).order(ByteOrder.LITTLE_ENDIAN);
					//IntBuffer transferred = BufferUtils.allocateIntBuffer();
					//System.out.println(System.nanoTime());//debug timing
					while(((timesReceived*timeForOneSampleNs)< (finalSampleTime*1000))|| keyListener.isKeyNotPressed()) {
						//System.out.println(System.nanoTime());//debug timing
						rawData = UsbConnection.reciveData(handle, finalNUMOFBYTE, finalInEndpoint);
						//LibUsb.bulkTransfer(handle, finalInEndpoint, rawData,transferred, 10000);
						rawDataQueue.add(rawData);
						timesReceived ++;
						try {
							Thread.sleep(4000,0);
						}catch (InterruptedException e) {}
					}
					finalTimingThread.interrupt();
					//System.out.println(System.nanoTime());//debug timing
					//System.out.println(timesReceived);//*timeForOneSampleNs);//debug
					//System.out.println(timesReceived*finalNUMOFBYTE*timeForOneSampleNs);//debug
					//mainThread.interrupt();
				}
			};
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------
			//Starts all  threads
			keyListenerThread.start();
			usbThread.start();
			timingThread.start();
			boolean appendToFile =false;
			int FileLength=0;
			String wavPath = Util.createWAVFileName(conf.getPathToSaveFiel(),timeString);
			FileIO.createWAVFile(wavPath,(float)conf.getFrequency());
			//begins to save data from LinkedBlockingQueue in files 
			//while(!rawDataQueue.isEmpty()||((timesReceived*timeForOneSampleNs)< (conf.getSampleTime()*1000))){//(numberOfValues*conf.getNUMOFBYTE())*timeForOneSampleNs < (conf.getSampleTime()*1000) && (Runtime.getRuntime().maxMemory()-1000)>(numberOfValues*16)) {
			while(!rawDataQueue.isEmpty()||usbThread.isAlive()){
				if(!rawDataQueue.isEmpty()) {
					ByteBuffer rawData = rawDataQueue.poll();
					ByteBuffer rawData2 = rawData.duplicate();
					FileIO.appendWAVFile(wavPath, FileLength, rawData);
					double[][] dataArray =ProcessRecivedData.processToArray(rawData2.capacity(), rawData2, conf.getGainCH1(), conf.getGainCH2(), conf.getProbeAttenuationCH1(), conf.getProbeAttenuationCH2());
					appendToFile = FileIO.writeDataToFile3(conf.getPathToSaveFiel() ,dataArray ,timeString ,conf.getFrequency() ,conf.getGainCH1() ,conf.getGainCH2(),appendToFile);
					FileLength += conf.getNUMOFBYTE();
					//System.out.println("added : "+FileLength/conf.getNUMOFBYTE());//debug
				}else {
					Thread.sleep(2);
				}
			}
			
			//just making sure all threads are closed 
			//System.out.println("main thread end");//debug
			usbThread.stop();
			frame.dispose();
			keyListenerThread.stop();
			UsbConnection.terminateUSB(handle, conf.getmInterface());
		}catch(LibUsbException e1) {
			System.out.println("Unable to read data");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
