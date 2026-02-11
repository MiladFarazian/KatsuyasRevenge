import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * Utility to generate placeholder images for Katsuya's Revenge.
 * Run once to create the images/ directory with all needed assets.
 * Replace these with the original art when available.
 */
public class GeneratePlaceholders {

    public static void main(String[] args) throws Exception {
        String base = new File("").getAbsolutePath() + File.separator;
        // If run from the KatsuyasRevenge directory, use that; otherwise try to detect
        if (!new File(base + "Main.java").exists()) {
            // Try the class location
            try {
                base = GeneratePlaceholders.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
                File f = new File(base);
                if (f.isFile()) f = f.getParentFile();
                base = f.getAbsolutePath() + File.separator;
            } catch (Exception e) {
                base = "";
            }
        }

        String imgDir = base + "images" + File.separator;
        String bgDir = imgDir + "Backgrounds" + File.separator;
        new File(bgDir).mkdirs();

        // ===== Player sprite sheet =====
        // 5 rows (motion states), max 8 columns, each frame 82x82
        // Row 0: RUN_RIGHT (5 frames), Row 1: THROW (2), Row 2: IDLE (8), Row 3: DIE (2), Row 4: RUN_LEFT (5)
        int[] frameCounts = {5, 2, 8, 2, 5};
        int maxCols = 8;
        int fw = 82, fh = 82;
        BufferedImage spriteSheet = new BufferedImage(maxCols * fw, frameCounts.length * fh, BufferedImage.TYPE_INT_ARGB);
        Graphics2D sg = spriteSheet.createGraphics();
        sg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Color[] rowColors = {
            new Color(60, 120, 200),   // run right - blue
            new Color(200, 200, 60),   // throw - yellow
            new Color(60, 120, 200),   // idle - blue
            new Color(200, 60, 60),    // die - red
            new Color(60, 120, 200)    // run left - blue
        };
        String[] rowLabels = {"R>", "T", "I", "D", "<L"};
        for (int row = 0; row < frameCounts.length; row++) {
            for (int col = 0; col < frameCounts[row]; col++) {
                int x = col * fw, y = row * fh;
                sg.setColor(rowColors[row]);
                sg.fillRoundRect(x + 8, y + 4, fw - 16, fh - 8, 12, 12);
                sg.setColor(Color.WHITE);
                sg.drawRoundRect(x + 8, y + 4, fw - 16, fh - 8, 12, 12);
                // Head
                sg.setColor(new Color(240, 210, 170));
                sg.fillOval(x + 24, y + 8, 34, 30);
                // Body label
                sg.setColor(Color.WHITE);
                sg.setFont(new Font("SansSerif", Font.BOLD, 14));
                sg.drawString(rowLabels[row] + (col+1), x + 22, y + 62);
            }
        }
        sg.dispose();
        ImageIO.write(spriteSheet, "png", new File(imgDir + "pcNewAttempt2.png"));
        System.out.println("Created: pcNewAttempt2.png");

        // ===== Enemy images =====
        createEnemy(imgDir + "enemyRanger.png", 72, 81, new Color(180, 40, 40), "E");
        createEnemy(imgDir + "boss.png", 72, 81, new Color(160, 30, 100), "B");
        createEnemy(imgDir + "boss2.png", 72, 81, new Color(100, 30, 160), "B2");

        // ===== Bullet / Knife images =====
        createKnife(imgDir + "throwingKnifeRight.png", 55, 15, true);
        createKnife(imgDir + "throwingKnifeLeft.png", 55, 15, false);
        createSimple(imgDir + "throwingKnifeAmmo.png", 50, 50, new Color(100, 150, 255), "AMMO");

        // ===== Collectibles =====
        createCollectable(imgDir + "health.png", 50, 50, new Color(50, 200, 50), "+HP");

        // ===== Crosshair =====
        createCrosshair(imgDir + "crosshair.png", 32, 32);

        // ===== Platform images =====
        // Level 1 platforms
        createPlatform(imgDir + "base1.png", 2200, 300, new Color(112, 72, 43), "Ground");
        createPlatform(imgDir + "base2.png", 800, 300, new Color(130, 85, 50), "Ground");
        createPlatform(imgDir + "base3.png", 400, 525, new Color(100, 70, 40), "Cliff");
        createPlatform(imgDir + "base4.png", 200, 450, new Color(90, 80, 70), "Rock");
        createPlatform(imgDir + "rock.png", 90, 40, new Color(140, 130, 120), "");
        createPlatform(imgDir + "stone.png", 100, 50, new Color(150, 140, 130), "");
        createPlatform(imgDir + "enemyPlatform.png", 400, 50, new Color(100, 80, 60), "");
        createPlatform(imgDir + "smallEnemyPlatform.png", 150, 50, new Color(100, 80, 60), "");
        createPlatform(imgDir + "moving platform1.png", 400, 50, new Color(60, 160, 60), "~");
        createPlatform(imgDir + "largeStilt.png", 10, 575, new Color(90, 65, 40), "");
        createPlatform(imgDir + "mediumStilt.png", 10, 425, new Color(90, 65, 40), "");
        createPlatform(imgDir + "smallStilt.png", 10, 100, new Color(90, 65, 40), "");
        createPlatform(imgDir + "miniStilt.png", 10, 50, new Color(90, 65, 40), "");
        createPlatform(imgDir + "miniBase.png", 100, 250, new Color(110, 75, 45), "");
        createSign(imgDir + "sign1.png", 200, 200, new Color(160, 120, 60), "Level 1");
        createPlatform(imgDir + "pipe2.png", 20, 525, new Color(80, 80, 90), "");

        // Level 2 platforms
        createPlatform(imgDir + "sewer1.png", 2200, 300, new Color(60, 70, 60), "Sewer");
        createPlatform(imgDir + "sewer2.png", 195, 375, new Color(70, 80, 70), "");
        createPlatform(imgDir + "sewer3.png", 300, 25, new Color(50, 60, 50), "");
        createPlatform(imgDir + "sewer4.png", 300, 25, new Color(55, 65, 55), "");
        createPlatform(imgDir + "pipe1.png", 195, 575, new Color(80, 90, 80), "Pipe");
        createPlatform(imgDir + "extraPipe.png", 10, 575, new Color(80, 90, 80), "");
        createSign(imgDir + "sign2.png", 200, 200, new Color(60, 100, 60), "Level 2");
        createArrow(imgDir + "downArrow.png", 40, 40, true);
        createArrow(imgDir + "upArrow.png", 40, 40, false);

        // Level 3 platforms
        createSign(imgDir + "sign3.png", 200, 1000, new Color(140, 50, 50), "Level 3");
        createPlatform(imgDir + "pagota1.png", 225, 25, new Color(180, 50, 50), "");
        createPagoda(imgDir + "pagota2.png", 200, 200, new Color(180, 50, 50));
        createPlatform(imgDir + "ledge.png", 560, 25, new Color(140, 120, 100), "");
        createPlatform(imgDir + "pillar.png", 30, 80, new Color(160, 150, 140), "");
        createDragonbird(imgDir + "dragonbird.png", 80, 60);
        createDoor(imgDir + "evil door.png", 80, 120);

        // ===== Background images =====
        createBackground(bgDir + "mountain_background1-2.jpg", 3200, 1600,
            new Color(135, 170, 210), new Color(80, 130, 80), "Mountain Pass");
        createBackground(bgDir + "sewer2.jpg", 3200, 1600,
            new Color(40, 50, 40), new Color(30, 40, 30), "The Sewers");
        createBackground(bgDir + "city2.jpg", 3200, 1600,
            new Color(60, 60, 90), new Color(40, 40, 60), "The City");
        createBackground(bgDir + "Dojo_Background2.jpg", 3200, 1600,
            new Color(120, 60, 40), new Color(80, 40, 25), "The Dojo");

        // ===== Story page images (1600x800) =====
        createStoryPage(imgDir + "Page1-2.PNG", "KATSUYA'S REVENGE",
            "A tale of honor and vengeance", new Color(20, 20, 30), new Color(200, 180, 140));
        createStoryPage(imgDir + "Page2.PNG", "PROLOGUE - Part 1",
            "The story begins...", new Color(30, 20, 20), new Color(200, 160, 140));
        createStoryPage(imgDir + "Page3.PNG", "PROLOGUE - Part 2",
            "The journey continues...", new Color(20, 20, 30), new Color(180, 180, 200));
        createStoryPage(imgDir + "Page4.PNG", "CHAPTER 2",
            "Into the sewers...", new Color(20, 30, 20), new Color(140, 200, 140));
        createStoryPage(imgDir + "PAGE5.PNG", "CHAPTER 3",
            "The city awaits...", new Color(20, 20, 35), new Color(160, 160, 220));
        createStoryPage(imgDir + "PAGE6.PNG", "CHAPTER 4",
            "The final confrontation...", new Color(35, 15, 15), new Color(220, 160, 140));
        createStoryPage(imgDir + "Page7.PNG", "THE END",
            "Thank you for playing Katsuya's Revenge!", new Color(10, 10, 15), new Color(220, 200, 160));

        System.out.println("\nAll placeholder images generated successfully!");
        System.out.println("Replace them with the original art when available.");
    }

