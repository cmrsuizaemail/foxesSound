package foxesSound.player;

import java.io.InputStream;

import foxesSound.decoder.Bitstream;
import foxesSound.decoder.BitstreamException;
import foxesSound.decoder.Decoder;
import foxesSound.decoder.Header;
import foxesSound.decoder.JavaLayerException;
import foxesSound.decoder.SampleBuffer;
import java.nio.ByteBuffer;
import static org.SysUtils.sendErr;
	
/**
 * The <code>Player</code> class implements a simple player for playback
 * of an MPEG audio stream. 
 * 
 * @author	AidenFox
 * @since	0.0.9
 */

// REVIEW: the audio device should not be opened until the
// first MPEG audio frame has been decoded. 
public class Player
{	  	
	
	private int frame = 0; /* The current frame number. */
	
	/*final*/ private Bitstream bitstream; /* The MPEG audio bitstream. */
	/*final*/ private Decoder decoder; /* The MPEG audio decoder. */
	private AudioDevice audio; /* The AudioDevice the audio samples are written to.  */
	private boolean	closed = false;/* Has the player been closed? */
	private boolean	complete = false; /* Has the player played back all frames from the stream?*/
	private int lastPosition = 0;
	
	/* Creates a new <code>Player</code> instance. */
	public Player(InputStream stream) throws JavaLayerException {
		this(stream, null);	
	}
	
	public Player(InputStream stream, AudioDevice device) throws JavaLayerException {
		bitstream = new Bitstream(stream);		
		decoder = new Decoder();
				
		if (device!=null)
		{		
			audio = device;
		}
		else
		{			
			FactoryRegistry r = FactoryRegistry.systemRegistry();
			audio = r.createAudioDevice();
		}
		audio.open(decoder);
	}
	
	public void play() throws JavaLayerException {
		play(Integer.MAX_VALUE);
	}
	
	/**
	 * Plays a number of MPEG audio frames. 
	 * 
	 * @param frames	The number of frames to play. 
	 * @return	true if the last frame was played, or false if there are
	 *			more frames. 
         * @throws foxesSound.decoder.JavaLayerException 
	 */
	public boolean play(int frames) throws JavaLayerException {
            boolean ret = true;
			
            while (frames-- > 0 && ret) {
            ret = decodeFrame();			
            }
		
            if (!ret) {
		// last frame, ensure all data flushed to the audio device. 
                    AudioDevice out = audio;
                if (out!=null){				
                    out.flush();
                    synchronized (this){
                        complete = (!closed);
                        close();
                    }				
                }
            }
            return ret;
	}
		
	/* Cloases this player. Any audio currently playing is stopped immediately. */
	public synchronized void close() {		
		AudioDevice out = audio;
		if (out!=null)
		{ 
			closed = true;
			audio = null;	
			// this may fail, so ensure object state is set up before
			// calling this method. 
			out.close();
			lastPosition = out.getPosition();
			try
			{
				bitstream.close();
			}
			catch (BitstreamException ex)
			{
			}
		}
	}
	
	/**
	 * Returns the completed status of this player.
	 * @return	true if all available MPEG audio frames have been
	 * decoded, or false otherwise. 
	 */
	public synchronized boolean isComplete()
	{
		return complete;	
	}
				
	/**
	 * Retrieves the position in milliseconds of the current audio
	 * sample being played. This method delegates to the <code>
	 * AudioDevice</code> that is used by this player to sound
	 * the decoded audio samples. 
         * @return 
	 */
	public int getPosition()
	{
		int position = lastPosition;
		
		AudioDevice out = audio;		
		if (out!=null)
		{
			position = out.getPosition();	
		}
		return position;
	}		
	
	/*
	 * Decodes a single frame
	 * @return true if there are no more frames to decode, false otherwise.
         * @throws foxesSound.decoder.JavaLayerException
	 */
	protected boolean decodeFrame() throws JavaLayerException {		
		try {
			AudioDevice out = audio;
			if (out == null){
                            sendErr("Audio device is not avialable");
                            return false;
                        }

			Header h = bitstream.readFrame();	
			
			if (h == null){
                            return false;
                        }
				
			// sample buffer set when decoder constructed
			SampleBuffer output = (SampleBuffer)decoder.decodeFrame(h, bitstream);
			byte[] array = ShortToByte(output.getBuffer());
                        org.SysUtils.send(output.getBuffer()+"");
                        int size = array.length;
			
                        synchronized (this) {
                            out = audio;
                            if (out != null) {					
                                //out.write(output.getBuffer(), 0, array.length);
                                out.write(output.getBuffer(),  0,  size);
                            }				
			}															
			bitstream.closeFrame();
		} catch (RuntimeException ex) {
                    throw new JavaLayerException("Exception decoding audio frame", ex);
		}		
		return true;
	}
        
        private byte[] ShortToByte(short[] buffer) {
            int N = buffer.length;
            float f[] = new float[N];
            float min = 0.0f;
            float max = 0.0f;
            for (int i=0; i<N; i++) {
            f[i] = (float)(buffer[i]);
            if (f[i] > max) max = f[i];
            if (f[i] < min) min = f[i];
            }
            float scaling = 1.0f+(max-min)/256.0f; // +1 ensures we stay within range and guarantee no divide by zero if sequence is pure silence ...

            ByteBuffer byteBuf = ByteBuffer.allocate(N);
            for (int i=0; i<N; i++) {
            byte b = (byte)(f[i]/scaling); /*convert to byte. */
            byteBuf.put(b);
            }
            return byteBuf.array();
        }
        
        public static byte[] toBytes(short s) {
            return new byte[]{(byte)(s & 0x00FF),(byte)((s & 0xFF00)>>8)};
        }
        
        public boolean setGain(float newGain) {
            if (audio instanceof JavaSoundAudioDevice) {
                //System.out.println("Instance Of "+audio);
                JavaSoundAudioDevice jsAudio = (JavaSoundAudioDevice) audio;
                    try {
                        jsAudio.write(null, 0, 0);
                    } catch (JavaLayerException ex) {
                        ex.printStackTrace();
                    }
                    return jsAudio.setLineGain(newGain);
                }
                return false;
            }

	
}
