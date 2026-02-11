import java.util.ArrayList;
import java.util.HashMap;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JFrame;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
public class GamePanel extends JPanel{
    private static final long serialVersionUID = 1L;

    private int level = 0;
    private int currentSongLevel = 0;
    private GameState state = GameState.TITLE;
    private ArrayList<GameObject> gameObjects = new ArrayList<GameObject>();
    private ArrayList<Collectable> coll = new ArrayList<Collectable>();
    private ArrayList<Enemy> enemies = new ArrayList<Enemy>();
    private ArrayList<Enemy> enemies2 = new ArrayList<Enemy>();
    private ArrayList<Enemy> enemies3 = new ArrayList<Enemy>();
    private ArrayList<Enemy> enemies4 = new ArrayList<Enemy>();

    private PlayerCharacter pc;
    public Platform mP1, mP2, mP3, mP4, mP5, mP6;
    private Camera camera;
    private ArrayList<JumpSpot> jumpSpots = new ArrayList<JumpSpot>();
    private String source;

    public int offset = 200;
    private JButton pause, resume, restartLevel, quit, next, bottomLeft, topRight;
    private SongLibrary songs = new SongLibrary();
    private SoundLibrary sounds;
    private ImageIcon imgPC;

    // Cache for story/background images to avoid reloading every frame
    private HashMap<String, ImageIcon> imageCache = new HashMap<String, ImageIcon>();

    private ImageIcon loadImage(String path) {
        if (imageCache.containsKey(path)) {
            return imageCache.get(path);
        }
        String resolved = ResourcePath.resolve(path);
        ImageIcon icon = new ImageIcon(resolved);
        imageCache.put(path, icon);
        return icon;
    }