    static void createEnemy(String path, int w, int h, Color color, String label) throws Exception {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(color);
        g.fillRoundRect(4, 10, w - 8, h - 14, 10, 10);
        g.setColor(Color.BLACK);
        g.drawRoundRect(4, 10, w - 8, h - 14, 10, 10);
        // Eyes
        g.setColor(Color.WHITE);
        g.fillOval(18, 18, 12, 10);
        g.fillOval(38, 18, 12, 10);
        g.setColor(Color.RED);
        g.fillOval(22, 20, 6, 6);
        g.fillOval(42, 20, 6, 6);
        g.setColor(Color.WHITE);
        g.setFont(new Font("SansSerif", Font.BOLD, 16));
        g.drawString(label, w/2 - 8, h - 16);
        g.dispose();
        ImageIO.write(img, "png", new File(path));
        System.out.println("Created: " + new File(path).getName());
    }

    static void createKnife(String path, int w, int h, boolean right) throws Exception {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(new Color(200, 200, 210));
        if (right) {
            g.fillRect(0, 3, w - 10, h - 6);
            int[] xp = {w - 10, w, w - 10};
            int[] yp = {0, h/2, h};
            g.fillPolygon(xp, yp, 3);
        } else {
            g.fillRect(10, 3, w - 10, h - 6);
            int[] xp = {10, 0, 10};
            int[] yp = {0, h/2, h};
            g.fillPolygon(xp, yp, 3);
        }
        g.setColor(new Color(120, 80, 40));
        if (right) g.fillRect(0, 2, 12, h - 4);
        else g.fillRect(w - 12, 2, 12, h - 4);
        g.dispose();
        ImageIO.write(img, "png", new File(path));
        System.out.println("Created: " + new File(path).getName());
    }

