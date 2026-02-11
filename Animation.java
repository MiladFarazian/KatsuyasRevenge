import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Animation extends ScreenObject{

    private ImageIcon spriteSheet;
    private int width, height;
    private GameObject gameObject = null;   // null when animation does not belong to a GameObject

    private int cycles;     // number of times animation should loop, 0 is infinite.
    private int currentCycle = 0;

    private int[] numFrames;    // number of frames in each row (motion clip)
    private int currentClip;    // clip (row) of the spritesheet
    private int currentFrame;   // frame (col) of the spritesheet
    private int count = 1;
    public Animation(GameObject go, String src, int[] numFrames) {
        this(go.getPanel(), go.getX(), go.getY(), go.getWidth(), go.getHeight(), src, numFrames, 0);
        gameObject = go;
    }

    public Animation(GameObject go, String src, int[] numFrames, int cycles) {
        this(go.getPanel(), go.getX(), go.getY(), go.getWidth(), go.getHeight(), src, numFrames, cycles);
        gameObject = go;
    }

    public Animation(JPanel jp, int x, int y, int width, int height, String src, int[] numFrames, int cycles) {
        super(jp, x, y);

        try{
            spriteSheet = new ImageIcon(ResourcePath.resolve(src));
        }
        catch(Exception e) {
            System.out.println(e);
        }

        this.width = width;
        this.height = height;
        this.numFrames = numFrames;
        this.cycles = cycles;
        currentClip = 0;
        currentFrame = 0;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void draw(Graphics g) {
        drawFrame((Graphics2D) g);
    }

    public void setMotionState(int st) {
        if(st != currentClip) {
            currentClip = st;
            currentFrame = 0;
        }
    }

    private void drawFrame(Graphics2D g2d){
        if(cycles == 0 || currentCycle < cycles) {
            if(currentFrame == numFrames[currentClip]) {
                currentFrame = 0;
                currentCycle++;
            }
            int frameX = currentFrame * width;
            int frameY = currentClip * height;

            if(gameObject != null) {
                setX(gameObject.getX());
                setY(gameObject.getY());
            }

            if(spriteSheet != null && spriteSheet.getIconWidth() > 0) {
                g2d.drawImage(spriteSheet.getImage(),
                        getX(), getY(),
                        this.getX() + this.width, this.getY() + this.height,
                        frameX, frameY,
                    frameX + this.width, frameY + this.height,
                        null);
            } else {
                // Fallback: draw player as a colored rectangle
                g2d.setColor(new Color(60, 120, 200));
                g2d.fillRect(getX(), getY(), width, height);
                g2d.setColor(Color.WHITE);
                g2d.drawRect(getX(), getY(), width, height);
                g2d.drawString("P", getX() + width/2 - 4, getY() + height/2 + 4);
            }

            count++;
            if(count % 7 == 0){
                currentFrame++;
                count = 1;
            }
        }
    }
}
