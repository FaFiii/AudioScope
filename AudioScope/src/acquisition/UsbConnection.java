package acquisition;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import org.usb4java.BufferUtils;
import org.usb4java.DeviceHandle;
import org.usb4java.LibUsb;
import org.usb4java.LibUsbException;

public class UsbConnection {
/** TODO remove 
	//Bytes for start Sampling. 
	private static final byte[] ConfigStartsSampling = new byte[] {0x02, 0x08, 0x01, 0x00, (byte)0x0d, (byte)0xe9, 0x00};
	//private static final byte[] ConfigStartsSampling = new byte[] {0x02, 0x08, 0x01, 0x00, (byte)0xE6, (byte)0x97, 0x00};
	//private static final byte[] ConfigStartsSampling = new byte[] { 0x07, 0x08, 0x01, 0x00,(byte) 0x0E, 0x49, 0x00};

	//The vendor ID of Device 0x04d8. 
	private static final short VENDOR_ID = 0x04d8;

	//The product ID of Device 0x0053. 
	private static final short PRODUCT_ID = 0x0053;

	//The ADB interface number of the Device. 
	private static final byte INTERFACE = 0;

	//The ADB input endpoint of the Device. 
	private static final byte IN_ENDPOINT = (byte) 0x81;//0x01;

	// The ADB output endpoint of the Device. 
	private static final byte OUT_ENDPOINT = (byte) 0x01;//0x81;
*/
	/** The communication timeout in milliseconds. */
	private static final int TIMEOUT = 5000;

	/**
	 * Writes some data to the device.
	 * 
	 * @author Klaus Reimer, k@ailis.de
	 * 
	 * @param handle The device handle.
	 * @param data The data to send to the device.
	 */
	public static void write(DeviceHandle handle, byte[] data,final byte OUT_ENDPOINT){
		ByteBuffer buffer = BufferUtils.allocateByteBuffer(data.length);
		buffer.put(data);
		IntBuffer transferred = BufferUtils.allocateIntBuffer();
		int result = LibUsb.bulkTransfer(handle, OUT_ENDPOINT, buffer,transferred, TIMEOUT);
		if (result != LibUsb.SUCCESS){
			throw new LibUsbException("Unable to send data", result);
		}
		//System.out.println(transferred.get() + " bytes sent to device"); //debug
	}

	/**
	 * Reads some data from the device.
	 * 
	 * @param handle The device handle.
	 * @param size The number of bytes to read from the device.
	 * @return The read data.
	 */
	public static ByteBuffer read(DeviceHandle handle, int size,final byte IN_ENDPOINT){
		ByteBuffer buffer = BufferUtils.allocateByteBuffer(size).order(ByteOrder.LITTLE_ENDIAN);
		IntBuffer transferred = BufferUtils.allocateIntBuffer();
		int result = LibUsb.bulkTransfer(handle, IN_ENDPOINT, buffer,transferred, TIMEOUT);
		if (result != LibUsb.SUCCESS){
			throw new LibUsbException("Unable to read data", result);
		}
		//System.out.println(transferred.get() + " bytes read from device"); //debug
		return buffer;
	}

	
	/**
	 * creats an USB connection ready for sending 
	 * doing this every time you want to get data takes to much time
	 * Initialize the libusb context 
	 * Open test device
	 * Claim the ADB interface
	 * @param VENDOR_ID
	 * @param PRODUCT_ID
	 * @param INTERFACE
	 * @return
	 */
	public static DeviceHandle setupUSB(final short VENDOR_ID,final short PRODUCT_ID,final byte INTERFACE) {
		// Initialize the libusb context
		int result = LibUsb.init(null);
		if (result != LibUsb.SUCCESS){
			throw new LibUsbException("Unable to initialize libusb", result);
		}

		// Open test device
		DeviceHandle handle = LibUsb.openDeviceWithVidPid(null, VENDOR_ID,PRODUCT_ID);
		//TODO  Handle == null
		if (handle == null){
			System.err.println("Test device not found.");
			System.exit(1);
		}

		// Claim the ADB interface
		result = LibUsb.claimInterface(handle, INTERFACE);
		if (result != LibUsb.SUCCESS){
			throw new LibUsbException("Unable to claim interface", result);
		}		
		return handle;
	}
	
