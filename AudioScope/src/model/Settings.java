package model;


public class Settings {
	private String pathToConfFiel =new String();
    private String pathToSaveFiel =new String();
    //gainmapping = {0, 1, 2, 5, 10, 20, 50, 100, 0}; 
    //byte[] gainToSet = { 8,2,3,1,2,3,4,5,6,7,7,7}
    private int gainCH1;
    private byte internalGainCH1;
    private int gainCH2;
    private byte internalGainCH2;
    int probeAttenuationCH1;
    int probeAttenuationCH2;
    private boolean resetCaputer;
	//mFreq = (int) (1/period);
	//short tmp = (short) (period/12.5e-9 -343);
	//byte[] data = {10,10,10,0, (byte) (tmp>>8), (byte) (tmp) };
	//tx[4] = (byte) (tmp>>8);
	//tx[5] = (byte) (tmp);
    // possible in Lechner’s app are 96000 48000 44100
    //MAX 819us/ MIN 1220 Hz (16 Bit Timer)
    private double frequency;
    private byte MSBSampelperiode;
    private byte LSBSampelperiode;
    private int sampleTime;
    
    private final int NUMOFBYTE= 4096;
	private short vendorID = 0x04d8;
	/** The product ID of Device 0x0053. */
	private short productID = 0x0053;
	/** The ADB interface number of the Device. */
	private byte mInterface = 0;
	/** The ADB input endpoint of the Device. */
	private byte inEndpoint = (byte) 0x81;//0x01;
	/** The ADB output endpoint of the Device. */
	private byte outEndpoint = (byte) 0x01;//0x81;
	
	
	//Time get posible servers from http://tf.nist.gov/tf-cgi/servers.cgi
	//TODO add to config.cfg
	private String atomicTimeServer="129.6.15.30";
	private int atomicTimePort = 13;
	/**
	 * All posible time zone IDs goten trought 	System.out.println(Arrays.toString(ControllComand));
	 * 
	 * Africa/Abidjan, Africa/Accra, Africa/Addis_Ababa, Africa/Algiers, Africa/Asmara, Africa/Asmera, Africa/Bamako, Africa/Bangui, Africa/Banjul, Africa/Bissau, Africa/Blantyre, Africa/Brazzaville, Africa/Bujumbura, Africa/Cairo, Africa/Casablanca, Africa/Ceuta, Africa/Conakry, Africa/Dakar, Africa/Dar_es_Salaam, Africa/Djibouti, Africa/Douala, Africa/El_Aaiun, Africa/Freetown, Africa/Gaborone, Africa/Harare, Africa/Johannesburg, Africa/Juba, Africa/Kampala, Africa/Khartoum, Africa/Kigali, Africa/Kinshasa, Africa/Lagos, Africa/Libreville, Africa/Lome, Africa/Luanda, Africa/Lubumbashi, Africa/Lusaka, Africa/Malabo, Africa/Maputo, Africa/Maseru, Africa/Mbabane, Africa/Mogadishu, Africa/Monrovia, Africa/Nairobi, Africa/Ndjamena, Africa/Niamey, Africa/Nouakchott, Africa/Ouagadougou, Africa/Porto-Novo, Africa/Sao_Tome, Africa/Timbuktu, Africa/Tripoli, Africa/Tunis, Africa/Windhoek, 
	 * America/Adak, America/Anchorage, America/Anguilla, America/Antigua, America/Araguaina, America/Argentina/Buenos_Aires, America/Argentina/Catamarca, America/Argentina/ComodRivadavia, America/Argentina/Cordoba, America/Argentina/Jujuy, America/Argentina/La_Rioja, America/Argentina/Mendoza, America/Argentina/Rio_Gallegos, America/Argentina/Salta, America/Argentina/San_Juan, America/Argentina/San_Luis, America/Argentina/Tucuman, America/Argentina/Ushuaia, America/Aruba, America/Asuncion, America/Atikokan, America/Atka, America/Bahia, America/Bahia_Banderas, America/Barbados, America/Belem, America/Belize, America/Blanc-Sablon, America/Boa_Vista, America/Bogota, America/Boise, America/Buenos_Aires, America/Cambridge_Bay, America/Campo_Grande, America/Cancun, America/Caracas, America/Catamarca, America/Cayenne, America/Cayman, America/Chicago, America/Chihuahua, America/Coral_Harbour, America/Cordoba, America/Costa_Rica, America/Creston, America/Cuiaba, America/Curacao, America/Danmarkshavn, America/Dawson, America/Dawson_Creek, America/Denver, America/Detroit, America/Dominica, America/Edmonton, America/Eirunepe, America/El_Salvador, America/Ensenada, America/Fort_Nelson, America/Fort_Wayne, America/Fortaleza, America/Glace_Bay, America/Godthab, America/Goose_Bay, America/Grand_Turk, America/Grenada, America/Guadeloupe, America/Guatemala, America/Guayaquil, America/Guyana, America/Halifax, America/Havana, America/Hermosillo, America/Indiana/Indianapolis, America/Indiana/Knox, America/Indiana/Marengo, America/Indiana/Petersburg, America/Indiana/Tell_City, America/Indiana/Vevay, America/Indiana/Vincennes, America/Indiana/Winamac, America/Indianapolis, America/Inuvik, America/Iqaluit, America/Jamaica, America/Jujuy, America/Juneau, America/Kentucky/Louisville, America/Kentucky/Monticello, America/Knox_IN, America/Kralendijk, America/La_Paz, America/Lima, America/Los_Angeles, America/Louisville, America/Lower_Princes, America/Maceio, America/Managua, America/Manaus, America/Marigot, America/Martinique, America/Matamoros, America/Mazatlan, America/Mendoza, America/Menominee, America/Merida, America/Metlakatla, America/Mexico_City, America/Miquelon, America/Moncton, America/Monterrey, America/Montevideo, America/Montreal, America/Montserrat, America/Nassau, America/New_York, America/Nipigon, America/Nome, America/Noronha, America/North_Dakota/Beulah, America/North_Dakota/Center, America/North_Dakota/New_Salem, America/Nuuk, America/Ojinaga, America/Panama, America/Pangnirtung, America/Paramaribo, America/Phoenix, America/Port-au-Prince, America/Port_of_Spain, America/Porto_Acre, America/Porto_Velho, America/Puerto_Rico, America/Punta_Arenas, America/Rainy_River, America/Rankin_Inlet, America/Recife, America/Regina, America/Resolute, America/Rio_Branco, America/Rosario, America/Santa_Isabel, America/Santarem, America/Santiago, America/Santo_Domingo, America/Sao_Paulo, America/Scoresbysund, America/Shiprock, America/Sitka, America/St_Barthelemy, America/St_Johns, America/St_Kitts, America/St_Lucia, America/St_Thomas, America/St_Vincent, America/Swift_Current, America/Tegucigalpa, America/Thule, America/Thunder_Bay, America/Tijuana, America/Toronto, America/Tortola, America/Vancouver, America/Virgin, America/Whitehorse, America/Winnipeg, America/Yakutat, America/Yellowknife, 
	 * Antarctica/Casey, Antarctica/Davis, Antarctica/DumontDUrville, Antarctica/Macquarie, Antarctica/Mawson, Antarctica/McMurdo, Antarctica/Palmer, Antarctica/Rothera, Antarctica/South_Pole, Antarctica/Syowa, Antarctica/Troll, Antarctica/Vostok, Arctic/Longyearbyen, 
	 * Asia/Aden, Asia/Almaty, Asia/Amman, Asia/Anadyr, Asia/Aqtau, Asia/Aqtobe, Asia/Ashgabat, Asia/Ashkhabad, Asia/Atyrau, Asia/Baghdad, Asia/Bahrain, Asia/Baku, Asia/Bangkok, Asia/Barnaul, Asia/Beirut, Asia/Bishkek, Asia/Brunei, Asia/Calcutta, Asia/Chita, Asia/Choibalsan, Asia/Chongqing, Asia/Chungking, Asia/Colombo, Asia/Dacca, Asia/Damascus, Asia/Dhaka, Asia/Dili, Asia/Dubai, Asia/Dushanbe, Asia/Famagusta, Asia/Gaza, Asia/Harbin, Asia/Hebron, Asia/Ho_Chi_Minh, Asia/Hong_Kong, Asia/Hovd, Asia/Irkutsk, Asia/Istanbul, Asia/Jakarta, Asia/Jayapura, Asia/Jerusalem, Asia/Kabul, Asia/Kamchatka, Asia/Karachi, Asia/Kashgar, Asia/Kathmandu, Asia/Katmandu, Asia/Khandyga, Asia/Kolkata, Asia/Krasnoyarsk, Asia/Kuala_Lumpur, Asia/Kuching, Asia/Kuwait, Asia/Macao, Asia/Macau, Asia/Magadan, Asia/Makassar, Asia/Manila, Asia/Muscat, Asia/Nicosia, Asia/Novokuznetsk, Asia/Novosibirsk, Asia/Omsk, Asia/Oral, Asia/Phnom_Penh, Asia/Pontianak, Asia/Pyongyang, Asia/Qatar, Asia/Qostanay, Asia/Qyzylorda, Asia/Rangoon, Asia/Riyadh, Asia/Saigon, Asia/Sakhalin, Asia/Samarkand, Asia/Seoul, Asia/Shanghai, Asia/Singapore, Asia/Srednekolymsk, Asia/Taipei, Asia/Tashkent, Asia/Tbilisi, Asia/Tehran, Asia/Tel_Aviv, Asia/Thimbu, Asia/Thimphu, Asia/Tokyo, Asia/Tomsk, Asia/Ujung_Pandang, Asia/Ulaanbaatar, Asia/Ulan_Bator, Asia/Urumqi, Asia/Ust-Nera, Asia/Vientiane, Asia/Vladivostok, Asia/Yakutsk, Asia/Yangon, Asia/Yekaterinburg, Asia/Yerevan, 
	 * Atlantic/Azores, Atlantic/Bermuda, Atlantic/Canary, Atlantic/Cape_Verde, Atlantic/Faeroe, Atlantic/Faroe, Atlantic/Jan_Mayen, Atlantic/Madeira, Atlantic/Reykjavik, Atlantic/South_Georgia, Atlantic/St_Helena, Atlantic/Stanley, 
	 * Australia/ACT, Australia/Adelaide, Australia/Brisbane, Australia/Broken_Hill, Australia/Canberra, Australia/Currie, Australia/Darwin, Australia/Eucla, Australia/Hobart, Australia/LHI, Australia/Lindeman, Australia/Lord_Howe, Australia/Melbourne, Australia/NSW, Australia/North, Australia/Perth, Australia/Queensland, Australia/South, Australia/Sydney, Australia/Tasmania, Australia/Victoria, Australia/West, Australia/Yancowinna, 
	 * Brazil/Acre, Brazil/DeNoronha, Brazil/East, Brazil/West, 
	 * CET, CST6CDT, Canada/Atlantic, 
	 * Canada/Central, Canada/Eastern, Canada/Mountain, Canada/Newfoundland, Canada/Pacific, Canada/Saskatchewan, Canada/Yukon, 
	 * Chile/Continental, Chile/EasterIsland, Cuba, EET, EST5EDT, Egypt, Eire, 
	 * Etc/GMT, Etc/GMT+0, Etc/GMT+1, Etc/GMT+10, Etc/GMT+11, Etc/GMT+12, Etc/GMT+2, Etc/GMT+3, Etc/GMT+4, Etc/GMT+5, Etc/GMT+6, Etc/GMT+7, Etc/GMT+8, Etc/GMT+9, Etc/GMT-0, Etc/GMT-1, Etc/GMT-10, Etc/GMT-11, Etc/GMT-12, Etc/GMT-13, Etc/GMT-14, Etc/GMT-2, Etc/GMT-3, Etc/GMT-4, Etc/GMT-5, Etc/GMT-6, Etc/GMT-7, Etc/GMT-8, Etc/GMT-9, Etc/GMT0, Etc/Greenwich, Etc/UCT, Etc/UTC, Etc/Universal, Etc/Zulu, 
	 * Europe/Amsterdam, Europe/Andorra, Europe/Astrakhan, Europe/Athens, Europe/Belfast, Europe/Belgrade, Europe/Berlin, Europe/Bratislava, Europe/Brussels, Europe/Bucharest, Europe/Budapest, Europe/Busingen, Europe/Chisinau, Europe/Copenhagen, Europe/Dublin, Europe/Gibraltar, Europe/Guernsey, Europe/Helsinki, Europe/Isle_of_Man, Europe/Istanbul, Europe/Jersey, Europe/Kaliningrad, Europe/Kiev, Europe/Kirov, Europe/Lisbon, Europe/Ljubljana, Europe/London, Europe/Luxembourg, Europe/Madrid, Europe/Malta, Europe/Mariehamn, Europe/Minsk, Europe/Monaco, Europe/Moscow, Europe/Nicosia, Europe/Oslo, Europe/Paris, Europe/Podgorica, Europe/Prague, Europe/Riga, Europe/Rome, Europe/Samara, Europe/San_Marino, Europe/Sarajevo, Europe/Saratov, Europe/Simferopol, Europe/Skopje, Europe/Sofia, Europe/Stockholm, Europe/Tallinn, Europe/Tirane, Europe/Tiraspol, Europe/Ulyanovsk, Europe/Uzhgorod, Europe/Vaduz, Europe/Vatican, Europe/Vienna, Europe/Vilnius, Europe/Volgograd, Europe/Warsaw, Europe/Zagreb, Europe/Zaporozhye, Europe/Zurich, 
	 * GB, GB-Eire, GMT, GMT0, Greenwich, Hongkong, Iceland, 
	 * Indian/Antananarivo, Indian/Chagos, Indian/Christmas, Indian/Cocos, Indian/Comoro, Indian/Kerguelen, Indian/Mahe, Indian/Maldives, Indian/Mauritius, Indian/Mayotte, Indian/Reunion, Iran, Israel, 
	 * Jamaica, Japan, Kwajalein, Libya, MET, MST7MDT, 
	 * Mexico/BajaNorte, Mexico/BajaSur, Mexico/General, 
	 * NZ, NZ-CHAT, Navajo, PRC, PST8PDT, Pacific/Apia,
	 * Pacific/Auckland, Pacific/Bougainville, Pacific/Chatham, Pacific/Chuuk, Pacific/Easter, Pacific/Efate, Pacific/Enderbury, Pacific/Fakaofo, Pacific/Fiji, Pacific/Funafuti, Pacific/Galapagos, Pacific/Gambier, Pacific/Guadalcanal, Pacific/Guam, Pacific/Honolulu, Pacific/Johnston, Pacific/Kiritimati, Pacific/Kosrae, Pacific/Kwajalein, Pacific/Majuro, Pacific/Marquesas, Pacific/Midway, Pacific/Nauru, Pacific/Niue, Pacific/Norfolk, Pacific/Noumea, Pacific/Pago_Pago, Pacific/Palau, Pacific/Pitcairn, Pacific/Pohnpei, Pacific/Ponape, Pacific/Port_Moresby, Pacific/Rarotonga, Pacific/Saipan, Pacific/Samoa, Pacific/Tahiti, Pacific/Tarawa, Pacific/Tongatapu, Pacific/Truk, Pacific/Wake, Pacific/Wallis, Pacific/Yap, Poland, Portugal, 
	 * ROK, Singapore, SystemV/AST4, SystemV/AST4ADT, SystemV/CST6, SystemV/CST6CDT, SystemV/EST5, SystemV/EST5EDT, SystemV/HST10, SystemV/MST7, SystemV/MST7MDT, SystemV/PST8, SystemV/PST8PDT, SystemV/YST9, SystemV/YST9YDT, 
	 * Turkey, UCT, US/Alaska, US/Aleutian, US/Arizona, US/Central, US/East-Indiana, US/Eastern, US/Hawaii, US/Indiana-Starke, US/Michigan, US/Mountain, US/Pacific, US/Samoa, 
	 * UTC, Universal, W-SU, WET, Zulu, EST, HST, MST, ACT, AET, AGT, ART, AST, BET, BST, CAT, CNT, CST, CTT, EAT, ECT, IET, IST, JST, MIT, NET, NST, PLT, PNT, PRT, PST, SST, VST
	 */
	private String timeZone = new String("Europe/Berlin"); 
	
	
	
