package org;

/**
 * @author AidenFox
 */

import foxesSound.decoder.JavaLayerException;
import java.io.FileNotFoundException;

/* 
 *   A test class
 *   playInternalSound(String, float) for an internal sound
 *   playExternalSound(String, float) for an external sound
 */

public class test {
    public static void main(String[] args) throws JavaLayerException, FileNotFoundException {
        playSound snd = new playSound();
       snd.playExternalSound("C:\\Users\\AidenFox\\Desktop\\370.mp3", (float) -20f);
        snd.playInternalSound("/assets/340.mp3", 5f);
    }
}