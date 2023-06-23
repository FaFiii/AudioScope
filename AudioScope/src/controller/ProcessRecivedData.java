package controller;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

public class ProcessRecivedData {
	
	/**
	 * turns raw bytebuffer data in short array
	 * @param byteBufferLeng = length of the ByteBuffer
	 * @param rawData = byte buffer tow byte are one short and CH1 CH2 alternate 
	 * @param gainCH1 = gainCH1 of controller (real) if gain=0 there is no ADC correction
	 * @param gainCH2 = gainCH2 of controller (real) if gain=0 there is no ADC correction
	 * @param probe= Attenuation setting of the probe
	 * @return double [][] array where ch1 is double[0][x] and CH2 is double[1][x]
	 */
	
	public static double[][] processToArray(int byteBufferLeng, ByteBuffer rawData, int gainCH1,int gainCH2,int probeCH1, int probeCH2) {
		double[][] dataArray = new double[2][byteBufferLeng/4]; 
		int i =0;
		//int j=4096; just a reminder that there are 4096 byte in one pakeg
		rawData.order( ByteOrder.LITTLE_ENDIAN );
		ShortBuffer sb = rawData.asShortBuffer( );
		short [] shortArray = new short[byteBufferLeng/2]; 
		sb.get(shortArray);
		int k=0;
		while(i<(byteBufferLeng/2)) {
			if(k==0) {
				double voltage = ProcessRecivedData.computeRawData(shortArray[i],gainCH1,probeCH1);
				//short temp = ((short) (((rawData.get(i+1)& 0xff) << 8) | (rawData.get(i)& 0xff)));
				//System.out.print("CH1:"+rawData.get(i+1)+""+rawData.get(i)+"="+ temp +"="+voltage+" "); //debug
				//System.out.print(voltage +" ");
				dataArray[0][i/2]= voltage;
				k++;
			}else {
				double voltage = ProcessRecivedData.computeRawData(shortArray[i],gainCH2,probeCH2);
				//short temp = ((short) (((rawData.get(i+1)& 0xff) << 8) | (rawData.get(i)& 0xff)));
				//System.out.print("CH2:"+rawData.get(i+1)+""+rawData.get(i)+"="+ temp +"="+voltage+" "); //debug
				//System.out.print(voltage +" ");
				dataArray[1][i/2]= voltage;
				k=0;	
			}
			i++;
		}
		//System.out.println(""); //debug
		return dataArray;
	}
	
	/**
	 * typecast two byte in short and adds correction factor 
	 * @param data ADC 16 bit output
	 * @param gain = gain of controller (real) if gain=0 there is no ADC correction 
	 * @param probe= Attenuation setting of the probe 
	 * @return corrected double
	 * 
	 * @author Fabian Fiebich
	 * 
	 */
	
	public static double computeRawData(short data,int gain,int probe) {
		if(gain != 0) {
			//double factor=(2*5*probe) /(double)(gain*65536);
			double factor=(5*probe) /(double)(gain*65536);
			return (double) (data*factor);
		}else {
			return (double) data;
		}
	}
	
}
