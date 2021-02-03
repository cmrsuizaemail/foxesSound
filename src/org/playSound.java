package org;


import foxesworld.foxesSound.decoder.JavaLayerException;
import foxesworld.foxesSound.player.Player;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.SysUtils.sendErr;

/**
 * @author AidenFox
 */
public class playSound {
    private Player player;
    private static Thread[] threads = new Thread[4];
    int num = 0;
        
    public static void playExternalSound(String file, float volume) throws JavaLayerException {
        if(file.contains("http:") || file.contains("https:")){
               String[] url = new String[2];
                url[0] = "-url";
                url[1] = file;
                Thread musPlay;
                        musPlay = new Thread(new Runnable() {
                   @Override
                   public void run() {
                       foxesworld.foxesSound.player.URLplayer.main(url);
                   }
               });
               musPlay.start();
        } else {        
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
    }
    
    public void playInternalSound(String filename, float volume) throws FileNotFoundException {
        try {
            InputStream is = getClass().getResourceAsStream(filename);
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
                        sendErr("Problem playing sound " + filename);
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
