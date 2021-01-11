package foxesSound.player;

import foxesSound.decoder.JavaLayerException;

/**
 * This class is responsible for creating instances of the
 * JavaSoundAudioDevice. The audio device implementation is loaded
 * and tested dynamically as not all systems will have support
 * for JavaSound, or they may have the incorrect version. 
 */
public class JavaSoundAudioDeviceFactory extends AudioDeviceFactory
{
	private boolean tested = false;

	static private final String DEVICE_CLASS_NAME = "foxesSound.player.JavaSoundAudioDevice";
	
	public synchronized AudioDevice createAudioDevice()
		throws JavaLayerException
	{
		if (!tested)
		{			
			testAudioDevice();
			tested = true;
		}
		
		try
		{			
			return createAudioDeviceImpl();
		}
		catch (Exception ex)
		{
			throw new JavaLayerException("unable to create JavaSound device: "+ex);
		}
		catch (LinkageError ex)
		{
			throw new JavaLayerException("unable to create JavaSound device: "+ex);
		}
	}
	
	protected JavaSoundAudioDevice createAudioDeviceImpl()
		throws JavaLayerException
	{
		ClassLoader loader = getClass().getClassLoader();
		try
		{
			JavaSoundAudioDevice dev = (JavaSoundAudioDevice)instantiate(loader, DEVICE_CLASS_NAME);
			return dev;
		}
		catch (Exception ex)
		{
			throw new JavaLayerException("Cannot create JavaSound device", ex);
		}
		catch (LinkageError ex)
		{
			throw new JavaLayerException("Cannot create JavaSound device", ex);
		}
		
	}
	
	public void testAudioDevice() throws JavaLayerException
	{
		JavaSoundAudioDevice dev = createAudioDeviceImpl();
		//dev.;
	}
}
