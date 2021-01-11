package org;


import foxesSound.decoder.JavaLayerException;
import foxesSound.player.Player;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.SysUtils.send;
import static org.SysUtils.sendErr;

/**
 *
 * @author AidenFox
 */
public class playSound {
    private Player player;
    private static Thread[] threads = new Thread[4];
    int num = 0;
        
    public static void playExternalSound(String file, float volume) throws JavaLayerException{
        try {
             File f = new File(file);
             Player player = new Player(new FileInputStream(f));
             player.setGain(volume);
                        Thread musPlay;
                        musPlay = new Thread(() -> {
                            try {
                                player.play();
                            } catch (JavaLayerException ex) {
                                Logger.getLogger(playSound.class.getName()).log(Level.SEVERE, null, ex);
                            }
                          });
                          musPlay.start();
         } catch (FileNotFoundException ex) {
             ex.printStackTrace();
         }
    }
    
    public void playInternalSound(String filename, float volume) throws FileNotFoundException {
        try {
            InputStream is = getClass().getResourceAsStream(filename);
            send(is+"GG");
            player = new Player(is);
            player.setGain(volume);
        } catch (JavaLayerException e) {
            sendErr("Problem playing file " + filename);
        }

        threads[num] = new Thread() {
                @Override
                public void run() {
                    try {
                        player.play();
                    } catch (JavaLayerException e) {
                    }
                }
        };
          threads[num].start();
          num++;
          if (num > 3) {
              num = 0;
          }
    }
    
    byte[] ShortToByte(short[] buffer) {
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
}
