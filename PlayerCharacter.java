import java.awt.Graphics;
import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import javax.sound.sampled.Clip;

public class PlayerCharacter extends Rigidbody{
    private final static int MAX_SPEED = 5;
    private final static int JUMP_FORCE = -18;
    private CircleCollider c;
    public BoxCollider rightCollider, leftCollider;
    private Platform pF = null;
    private int ammo = 10;
    private Animation animation;
    private int life = 10;
    private SoundLibrary sounds;
    public PlayerCharacter(JPanel jp, String src, int x, int y, int w, int h, int[] numFrames, int cycles) throws Exception{
        super(jp, x, y, w, h);
        BoxCollider bc = new BoxCollider(this,0,0,getWidth(), getHeight());
        getColliders().add(bc);
        try{
             sounds = new SoundLibrary("x");
        } catch (Exception e ){

        }
        animation = new Animation(this, src, numFrames);
        animation.setMotionState(0);
    }
    public void setAmmo(int a){
        this.ammo = a;
    }
    public int getAmmo(){
        return this.ammo;
    }
    public void setLife(int l){
        this.life = l;
    }
    public void throwAnim(){
        animation.setMotionState(PlayerMotionState.THROW);
    }
    public void reduceLife(int l){
        this.life -= l;
        if(this.life < 0){
            this.life += l;
            animation.setMotionState(PlayerMotionState.DIE);
            if(sounds != null && sounds.getClips("run") != null)
                sounds.getClips("run").stop();
        }
    }
    public int getLife(){
        return this.life;
    }
    public PlayerCharacter(JPanel jpanel, int x, int y, int w, int h, String src) throws Exception{
        this(jpanel, src, x, y, w, h, new int[] {1}, 0);
    }
    public void setPlatform(Platform p){
        pF = p;
    }
    public Collider getRightCollider(){
        return this.rightCollider;
    }
    public Collider getLeftCollider(){
        return this.leftCollider;
    }
    public void jump(){
        if(isInAir()){return;}
        setFallSpeed(JUMP_FORCE);
        setInAir(true);

        if(sounds != null && sounds.getClips("jump") != null)
            sounds.getClips("jump").loop(0);
    }
    public void speedLeft(){
        if(speed >= -1 * MAX_SPEED){
            speed --;
            if(speed <= -5 && !isInAir()){
                if(sounds != null && sounds.getClips("run") != null)
                    sounds.getClips("run").loop(Clip.LOOP_CONTINUOUSLY);
            }
        }

        animation.setMotionState(PlayerMotionState.RUN_LEFT);
    }
    public void speedRight(){
        if(speed <=  MAX_SPEED){
            speed ++;
            if(speed >= 5 && !isInAir()){
                if(sounds != null && sounds.getClips("run") != null)
                    sounds.getClips("run").loop(Clip.LOOP_CONTINUOUSLY);
            }
        }

        animation.setMotionState(PlayerMotionState.RUN_RIGHT);
    }
    public void startRun(){
        if(sounds != null && sounds.getClips("run") != null)
            sounds.getClips("run").loop(Clip.LOOP_CONTINUOUSLY);
    }
    public void stopRun(){
        if(sounds != null && sounds.getClips("run") != null)
            sounds.getClips("run").stop();
    }
    public void slowDown(){
        if(speed > 0){
            speed--;
        } else if(speed < 0){
            speed++;
        } else if(this.life > 0){
            animation.setMotionState(PlayerMotionState.IDLE);
        }
        if(sounds != null && sounds.getClips("run") != null)
            sounds.getClips("run").stop();
    }
    @Override
    public void draw(Graphics g) {
        if(isVisible()){
            animation.draw(g);
        }
        for(Collider c: getColliders()){
            //c.draw(g);
        }

        //Lifebar
        g.setColor(Color.RED);
        g.fillRect(getX(),getY() - getHeight()/2, 80, 10);
        g.setColor(Color.GREEN);
        g.fillRect(getX(),getY() - getHeight()/2, this.life * 8, 10);
    }
    public String toString(){
        String str = "Player(X: " + getX() + ", Y: " + getY() + ", Width: " + getWidth() + ", Height: " + getHeight()+")";
        return str;
    }
}