    static void createSimple(String path, int w, int h, Color color, String label) throws Exception {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(color);
        g.fillRoundRect(2, 2, w - 4, h - 4, 8, 8);
        g.setColor(Color.WHITE);
        g.setFont(new Font("SansSerif", Font.BOLD, 11));
        FontMetrics fm = g.getFontMetrics();
        g.drawString(label, (w - fm.stringWidth(label)) / 2, h/2 + 4);
        g.dispose();
        ImageIO.write(img, "png", new File(path));
        System.out.println("Created: " + new File(path).getName());
    }

    static void createCollectable(String path, int w, int h, Color color, String label) throws Exception {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(color);
        g.fillOval(2, 2, w - 4, h - 4);
        g.setColor(Color.WHITE);
        g.setFont(new Font("SansSerif", Font.BOLD, 14));
        FontMetrics fm = g.getFontMetrics();
        g.drawString(label, (w - fm.stringWidth(label)) / 2, h/2 + 5);
        g.dispose();
        ImageIO.write(img, "png", new File(path));
        System.out.println("Created: " + new File(path).getName());
    }

    static void createCrosshair(String path, int w, int h) throws Exception {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.RED);
        g.setStroke(new BasicStroke(2));
        g.drawOval(4, 4, w - 8, h - 8);
        g.drawLine(w/2, 0, w/2, h);
        g.drawLine(0, h/2, w, h/2);
        g.dispose();
        ImageIO.write(img, "png", new File(path));
        System.out.println("Created: " + new File(path).getName());
    }

    static void createPlatform(String path, int w, int h, Color color, String label) throws Exception {
        BufferedImage img = new BufferedImage(Math.max(w, 1), Math.max(h, 1), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setColor(color);
        g.fillRect(0, 0, w, h);
        g.setColor(color.darker());
        g.drawRect(0, 0, w - 1, h - 1);
        if (!label.isEmpty() && w > 30 && h > 20) {
            g.setColor(new Color(255, 255, 255, 80));
            g.setFont(new Font("SansSerif", Font.PLAIN, 12));
            g.drawString(label, 4, 16);
        }
        g.dispose();
        ImageIO.write(img, "png", new File(path));
        System.out.println("Created: " + new File(path).getName());
    }

    static void createSign(String path, int w, int h, Color color, String label) throws Exception {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(color);
        g.fillRoundRect(0, 0, w, Math.min(h, 180), 10, 10);
        g.setColor(color.darker());
        g.fillRect(w/2 - 8, Math.min(h, 180) - 10, 16, h - Math.min(h, 180) + 10);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Serif", Font.BOLD, 18));
        FontMetrics fm = g.getFontMetrics();
        g.drawString(label, (w - fm.stringWidth(label)) / 2, 100);
        g.dispose();
        ImageIO.write(img, "png", new File(path));
        System.out.println("Created: " + new File(path).getName());
    }

    static void createPagoda(String path, int w, int h, Color color) throws Exception {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // Simple pagoda shape
        for (int i = 0; i < 3; i++) {
            int yy = i * 55;
            int shrink = i * 20;
            g.setColor(color);
            g.fillRect(shrink, yy + 20, w - shrink * 2, 35);
            g.setColor(color.darker());
            int[] xp = {shrink - 10, w/2, w - shrink + 10};
            int[] yp = {yy + 20, yy, yy + 20};
            g.fillPolygon(xp, yp, 3);
        }
        g.dispose();
        ImageIO.write(img, "png", new File(path));
        System.out.println("Created: " + new File(path).getName());
    }

    static void createDragonbird(String path, int w, int h) throws Exception {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(new Color(200, 60, 60));
        g.fillOval(10, 10, w - 20, h - 20);
        // Wings
        g.setColor(new Color(180, 40, 40));
        g.fillArc(0, 15, 30, 30, 90, 180);
        g.fillArc(w - 30, 15, 30, 30, 270, 180);
        // Eye
        g.setColor(Color.YELLOW);
        g.fillOval(w/2 - 3, h/2 - 8, 10, 8);
        g.dispose();
        ImageIO.write(img, "png", new File(path));
        System.out.println("Created: " + new File(path).getName());
    }

    static void createDoor(String path, int w, int h) throws Exception {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(new Color(60, 20, 20));
        g.fillRect(5, 0, w - 10, h);
        g.setColor(new Color(40, 10, 10));
        g.fillArc(5, 0, w - 10, 40, 0, 180);
        g.setColor(Color.RED);
        g.drawRect(5, 0, w - 10, h - 1);
        g.setColor(new Color(200, 180, 50));
        g.fillOval(w - 22, h/2, 8, 8);
        g.dispose();
        ImageIO.write(img, "png", new File(path));
        System.out.println("Created: " + new File(path).getName());
    }

    static void createArrow(String path, int w, int h, boolean down) throws Exception {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.YELLOW);
        if (down) {
            int[] xp = {0, w/2, w};
            int[] yp = {0, h, 0};
            g.fillPolygon(xp, yp, 3);
        } else {
            int[] xp = {0, w/2, w};
            int[] yp = {h, 0, h};
            g.fillPolygon(xp, yp, 3);
        }
        g.dispose();
        ImageIO.write(img, "png", new File(path));
        System.out.println("Created: " + new File(path).getName());
    }

    static void createBackground(String path, int w, int h, Color skyColor, Color groundColor, String title) throws Exception {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        // Sky gradient
        GradientPaint sky = new GradientPaint(0, 0, skyColor, 0, h/2, skyColor.brighter());
        g.setPaint(sky);
        g.fillRect(0, 0, w, h/2);
        // Ground gradient
        GradientPaint ground = new GradientPaint(0, h/2, groundColor, 0, h, groundColor.darker());
        g.setPaint(ground);
        g.fillRect(0, h/2, w, h/2);
        // Title watermark
        g.setColor(new Color(255, 255, 255, 30));
        g.setFont(new Font("Serif", Font.BOLD, 80));
        FontMetrics fm = g.getFontMetrics();
        g.drawString(title, (w - fm.stringWidth(title)) / 2, h / 2);
        g.dispose();
        ImageIO.write(img, "jpg", new File(path));
        System.out.println("Created: " + new File(path).getName());
    }

    static void createStoryPage(String path, String title, String subtitle, Color bgColor, Color textColor) throws Exception {
        BufferedImage img = new BufferedImage(1600, 800, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        // Background
        GradientPaint bg = new GradientPaint(0, 0, bgColor, 0, 800, bgColor.darker());
        g.setPaint(bg);
        g.fillRect(0, 0, 1600, 800);
        // Decorative border
        g.setColor(new Color(textColor.getRed(), textColor.getGreen(), textColor.getBlue(), 60));
        g.setStroke(new BasicStroke(3));
        g.drawRect(40, 40, 1520, 720);
        g.drawRect(50, 50, 1500, 700);
        // Title
        g.setColor(textColor);
        g.setFont(new Font("Serif", Font.BOLD, 64));
        FontMetrics fm = g.getFontMetrics();
        g.drawString(title, (1600 - fm.stringWidth(title)) / 2, 350);
        // Subtitle
        g.setFont(new Font("Serif", Font.ITALIC, 28));
        fm = g.getFontMetrics();
        g.drawString(subtitle, (1600 - fm.stringWidth(subtitle)) / 2, 420);
        // "Click to continue" hint
        g.setColor(new Color(textColor.getRed(), textColor.getGreen(), textColor.getBlue(), 120));
        g.setFont(new Font("SansSerif", Font.PLAIN, 18));
        fm = g.getFontMetrics();
        String hint = "Click 'Next' to continue";
        g.drawString(hint, (1600 - fm.stringWidth(hint)) / 2, 700);
        g.dispose();
        ImageIO.write(img, "png", new File(path));
        System.out.println("Created: " + new File(path).getName());
    }
}