    public GamePanel(int x, int y)throws Exception{
        setLayout(null);

        try {
            sounds = new SoundLibrary("x");
        } catch (Exception e) {
            System.out.println("Warning: Could not load sounds: " + e.getMessage());
        }

        pc = new PlayerCharacter(this, "images/pcNewAttempt2.png", 30, 500, 82, 82, new int[] {5,2,8,2,5}, 0);
        camera  = new Camera(this,0,0);

        resume = new JButton("Resume");
        pause = new JButton();
        restartLevel = new JButton("Restart");
        next = new JButton("Next");
        quit = new JButton("Quit");
        bottomLeft = new JButton();
        topRight = new JButton();

        this.add(bottomLeft);
        bottomLeft.setBounds(camera.getX(), camera.getY() + y, 1, 1);

        this.add(topRight);
        topRight.setBounds(camera.getX() + x, camera.getY(), 1, 1);

        pause.setVisible(false);
        pause.setOpaque(false);
        pause.setContentAreaFilled(false);
        pause.setBorderPainted(false);
        this.add(pause);
        pause.setBounds(camera.getX() + 30,camera.getY() + 720,30, 30);
        pause.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pause.setVisible(false);
                resume.setVisible(true);
                quit.setVisible(true);
                state = GameState.PAUSE;
            }
        });

        this.add(resume);
        resume.setBounds(camera.getX() + 285, camera.getY() + 720, 100, 30);
        resume.setVisible(false);
        resume.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pause.setVisible(true);
                resume.setVisible(false);
                restartLevel.setVisible(false);
                quit.setVisible(false);
                switch(level){
                    case 1:
                        state = GameState.LEVEL_1;
                        break;
                    case 2:
                        state = GameState.LEVEL_2;
                        break;
                    case 3:
                        state = GameState.LEVEL_3;
                        break;
                    case 4:
                        state = GameState.LEVEL_4;
                        break;
                }
            }
        });

        this.add(restartLevel);
        restartLevel.setBounds(camera.getX() + 285, camera.getY() + 680, 100, 30);
        restartLevel.setVisible(false);
        restartLevel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pause.setVisible(true);
                resume.setVisible(false);
                restartLevel.setVisible(false);
                quit.setVisible(false);
                try{
                    setLevel(level);
                }catch(Exception b){}
            }
        });


        this.add(quit);
        quit.setBounds(camera.getX() + 285, camera.getY() + 620, 100, 30);
        quit.setVisible(false);
        quit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resume.setVisible(false);
                restartLevel.setVisible(false);
                quit.setVisible(false);
                state = GameState.TITLE;
                next.setVisible(true);
                next.setBounds(1200, 650, 320, 75);
                try {
                    System.exit(0);
                } catch (Exception b){}
            }
        });

        this.add(next);
        next.setBounds(1200, 650, 320, 75);
        next.setFont(new Font("SansSerif", Font.BOLD, 24));
        next.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                switch(state) {
                    case TITLE:
                        bottomLeft.setVisible(false);
                        topRight.setVisible(false);
                        state = GameState.BETWEEN_1_1;
                        next.setBounds(1200, 650, 320, 75);
                        break;
                    case BETWEEN_1_1:
                        bottomLeft.setVisible(true);
                        topRight.setVisible(true);
                        state = GameState.BETWEEN_1_2;
                        next.setBounds(1200, 650, 320, 75);
                        break;
                    case BETWEEN_1_2:
                        pause.setVisible(true);
                        level = 1;
                        try {
                            setLevel(level);
                        } catch(Exception b) {}
                        state = GameState.LEVEL_1;
                        next.setVisible(false);
                        break;
                    case BETWEEN_2:
                        pause.setVisible(true);
                        level = 2;
                        try {
                            setLevel(level);
                        } catch(Exception b) {}
                        state = GameState.LEVEL_2;
                        next.setVisible(false);
                        break;
                    case BETWEEN_3:
                        pause.setVisible(true);
                        level = 3;
                        try {
                            setLevel(level);
                        } catch(Exception b) {}
                        state = GameState.LEVEL_3;
                        next.setVisible(false);
                        break;
                    case BETWEEN_4:
                        pause.setVisible(true);
                        level = 4;
                        try {
                            setLevel(level);
                        } catch(Exception b) {}
                        state = GameState.LEVEL_4;
                        next.setVisible(false);
                        break;
                    }
            }
        });

        setLevel(level);
    }
    public void setLevel(int s) throws Exception{
        if(s == 1){
            pc.setLife(10);
            pc.setAmmo(10);
            imgPC = loadImage("images/Backgrounds/mountain_background1-2.jpg");
            if(currentSongLevel != 1){
                if(songs.getSong() != null){
                    songs.getSong().stop();
                }
                try {
                    songs.setSong(1);
                    if(songs.getSong() != null) songs.getSong().loop(javax.sound.sampled.Clip.LOOP_CONTINUOUSLY);
                    currentSongLevel = 1;
                } catch (Exception e) {
                    System.out.println("Could not load song 1: " + e.getMessage());
                }
            }
            offset = 200;
            level = 1;
            coll.clear();
            enemies.clear();
            enemies2.clear();
            enemies3.clear();
            enemies4.clear();
            gameObjects.clear();
            pc.setX(30);
            pc.setY(500);
            this.state = GameState.LEVEL_1;
            coll.add(new Health(this, 3400, 370));
            coll.add(new Ammo(this, 3500, 370));
            coll.add(new Ammo(this, 8600, 370));
            coll.add(new Health(this, 5900, 370));
            coll.add(new Ammo(this, 6000, 370));
            enemies.add(new Enemy(this, 9100, 370, "images/boss.png", 3, 8, true, 6));
            gameObjects.add(new Platform(this, -1000,   700,  2200,  300, new Color(112,72,43,100), false, "base1"));
            gameObjects.add(new Platform(this,  1600,   700,  800,  300, new Color(112,72,43,100), false, "base2"));
            gameObjects.add(new Platform(this,  -500,   525,  200,  200, new Color(112,72,43,100), false, "sign"));
            gameObjects.add(new Platform(this,  1350,   750, 100,  250, new Color(112,72,43,100), false, "miniBase"));
            mP1 = new Platform(this,1200,400,400,50,Color.GREEN, true, "moving1");
            gameObjects.add(mP1);
            gameObjects.add(new Platform(this, 550,    655,  90,  40, new Color(112,72,43,100), false, "smallStone"));
            gameObjects.add(new Platform(this, 800,    550,  150,  50, new Color(112,72,43,100), false, "smallEnemyPlatform"));
            gameObjects.add(new Platform(this, 800,    600,  10,  100, new Color(112,72,43,100), false, "smallStilt"));
            gameObjects.add(new Platform(this, 940,    600,   10,  100, new Color(112,72,43,100), false, "smallStilt"));

            gameObjects.add(new Platform(this, 1850,    550,  150,  50, new Color(112,72,43,100), false, "smallEnemyPlatform"));
            gameObjects.add(new Platform(this, 1850,    600,  10,  100, new Color(112,72,43,100), false, "smallStilt"));
            gameObjects.add(new Platform(this, 1990,    600,   10,  100, new Color(112,72,43,100), false, "smallStilt"));

            gameObjects.add(new Platform(this, 2600,   700,  200,  300, new Color(112,72,43,100), false, "base2"));
            mP2 = new Platform(this, 2800,   625,  400, 375, new Color(112,72,43,100), false, "base2");
            gameObjects.add(mP2);
            mP3 = new Platform(this, 3000,   550,  200, 450, new Color(112,100,100,100), false, "base4");
            gameObjects.add(mP3);
            gameObjects.add(new Platform(this, 3200,   475,  400, 525, new Color(112,72,43,100), false, "base3"));

            gameObjects.add(new Platform(this, 3700,   525,  400, 50, new Color(112,72,43,100), false, "enemyPlatform"));
            gameObjects.add(new Platform(this, 3700,   575,  10,  425, new Color(112,72,43,100), false, "mediumStilt"));
            gameObjects.add(new Platform(this, 4090,   575,  10,  425, new Color(112,72,43,100), false, "mediumStilt"));

            gameObjects.add(new Platform(this, 4200,   375,  400, 50, new Color(112,72,43,100), false, "enemyPlatform"));
            gameObjects.add(new Platform(this, 4200,   425,  10,  575, new Color(112,72,43,100), false, "largeStilt"));
            gameObjects.add(new Platform(this, 4590,   425,  10,  575, new Color(112,72,43,100), false, "largeStilt"));

            gameObjects.add(new Platform(this, 4700,   525,  400, 50, new Color(112,72,43,100), false, "enemyPlatform"));
            gameObjects.add(new Platform(this, 4700,   575,  10, 425, new Color(112,72,43,100), false, "mediumStilt"));
            gameObjects.add(new Platform(this, 5090,   575,  10, 425, new Color(112,72,43,100), false, "mediumStilt"));

            gameObjects.add(new Platform(this, 5200,   375,  400, 50, new Color(112,72,43,100), false, "enemyPlatform"));
            gameObjects.add(new Platform(this, 5200,   425,  10, 575, new Color(112,72,43,100), false, "largeStilt"));
            gameObjects.add(new Platform(this, 5590,   425,  10, 575, new Color(112,72,43,100), false, "largeStilt"));

            gameObjects.add(new Platform(this, 5800,   475,  400, 525, new Color(112,72,43,100), false, "base3"));

            gameObjects.add(new Platform(this, 6300,   475,  100, 50, new Color(112,72,43,100), false, "stone"));
            gameObjects.add(new Platform(this, 6400,   500,  100, 50, new Color(112,72,43,100), false, "stone"));
            gameObjects.add(new Platform(this, 6500,   525,  100, 50, new Color(112,72,43,100), false, "stone"));
            gameObjects.add(new Platform(this, 6600,   550,  100, 50, new Color(112,72,43,100), false, "stone"));
            gameObjects.add(new Platform(this, 6700,   575,  100, 50, new Color(112,72,43,100), false, "stone"));
            gameObjects.add(new Platform(this, 6800,   600,  100, 50, new Color(112,72,43,100), false, "stone"));
            gameObjects.add(new Platform(this, 6900,   625,  100, 50, new Color(112,72,43,100), false, "stone"));
            gameObjects.add(new Platform(this, 7000,   650,  100, 50, new Color(112,72,43,100), false, "stone"));

            gameObjects.add(new Platform(this, 6350,   487,  100, 50, new Color(112,72,43,100), false, "stone"));
            gameObjects.add(new Platform(this, 6450,   512,  100, 50, new Color(112,72,43,100), false, "stone"));
            gameObjects.add(new Platform(this, 6550,   537,  100, 50, new Color(112,72,43,100), false, "stone"));
            gameObjects.add(new Platform(this, 6650,   562,  100, 50, new Color(112,72,43,100), false, "stone"));
            gameObjects.add(new Platform(this, 6750,   587,  100, 50, new Color(112,72,43,100), false, "stone"));
            gameObjects.add(new Platform(this, 6850,   612,  100, 50, new Color(112,72,43,100), false, "stone"));
            gameObjects.add(new Platform(this, 6950,   637,  100, 50, new Color(112,72,43,100), false, "stone"));
            gameObjects.add(new Platform(this, 7050,   662,  100, 50, new Color(112,72,43,100), false, "stone"));


            gameObjects.add(new Platform(this, 7350,   575,  400, 50, new Color(112,72,43,100), false, "enemyPlatform"));
            gameObjects.add(new Platform(this, 7350,   575,  10, 425, new Color(112,72,43,100), false, "mediumStilt"));
            gameObjects.add(new Platform(this, 7740,   575,  10, 425, new Color(112,72,43,100), false, "mediumStilt"));
            gameObjects.add(new Platform(this, 7650,   525,  10,  50, new Color(112,72,43,100), false, "miniStilt"));

            gameObjects.add(new Platform(this, 7650,   475,  400, 50, new Color(112,72,43,100), false, "enemyPlatform"));
            gameObjects.add(new Platform(this, 8040,   475,  10,  525, new Color(112,72,43,100), false, "largeStilt"));
            gameObjects.add(new Platform(this, 7950,   425,  10,  50, new Color(112,72,43,100), false, "miniStilt"));

            gameObjects.add(new Platform(this, 7950,   375,  400, 50, new Color(112,72,43,100), false, "enemyPlatform"));
            gameObjects.add(new Platform(this, 8340,   425,  10,  575, new Color(112,72,43,100), false, "largeStilt"));

            gameObjects.add(new Platform(this, 8500,   475,  400, 525, new Color(112,72,43,100), false, "base3"));
            gameObjects.add(new Platform(this, 8900,   475,  400, 525, new Color(112,72,43,100), false, "base3"));
            gameObjects.add(new Platform(this, 9250,   475,  20, 525, new Color(112,72,43,100), false, "pipe"));
            gameObjects.add(new Platform(this, 9400,   0,  20, 1000, new Color(112,72,43,100), false, ""));

            //initialize enemies

            enemies.add(new Enemy(this, 825, 400, "images/enemyRanger.png"));
            enemies.add(new Enemy(this, 1875, 400, "images/enemyRanger.png"));
            enemies.add(new Enemy(this, 4000, 400, "images/enemyRanger.png"));
            enemies.add(new Enemy(this, 4500, 200, "images/enemyRanger.png"));
            enemies.add(new Enemy(this, 5000, 400, "images/enemyRanger.png"));
            enemies.add(new Enemy(this, 5500, 200, "images/enemyRanger.png"));
            enemies.add(new Enemy(this, 7200, 200, "images/enemyRanger.png"));
            enemies.add(new Enemy(this, 7700, 200, "images/enemyRanger.png"));
            enemies.add(new Enemy(this, 8250, 200, "images/enemyRanger.png"));

            //Add images to Platform
            for(GameObject g: gameObjects){
                    if(g instanceof Platform){
                    if(((Platform)g).getTag().equals("base1")){
                        ((Platform)g).setSource("images/base1.png");
                    } else if(((Platform)g).getTag().equals("base2"))  {
                        ((Platform)g).setSource("images/base2.png");
                    } else if(((Platform)g).getTag().equals("smallStone"))  {
                        ((Platform)g).setSource("images/rock.png");
                    }else if(((Platform)g).getTag().equals("base3"))  {
                        ((Platform)g).setSource("images/base3.png");
                    }else if(((Platform)g).getTag().equals("base4"))  {
                        ((Platform)g).setSource("images/base4.png");
                    }else if(((Platform)g).getTag().equals("enemyPlatform"))  {
                        ((Platform)g).setSource("images/enemyPlatform.png");
                    } else if(((Platform)g).getTag().equals("moving1")){
                        ((Platform)g).setSource("images/moving platform1.png");
                    } else if(((Platform)g).getTag().equals("smallEnemyPlatform")){
                        ((Platform)g).setSource("images/smallEnemyPlatform.png");
                    }else if(((Platform)g).getTag().equals("largeStilt")){
                        ((Platform)g).setSource("images/largeStilt.png");
                    }else if(((Platform)g).getTag().equals("mediumStilt")){
                        ((Platform)g).setSource("images/mediumStilt.png");
                    }else if(((Platform)g).getTag().equals("smallStilt")){
                        ((Platform)g).setSource("images/smallStilt.png");
                    }else if(((Platform)g).getTag().equals("stone")){
                        ((Platform)g).setSource("images/stone.png");
                    }else if(((Platform)g).getTag().equals("miniStilt")){
                        ((Platform)g).setSource("images/miniStilt.png");
                    }else if(((Platform)g).getTag().equals("miniBase")){
                        ((Platform)g).setSource("images/miniBase.png");
                    }else if(((Platform)g).getTag().equals("sign")){
                        ((Platform)g).setSource("images/sign1.png");
                    } else if(((Platform)g).getTag().equals("pipe")){
                        ((Platform)g).setSource("images/pipe2.png");
                    } else {}
                }
            }
        } else if(s == 2){
            pc.setLife(10);
            pc.setAmmo(10);
            imgPC = loadImage("images/Backgrounds/sewer2.jpg");
            if(currentSongLevel != 2){
                if(songs.getSong() != null){
                    songs.getSong().stop();
                }
                try {
                    songs.setSong(2);
                    if(songs.getSong() != null) songs.getSong().loop(javax.sound.sampled.Clip.LOOP_CONTINUOUSLY);
                    currentSongLevel = 2;
                } catch (Exception e) {
                    System.out.println("Could not load song 2: " + e.getMessage());
                }
            }
            offset = 200;
            this.level = 2;
            this.state = GameState.LEVEL_2;

            coll.clear();
            enemies.clear();
            enemies2.clear();
            enemies3.clear();
            enemies4.clear();
            gameObjects.clear();
            jumpSpots.clear();
            pc.setX(600);
            pc.setY(550);
            coll.add(new Health(this, 1600, 320));
            coll.add(new Ammo(this, 1450, 190));
            coll.add(new Health(this, 2900, 320));
            coll.add(new Ammo(this, 3150, 190));
            coll.add(new Health(this, 4500, 820));
            coll.add(new Ammo(this, 4400, 820));
            coll.add(new Ammo(this, 4700, 1220));
            enemies2.add(new Enemy(this, 4900,370, 80, 100, "images/boss.png", 3, 4, true, 6));
            gameObjects.add(new Platform(this, -1000,   700,  2200,  300, new Color(112,72,43,100), false, "sewer1"));
            gameObjects.add(new Platform(this, 1350,   700,  2200,  300, new Color(112,72,43,100), false, "sewer1"));
            gameObjects.add(new Platform(this,  0,   525,  200,  200, new Color(112,72,43,100), false, "sign"));
            gameObjects.add(new Platform(this, -600,   -400,  2200,  300, new Color(112,72,43,100), false, "sewer1"));
            gameObjects.add(new Platform(this, 600,   -100,  195,  575, new Color(112,72,43,100), false, "pipe1"));
            gameObjects.add(new Platform(this, 1200,   540,  195,  460, new Color(112,72,43,100), false, "pipe2"));
            gameObjects.add(new Platform(this, 1600,   400,  300,  25, new Color(112,72,43,100), false, "sewer4"));
            gameObjects.add(new Platform(this, 1900,   400,  300,  25, new Color(112,72,43,100), false, "sewer4"));
            gameObjects.add(new Platform(this, 2400,   400,  300,  25, new Color(112,72,43,100), false, "sewer4"));
            gameObjects.add(new Platform(this, 2700,   400,  300,  25, new Color(112,72,43,100), false, "sewer4"));
            gameObjects.add(new Platform(this, 2150,   550,  300,  25, new Color(112,72,43,100), false, "sewer4"));
            gameObjects.add(new Platform(this, 1400,   250,  300,  25, new Color(112,72,43,100), false, "sewer4"));
            gameObjects.add(new Platform(this, 1700,   250,  300,  25, new Color(112,72,43,100), false, "sewer4"));
            gameObjects.add(new Platform(this, 2600,   250,  300,  25, new Color(112,72,43,100), false, "sewer4"));
            gameObjects.add(new Platform(this, 2900,   250,  300,  25, new Color(112,72,43,100), false, "sewer4"));
            gameObjects.add(new Platform(this, 3550,   540,  50,  460, new Color(112,72,43,100), false, "pipe2"));
            gameObjects.add(new Platform(this, 3615,   350,  1,  1, new Color(112,72,43,100), false, "downArrow"));
            gameObjects.add(new Platform(this, 3700,   200,  50,  700, new Color(112,72,43,100), false, ""));
            gameObjects.add(new Platform(this, 2600,   1300,  2250,  300, new Color(112,72,43,100), false, "sewer1"));
            gameObjects.add(new Platform(this, 5050,   1300,  2250,  300, new Color(112,72,43,100), false, "sewer1"));

            gameObjects.add(new Platform(this, 4000,   1150,  300,  25, new Color(112,72,43,100), false, "sewer4"));
            gameObjects.add(new Platform(this, 4200,   1025,  300,  25, new Color(112,72,43,100), false, "sewer4"));
            gameObjects.add(new Platform(this, 4400,   900,  300,  25, new Color(112,72,43,100), false, "sewer4"));
            gameObjects.add(new Platform(this, 4600,   775,  300,  25, new Color(112,72,43,100), false, "sewer4"));
            gameObjects.add(new Platform(this, 4800,   650,  300,  25, new Color(112,72,43,100), false, "sewer4"));
            gameObjects.add(new Platform(this, 5000,   775,  300,  25, new Color(112,72,43,100), false, "sewer4"));
            gameObjects.add(new Platform(this, 5200,   900,  300,  25, new Color(112,72,43,100), false, "sewer4"));
            gameObjects.add(new Platform(this, 5400,   1025,  300,  25, new Color(112,72,43,100), false, "sewer4"));
            gameObjects.add(new Platform(this, 5600,   1150,  300,  25, new Color(112,72,43,100), false, "sewer4"));
            gameObjects.add(new Platform(this, 6050,   1150,  195,  460, new Color(112,72,43,100), false, "pipe2"));
            gameObjects.add(new Platform(this, 6600,   560,  195,  375, new Color(112,72,43,100), false, "pipe1"));
            gameObjects.add(new Platform(this, 6600,   560,  10,  575, new Color(112,72,43,100), false, "pipe1"));
            gameObjects.add(new Platform(this, 6600,   300,  10,  575, new Color(112,72,43,100), false, "extraPipe"));
            gameObjects.add(new Platform(this, 6800,   560,  10,  800, new Color(112,72,43,100), false, ""));
            gameObjects.add(new Platform(this, 6650,   1350,  1,  1, new Color(112,72,43,100), false, "upArrow"));

            enemies2.add(new Enemy(this, 1900, 300, "images/enemyRanger.png"));
            enemies2.add(new Enemy(this, 2600, 300, "images/enemyRanger.png"));
            enemies2.add(new Enemy(this, 1700, 100, "images/enemyRanger.png"));
            enemies2.add(new Enemy(this, 2800, 100, "images/enemyRanger.png"));
            enemies2.add(new Enemy(this, 4750, 1220, "images/enemyRanger.png"));
            enemies2.add(new Enemy(this, 2260, 620, "images/enemyRanger.png"));
            for(GameObject go: gameObjects){
                if(go instanceof Platform){
                    if(((Platform)go).getTag().equals("pipe1")){
                        ((Platform)go).setSource("images/pipe1.png");
                    }else if(((Platform)go).getTag().equals("pipe2")){
                        ((Platform)go).setSource("images/pipe2.png");
                    }else if(((Platform)go).getTag().equals("sewer1")){
                        ((Platform)go).setSource("images/sewer1.png");
                    }else if(((Platform)go).getTag().equals("downArrow")){
                        ((Platform)go).setSource("images/downArrow.png");
                    }else if(((Platform)go).getTag().equals("sign")){
                        ((Platform)go).setSource("images/sign2.png");
                    }else if(((Platform)go).getTag().equals("sewer3")){
                        ((Platform)go).setSource("images/sewer3.png");
                    }else if(((Platform)go).getTag().equals("sewer2")){
                        ((Platform)go).setSource("images/sewer2.png");
                    }else if(((Platform)go).getTag().equals("upArrow")){
                        ((Platform)go).setSource("images/upArrow.png");
                    }else if(((Platform)go).getTag().equals("extraPipe")){
                        ((Platform)go).setSource("images/extraPipe.png");
                    }else if(((Platform)go).getTag().equals("sewer4")){
                        ((Platform)go).setSource("images/sewer4.png");
                    } else{}
                }

            }
        } else if(s == 3){
            pc.setLife(10);
            pc.setAmmo(10);
            imgPC = loadImage("images/Backgrounds/city2.jpg");
            if(currentSongLevel != 3){
                if(songs.getSong() != null){
                    songs.getSong().stop();
                }
                try{
                    songs.setSong(3);
                    if(songs.getSong() != null) songs.getSong().loop(javax.sound.sampled.Clip.LOOP_CONTINUOUSLY);
                    currentSongLevel = 3;
                } catch(Exception e){
                    System.out.println("Could not load song 3: " + e.getMessage());
                }
            }
            offset = -100;
            this.level = 3;
            this.state = GameState.LEVEL_3;

            pc.setX(50);
            pc.setY(200);
            coll.clear();
            enemies.clear();
            enemies2.clear();
            enemies3.clear();
            enemies4.clear();
            gameObjects.clear();
            //Hard
            enemies3.add(new Enemy(this, 2280,410, "images/boss.png", 3, 4, true, 6));
            enemies3.add(new Enemy(this, 4070,470, "images/boss.png", 3, 4, true, 6));
            enemies3.add(new Enemy(this, 8010,510, "images/boss.png", 3, 4, true, 6));


            //Normal

            enemies3.add(new Enemy(this, 7900, 510, "images/enemyRanger.png"));
            enemies3.add(new Enemy(this, 760, 390, "images/enemyRanger.png"));
            enemies3.add(new Enemy(this, 1060, 515, "images/enemyRanger.png"));
            enemies3.add(new Enemy(this, 1500, 515, "images/enemyRanger.png"));
            enemies3.add(new Enemy(this, 3570, 315, "images/enemyRanger.png"));
            enemies3.add(new Enemy(this, 5030, 510, "images/enemyRanger.png"));
            enemies3.add(new Enemy(this, 6430, 510, "images/enemyRanger.png"));
            //
            coll.add(new Health(this, 2770, 290));
            coll.add(new Health(this, 4600, 410));
            coll.add(new Health(this, 6790, 410));
            coll.add(new Ammo(this, 1771, 392));
            coll.add(new Ammo(this, 3750, 322));
            coll.add(new Ammo(this, 4630, 410));
            coll.add(new Ammo(this, 5730, 360));

            gameObjects.add(new Platform(this, -300,   500,  200,  1000, new Color(112,72,43,100), false, "sign"));

            // Skyscrapers - all extend from rooftop Y down 800px to form tall buildings
            gameObjects.add(new Platform(this, -25,   560,  560,  800, new Color(112,72,43,100), false, "ledge"));

            gameObjects.add(new Platform(this, 700,   475,  225,  800, new Color(112,72,43,100), false, "pagota1"));

            gameObjects.add(new Platform(this, 1050,   600,  560,  800, new Color(112,72,43,100), false, "ledge"));

            gameObjects.add(new Platform(this, 1710,   475,  225,  800, new Color(112,72,43,100), false, "pagota1"));

            gameObjects.add(new Platform(this, 2040,   500,  560,  800, new Color(112,72,43,100), false, "ledge"));

            gameObjects.add(new Platform(this, 2700,   375,  225,  800, new Color(112,72,43,100), false, "pagota1"));

            gameObjects.add(new Platform(this, 3100,   400,  560,  800, new Color(112,72,43,100), false, "ledge"));

            gameObjects.add(new Platform(this, 3825,   560,  560,  800, new Color(112,72,43,100), false, "ledge"));

            gameObjects.add(new Platform(this, 4500,   500,  225,  800, new Color(112,72,43,100), false, "pagota1"));

            gameObjects.add(new Platform(this, 4800,   600,  560,  800, new Color(112,72,43,100), false, "ledge"));
            gameObjects.add(new Platform(this, 5500,   450,  560,  800, new Color(112,72,43,100), false, "ledge"));
            gameObjects.add(new Platform(this, 6200,   600,  560,  800, new Color(112,72,43,100), false, "ledge"));

            gameObjects.add(new Platform(this, 6900,   500,  225,  800, new Color(112,72,43,100), false, "pagota1"));
            gameObjects.add(new Platform(this, 7300,   600,  560,  800, new Color(112,72,43,100), false, "ledge"));
            gameObjects.add(new Platform(this, 7800,   600,  560,  800, new Color(112,72,43,100), false, "ledge"));
            gameObjects.add(new Platform(this, 8300,   600,  560,  800, new Color(112,72,43,100), false, "ledge"));
            gameObjects.add(new Platform(this, 8677,   370,  1,  1, new Color(112,72,43,100), false, "evil door"));


            for(GameObject go: gameObjects){
                if(go instanceof Platform){
                    if(((Platform)go).getTag().equals("sign")){
                        ((Platform)go).setSource("images/sign3.png");
                    } else if(((Platform)go).getTag().equals("pagota1")){
                        ((Platform)go).setSource("images/pagota1.png");
                    }else if(((Platform)go).getTag().equals("ledge")){
                        ((Platform)go).setSource("images/ledge.png");
                    }else if(((Platform)go).getTag().equals("evil door")){
                        ((Platform)go).setSource("images/evil door.png");
                    }
                }
            }
        } else if(s == 4){
            pc.setLife(10);
            pc.setAmmo(10);
            imgPC = loadImage("images/Backgrounds/Dojo_Background2.jpg");
            if(currentSongLevel != 4){
                if(songs.getSong() != null){
                    songs.getSong().stop();
                }
                try{
                    songs.setSong(4);
                    if(songs.getSong() != null) songs.getSong().loop(javax.sound.sampled.Clip.LOOP_CONTINUOUSLY);
                    currentSongLevel = 4;
                } catch(Exception e){
                    System.out.println("Could not load song 4: " + e.getMessage());
                }
            }
            offset = 250;
            this.level = 4;
            this.state = GameState.LEVEL_4;
            pc.setX(2530);
            pc.setY(500);
            coll.clear();
            enemies.clear();
            enemies2.clear();
            enemies3.clear();
            enemies4.clear();
            gameObjects.clear();
            coll.add(new Health(this,2230, 610));
            coll.add(new Ammo(this,2290, 610));
            coll.add(new Ammo(this,3350, 610));
            coll.add(new Health(this,3400, 610));
            coll.add(new Ammo(this,3660, 610));
            gameObjects.add(new Platform(this, 4000,   500,  1,  1, new Color(112,72,43,100), false, "evil door"));
            gameObjects.add(new Platform(this, 2200,   700,  2300,  25, new Color(112,72,43,100), false, ""));
            gameObjects.add(new Platform(this, 2200,   -500,  10,  2000, new Color(112,72,43,100), false, ""));
            gameObjects.add(new Platform(this, 4200,   -500,  10,  2000, new Color(112,72,43,100), false, ""));
            enemies4.add(new Enemy(this, 3060, 600, "images/enemyRanger.png", 2, 3, true, 6, 500));
            enemies4.add(new Enemy(this, 2960, 600, "images/enemyRanger.png", 2, 3, true, 6, 500));

            enemies4.add(new Enemy(this, 4100, 500, 144, 162, "images/boss2.png", 4, 8, true, 6));
            enemies4.add(new Enemy(this, 3700, 500, "images/boss.png", 3, 6, true, 6, 800));

            for(GameObject go: gameObjects){
                if(go instanceof Platform){
                    if(((Platform)go).getTag().equals("evil door")){
                        ((Platform)go).setSource("images/evil door.png");
                    }
                }
            }
        } else if(s == 5){
            coll.clear();
            enemies.clear();
            enemies2.clear();
            enemies3.clear();
            enemies4.clear();
            gameObjects.clear();
        }
    }
    public Camera getCam(){
        return camera;
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        switch(state){

            case TITLE:
                imgPC = loadImage("images/Page1-2.PNG");
                drawTitle(g);
                break;
            case BETWEEN_1_1:
                imgPC = loadImage("images/Page2.PNG");
                drawBetween_1_1(g);
                next.setVisible(true);
                break;
            case BETWEEN_1_2:
                imgPC = loadImage("images/Page3.PNG");
                drawBetween_1_2(g);
                break;
            case LEVEL_1:
                drawLevel_1(g);
                break;
            case BETWEEN_2:
                imgPC = loadImage("images/Page4.PNG");
                drawBetween_2(g);
                break;
            case LEVEL_2:
                drawLevel_2(g);
                break;
            case BETWEEN_3:
                imgPC = loadImage("images/PAGE5.PNG");
                drawBetween_2(g);
                break;
            case LEVEL_3:
                drawLevel_3(g);
                break;
            case BETWEEN_4:
                imgPC = loadImage("images/PAGE6.PNG");
                drawBetween_4(g);
                break;
            case LEVEL_4:
                drawLevel_4(g);
                break;
            case END:
                imgPC = loadImage("images/Page7.PNG");
                drawEnd(g);
                break;
            case PAUSE:
                drawPause(g);
                break;
        }
        pause.paint(g);
        resume.repaint();
        restartLevel.repaint();
        quit.repaint();
    }
    public JButton getQuit(){
        return this.quit;
    }
    public void drawEnd(Graphics g){
        drawImageOrFallback(g, "ENDING", 0, 0, getWidth(), getHeight());
    }
    public void drawBetween_1_1(Graphics g) {
        drawImageOrFallback(g, "PROLOGUE - Part 1", 0, 0, getWidth(), getHeight());
    }
    public void drawBetween_1_2(Graphics g) {
        drawImageOrFallback(g, "PROLOGUE - Part 2", 0, 0, getWidth(), getHeight());
    }
    public void drawBetween_2(Graphics g) {
        drawImageOrFallback(g, "BETWEEN LEVELS", 0, 0, getWidth(), getHeight());
    }
    public void drawBetween_3(Graphics g) {
        drawImageOrFallback(g, "BETWEEN LEVELS", 0, 0, getWidth(), getHeight());
    }
    public void drawBetween_4(Graphics g) {
        drawImageOrFallback(g, "BETWEEN LEVELS", 0, 0, getWidth(), getHeight());
    }

    private void drawImageOrFallback(Graphics g, String label, int x, int y, int w, int h) {
        if (imgPC != null && imgPC.getIconWidth() > 0) {
            imgPC.paintIcon(pc.getPanel(), g, x, y);
        } else {
            // Fallback: draw a styled screen when image is missing
            g.setColor(new Color(20, 20, 30));
            g.fillRect(x, y, w, h);
            g.setColor(new Color(200, 180, 140));
            g.setFont(new Font("Serif", Font.BOLD, 48));
            FontMetrics fm = g.getFontMetrics();
            int textW = fm.stringWidth(label);
            g.drawString(label, x + (w - textW) / 2, y + h / 2 - 20);

            g.setFont(new Font("SansSerif", Font.PLAIN, 18));
            String sub = "Click 'Next' to continue";
            fm = g.getFontMetrics();
            textW = fm.stringWidth(sub);
            g.drawString(sub, x + (w - textW) / 2, y + h / 2 + 30);
        }
    }

    public void drawLevel_1(Graphics g){
        g.translate(camera.getX() * -1,camera.getY() * -1);

        // Draw background or fallback color
        if (imgPC != null && imgPC.getIconWidth() > 0) {
            imgPC.paintIcon(getPanel(), g, -800 + (pc.getX()*53)/57, -400 - pc.getY()/7);
        } else {
            g.setColor(new Color(135, 170, 210));
            g.fillRect(camera.getX(), camera.getY(), getWidth(), getHeight());
        }
        pc.draw(g);
        for(GameObject go : gameObjects){
            go.draw(g);
        }
        for(JumpSpot j : jumpSpots){
            j.draw(g);
        }
        for(Collectable a : coll){
            a.draw(g);
        }
        for(Enemy e : enemies){
            e.draw(g);
        }
        drawHUD(g);
    }
    public void drawLevel_2(Graphics g){

        g.translate(camera.getX() * -1,camera.getY() * -1);

        if (imgPC != null && imgPC.getIconWidth() > 0) {
            imgPC.paintIcon(getPanel(), g, -800 + (pc.getX()*53)/57, -600 + (pc.getY()*40)/57);
        } else {
            g.setColor(new Color(60, 70, 60));
            g.fillRect(camera.getX(), camera.getY(), getWidth(), getHeight());
        }

        pc.draw(g);
        for(GameObject go : gameObjects){
            go.draw(g);
        }
        for(JumpSpot j : jumpSpots){
            j.draw(g);
        }
        for(Collectable a : coll){
            a.draw(g);
        }
        for(Enemy e : enemies2){
            e.draw(g);
        }
        drawHUD(g);
    }
    public void drawLevel_3(Graphics g) {

        g.translate(camera.getX() * -1,camera.getY() * -1);

        if (imgPC != null && imgPC.getIconWidth() > 0) {
            imgPC.paintIcon(getPanel(), g, -800 + (pc.getX()*53)/57, -100 - (pc.getY()*5)/57);
        } else {
            g.setColor(new Color(80, 80, 100));
            g.fillRect(camera.getX(), camera.getY(), getWidth(), getHeight());
        }
        pc.draw(g);
        for(GameObject go : gameObjects){
            go.draw(g);
        }
        for(JumpSpot j : jumpSpots){
            j.draw(g);
        }
        for(Collectable a : coll){
            a.draw(g);
        }
        for(Enemy e : enemies3){
            e.draw(g);
        }
        drawHUD(g);
    }
    public void drawLevel_4(Graphics g) {
        g.translate(camera.getX() * -1,camera.getY() * -1);

        if (imgPC != null && imgPC.getIconWidth() > 0) {
            imgPC.paintIcon(getPanel(), g, camera.getX() + pc.getWidth()/2 - pc.getX()/23 , camera.getY());
        } else {
            g.setColor(new Color(120, 60, 40));
            g.fillRect(camera.getX(), camera.getY(), getWidth(), getHeight());
        }

        pc.draw(g);
        for(GameObject go : gameObjects){
            go.draw(g);
        }
        for(JumpSpot j : jumpSpots){
            j.draw(g);
        }
        for(Collectable a : coll){
            a.draw(g);
        }
        for(Enemy e : enemies4){
            e.draw(g);
        }
        drawHUD(g);
    }

    private void drawHUD(Graphics g) {
        g.setColor(Color.CYAN);
        g.fillRect(getWidth() + camera.getX() - 50,getHeight() + camera.getY() - 60, 25, pc.getAmmo()* 4);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
        g.drawString(pc.getAmmo() + "", getWidth() + camera.getX() - 50, getHeight() + camera.getY() - 65);
        g.setColor(Color.BLACK);
        g.fillRect(camera.getX() + 10, camera.getY() + 700, 20, 60);
        g.fillRect(camera.getX() + 40, camera.getY() + 700, 20, 60);
    }

    public void drawPause(Graphics g) {
        switch (level){
            case 1:
            drawLevel_1(g);
            break;
            case 2:
            drawLevel_2(g);
            break;
            case 3:
            drawLevel_3(g);
            break;
            case 4:
            drawLevel_4(g);
            break;
        }

        g.setColor(new Color(0, 0, 0, 90));
        g.fillRect(camera.getX(), camera.getY(), 1600, 800);
        g.setColor(new Color(93, 159, 183));
        g.fillRect(camera.getX(), camera.getY(), 600, 800);
        g.setColor(new Color(61, 106, 122));
        g.fillRect(camera.getX() + 600 - 15, camera.getY(), 15, 800);

        g.setColor(Color.WHITE);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
        g.drawString("PAUSE", camera.getX() + 30, camera.getY() + 30);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 15));
        g.drawString("Level " + level, camera.getX() + 30, camera.getY() + 60);
    }
    public void drawTitle(Graphics g){
        drawImageOrFallback(g, "KATSUYA'S REVENGE", 0, 0, getWidth(), getHeight());
    }
    public PlayerCharacter getPlayer(){
       return this.pc;
    }
    public ArrayList<GameObject> getGo(){
        return gameObjects;
    }
    public JPanel getPanel(){
        return this;
    }
    public JButton bLeft(){
        return this.bottomLeft;
    }
    public JButton tRight(){
        return this.topRight;
    }

    public int getLevel(){
        return this.level;
    }
    public void setSource(String src){
        this.source = src;
    }
    public void setState(GameState s, int l) {
        state = s;
       this.level = l;
    }
    public JButton getNext(){
        return next;
    }
    public JButton getPause(){
        return pause;
    }
    public GameState getState(){
        return this.state;
    }
    public void physicsUpdate(){
        if(state == GameState.LEVEL_1 ||
           state == GameState.LEVEL_2 ||
           state == GameState.LEVEL_3 ||
           state == GameState.LEVEL_4){
            ArrayList<Enemy> en = new ArrayList<Enemy>();
            switch(state){
                case LEVEL_1:
                    en = enemies;
                    break;
                case LEVEL_2:
                    en = enemies2;
                    break;
                case LEVEL_3:
                    en = enemies3;
                    break;
                case LEVEL_4:
                    en = enemies4;
                    break;
            }
            pc.move(gameObjects);
            for(int i = gameObjects.size()-1; i >= 0; i--){
                GameObject go = gameObjects.get(i);
                if(go instanceof Bullet){
                    ((Bullet)go).move(gameObjects);
                    for(int j = gameObjects.size()-1; j >= 0; j--){
                        GameObject g = gameObjects.get(j);
                        if(g instanceof Platform){
                            if(g.hit(go)){
                                ((Bullet)go).setSpeed(0);
                            }
                        }
                    }
                    for(int j = en.size()-1; j >= 0; j--){
                        Enemy g = en.get(j);
                        if(g.hit(((Bullet)go)) && !g.getHit().contains(go) && ((Bullet)go).isUsable()){
                            ((Bullet)go).setUsable(false);
                            g.getHit().add(go);
                            if(((Enemy)g).getLife() <= 1){
                                en.remove(g);
                            } else {
                                g.setLife(g.getLife() - 1);
                            }
                            go.setVisible(false);
                        }
                    }
                }
            }

            for(int i = coll.size() - 1; i >= 0 ; i--){
                Collectable c = coll.get(i);
                if(c instanceof Ammo){
                    if(c.hit(pc) && pc.getAmmo() < 10){
                        if(pc.getAmmo() >= 5){
                            pc.setAmmo(10);
                        } else {
                            pc.setAmmo(pc.getAmmo() + 5);
                        }
                        coll.remove(c);
                        try{
                            if(sounds != null && sounds.getClips("powerUp") != null)
                                sounds.getClips("powerUp").loop(0);
                        } catch (Exception e){
                            System.out.println(e);
                        }
                    }
                } else if(c instanceof Health){
                    if(c.hit(pc) && pc.getLife() < 10){
                        if(pc.getLife() >= 6){
                            pc.setLife(10);
                        } else {
                            pc.setLife(pc.getLife() + 4);
                        }
                        coll.remove(c);
                        try{
                            if(sounds != null && sounds.getClips("powerUp") != null)
                                sounds.getClips("powerUp").loop(0);
                        } catch (Exception e){
                            System.out.println(e);
                        }
                    }
                }
            }
            for(int i = en.size() - 1; i >= 0; i--){
                Enemy e = en.get(i);
                e.fall(gameObjects);
                e.detect(pc, gameObjects);
                if(state == GameState.LEVEL_1){
                    if(e.getY() >= 900){
                        en.remove(e);
                    }
                }
            }
            pc.fall(gameObjects);
            camera.follow(pc, 0, offset);
        }
    }
}
