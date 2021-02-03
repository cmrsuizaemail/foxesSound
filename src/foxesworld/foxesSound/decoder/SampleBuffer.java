package foxesworld.foxesSound.decoder;

/**
 * The <code>SampleBuffer</code> class implements an output buffer
 * that provides storage for a fixed size block of samples. 
 */
public class SampleBuffer extends Obuffer
{
  private short[] 		buffer;
  private int[] 		bufferp;
  private int 			channels;
  private int			frequency;
  
  /**
   * Constructor
   * @param sample_frequency
   * @param number_of_channels
   */
  public SampleBuffer(int sample_frequency, int number_of_channels)
  {
  	buffer = new short[OBUFFERSIZE];
	bufferp = new int[MAXCHANNELS];
	channels = number_of_channels;
	frequency = sample_frequency;
	
	for (int i = 0; i < number_of_channels; ++i) 
		bufferp[i] = (short)i;
	
  }

  public int getChannelCount()
  {
	return this.channels;  
  }
  
  public int getSampleFrequency()
  {
	  return this.frequency;
  }
  
  public short[] getBuffer()
  {
	return this.buffer;  
  }
  
  public int getBufferLength()
  {
	  return bufferp[0];
  }
  
  /**
   * Takes a 16 Bit PCM sample.
   * @param channel
   * @param value
   */
  @Override
  public void append(int channel, short value)
  {
	buffer[bufferp[channel]] = value;
	bufferp[channel] += channels;	  	
  }
  
  @Override
	public void appendSamples(int channel, float[] f)
	{
	    int pos = bufferp[channel];
		
		short s;
		float fs;
	    for (int i=0; i<32;)
	    {
		  	fs = f[i++];
			fs = (fs>32767.0f ? 32767.0f 
						   : (fs < -32767.0f ? -32767.0f : fs));
			
			s = (short)fs;
			buffer[pos] = s;
			pos += channels;
	    }
		
		bufferp[channel] = pos;
	}
  
  
  /**
   * Write the samples to the file (Random Acces).
   * @param val
   */
  @Override
  public void write_buffer(int val)
  {
				  
	//for (int i = 0; i < channels; ++i) 
	//	bufferp[i] = (short)i;

  }

  @Override
  public void close(){
  }
  
  /**
   *
   */
  @Override
  public void clear_buffer()
  {
	for (int i = 0; i < channels; ++i) 
		bufferp[i] = (short)i;
  }

  /**
   *
   */
  @Override
  public void set_stop_flag(){
  }
}
