package org;

/**
 * @author AidenFox
 */

import foxesSound.decoder.JavaLayerException;
import java.io.FileNotFoundException;
import static org.SysUtils.send;

/* 
 *   A test class
 *   playInternalSound(String, float) for an internal sound Now supports URL!!!
 *   playExternalSound(String, float) for an external sound
 */

public class test {
    public static void test() throws JavaLayerException, FileNotFoundException, InterruptedException {
        playSound snd = new playSound();
        snd.playInternalSound("/assets/start.mp3", 5f);
        playSound.playExternalSound("https://login.foxesworld.ru/launcher/files/eventSounds/mus/mus9.mp3", (float) -20f);
        Integer wait = getRandomIntegerBetweenRange(10000, 35000);
        String sound;
        
        if(wait >= 20000){
            sound = "/assets/amazing.mp3";
        } else {
            sound = "/assets/look&amaze.mp3";
        }
        send (sound + " " + wait);
        Thread.sleep(wait);
        snd.playInternalSound(sound, 5f);   
    }
    
    public static Integer getRandomIntegerBetweenRange(Integer min, Integer max) {
        Integer x = (int) (Math.random() * ((max - min) + 1)) + min;
        return x;
    }
}