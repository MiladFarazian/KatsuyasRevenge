import java.awt.Graphics;
import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import java.util.ArrayList;

public class Collectable extends GameObject
{
    private CircleCollider c;
    private String src;
    private ImageIcon cachedImage;
    public Collectable(JPanel jp, String s, int x, int y, int w, int h){
        super(jp, x, y, w, h);
        this.src = s;
        c = new CircleCollider(this,0,0,w/2);
        getColliders().add(c);
    }
    @Override
    public void draw(Graphics g) {
        if(isVisible()){
            if(cachedImage == null) {
                cachedImage = new ImageIcon(ResourcePath.resolve(src));
            }
            if(cachedImage.getIconWidth() > 0) {
                cachedImage.paintIcon(getPanel(), g, getX(), getY());
            } else {
                // Fallback: draw as colored circle
                boolean isHealth = (this instanceof Health);
                g.setColor(isHealth ? new Color(50, 200, 50) : new Color(100, 150, 255));
                g.fillOval(getX(), getY(), getWidth(), getHeight());
                g.setColor(Color.WHITE);
                g.drawString(isHealth ? "HP" : "AM", getX() + 10, getY() + 30);
            }
        }
        for(Collider c: getColliders()){
            //c.draw(g);
        }
    }
    public String toString(){
        String str = "Collectable(X: " + getX() + ", Y: " + getY() + ", Width: " + getWidth() + ", Height: " + getHeight()+")";
        return str;
    }
}
