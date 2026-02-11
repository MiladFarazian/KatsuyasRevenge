import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JFrame;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
public class GameController extends JFrame implements KeyListener,MouseListener{
    private static final long serialVersionUID = 1L;

    private GamePanel gamePanel;
    private TitlePanel titlePanel;
    private boolean aPressed, wPressed, dPressed, spacePressed, aReleased,dReleased; // default values are false

    private Timer gameloopTimer;
    private static final int GAMELOOP_FREQUENCY = 20;   // 50 fps
    private SoundLibrary sounds;
    private Animation animation;
    private int deathTimer = 0;
    private static final int DEATH_DELAY = 80; // ~1.6 seconds at 50fps before restart
    public GameController(int width, int height) throws Exception{

        try {
            sounds = new SoundLibrary("x");
        } catch (Exception e) {
            System.out.println("Warning: Could not load sounds: " + e.getMessage());
        }

        setFocusable(true);
        addMouseListener(this);
        customCursor();
        addKeyListener(this);
        setTitle("KATSUYA'S REVENGE");
        setSize(width, height);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            gamePanel = new GamePanel(width, height);
        getContentPane().add(gamePanel);
        titlePanel = new TitlePanel();

        setComponentZOrder(gamePanel, 0);

        setVisible(true);

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run(){
                ActionListener gameloopListener = new ActionListener() {
                    public void actionPerformed(ActionEvent actionEvent) {
                        gameloop();
                    }
                };
                gameloopTimer = new Timer(GAMELOOP_FREQUENCY, gameloopListener);
                gameloopTimer.start();
            }
        });
    }

    private void gameloop(){
        this.requestFocus();
        PlayerCharacter pc = gamePanel.getPlayer();
        boolean isInLevel = gamePanel.getState() == GameState.LEVEL_1 ||
                            gamePanel.getState() == GameState.LEVEL_2 ||
                            gamePanel.getState() == GameState.LEVEL_3 ||
                            gamePanel.getState() == GameState.LEVEL_4;

        if(isInLevel && pc.getLife() > 0) {
           deathTimer = 0;
           setVisible(true);
           if(spacePressed){pc.jump();}
           if(aPressed){pc.speedLeft();}
           if(dPressed){pc.speedRight();}
           if(!aPressed && !dPressed || (aPressed && dPressed)){pc.slowDown();}
           if(gamePanel.getState() == GameState.LEVEL_1){
               if(pc.getX() > 9000 && pc.getY() > 800){
                    pc.stopRun();
                    gamePanel.setState(GameState.BETWEEN_2,2);
                    gamePanel.getNext().setVisible(true);
                    gamePanel.getPause().setVisible(false);
                    gamePanel.getNext().setBounds(1200, 650, 320, 75);
               } else if((pc.getX() <= 8999 && pc.getY() > 800)){
                   try{
                       gamePanel.setLevel(1);
                    } catch (Exception e){

                    }
               }
           } else if(gamePanel.getState() == GameState.LEVEL_2){
               if(pc.getX() > 6600 && pc.getY() < 1120){
                    pc.stopRun();
                    gamePanel.setState(GameState.BETWEEN_3,3);
                    gamePanel.getNext().setVisible(true);
                    gamePanel.getPause().setVisible(false);

                    gamePanel.getNext().setBounds(1200, 650, 320, 75);
               } else if((pc.getX() <= 6600 && pc.getY() > 1400)){
                   try{
                       gamePanel.setLevel(2);
                    } catch (Exception e){

                    }
               }
           }else if(gamePanel.getState() == GameState.LEVEL_3){
               if(pc.getX() > 8680 && pc.getY() < 8860){
                    pc.stopRun();
                    gamePanel.setState(GameState.BETWEEN_4,4);
                    gamePanel.getNext().setVisible(true);
                    gamePanel.getPause().setVisible(false);

                    gamePanel.getNext().setBounds(1200, 650, 320, 75);
               }else if((pc.getX() <= 8679 && pc.getY() > 700)){
                   try{
                       gamePanel.setLevel(3);
                    } catch (Exception e){

                    }
               }
           } else if(gamePanel.getState() == GameState.LEVEL_4){

               if(pc.getX() > 4000){
                    pc.stopRun();
                    gamePanel.setState(GameState.END,5);
                    gamePanel.getNext().setVisible(false);
                    gamePanel.getPause().setVisible(false);
                    gamePanel.getQuit().setVisible(true);
               }
           }
           gamePanel.physicsUpdate();
           gamePanel.repaint();
        } else if(isInLevel && pc.getLife() <= 0) {
           // Player is dead - show death animation then restart level
           deathTimer++;
           gamePanel.repaint();
           if(deathTimer >= DEATH_DELAY) {
               deathTimer = 0;
               try {
                   gamePanel.setLevel(gamePanel.getLevel());
               } catch (Exception e) {}
           }
        }
    }
    @Override
    public void mousePressed(MouseEvent m){
            PlayerCharacter pc = gamePanel.getPlayer();
            if(pc.getAmmo() >= 1){
                int direction = m.getXOnScreen() - pc.getX() + gamePanel.getCam().getX();
                if(direction >= 0){
                    direction = 1;
                } else {direction = -1;}

                Bullet b = new Bullet(gamePanel.getGo(), gamePanel.getPanel(), pc.getX() + pc.getWidth()/2,pc.getY() + pc.getHeight()/2, 55,15, direction);
                gamePanel.getGo().add(b);
                pc.setAmmo(pc.getAmmo() - 1);
            }
    }
    public void mouseClicked(MouseEvent m){

    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_A) aPressed = true;
        else if(e.getKeyCode() == KeyEvent.VK_W) wPressed = true;
        else if(e.getKeyCode() == KeyEvent.VK_D) dPressed = true;
        else if(e.getKeyCode() == KeyEvent.VK_SPACE) spacePressed = true;
    }

    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_A) aPressed = false;
        else if(e.getKeyCode() == KeyEvent.VK_W) wPressed = false;
        else if(e.getKeyCode() == KeyEvent.VK_D) dPressed = false;
        else if(e.getKeyCode() == KeyEvent.VK_SPACE) spacePressed = false;
    }

    public void keyTyped(KeyEvent e) {}

    public void customCursor(){
        try {
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            String cursorPath = ResourcePath.resolve("images/crosshair.png");
            java.io.File cursorFile = new java.io.File(cursorPath);
            if (cursorFile.exists()) {
                Image img = toolkit.getImage(cursorPath);
                Point point = new Point(10,10);
                Cursor cursor = toolkit.createCustomCursor(img, point, "Cursor");
                setCursor(cursor);
            } else {
                setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
            }
        } catch (Exception e) {
            setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        }
    }
}