	//*******************************************getter and setter******************************************************
	
	
	
	
	public String getTimeZone() {
		return timeZone;
	}
	public int getNUMOFBYTE() {
		return NUMOFBYTE;
	}
	public int getSampleTime() {
		return sampleTime;
	}
	public void setSampleTime(int sampleTime) {
		this.sampleTime = sampleTime;
	}
	public double getFrequency() {
		return frequency;
	}
	public void setFrequency(double frequency) {
		this.frequency = frequency;
	}
	public int getProbeAttenuationCH1() {
		return probeAttenuationCH1;
	}
	public void setProbeAttenuationCH1(int probeAttenuationCH1) {
		this.probeAttenuationCH1 = probeAttenuationCH1;
	}
	public int getProbeAttenuationCH2() {
		return probeAttenuationCH2;
	}
	public void setProbeAttenuationCH2(int probeAttenuationCH2) {
		this.probeAttenuationCH2 = probeAttenuationCH2;
	}
	public short getVendorID() {
		return vendorID;
	}
	public void setVendorID(short vendorID) {
		this.vendorID = vendorID;
	}
	public short getProductID() {
		return productID;
	}
	public void setProductID(short productID) {
		this.productID = productID;
	}
	public byte getmInterface() {
		return mInterface;
	}
	public void setmInterface(byte mInterface) {
		this.mInterface = mInterface;
	}
	public byte getInEndpoint() {
		return inEndpoint;
	}
	public void setInEndpoint(byte inEndpoint) {
		this.inEndpoint = inEndpoint;
	}
	public byte getOutEndpoint() {
		return outEndpoint;
	}
	public void setOutEndpoint(byte outEndpoint) {
		this.outEndpoint = outEndpoint;
	}
	public String getAtomicTimeServer() {
		return atomicTimeServer;
	}
	public void setAtomicTimeServer(String atomicTimeServer) {
		this.atomicTimeServer = atomicTimeServer;
	}
	public int getAtomicTimePort() {
		return atomicTimePort;
	}
	public void setAtomicTimePort(int atomicTimePort) {
		this.atomicTimePort = atomicTimePort;
	}
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}    
	public String getPathToConfFiel() {
		return pathToConfFiel;
	}
	public void setPathToConfFiel(String pathToConfFiel) {
		this.pathToConfFiel = pathToConfFiel;
	}
	public String getPathToSaveFiel() {
		return pathToSaveFiel;
	}
	public void setPathToSaveFiel(String pathToSaveFiel) {
		this.pathToSaveFiel = pathToSaveFiel;
	}
	public int getGainCH1() {
		return gainCH1;
	}
	public void setGainCH1(int gainCH1) {
		if (gainCH1<0) {
			//throw new invaledGainCh1();
		}
		this.gainCH1 = gainCH1;
	}
	public int getGainCH2() {
		return gainCH2;
	}
	public void setGainCH2(int gainCH2) {
		if (gainCH2<0) {
			//throw new invaledGainCh2();
		}
		this.gainCH2 = gainCH2;
	}
	public boolean isResetCaputer() {
		return resetCaputer;
	}
	public void setResetCaputer(boolean resetCaputer) {
		this.resetCaputer = resetCaputer;
	}
	public byte getMSBSampelperiode() {
		return MSBSampelperiode;
	}
	public void setMSBSampelperiode(byte mSBSampelperiode) {
		MSBSampelperiode = mSBSampelperiode;
	}
	public byte getLSBSampelperiode() {
		return LSBSampelperiode;
	}
	public void setLSBSampelperiode(byte lSBSampelperiode) {
		LSBSampelperiode = lSBSampelperiode;
	}
	public byte getInternalGainCH1() {
		return internalGainCH1;
	}
	public void setInternalGainCH1(byte internalGainCH1) {
		this.internalGainCH1 = internalGainCH1;
	}
	public byte getInternalGainCH2() {
		return internalGainCH2;
	}
	public void setInternalGainCH2(byte internalGainCH2) {
		this.internalGainCH2 = internalGainCH2;
	}
}
