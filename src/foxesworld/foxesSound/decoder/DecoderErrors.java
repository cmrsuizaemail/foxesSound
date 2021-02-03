package foxesworld.foxesSound.decoder;

/**
 * This interface provides constants describing the error
 * codes used by the Decoder to indicate errors. 
 * 
 * @author	MDM
 */
public interface DecoderErrors extends JavaLayerErrors
{
	
	static public final int UNKNOWN_ERROR = DECODER_ERROR + 0;
	
	/**
	 * Layer not supported by the decoder. 
	 */
	static public final int UNSUPPORTED_LAYER = DECODER_ERROR + 1;

    /**
	 * Illegal allocation in subband layer. Indicates a corrupt stream.
	 */
	static public final int ILLEGAL_SUBBAND_ALLOCATION = DECODER_ERROR + 2;

}
