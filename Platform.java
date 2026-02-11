import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import javax.swing.ImageIcon;
public class Platform extends GameObject{
    private boolean visible = true;
    private Color color = new Color(150,170,15, 100);
    private boolean isOscelating = false;
    private boolean right = true;
    private int dir = 0;
    private String tag;
    private String source;
    private ImageIcon cachedImage;
    public Platform(JPanel jp, int x_,  int y_, int w_, int h_){
        this(jp,x_,y_,w_,h_, new Color(150,170,15, 100), false, "");
    }

    public Platform(JPanel jp, int x_,  int y_, int w_, int h_, Color c, boolean o, String t){
        super(jp,x_,y_,w_,h_);
        this.color = c;
        BoxCollider bc = new BoxCollider(this,0,0,w_,h_);
        bc.setVisible(true);
        getColliders().add(bc);
        this.isOscelating = o;
        this.tag = t;
    }

    public void setOscelate(boolean v){
        this.isOscelating = v;
    }

    public boolean willOscelate(){
        return this.isOscelating;
    }

    public String getTag(){
        return this.tag;
    }

    public void oscelate(int minx, int maxx, PlayerCharacter p){
        this.isOscelating = true;
        if(this.right && getX() <= maxx){
            moveRight(1);
            dir = 1;
            for(Collider c : p.getColliders()){
                if(c.hit(this)){
                    p.setX(p.getX() + 1);

                }
            }
            if(getX() >= maxx){
                moveLeft(1);
                this.right = false;
            }
        } else if(!this.right && getX() >= minx){
            moveLeft(1);
            dir = -1;
            for(Collider c : p.getColliders()){
                if(c.hit(this)){
                    p.setX(p.getX() - 1);
                }
            }
            if(getX() <= minx){
                moveRight(1);
                this.right = true;
            }
        }
    }
    public int getSpeed(){
        return dir;
    }
    public void moveRight(int amt){
        setX(getX() + amt);
    }
    public void setSource(String src){
        this.source = src;
        this.cachedImage = null;
    }

    public void moveLeft(int amt){
        setX(getX() - amt);
    }

    public void draw(Graphics g){
        if(isVisible()){
            if(source != null && !source.isEmpty()) {
                if(cachedImage == null) {
                    String resolved = ResourcePath.resolve(source);
                    cachedImage = new ImageIcon(resolved);
                }
                if(cachedImage.getIconWidth() > 0) {
                    cachedImage.paintIcon(getPanel(), g, getX(), getY());
                } else {
                    g.setColor(this.color);
                    g.fillRect(getX(), getY(), getWidth(), getHeight());
                }
            } else {
                g.setColor(this.color);
                g.fillRect(getX(), getY(), getWidth(), getHeight());
            }
        }
        for(Collider c: getColliders()){
            //c.draw(g);
        }
    }
    public String toString(){
        String str = "Platform(X: " + getX() + ", Y: " + getY() + ", Width: " + getWidth() + ", Height: " + getHeight()+")";
        return str;
    }
}
