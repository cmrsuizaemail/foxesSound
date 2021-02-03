package foxesworld.foxesSound.player;

/**
 * The <code>NullAudioDevice</code> implements a silent, no-op
 * audio device. This is useful for testing purposes.
 * 
 * @since 0.0.8
 * @author Mat McGowan
 */
public class NullAudioDevice extends AudioDeviceBase
{
			
	public int getPosition()
	{
		return 0;
	}
}
