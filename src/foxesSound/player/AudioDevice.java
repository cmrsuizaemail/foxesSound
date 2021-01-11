package foxesSound.player;

import foxesSound.decoder.Decoder;
import foxesSound.decoder.JavaLayerException;

/**
 * The <code>AudioDevice</code> interface provides an abstraction for
 * a device capable of sounding audio samples. Samples are written to 
 * the device wia the write() method. The device assumes
 * that these samples are signed 16-bit samples taken at the output frequency
 * of the decoder. If the decoder outputs more than one channel, the samples for
 * each channel are assumed to appear consecutively, with the lower numbered
 * channels preceeding higher-numbered channels. E.g. if there are two
 * channels, the samples will appear in this order:
 * <pre><code>
 * 
 *		l0, r0, l1, r1, l2, r2...
 * 
 * where 
 *	l<i>x</i> indicates the <i>x</i>th sample on channel 0
 *  r<i>x</i> indicates the <i>x</i>th sample on channel 1
 * </code></pre>
 *   
 * @since	0.0.8
 * @author	Mat McGowan
 */
public interface AudioDevice
{
	/**
	 * Prepares the AudioDevice for playback of audio samples. 
	 * @param decoder	The decoder that will be providing the audio
	 *					samples. 
	 * 
	 * If the audio device is already open, this method returns silently. 
	 * 
	 */
	public void open(Decoder decoder) throws JavaLayerException;
		
	/**
	 * Retrieves the open state of this audio device. 
	 * 
	 * @return <code>true</code> if this audio device is open and playing
	 *			audio samples, or <code>false</code> otherwise. 
	 */
	public boolean isOpen();
	
	/**
	 * Writes a number of samples to this <code>AudioDevice</code>. 
	 * 
	 * @param samples	The array of signed 16-bit samples to write
	 *					to the audio device. 
	 * @param offs		The offset of the first sample.
	 * @param len		The number of samples to write. 
	 * 
	 * This method may return prior to the samples actually being played 
	 * by the audio device. 
	 */
	public void write(short[] samples, int offs, int len) throws JavaLayerException;
		
	
	/**
	 * Closes this audio device. Any currently playing audio is stopped 
	 * as soon as possible. Any previously written audio data that has not been heard
	 * is discarded. 
	 * 
	 * The implementation should ensure that any threads currently blocking
	 * on the device (e.g. during a <code>write</code> or <code>flush</code>
	 * operation should be unblocked by this method. 
	 */
	public void close();
	
	
	/**
	 * Blocks until all audio samples previously written to this audio device have
	 * been heard. 
	 */
	public void flush();
			
	/**
	 * Retrieves the current playback position in milliseconds. 
	 */
	public int getPosition();
}
