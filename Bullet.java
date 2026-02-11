import java.awt.Graphics;
import javax.swing.JPanel;
import java.awt.Color;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.sound.sampled.Clip;

public class Bullet extends GameObject
{
    private int x,y,w,h, deltaX, deltaY,dir;
    private ArrayList<GameObject> go;
    private int speed = 15;
    private boolean die = false;
    private double deathTime = 5;
    private boolean attached = false;
    private boolean use = true;
    private SoundLibrary sounds;
    private ImageIcon imgPC;
    public Bullet(ArrayList<GameObject> k,JPanel jp, int x_, int y_,  int w_, int h_, int dir)
    {
        super(jp, x_, y_, w_, h_);
        BoxCollider bc = new BoxCollider(this, 0,0,w_,h_);
        getColliders().add(bc);
        this.dir = dir;
        go = k;
        if(dir > 0){
            imgPC  = new ImageIcon(ResourcePath.resolve("images/throwingKnifeRight.png"));
            setX(getX() + 25);
        } else {
            imgPC  = new ImageIcon(ResourcePath.resolve("images/throwingKnifeLeft.png"));
            setX(getX() - 70);
        }
        try{
             sounds = new SoundLibrary("x");
        } catch (Exception e ){

        }
        if(sounds != null && sounds.getClips("throw") != null)
            sounds.getClips("throw").loop(0);
    }
    public boolean isUsable(){
        return use;
    }
    public void setUsable(boolean b){
        this.use = b;
    }
    public void move(ArrayList<GameObject> k){
        setX(getX() + (speed*dir));

        deathTime-= .1;
        if(deathTime <= 0){
            k.remove(this);
        }
    }
    public boolean willAttach(){
        return this.attached;
    }
    public void attach(GameObject g, Camera c){
        if(dir > 0){
            setX(g.getX());
        } else {
            setX(g.getX() + g.getWidth());
        }
    }
    public void setSpeed(int s){
        speed = 0;
    }
    public boolean willDie(){
        return die;
    }
    public void draw(Graphics g){
        if(isVisible()){
            if(imgPC != null && imgPC.getIconWidth() > 0) {
                imgPC.paintIcon(getPanel(), g, getX(), getY());
            } else {
                // Fallback: draw knife as a small colored rectangle
                g.setColor(new Color(200, 200, 200));
                g.fillRect(getX(), getY(), getWidth(), getHeight());
                g.setColor(Color.DARK_GRAY);
                g.drawRect(getX(), getY(), getWidth(), getHeight());
            }
            for(Collider c: getColliders()){
                //c.draw(g);
            }
        }
    }
    public String toString(){
        String str = "Bullet(X: " + getX() + ", Y: " + getY() + ", Width: " + getWidth() + ", Height: " + getHeight()+")";
        return str;
    }
}
