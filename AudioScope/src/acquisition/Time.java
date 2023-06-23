package acquisition;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;


public class Time {
	/**
	 * get system Time https://stackabuse.com/how-to-get-current-date-and-time-in-java/
	 * @return returns the time as string in format yyyy-MM-dd HH:mm:ss
	 */
	public static String getSystemTimeNDate() {
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return "System :"+formatter.format(date);
	}
	
	
	/**
	 * get time and date from time server on the internet 
	 * all possible servers: https://www.rgagnon.com/javadetails/java-0589.html
	 * 
	 * @param ATOMICTIME_SERVER adress to atomic clock server 
	 * @param ATOMICTIME_PORT port of atomic clock server
	 * @param pTimeZone
	 * @return GregorianCalendar object 
	 * @throws IOException
	 */

	  public final static GregorianCalendar getAtomicTime(final String ATOMICTIME_SERVER, final int ATOMICTIME_PORT, String pTimeZone) throws IOException{
	    BufferedReader in = null;
	    Socket conn = null;

	    try {
	       conn = new Socket(ATOMICTIME_SERVER, ATOMICTIME_PORT);

	       in = new BufferedReader
	         (new InputStreamReader(conn.getInputStream()));

	       String atomicTime;
	       while (true) {
	          if ( (atomicTime = in.readLine()).indexOf("*") > -1) {
	             break;
	          }
	       }
	       //System.out.println("DEBUG 1 : " + atomicTime);//debug
	       String[] fields = atomicTime.split(" ");
	       GregorianCalendar calendar = new GregorianCalendar();

	       String[] date = fields[1].split("-");
	       calendar.set(Calendar.YEAR, 2000 +  Integer.parseInt(date[0]));
	       calendar.set(Calendar.MONTH, Integer.parseInt(date[1])-1);
	       calendar.set(Calendar.DATE, Integer.parseInt(date[2]));

	       // deals with the timezone and the daylight-saving-time (you may need to adjust this)
	       // here i'm using "EST" for Eastern Standart Time (to support Daylight Saving Time)
	       TimeZone tz = TimeZone.getTimeZone(pTimeZone); // or .getDefault()
	       int gmt = (tz.getRawOffset() + tz.getDSTSavings()) / 3600000;
	       //System.out.println("DEBUG 2 : " + gmt);//debug

	       String[] time = fields[2].split(":");
	       calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time[0]) + gmt);
	       calendar.set(Calendar.MINUTE, Integer.parseInt(time[1]));
	       calendar.set(Calendar.SECOND, Integer.parseInt(time[2]));
	       return calendar;
	    }
	    catch (IOException e){
	       throw e;
	    }
	    finally {
	       if (in != null) { in.close();   }
	       if (conn != null) { conn.close();   }
	    }
	  }

	  /**
	   * if there is an internet connection returns the time from an time server
	   * else uses System time  
	   * @param ATOMICTIME_SERVER adress to atomic clock server 
	   * @param ATOMICTIME_PORT port of atomic clock server
	   * @param pTimeZone 
	   * @return String of time with the format yyyy-MM-dd HH:mm:ss
	   */
	  
	  public static String getMostAcurateTime(final String ATOMICTIME_SERVER, final int ATOMICTIME_PORT, String pTimeZone) {
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    //System.out.println(Arrays.toString(TimeZone.getAvailableIDs()));//get all available IDs
	    try {
			//System.out.println("Atomic time : " + sdf.format(Time.getAtomicTime(ATOMICTIME_SERVER , ATOMICTIME_PORT, pTimeZone).getTime()));
			return "Atomic time : " + sdf.format(Time.getAtomicTime(ATOMICTIME_SERVER , ATOMICTIME_PORT, pTimeZone).getTime());
		} catch (IOException e) {
			//System.out.println("System time : " + getSystemTimeNDate());
			//e.printStackTrace();
			return "System time : " +getSystemTimeNDate();
		}
	  }
	
	
}
