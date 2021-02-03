package foxesworld.foxesSound.decoder;

import java.io.PrintStream;


/**
 * The JavaLayerException is the base class for all API-level
 * exceptions thrown by JavaLayer. To facilitate conversion and 
 * common handling of exceptions from other domains, the class 
 * can delegate some functionality to a contained Throwable instance. 
 * <p> 
 * 
 * @author MDM
 */
public class JavaLayerException extends Exception
{
	
	private Throwable		exception;
	
	
	public JavaLayerException()
	{
	}
	
	public JavaLayerException(String msg)
	{
		super(msg);
	}
	
	public JavaLayerException(String msg, Throwable t)
	{
		super(msg);
		exception = t;
	}
	
	public Throwable getException()
	{
		return exception;	
	}
	
	
	public void printStackTrace()
	{
		printStackTrace(System.err);	
	}
	
	public void printStackTrace(PrintStream ps)
	{
		if (this.exception==null)
		{
			super.printStackTrace(ps);	
		}
		else
		{
			exception.printStackTrace();
		}
	}
	
	
}
