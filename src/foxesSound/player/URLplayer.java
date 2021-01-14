package foxesSound.player;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import foxesSound.decoder.JavaLayerException;
import static org.SysUtils.send;
import static org.SysUtils.sendErr;

/**
 * The URLplayer class implements a simple command-line
 * player for MPEG audio files.
 *
 * @author AidenFox
 */
public class URLplayer
{
	private String fFilename = null;
	private boolean remote = false;

	public static void main(String[] args) {
		int retval = 0;
		try {
			URLplayer player = createInstance(args);
			if (player!=null)
				player.play();
		} catch (Exception ex){
			System.err.println(ex);
			ex.printStackTrace(System.err);
			retval = 1;
		}
		System.exit(retval);
	}

	static public URLplayer createInstance(String[] args) {
		URLplayer player = new URLplayer();
		if (!player.parseArgs(args)) {
                    player = null;
                }
		return player;
	}

	private URLplayer()
	{
	}

	public URLplayer(String filename)
	{
		init(filename);
	}

	protected final void init(String filename)
	{
		fFilename = filename;
	}

	protected boolean parseArgs(String[] args) {
		boolean parsed = false;
		if (args.length == 1)
		{
			init(args[0]);
			parsed = true;
			remote = false;
		}
		else if (args.length == 2) {
			if (!(args[0].equals("-url"))) {
                            sendErr("Not found `-url` argument");
			} else {
                            init(args[1]);
                            parsed = true;
                            remote = true;
			}
		} else {
                    sendErr("More than two arguments");
		}
		return parsed;
	}

    public void play() throws JavaLayerException {
        try {
            send("playing "+fFilename+"...");
            InputStream in = null;
            if (remote == true) in = getURLInputStream();
            else in = getInputStream();
            AudioDevice dev = getAudioDevice();
            Player player = new Player(in, dev);
            player.play();
        } catch (IOException ex) {
            throw new JavaLayerException("Problem playing file "+fFilename, ex);
        } catch (Exception ex) {
            throw new JavaLayerException("Problem playing file "+fFilename, ex);
        }
    }

	/**
	 * Playing file from URL (Streaming).
	 */
	protected InputStream getURLInputStream()
		throws Exception
	{

		URL url = new URL(fFilename);
		InputStream fin = url.openStream();
		BufferedInputStream bin = new BufferedInputStream(fin);
		return bin;
	}

	/**
	 * Playing file from FileInputStream.
	 */
	protected InputStream getInputStream() throws IOException {
		FileInputStream fin = new FileInputStream(fFilename);
		BufferedInputStream bin = new BufferedInputStream(fin);
		return bin;
	}

	protected AudioDevice getAudioDevice() throws JavaLayerException {
		return FactoryRegistry.systemRegistry().createAudioDevice();
	}

}
