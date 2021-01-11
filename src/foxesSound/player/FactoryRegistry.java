package foxesSound.player;

import java.util.Enumeration;
import java.util.Hashtable;

import foxesSound.decoder.JavaLayerException;

/**
 * The <code>FactoryRegistry</code> class stores the factories
 * for all the audio device implementations available in the system. 
 * <p>
 * Instances of this class are thread-safe. 
 * 
 * @since 0.0.8
 * @author Mat McGowan
 */

public class FactoryRegistry extends AudioDeviceFactory
{
	static private FactoryRegistry instance = null;
	
	static synchronized public FactoryRegistry systemRegistry()
	{
		if (instance==null)
		{
			instance = new FactoryRegistry();
			instance.registerDefaultFactories();
		}
		return instance;
	}

	
	protected Hashtable factories = new Hashtable();
	
	/**
	 * Registers an <code>AudioDeviceFactory</code> instance
	 * with this registry. 
	 */
	public void addFactory(AudioDeviceFactory factory)
	{	
		factories.put(factory.getClass(), factory);						  
	}
	
	public void removeFactoryType(Class cls)
	{
		factories.remove(cls);
	}
	
	public void removeFactory(AudioDeviceFactory factory)
	{
		factories.remove(factory.getClass());	
	}
	
	public AudioDevice createAudioDevice() throws JavaLayerException
	{
		AudioDevice device = null;
		AudioDeviceFactory[] factories = getFactoriesPriority();
		
		if (factories==null)
			throw new JavaLayerException(this+": no factories registered");
		
		JavaLayerException lastEx = null;
		for (int i=0; (device==null) && (i<factories.length); i++)
		{
			try
			{
				device = factories[i].createAudioDevice();
			}
			catch (JavaLayerException ex)
			{
				lastEx = ex;
			}
		}
		
		if (device==null && lastEx!=null)
		{
			throw new JavaLayerException("Cannot create AudioDevice", lastEx);	
		}
		
		return device;
	}
	
	
	protected AudioDeviceFactory[] getFactoriesPriority()
	{
		AudioDeviceFactory[] fa = null;
		synchronized (factories)
		{
			int size = factories.size();
			if (size!=0)
			{
				fa = new AudioDeviceFactory[size];
				int idx = 0;
				Enumeration e = factories.elements();
				while (e.hasMoreElements())
				{
					AudioDeviceFactory factory = (AudioDeviceFactory)e.nextElement();
					fa[idx++] = factory;	
				}
			}
		}
		return fa;
	}

	protected void registerDefaultFactories()
	{
		addFactory(new JavaSoundAudioDeviceFactory());
	}
}
