package controller;


public class Util {
	
	/**
	 * changes ending of file name / path to .wav
	 * @param path
	 * @return
	 */
	
	public static String createWAVFileName(String path,String Time ) {
		if(path.contains(".")) {
			return path.substring(0, path.lastIndexOf('.'))+Time.substring(10)+".wav" ;
		}
		return path+".wav";
	}
	
	/**
	 * turns frequency in (short) Period in an format that is understoud by the device 
	 * @param frequency
	 * @return device Period in arrays example 100 kHz == 457
	 */
	public static byte [] devicePeriod(double frequency) {
		double period= (double) (1/frequency);
		short devicePeriod = (short) (period/12.5e-9 -343);
		byte[] devicePeriod2 = new byte[2];
		devicePeriod2[0]= (byte) (devicePeriod>>8);
		devicePeriod2[1]= (byte) (devicePeriod);
		return devicePeriod2; 
	}
	
	
	
	/**
	 * calculates send byte gain form human readebel gain values  
	 * gainmapping = {0, 1, 2, 5, 10, 20, 50, 100, 0}; 
	 * byte[] gainToSet = { 8,2,3,1,2,3,4,5,6,7,7,7}
	 * 10-100x gain are created throu changing setting on probe 
	 * gain=0 => channel off
	 * 
	 * @param gain is the real gain setting 0,10...
	 * @return the byte that is used in the 7 byte witch control the device
	 */
	
	public static byte sendByteGain(int gain) {
		byte gainInternal=0x00;
		if (gain == 0) {
			gainInternal = 0x08;
		}else if (gain == 1 || gain == 10) {
			gainInternal = 0x02;
		}else if (gain == 2 || gain == 20) {
			gainInternal = 0x03;
		}else if (gain == 5) {
			gainInternal = 0x01;
		}else if (gain == 50) {
			gainInternal = 0x04;
		}else if (gain == 100) {
			gainInternal = 0x05;
		}else if (gain == 0) {
			gainInternal = 0x06;
		}else {
			gainInternal = 0x07;
		}
		return gainInternal;
	} 
	

	/**
	 *creats byte array that is send it is 7 byte long 
	 *test signal is not jet implented  
	 *TODO implement reset
	 * @author Fabian Fiebich
	 * @param gainCh1 gain of first channel
	 * @param gainCh2 gain of second channel
	 * @param reset not implemented 
	 * @param mSBSamplePeriod most significant byte of sample period  
	 * @param lSBSamplePeriod least significant byte of sample period
	 * @return 7 long byte array 
	 */
	public static byte[] createControlCommand(byte gainCh1,byte gainCh2,boolean reset, byte mSBSamplePeriod,byte lSBSamplePeriod){
		byte[] controlCommand = new byte[7];
		controlCommand[0]=gainCh1;
		controlCommand[1]=gainCh2;
		controlCommand[2]=(byte) 0x01;
		controlCommand[3]=(byte) 0x00; //if(reset){controlCommand[3] = (byte) 0x01;} else{controllComand[3] = (byte) 0x00;}
		controlCommand[4]=mSBSamplePeriod;
		controlCommand[5]=lSBSamplePeriod;
		controlCommand[6]=(byte) 0x00;
		return controlCommand;
	} 
	
}
