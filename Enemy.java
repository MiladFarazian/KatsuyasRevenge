import java.awt.*;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import javax.sound.sampled.Clip;
public class Enemy extends Rigidbody{
    private int MAX_SPEED = 4;
    private final static int JUMP_FORCE = -18;
    private boolean inAir = true;
    private double attackDelay = 5;
    private int attackDamage;
    private CircleCollider detectCircle;
    private int health;
    private ArrayList hitMe = new ArrayList();
    private int c;
    private SoundLibrary sounds;
    private ImageIcon imgPC;
    public Enemy(JPanel j, int x, int y, String src) throws Exception{
        this(j, x,y,src,1,2);
    }
    public Enemy(JPanel j, int x, int y, String src, int aDamage, int health_, boolean boss, int speed, int radius) throws Exception{
        super(j, x, y, 72, 81);
        BoxCollider bc = new BoxCollider(this,0,getHeight()/2,getWidth(),getHeight()/2 - 5);
        getColliders().add(bc);
        CircleCollider cc = new CircleCollider(this, 0, 0, getWidth()/2);
        getColliders().add(cc);
        this.attackDamage = aDamage;
        detectCircle = new CircleCollider(this, -radius + getWidth()/2, -200 + getHeight()/2, radius);
        this.MAX_SPEED = speed;
        this.health = health_;
        c = this.health * 12;
        try { sounds = new SoundLibrary("x"); } catch (Exception e) {}
        imgPC = new ImageIcon(ResourcePath.resolve(src));
    }
    public Enemy(JPanel jpanel, int x, int y, String src, int aDamage, int health_) throws Exception{
        this(jpanel, x, y, 72, 81, src, aDamage, health_, false, 4);
    }
    public Enemy(JPanel jpanel, int x, int y, String src, int aDamage, int health_, boolean boss, int speed) throws Exception{
        super(jpanel, x, y, 72, 81);
        BoxCollider bc = new BoxCollider(this,0,getHeight()/2,getWidth(),getHeight()/2 - 5);
        getColliders().add(bc);
        CircleCollider cc = new CircleCollider(this, 0, 0, getWidth()/2);
        getColliders().add(cc);
        this.attackDamage = aDamage;
        if(boss){
            detectCircle = new CircleCollider(this, -600 + getWidth()/2, -200 + getHeight()/2, 600);
        }
        this.MAX_SPEED = speed;
        this.health = health_;
        c = this.health * 12;
        try { sounds = new SoundLibrary("x"); } catch (Exception e) {}
        imgPC = new ImageIcon(ResourcePath.resolve(src));
    }
    public Enemy(JPanel jpanel, int x, int y, int w, int h, String src, int aDamage, int health_, boolean boss, int speed) throws Exception{
        super(jpanel, x, y, w, h);
        BoxCollider bc = new BoxCollider(this,0,getHeight()/2,getWidth(),getHeight()/2 - 5);
        getColliders().add(bc);
        CircleCollider cc = new CircleCollider(this, 0, 0, getWidth()/2);
        getColliders().add(cc);
        this.attackDamage = aDamage;
        detectCircle = new CircleCollider(this, -200 + getWidth()/2, -200 + getHeight()/2, 200);
        this.health = health_;
        c = this.health * 12;
        try { sounds = new SoundLibrary("x"); } catch (Exception e) {}
        if(boss){
            detectCircle = new CircleCollider(this, -600 + getWidth()/2, -200 + getHeight()/2, 600);
        }
        this.MAX_SPEED = speed;
        imgPC = new ImageIcon(ResourcePath.resolve(src));
    }
    public ArrayList getHit(){
        return this.hitMe;
    }
    public void detect(PlayerCharacter pc, ArrayList<GameObject> go){
        attackDelay -= .1;
        if(detectCircle != null && detectCircle.hit((GameObject)pc)){
            if(!pc.hit(this) && (getX() + getWidth() < pc.getX() || getX() > pc.getX() + pc.getWidth())){
                if(pc.getX() > getX()){
                    speedRight();
                } else if (pc.getX() <= getX()){
                    speedLeft();
                }
            } else if(pc.hit(this) && attackDelay <= 0){
                if(pc.getLife() <= attackDamage){
                    pc.setLife(0);
                }
                pc.reduceLife(this.attackDamage);

                if(sounds != null && sounds.getClips("sword") != null)
                    sounds.getClips("sword").loop(0);
                attackDelay = 5;
            } else {
                setMoveSpeed(0);
            }
            move(go);
        }
    }
    public void setLife(int l){
        if(l >= 1){
            this.health = l;
        }
    }
    public int getLife(){
        return this.health;
    }
    public void detect(ArrayList<JumpSpot> js){
        for(JumpSpot j : js){
            if(hit(j)){
                setFallSpeed(JUMP_FORCE);
            }
        }
    }
    public void jump(){
        if(inAir){return;}
        setFallSpeed(JUMP_FORCE);
        inAir = true;
    }
    public void speedLeft(){
        if(speed >= -1 * MAX_SPEED){
            speed --;
        }
    }
    public void speedRight(){
        if(speed <=  MAX_SPEED){
            speed ++;
        }
    }
    public void slowDown(){
        if(speed > 0){
            speed--;
        } else if(speed < 0){
            speed++;
        }
    }
    public void draw(Graphics g) {
        if(isVisible()){
            if(imgPC != null && imgPC.getIconWidth() > 0) {
                imgPC.paintIcon(getPanel(), g, getX(), getY());
            } else {
                // Fallback: draw enemy as a red rectangle
                g.setColor(new Color(180, 40, 40));
                g.fillRect(getX(), getY(), getWidth(), getHeight());
                g.setColor(Color.BLACK);
                g.drawRect(getX(), getY(), getWidth(), getHeight());
            }
        }
        for(Collider c: getColliders()){
            //c.draw(g);
        }
        g.setColor(Color.RED);
        g.fillRect(getX(),getY() - getHeight()/2,c , 10);
        g.setColor(Color.GREEN);
        g.fillRect(getX(),getY() - getHeight()/2, this.health * 12, 10);
    }
    public String toString(){
        String str = "Enemy(X: " + getX() + ", Y: " + getY() + ", Width: " + getWidth() + ", Height: " + getHeight()+")";
        return str;
    }
}
