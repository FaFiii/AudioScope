package acquisition;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Scanner; // Import the Scanner class to read text files

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import model.Settings;
/**
 * @author Fabian Fiebich
 *
 */
public class FileIO {
	
	/**
	 * creates .wav file and Header with dummy size  
	 * https://docs.oracle.com/javase/8/docs/technotes/guides/sound/programmer_guide/chapter7.html#a114602
	 * @throws IOException 
	 * @throws UnsupportedAudioFileException 
	 * 
	 */
	public static void createWAVFile(String path,float sampleRate) throws UnsupportedAudioFileException, IOException{
		long length =1024;
		byte[] dataArray = new byte[(int)length];
		ByteArrayInputStream byteStream = new ByteArrayInputStream(dataArray);
		AudioFormat audioFormat = new AudioFormat(sampleRate, 16, 2, true,false);
		AudioInputStream audioInputStream = new AudioInputStream(byteStream , audioFormat, length);
		AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, new FileOutputStream(path));
	}
	
	/**
	 * appends ByteBuffer to the end of he wav file and changes exsiting header at 4 and 40 with new data size
	 * @param path
	 * @param oldLength
	 * @param rawData
	 * @throws IOException
	 */
	
	public static void appendWAVFile(String path,float oldLength, ByteBuffer rawData) throws IOException {
		rawData.order( ByteOrder.LITTLE_ENDIAN );
		byte[] dataArray = new byte[rawData.capacity()];
		rawData.get(dataArray);
		File wavFile =new File(path);
		RandomAccessFile accessWavFile = new RandomAccessFile(wavFile, "rw");
		accessWavFile.seek((long)(44+oldLength));
		accessWavFile.write(dataArray);
		//Data size -8 at byte 4
		accessWavFile.seek(4);
		byte[] newLenghtByte = ByteBuffer.allocate(4).putInt((int)((oldLength+dataArray.length)+36)).array();
		accessWavFile.write(newLenghtByte);
		//Data size -44 at byte 40
		accessWavFile.seek(40);
		newLenghtByte = ByteBuffer.allocate(4).putInt((int)((oldLength+dataArray.length))).array();
		accessWavFile.write(newLenghtByte);
		accessWavFile.close();
	}
	
	
	/**
	 * creates .wav file  
	 * https://docs.oracle.com/javase/8/docs/technotes/guides/sound/programmer_guide/chapter7.html#a114602
	 * @throws IOException 
	 * @throws UnsupportedAudioFileException 
	 * 
	 */
	public static void writeDataToWAVFile(ByteBuffer rawData ,String path, long length,float sampleRate) throws UnsupportedAudioFileException, IOException{ //,float sampleRate, int sampleSizeInBits, int channels, int frameSize, float frameRate, boolean bigEndian) throws UnsupportedAudioFileException, IOException {
		rawData.order( ByteOrder.LITTLE_ENDIAN );
		byte[] dataArray = new byte[4096];
		rawData.get(dataArray);
		ByteArrayInputStream byteStream = new ByteArrayInputStream(dataArray);
		//AudioInputStream audioStream = AudioSystem.getAudioInputStream(byteStream);
		AudioFormat audioFormat = new AudioFormat(sampleRate, 16, 2, true,false);
		AudioInputStream audioInputStream = new AudioInputStream(byteStream , audioFormat, length);
		AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, new FileOutputStream("Save.wav"));
		//AudioFileFormat.Type.WAVE
		//WaveHeader wh = new WaveHeader(WaveHeader.FORMAT_PCM, (short)2, samplerate, (short)16, (int)(size-44) );
		//AudioFormat audioFormat = new AudioFormat(AudioFormat.Encoding.PCM, sampleRate, sampleSizeInBits, channels, frameSize, frameRate, bigEndian);
		//AudioInputStream audioInputStream = new AudioInputStream(rawData, AudioFormat, long length);
		//File fileOut = new File(path);
		//AudioFileFormat.Type fileType = fileFormat.getType();
		//if (AudioSystem.isFileTypeSupported(fileType,audioInputStream)) {
		  //AudioSystem.write(audioInputStream, fileType, fileOut);
		//}
	}

	
	/**
	 * creates file data in double second version witch uses more ram but will macke less errors  
	 * 
	 * @param path where and what name created file should have
	 * @param data double [][] array where ch1 is double[0][x] and CH2 is double[1][x]
	 * @param Time the time as Sting 
	 * @param sampleRate 
	 * @param gain1 
	 * @param gain2 
	 * @param appendToFile if the file has to be created or just appended 
	 * @return true after the file has been created
	 * @throws IOException
	 */
	public static boolean writeDataToFile3(String path, double[][] data, String Time, double sampleRate, int gain1,int gain2, boolean appendToFile) throws IOException {
		String header = new String("Frequenz :"+ sampleRate+ " Hz  Gain CH1:"+gain1+"  Gain CH2:"+gain2+" "+ Time+" \n");
		File file;
		if(path.contains(".")) {
			file =new File(path.substring(0, path.lastIndexOf('.'))+Time.substring(10)+path.substring(path.lastIndexOf('.'),path.length() ));
		}else {
			file =new File(path+Time.substring(10));
		}
		FileWriter fileWriter = new FileWriter(file,true);
		if(!appendToFile) {
			fileWriter.write(header);
		}
		int i=0;
		while(i<data[0].length) {
			fileWriter.append(data[0][i]+", 	"+data[1][i]+"\n");	
			i++;
		}
		fileWriter.close();
		return true;
	}
	
	
	
	
	
	/**
	 * creates file data in double second version witch uses more ram but will macke less errors  
	 * 
	 * @param path where and what name created file should have
	 * @param data double [][] array where ch1 is double[0][x] and CH2 is double[1][x]
	 * @param Time the time as Sting 
	 * @param sampleRate 
	 * @param gain1 
	 * @param gain2 
	 * @param appendToFile if the file has to be created or just appended 
	 * @return true after the file has been created
	 * @throws IOException
	 */
	public static boolean writeDataToFile2(String path, double[][] data, String Time, double sampleRate, int gain1,int gain2, boolean appendToFile) throws IOException {
		String header = new String("Frequenz :"+ sampleRate+ " Hz Gain CH1:"+gain1+"Gain CH1:"+gain2+" "+ Time+" \n");
		StringBuilder headerLineSB = new StringBuilder();
		StringBuilder Ch1LineSB = new StringBuilder();
		StringBuilder Ch2LineSB = new StringBuilder();
		File file =new File(path);
		if(file.exists() && !appendToFile) {
			file.delete();
			file.createNewFile();
		}
		RandomAccessFile accessFile = new RandomAccessFile(file, "rw");
		if(!appendToFile) {
			FileWriter fileWriter = new FileWriter(file);
			fileWriter.write(header);
			fileWriter.write("\n");
			fileWriter.write(" ");
			fileWriter.flush();
			fileWriter.close();
		}else {
			accessFile.seek(1);
			headerLineSB = new StringBuilder(accessFile.readLine());
			Ch1LineSB = new StringBuilder(accessFile.readLine());
			Ch2LineSB = new StringBuilder(accessFile.readLine());
		}
		/*if(Ch1LineSB.indexOf("\n")!=-1) {
			Ch1LineSB.deleteCharAt(Ch1LineSB.indexOf("\n"));
		}*/
		int i=0;
		while(i<data[0].length) {
			Ch1LineSB.append(data[0][i]+", 	");	
			i++;
		}
		Ch1LineSB.append("\n");
		i=0;
		while(i<data[1].length) {
			Ch2LineSB.append(data[1][i]+", 	");	
			i++;
		}
		//accessFile.seek(0);
		//accessFile.writeChars(headerLineSB.toString()+Ch1LineSB.toString()+"\n"+Ch2LineSB.toString());
		FileWriter fileWriter = new  FileWriter(file);
		fileWriter.write(headerLineSB.toString()+Ch1LineSB.toString()+"\n"+Ch2LineSB.toString());
		accessFile.close();
		fileWriter.close();
		return true;
	}
	
	/**
	 * creates file with all meassurments   
	 * 
	 * @param path where and what name created file should have
	 * @param data double [][] array where ch1 is double[0][x] and CH2 is double[1][x]
	 * @param Time the time as Sting 
	 * @param sampleRate 
	 * @param gain1 
	 * @param gain2 
	 * @param appendToFile if the file has to be created or just appended 
	 * @return true after the file has been created
	 * @throws IOException
	 */
	public static boolean writeDataToFile(String path, double[][] data, String Time, double sampleRate, int gain1,int gain2, boolean appendToFile) throws IOException {
		File file =new File(path);
		if(file.exists() && !appendToFile) {
			file.delete();
			file.createNewFile();
		}
		RandomAccessFile accessFile = new RandomAccessFile(file, "rw");
		//FileWriter fileWriter = new FileWriter(file);
		//PrintWriter printWriter = new PrintWriter(fileWriter);
		String header = new String("Frequenz :"+ sampleRate+ " Hz Gain CH1:"+gain1+"Gain CH1:"+gain2+" "+ Time+"\n");
		int datalen = data[1].length;
		int i=0;
		accessFile.seek(0);
		if (!appendToFile) {
			accessFile.writeChars(header);
		}else {
			accessFile.seek((long)((accessFile.length()-header.length())/2)+header.length());
		}
		i=0;
		while(i<datalen) {
			accessFile.writeChars(data[0][i]+", 	");	
			//System.out.print(data[0][i]+" ");//debug
			//printWriter.print(data[0][i]+", 	");
			i++;
		}
		if (!appendToFile) {
			accessFile.writeChars("\n");
			//printWriter.print("\n");
		}else {
			accessFile.seek((long)(accessFile.length()));
		}
		i=0;

		while(i<datalen) {
			accessFile.writeChars(data[1][i]+", 	");
			//System.out.print(data[1][i]+" ");//debug
			//printWriter.print(data[1][i]+", 	");
			i++;
		}
		accessFile.close();
		//printWriter.close();
		return true;
	}
	
	
	/**
	 * creates digitalisierer.cfg whit default settings 
	 * @author Fabian Fiebich
	 * @throws IOException
	 */
	public static void genConfFile() throws IOException {
		File file =new File("digitalisierer.cfg");
		FileWriter fileWriter = new FileWriter(file);
		PrintWriter printWriter = new PrintWriter(fileWriter);
		printWriter.print("path to save fiel (string)=Save.txt\n");
		printWriter.print("gainCH1 {0, 1, 2, 5, 10, 20, 50, 100, 0}=1\n");
		printWriter.print("gainCH2 {0, 1, 2, 5, 10, 20, 50, 100, 0}=1\n");
		printWriter.print("ProbeAttenuationCH1 {1, 10}=1\n");
		printWriter.print("ProbeAttenuationCH2 {1, 10}=1\n");
		printWriter.print("reset caputer device{0,1} =0\n");
		printWriter.print("frequency MAX 100000 Hz/ MIN 1220 Hz =100000\n");
		printWriter.print("Sample Time in ms =1024\n");
		printWriter.print("The vendor ID of Device 0x04d8=0x04d8\n");
		printWriter.print("The product ID of Device 0x0053=0x0053\n");
		printWriter.print("The ADB interface number of the Device=0x00\n");
		printWriter.print("The ADB input endpoint of the Device 0x81=0x81\n");
		printWriter.print("The ADB output endpoint of the Device=0x01\n");
		printWriter.print("Time get posible servers from http://tf.nist.gov/tf-cgi/servers.cgi=129.6.15.30\n");
		printWriter.print("atomicTimePort =13\n");
		printWriter.print("timeZone =Europe/Berlin\n");
		printWriter.print("/*All possible time zone IDs gotten through System.out.println(Arrays.toString(ControllComand))\n");
		printWriter.print(" *Africa/Abidjan, Africa/Accra, Africa/Addis_Ababa, Africa/Algiers, Africa/Asmara, Africa/Asmera, Africa/Bamako, Africa/Bangui, Africa/Banjul, Africa/Bissau, Africa/Blantyre, Africa/Brazzaville, Africa/Bujumbura, Africa/Cairo, Africa/Casablanca, Africa/Ceuta, Africa/Conakry, Africa/Dakar, Africa/Dar_es_Salaam, Africa/Djibouti, Africa/Douala, Africa/El_Aaiun, Africa/Freetown, Africa/Gaborone, Africa/Harare, Africa/Johannesburg, Africa/Juba, Africa/Kampala, Africa/Khartoum, Africa/Kigali, Africa/Kinshasa, Africa/Lagos, Africa/Libreville, Africa/Lome, Africa/Luanda, Africa/Lubumbashi, Africa/Lusaka, Africa/Malabo, Africa/Maputo, Africa/Maseru, Africa/Mbabane, Africa/Mogadishu, Africa/Monrovia, Africa/Nairobi, Africa/Ndjamena, Africa/Niamey, Africa/Nouakchott, Africa/Ouagadougou, Africa/Porto-Novo, Africa/Sao_Tome, Africa/Timbuktu, Africa/Tripoli, Africa/Tunis, Africa/Windhoek,\n");
		printWriter.print(" *America/Adak, America/Anchorage, America/Anguilla, America/Antigua, America/Araguaina, America/Argentina/Buenos_Aires, America/Argentina/Catamarca, America/Argentina/ComodRivadavia, America/Argentina/Cordoba, America/Argentina/Jujuy, America/Argentina/La_Rioja, America/Argentina/Mendoza, America/Argentina/Rio_Gallegos, America/Argentina/Salta, America/Argentina/San_Juan, America/Argentina/San_Luis, America/Argentina/Tucuman, America/Argentina/Ushuaia, America/Aruba, America/Asuncion, America/Atikokan, America/Atka, America/Bahia, America/Bahia_Banderas, America/Barbados, America/Belem, America/Belize, America/Blanc-Sablon, America/Boa_Vista, America/Bogota, America/Boise, America/Buenos_Aires, America/Cambridge_Bay, America/Campo_Grande, America/Cancun, America/Caracas, America/Catamarca, America/Cayenne, America/Cayman, America/Chicago, America/Chihuahua, America/Coral_Harbour, America/Cordoba, America/Costa_Rica, America/Creston, America/Cuiaba, America/Curacao, America/Danmarkshavn, America/Dawson, America/Dawson_Creek, America/Denver, America/Detroit, America/Dominica, America/Edmonton, America/Eirunepe, America/El_Salvador, America/Ensenada, America/Fort_Nelson, America/Fort_Wayne, America/Fortaleza, America/Glace_Bay, America/Godthab, America/Goose_Bay, America/Grand_Turk, America/Grenada, America/Guadeloupe, America/Guatemala, America/Guayaquil, America/Guyana, America/Halifax, America/Havana, America/Hermosillo, America/Indiana/Indianapolis, America/Indiana/Knox, America/Indiana/Marengo, America/Indiana/Petersburg, America/Indiana/Tell_City, America/Indiana/Vevay, America/Indiana/Vincennes, America/Indiana/Winamac, America/Indianapolis, America/Inuvik, America/Iqaluit, America/Jamaica, America/Jujuy, America/Juneau, America/Kentucky/Louisville, America/Kentucky/Monticello, America/Knox_IN, America/Kralendijk, America/La_Paz, America/Lima, America/Los_Angeles, America/Louisville, America/Lower_Princes, America/Maceio, America/Managua, America/Manaus, America/Marigot, America/Martinique, America/Matamoros, America/Mazatlan, America/Mendoza, America/Menominee, America/Merida, America/Metlakatla, America/Mexico_City, America/Miquelon, America/Moncton, America/Monterrey, America/Montevideo, America/Montreal, America/Montserrat, America/Nassau, America/New_York, America/Nipigon, America/Nome, America/Noronha, America/North_Dakota/Beulah, America/North_Dakota/Center, America/North_Dakota/New_Salem, America/Nuuk, America/Ojinaga, America/Panama, America/Pangnirtung, America/Paramaribo, America/Phoenix, America/Port-au-Prince, America/Port_of_Spain, America/Porto_Acre, America/Porto_Velho, America/Puerto_Rico, America/Punta_Arenas, America/Rainy_River, America/Rankin_Inlet, America/Recife, America/Regina, America/Resolute, America/Rio_Branco, America/Rosario, America/Santa_Isabel, America/Santarem, America/Santiago, America/Santo_Domingo, America/Sao_Paulo, America/Scoresbysund, America/Shiprock, America/Sitka, America/St_Barthelemy, America/St_Johns, America/St_Kitts, America/St_Lucia, America/St_Thomas, America/St_Vincent, America/Swift_Current, America/Tegucigalpa, America/Thule, America/Thunder_Bay, America/Tijuana, America/Toronto, America/Tortola, America/Vancouver, America/Virgin, America/Whitehorse, America/Winnipeg, America/Yakutat, America/Yellowknife,\n");
		printWriter.print(" *Antarctica/Casey, Antarctica/Davis, Antarctica/DumontDUrville, Antarctica/Macquarie, Antarctica/Mawson, Antarctica/McMurdo, Antarctica/Palmer, Antarctica/Rothera, Antarctica/South_Pole, Antarctica/Syowa, Antarctica/Troll, Antarctica/Vostok, Arctic/Longyearbyen,\n");
		printWriter.print(" *Asia/Aden, Asia/Almaty, Asia/Amman, Asia/Anadyr, Asia/Aqtau, Asia/Aqtobe, Asia/Ashgabat, Asia/Ashkhabad, Asia/Atyrau, Asia/Baghdad, Asia/Bahrain, Asia/Baku, Asia/Bangkok, Asia/Barnaul, Asia/Beirut, Asia/Bishkek, Asia/Brunei, Asia/Calcutta, Asia/Chita, Asia/Choibalsan, Asia/Chongqing, Asia/Chungking, Asia/Colombo, Asia/Dacca, Asia/Damascus, Asia/Dhaka, Asia/Dili, Asia/Dubai, Asia/Dushanbe, Asia/Famagusta, Asia/Gaza, Asia/Harbin, Asia/Hebron, Asia/Ho_Chi_Minh, Asia/Hong_Kong, Asia/Hovd, Asia/Irkutsk, Asia/Istanbul, Asia/Jakarta, Asia/Jayapura, Asia/Jerusalem, Asia/Kabul, Asia/Kamchatka, Asia/Karachi, Asia/Kashgar, Asia/Kathmandu, Asia/Katmandu, Asia/Khandyga, Asia/Kolkata, Asia/Krasnoyarsk, Asia/Kuala_Lumpur, Asia/Kuching, Asia/Kuwait, Asia/Macao, Asia/Macau, Asia/Magadan, Asia/Makassar, Asia/Manila, Asia/Muscat, Asia/Nicosia, Asia/Novokuznetsk, Asia/Novosibirsk, Asia/Omsk, Asia/Oral, Asia/Phnom_Penh, Asia/Pontianak, Asia/Pyongyang, Asia/Qatar, Asia/Qostanay, Asia/Qyzylorda, Asia/Rangoon, Asia/Riyadh, Asia/Saigon, Asia/Sakhalin, Asia/Samarkand, Asia/Seoul, Asia/Shanghai, Asia/Singapore, Asia/Srednekolymsk, Asia/Taipei, Asia/Tashkent, Asia/Tbilisi, Asia/Tehran, Asia/Tel_Aviv, Asia/Thimbu, Asia/Thimphu, Asia/Tokyo, Asia/Tomsk, Asia/Ujung_Pandang, Asia/Ulaanbaatar, Asia/Ulan_Bator, Asia/Urumqi, Asia/Ust-Nera, Asia/Vientiane, Asia/Vladivostok, Asia/Yakutsk, Asia/Yangon, Asia/Yekaterinburg, Asia/Yerevan,\n");
		printWriter.print(" *Atlantic/Azores, Atlantic/Bermuda, Atlantic/Canary, Atlantic/Cape_Verde, Atlantic/Faeroe, Atlantic/Faroe, Atlantic/Jan_Mayen, Atlantic/Madeira, Atlantic/Reykjavik, Atlantic/South_Georgia, Atlantic/St_Helena, Atlantic/Stanley,\n");
		printWriter.print(" *Australia/ACT, Australia/Adelaide, Australia/Brisbane, Australia/Broken_Hill, Australia/Canberra, Australia/Currie, Australia/Darwin, Australia/Eucla, Australia/Hobart, Australia/LHI, Australia/Lindeman, Australia/Lord_Howe, Australia/Melbourne, Australia/NSW, Australia/North, Australia/Perth, Australia/Queensland, Australia/South, Australia/Sydney, Australia/Tasmania, Australia/Victoria, Australia/West, Australia/Yancowinna,\n");
		printWriter.print(" *Brazil/Acre, Brazil/DeNoronha, Brazil/East, Brazil/West,\n");
		printWriter.print(" *CET, CST6CDT, Canada/Atlantic,\n");
		printWriter.print(" *Canada/Central, Canada/Eastern, Canada/Mountain, Canada/Newfoundland, Canada/Pacific, Canada/Saskatchewan, Canada/Yukon,\n");
		printWriter.print(" *Chile/Continental, Chile/EasterIsland, Cuba, EET, EST5EDT, Egypt, Eire,\n");
		printWriter.print(" *Etc/GMT, Etc/GMT+0, Etc/GMT+1, Etc/GMT+10, Etc/GMT+11, Etc/GMT+12, Etc/GMT+2, Etc/GMT+3, Etc/GMT+4, Etc/GMT+5, Etc/GMT+6, Etc/GMT+7, Etc/GMT+8, Etc/GMT+9, Etc/GMT-0, Etc/GMT-1, Etc/GMT-10, Etc/GMT-11, Etc/GMT-12, Etc/GMT-13, Etc/GMT-14, Etc/GMT-2, Etc/GMT-3, Etc/GMT-4, Etc/GMT-5, Etc/GMT-6, Etc/GMT-7, Etc/GMT-8, Etc/GMT-9, Etc/GMT0, Etc/Greenwich, Etc/UCT, Etc/UTC, Etc/Universal, Etc/Zulu,\n");
		printWriter.print(" *Europe/Amsterdam, Europe/Andorra, Europe/Astrakhan, Europe/Athens, Europe/Belfast, Europe/Belgrade, Europe/Berlin, Europe/Bratislava, Europe/Brussels, Europe/Bucharest, Europe/Budapest, Europe/Busingen, Europe/Chisinau, Europe/Copenhagen, Europe/Dublin, Europe/Gibraltar, Europe/Guernsey, Europe/Helsinki, Europe/Isle_of_Man, Europe/Istanbul, Europe/Jersey, Europe/Kaliningrad, Europe/Kiev, Europe/Kirov, Europe/Lisbon, Europe/Ljubljana, Europe/London, Europe/Luxembourg, Europe/Madrid, Europe/Malta, Europe/Mariehamn, Europe/Minsk, Europe/Monaco, Europe/Moscow, Europe/Nicosia, Europe/Oslo, Europe/Paris, Europe/Podgorica, Europe/Prague, Europe/Riga, Europe/Rome, Europe/Samara, Europe/San_Marino, Europe/Sarajevo, Europe/Saratov, Europe/Simferopol, Europe/Skopje, Europe/Sofia, Europe/Stockholm, Europe/Tallinn, Europe/Tirane, Europe/Tiraspol, Europe/Ulyanovsk, Europe/Uzhgorod, Europe/Vaduz, Europe/Vatican, Europe/Vienna, Europe/Vilnius, Europe/Volgograd, Europe/Warsaw, Europe/Zagreb, Europe/Zaporozhye, Europe/Zurich,\n");
		printWriter.print(" *GB, GB-Eire, GMT, GMT0, Greenwich, Hongkong, Iceland,\n");
		printWriter.print(" *Indian/Antananarivo, Indian/Chagos, Indian/Christmas, Indian/Cocos, Indian/Comoro, Indian/Kerguelen, Indian/Mahe, Indian/Maldives, Indian/Mauritius, Indian/Mayotte, Indian/Reunion, Iran, Israel,\n");
		printWriter.print(" *Jamaica, Japan, Kwajalein, Libya, MET, MST7MDT,\n");
		printWriter.print(" *Mexico/BajaNorte, Mexico/BajaSur, Mexico/General,\n");
		printWriter.print(" *NZ, NZ-CHAT, Navajo, PRC, PST8PDT, Pacific/Apia,\n");
		printWriter.print(" *Pacific/Auckland, Pacific/Bougainville, Pacific/Chatham, Pacific/Chuuk, Pacific/Easter, Pacific/Efate, Pacific/Enderbury, Pacific/Fakaofo, Pacific/Fiji, Pacific/Funafuti, Pacific/Galapagos, Pacific/Gambier, Pacific/Guadalcanal, Pacific/Guam, Pacific/Honolulu, Pacific/Johnston, Pacific/Kiritimati, Pacific/Kosrae, Pacific/Kwajalein, Pacific/Majuro, Pacific/Marquesas, Pacific/Midway, Pacific/Nauru, Pacific/Niue, Pacific/Norfolk, Pacific/Noumea, Pacific/Pago_Pago, Pacific/Palau, Pacific/Pitcairn, Pacific/Pohnpei, Pacific/Ponape, Pacific/Port_Moresby, Pacific/Rarotonga, Pacific/Saipan, Pacific/Samoa, Pacific/Tahiti, Pacific/Tarawa, Pacific/Tongatapu, Pacific/Truk, Pacific/Wake, Pacific/Wallis, Pacific/Yap, Poland, Portugal,\n");
		printWriter.print(" *ROK, Singapore, SystemV/AST4, SystemV/AST4ADT, SystemV/CST6, SystemV/CST6CDT, SystemV/EST5, SystemV/EST5EDT, SystemV/HST10, SystemV/MST7, SystemV/MST7MDT, SystemV/PST8, SystemV/PST8PDT, SystemV/YST9, SystemV/YST9YDT,\n");
		printWriter.print(" *Turkey, UCT, US/Alaska, US/Aleutian, US/Arizona, US/Central, US/East-Indiana, US/Eastern, US/Hawaii, US/Indiana-Starke, US/Michigan, US/Mountain, US/Pacific, US/Samoa,\n");
		printWriter.print(" */UTC, Universal, W-SU, WET, Zulu, EST, HST, MST, ACT, AET, AGT, ART, AST, BET, BST, CAT, CNT, CST, CTT, EAT, ECT, IET, IST, JST, MIT, NET, NST, PLT, PNT, PRT, PST, SST, VST\n");
		printWriter.close();
		System.out.printf("File is located at %s%n", file.getAbsolutePath());
	}
	
	
	/**
	 * reads the config file and saves in Setting conf if conf file can’t be found creates new one whit default settings. 
	 * 
	 * @author Fabian Fiebich
	 * 
	 * @param conf Settings 
	 */
	static boolean createdFile = false;
	public static void readConfig(Settings conf) {
		try {
			//TODO file path dynamic
			File confFile = new File("digitalisierer.cfg");///home/ffiebich/Studium/Elektrotechnik/9.Semester/projektarbeit/AudioScope/AudioScope/src/controller/digitalisierer.cfg");
			Scanner myReader = new Scanner(confFile);
			
			//find path to save file
			myReader.findInLine("=");
			String data = myReader.nextLine();
			conf.setPathToSaveFiel(data);
		    //System.out.println(data); //debug
		    
		    //find gain 
		    myReader.findInLine("=");
		    if(myReader.hasNextInt() && myReader.hasNextLine()) {
		    	int gain1 = myReader.nextInt(); 
		    	conf.setGainCH1(gain1);
		    	//System.out.println(gain1); //debug
		    } else {
		    	System.out.println("config file error gain CH1");
		    }
		    myReader.nextLine();
		    
		    
		    //find gain2
		    myReader.findInLine("=");
		    if(myReader.hasNextInt() && myReader.hasNextLine()) {
		    	int gain2 = myReader.nextInt();
		    	conf.setGainCH2(gain2);
		    	//System.out.println(gain2); //debug
		    } else {	    	
		    	System.out.println("config file error gain CH2");
		    }
		    myReader.nextLine();

		    //ProbeAttenuationCH1 
		    myReader.findInLine("=");
		    if(myReader.hasNextInt() && myReader.hasNextLine()) {
		    	int ProbeAttenuation1 = myReader.nextInt(); 
		    	conf.setProbeAttenuationCH1(ProbeAttenuation1);
		    	//System.out.println(ProbeAttenuation1); //debug
		    } else {
		    	System.out.println("config file error ProbeAttenuation ch1");
		    }
		    myReader.nextLine();
		    
		    //ProbeAttenuationCH2 
		    myReader.findInLine("=");
		    if(myReader.hasNextInt() && myReader.hasNextLine()) {
		    	int ProbeAttenuation2 = myReader.nextInt(); 
		    	conf.setProbeAttenuationCH2(ProbeAttenuation2);
		    	//System.out.println(ProbeAttenuation2); //debug
		    } else {
		    	System.out.println("config file error ProbeAttenuation ch2");
		    }
		    myReader.nextLine();
		    
		    
		    //find Reset bool
		    myReader.findInLine("=");
		    if(myReader.hasNextInt() && myReader.hasNextLine()) {
		    	int reset = myReader.nextInt();
		    	if(reset == 1) {
		    		conf.setResetCaputer(true);
		    	}else {
		    		conf.setResetCaputer(false);
		    	}
		    	//System.out.println(reset); //debug
		    	myReader.nextLine();
		    } else {	    	
		    	System.out.println("config file error Reset");
		    }
		   		    
		  //find frequency
		    myReader.findInLine("=");
		    if(myReader.hasNextInt() && myReader.hasNextLine()) {
		    	int frequency = myReader.nextInt();
		    	conf.setFrequency((double)frequency);
		    	//System.out.println((double)frequency); //debug
		    } else {	    	
		    	System.out.println("config file error frequency");
		    }
		    myReader.nextLine();
		    
		    //Number of values to receive
			  //find frequency
		    myReader.findInLine("=");
		    if(myReader.hasNextInt() && myReader.hasNextLine()) {
		    	int numberOfValuesToReceive = myReader.nextInt();
		    	conf.setSampleTime(numberOfValuesToReceive);
		    	//System.out.println((double)frequency); //debug
		    } else {	    	
		    	System.out.println("config file error Number of values to receive");
		    }
		    myReader.nextLine();
		    		    
		    
		    
		    //The vendor ID of Device 0x04d8= 0x04d8
		    myReader.findInLine("=0x");
		    if(myReader.hasNextLine()) {
		    String vendorIDString = myReader.nextLine().trim();
		    //System.out.println("vendorIDString ="+vendorIDString);//debug
	    	short vendorID = (short) ((Character.digit(vendorIDString.charAt(0), 16) << 12) + (Character.digit(vendorIDString.charAt(1), 16) << 8)+ (Character.digit(vendorIDString.charAt(2), 16) << 4)+ Character.digit(vendorIDString.charAt(3), 16));
		    	conf.setVendorID(vendorID);
		    	//System.out.println("vendorID"+ vendorID); //debug
		    } else {	    	
		    	System.out.println("config file error vendorID");
		    }
		    
		    //The product ID of Device 0x0053= 0x0053
		    myReader.findInLine("=0x");
		    if(myReader.hasNextLine()) {
		    String productIDString = myReader.nextLine().trim();
	    	short productID = (short) ((Character.digit(productIDString.charAt(0), 16) << 12) + (Character.digit(productIDString.charAt(1), 16) << 8)+ (Character.digit(productIDString.charAt(2), 16) << 4)+ Character.digit(productIDString.charAt(3), 16)); 
		    	conf.setProductID(productID);
		    	//System.out.println("productID"+ productID); //debug
		    } else {	    	
		    	System.out.println("config file error productID");
		    }
		    
		    //The ADB interface number of the Device= 0
		    myReader.findInLine("=0x");
		    if(myReader.hasNextLine()) {
		    String pinterfaceString = myReader.nextLine().trim();
	    	byte pinterface= (byte) ((Character.digit(pinterfaceString.charAt(0), 16) << 4) + Character.digit(pinterfaceString.charAt(1), 16)); 
		    	conf.setmInterface(pinterface);
		    	//System.out.println("pinterface"+pinterface); //debug
		    } else {	    	
		    	System.out.println("config file error interface");
		    }
		    
		    //The ADB input endpoint of the Device 0x81=0x81
		    myReader.findInLine("=0x");
		    if(myReader.hasNextLine()) {
		    String inputEndpointString = myReader.nextLine().trim();
	    	byte inputEndpoint = (byte) ((Character.digit(inputEndpointString.charAt(0), 16) << 4) + Character.digit(inputEndpointString.charAt(1), 16)); 
		    	conf.setInEndpoint(inputEndpoint);
		    	//System.out.println(inputEndpoint); //debug
		    } else {	    	
		    	System.out.println("config file error inputEndpoint");
		    }		    
		    
		    //The ADB output endpoint of the Device=0x01
		    myReader.findInLine("=0x");
		    if(myReader.hasNextLine()) {
		    String outputEndpointString = myReader.nextLine().trim();
	    	byte outputEndpoint = (byte) ((Character.digit(outputEndpointString.charAt(0), 16) << 4) + Character.digit(outputEndpointString.charAt(1), 16)); 
		    	conf.setOutEndpoint(outputEndpoint);
		    	//System.out.println(outputEndpoint); //debug
		    } else {	    	
		    	System.out.println("config file error outputEndpoint");
		    }		    
		    
		    //Time get posible servers from http://tf.nist.gov/tf-cgi/servers.cgi=129.6.15.30
			myReader.findInLine("=");
			String serversString = myReader.nextLine();
			conf.setAtomicTimeServer(serversString);
			//System.out.println(serversString);//debug
		    
		    //atomicTimePort =13
		    myReader.findInLine("=");
		    if(myReader.hasNextInt() && myReader.hasNextLine()) {
		    	int patomicTimePort = myReader.nextInt();
		    	conf.setAtomicTimePort(patomicTimePort);
		    	//System.out.println(patomicTimePort); //debug
		    } else {	    	
		    	System.out.println("config file error gain interface");
		    }
		    myReader.nextLine();
		    
		    //timeZone =Europe/Berlin
			myReader.findInLine("=");
			String timeZone = myReader.nextLine();
			conf.setTimeZone(timeZone);
			//System.out.println(timeZone);//debug

			myReader.close();
		} 
		catch (FileNotFoundException e) {
			System.out.println("File not found create new");
			try {
				if(!createdFile) {
					createdFile=true;
					FileIO.genConfFile();
					FileIO.readConfig(conf);
				}else {
					System.out.println("File was not created right");
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
}
