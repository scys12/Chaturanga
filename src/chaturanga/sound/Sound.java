package chaturanga.sound;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Sound {
    public static void playSound(String filePath) {
        InputStream music;
        try {
            music = Files.newInputStream(Paths.get(filePath));
            AudioStream audios = new AudioStream(music);
            AudioPlayer.player.start(audios);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"Error");
        }
    }
}
