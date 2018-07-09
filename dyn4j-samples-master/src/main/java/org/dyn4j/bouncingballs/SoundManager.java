/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dyn4j.bouncingballs;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundManager {


    private AudioInputStream stream;

    String path;


    public SoundManager()
    {
        path = System.getProperty("user.dir")+"/Sounds/";
    }

    public void play(Sound sound)
    {
        try{
            String fileName = "";
            switch(sound)
            {
                case BUTTON_CLICKED:
                    fileName = "Button_1.wav";
                    break;
            }

            File f = new File(path + fileName);
            stream = AudioSystem.getAudioInputStream(f.toURI().toURL());
            Clip clip = AudioSystem.getClip();
            clip.open(stream);
            clip.start();
        }
        catch(LineUnavailableException | IOException | UnsupportedAudioFileException e)
        {
            System.out.println(e.getMessage());
        }
    }



}
