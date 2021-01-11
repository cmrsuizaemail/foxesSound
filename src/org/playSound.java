package org;


import foxesSound.decoder.JavaLayerException;
import foxesSound.player.Player;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    
    public void playInternalSound(String filename, float volume) {
        try {
            InputStream is = getClass().getResourceAsStream(filename);
            player = new Player(is);
            player.setGain(volume);
        } catch (JavaLayerException e) {
            System.out.println("Problem playing file " + filename);
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
}
