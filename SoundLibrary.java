import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

class SoundLibrary{
    private Clip soundThrow;
    private Clip soundImpact;
    private Clip soundJump;
    private Clip soundRun;
    private Clip soundSword;
    private Clip soundGong;
    private Clip soundDeath;
    private Clip soundBlip;
    private Clip soundMelee;
    private Clip soundYuh;
    private Clip soundPowerUp;
    public SoundLibrary(String filename)throws Exception{
        String base = getBasePath();

        //throw
        soundThrow = loadClip(base + "throw_1.wav");

        //impact
        soundImpact = loadClip(base + "Impact_1.wav");

        //jump
        soundJump = loadClip(base + "spin_jump.wav");

        //footsteps
        soundRun = loadClip(base + "running_1.wav");

        // enemy sword
        soundSword = loadClip(base + "sword_1.wav");

        //gong
        soundGong = loadClip(base + "gong.wav");

        //blip
        soundBlip = loadClip(base + "blip_1.wav");

        //melee
        soundMelee = loadClip(base + "slap_1.wav");

        //power up
        soundPowerUp = loadClip(base + "powerUp_1.wav");
    }

    private String getBasePath() {
        // Resolve path relative to where the class files are
        try {
            String classPath = SoundLibrary.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            File classDir = new File(classPath);
            if (classDir.isFile()) {
                classDir = classDir.getParentFile();
            }
            return classDir.getAbsolutePath() + File.separator;
        } catch (Exception e) {
            return "";
        }
    }

    private Clip loadClip(String path) {
        try {
            File audiofile = new File(path);
            if (!audiofile.exists()) {
                System.out.println("Sound file not found: " + path);
                return null;
            }
            AudioInputStream audiostream = AudioSystem.getAudioInputStream(audiofile);
            Clip clip = AudioSystem.getClip();
            clip.open(audiostream);
            return clip;
        } catch (Exception e) {
            System.out.println("Error loading sound: " + path + " - " + e.getMessage());
            return null;
        }
    }

    public Clip getClips(String file){
        if (file.equals("throw") && soundThrow != null){
            soundThrow.setFramePosition(0);
            return soundThrow;
        }
        else if (file.equals("impact") && soundImpact != null){
            soundImpact.setFramePosition(0);
            return soundImpact;
        }
        else if (file.equals("jump") && soundJump != null){
            soundJump.setFramePosition(0);
            return soundJump;
        }
        else if (file.equals("run") && soundRun != null){
            soundRun.setFramePosition(0);
            return soundRun;
        }
        else if (file.equals("sword") && soundSword != null){
            soundSword.setFramePosition(0);
            return soundSword;
        }
        else if (file.equals("gong") && soundGong != null){
            soundGong.setFramePosition(0);
            return soundGong;
        }
        else if (file.equals("death") && soundDeath != null){
            return soundDeath;
        }
        else if (file.equals("blip") && soundBlip != null){
            soundBlip.setFramePosition(0);
            return soundBlip;
        }
        else if (file.equals("melee") && soundMelee != null){
            soundMelee.setFramePosition(0);
            return soundMelee;
        }
        else if (file.equals("yuh") && soundYuh != null){
            soundYuh.setFramePosition(0);
            return soundYuh;
        }
        else if (file.equals("powerUp") && soundPowerUp != null){
            soundPowerUp.setFramePosition(0);
            return soundPowerUp;
        }
        else{
            return null;
        }
    }
}