	/**
	 * send config byte array for this device
	 * 
	 */
	public static void sendConfigArray(byte[] ConfigStartsSampling, DeviceHandle handle,final byte OUT_ENDPOINT) {
		write(handle, ConfigStartsSampling,OUT_ENDPOINT);
	} 
	
	/**
	 * get the raw data from device via USB
	 * @param handle
	 * @param size
	 * @param IN_ENDPOINT
	 * @return ByteBuffer raw data 
	 */
	public static ByteBuffer reciveData(DeviceHandle handle,int size,final byte IN_ENDPOINT) {
		ByteBuffer rawData = read(handle,size,IN_ENDPOINT);
		return rawData;
	}
	
	/**
	 * terminate USB connection
	 * Release the ADB interface
	 * Close the device
	 * Deinitialize the libusb context
	 * @param handle
	 * @param INTERFACE
	 */
	public static void terminateUSB(DeviceHandle handle,final byte INTERFACE) {
		// Release the ADB interface
		int result = LibUsb.releaseInterface(handle, INTERFACE);
		if (result != LibUsb.SUCCESS){
			throw new LibUsbException("Unable to release interface", result);
		}
		
		// Close the device
		LibUsb.close(handle);

       // Deinitialize the libusb context
       LibUsb.exit(null);
	}
	
	/**
	 * GetData recives raw data from conected USB device. 
	 * first this funktion sends 9 byte with all setting zb. 0x07, 0x08, 0x01, 0x00, 0x0E, 0x49, 0x00 (gainCh1, GainCh2, StartSampling, rest, MSBSampelPeriod,LSBSampelPeriod,frecuncytestsignal)
	 * then an request is send for 4096 byte
	 * answer locks licke this -127 -48 0 0 126 -48 0 0 -116 -48 0 0 120 -48 0 0 -120 -48 0 0 -128 -48 0 0 -127 -48 0 0 126 -48 0 0 -123 -48 0 0 -120 -48 0 0 -112  
	 * 
	 * in this examle only one chanel is used one messurment is 2 byte long ch1 and ch2 alternate 
	 * 
	 * @author Fabian Fiebich
	 * 
	 * @throws Exception = When something goes wrong.
	 * @param byte[] ConfigStartsSampling 7 byte long array (gainCh1, GainCh2, StartSampling, rest, MSBSampelPeriod,LSBSampelPeriod,frecuncytestsignal)
	 * @param final short VENDOR_ID in this case should be 0x04d8
	 * @param final short PRODUCT_ID in this case should be 0x0053
	 * @param final byte INTERFACE in this case should be 0 TODO make changebal thou conf file
	 * @param final byte IN_ENDPOINT in this case should be (byte) 0x81
	 * @param final byte OUT_ENDPOINT in this case should be (byte) 0x01 
	 * @deprecated
	 */
	
	public static ByteBuffer getData(byte[] ConfigStartsSampling,final short VENDOR_ID,final short PRODUCT_ID,final byte INTERFACE,final byte IN_ENDPOINT,final byte OUT_ENDPOINT, boolean controllSignalIsSend) throws Exception{
		// Initialize the libusb context
		int result = LibUsb.init(null);
		if (result != LibUsb.SUCCESS){
			throw new LibUsbException("Unable to initialize libusb", result);
		}

		// Open test device
		DeviceHandle handle = LibUsb.openDeviceWithVidPid(null, VENDOR_ID,PRODUCT_ID);
		//TODO  Handle == null
		if (handle == null){
			System.err.println("Test device not found.");
			System.exit(1);
		}

		// Claim the ADB interface
		result = LibUsb.claimInterface(handle, INTERFACE);
		if (result != LibUsb.SUCCESS){
			throw new LibUsbException("Unable to claim interface", result);
		}

		// Send StartSampling message
		if(!controllSignalIsSend) {
			write(handle, ConfigStartsSampling,OUT_ENDPOINT);
		}
		// Receive RAW Data from device
		/*int i=0;
		while(i<1) {
			read(handle, 4096,IN_ENDPOINT);
			i++;
		}
		*/
		ByteBuffer rawData = read(handle, 4096,IN_ENDPOINT);

		// Release the ADB interface
		result = LibUsb.releaseInterface(handle, INTERFACE);
		if (result != LibUsb.SUCCESS){
			throw new LibUsbException("Unable to release interface", result);
		}
		
		// Close the device
		LibUsb.close(handle);

       // Deinitialize the libusb context
       LibUsb.exit(null);
       return rawData;
   }
}
