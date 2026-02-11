import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

class SongLibrary{
    private Clip levelSong;
    File songFile;
    public void setSong (int value)throws Exception{
        // Stop previous song if playing
        if (levelSong != null) {
            levelSong.stop();
            levelSong.close();
            levelSong = null;
        }

        String path = ResourcePath.resolve("songs" + File.separator + "Level " + value + ".wav");
        songFile = new File(path);
        if (!songFile.exists()) {
            System.out.println("Song file not found: " + path);
            levelSong = null;
            return;
        }
        AudioInputStream audiostream = AudioSystem.getAudioInputStream(songFile);
        levelSong = AudioSystem.getClip();
        levelSong.open(audiostream);
    }

    public Clip getSong(){
        return levelSong;
    }
}
