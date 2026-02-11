import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.util.Random;

/**
 * Generates all game art for Katsuya's Revenge with Japanese-themed animated style.
 * Player = black ninja, enemies = red/purple ninja, boss = evil samurai.
 * Backgrounds: Osaka mountains, Sewers, Tokyo night, Dojo courtyard.
 */
public class GenerateGameArt {

    static Random rand = new Random(42); // Fixed seed for reproducibility

    public static void main(String[] args) throws Exception {
        String base = new File("").getAbsolutePath() + File.separator;
        if (!new File(base + "Main.java").exists()) {
            try {
                base = GenerateGameArt.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
                File f = new File(base);
                if (f.isFile()) f = f.getParentFile();
                base = f.getAbsolutePath() + File.separator;
            } catch (Exception e) { base = ""; }
        }
        String img = base + "images" + File.separator;
        String bg = img + "Backgrounds" + File.separator;
        new File(bg).mkdirs();

        System.out.println("=== Generating Katsuya's Revenge Game Art ===\n");

        // Player sprite sheet
        System.out.println("--- Player Sprite ---");
        generatePlayerSprite(img + "pcNewAttempt2.png");

        // Enemy sprites
        System.out.println("\n--- Enemy Sprites ---");
        generateRedNinja(img + "enemyRanger.png", 72, 81);
        generatePurpleNinja(img + "boss.png", 72, 81);
        generateEvilSamurai(img + "boss2.png", 144, 162);

        // Weapons & collectibles
        System.out.println("\n--- Weapons & Items ---");
        generateThrowingKnife(img + "throwingKnifeRight.png", 55, 15, true);
        generateThrowingKnife(img + "throwingKnifeLeft.png", 55, 15, false);
        generateKnifeAmmo(img + "throwingKnifeAmmo.png", 50, 50);
        generateHealthPickup(img + "health.png", 50, 50);
        generateCrosshair(img + "crosshair.png", 32, 32);

        // Level 1: Osaka platforms
        System.out.println("\n--- Level 1: Osaka Platforms ---");
        generateOsakaGround(img + "base1.png", 2200, 300);
        generateOsakaGround(img + "base2.png", 800, 300);
        generateOsakaCliff(img + "base3.png", 400, 525);
        generateOsakaRock(img + "base4.png", 200, 450);
        generateSmallRock(img + "rock.png", 90, 40);
        generateStoneStep(img + "stone.png", 100, 50);
        generateWoodPlatform(img + "enemyPlatform.png", 400, 50);
        generateWoodPlatform(img + "smallEnemyPlatform.png", 150, 50);
        generateMovingPlatform(img + "moving platform1.png", 400, 50);
        generateWoodPillar(img + "largeStilt.png", 10, 575);
        generateWoodPillar(img + "mediumStilt.png", 10, 425);
        generateWoodPillar(img + "smallStilt.png", 10, 100);
        generateWoodPillar(img + "miniStilt.png", 10, 50);
        generateOsakaGround(img + "miniBase.png", 100, 250);
        generateLevelSign(img + "sign1.png", 200, 200, "OSAKA", new Color(160, 80, 40));
        generateMarioGreenPipe(img + "pipe2.png", 195, 525);

        // Level 2: Sewer platforms
        System.out.println("\n--- Level 2: Sewer Platforms ---");
        generateSewerGround(img + "sewer1.png", 2200, 300);
        generateSewerWall(img + "sewer2.png", 195, 375);
        generateSewerGrate(img + "sewer3.png", 300, 25);
        generateSewerConcrete(img + "sewer4.png", 300, 25);
        generateMarioGreenPipe(img + "pipe1.png", 195, 575);
        generateMarioGreenPipe(img + "extraPipe.png", 10, 575);
        generateLevelSign(img + "sign2.png", 200, 200, "SEWERS", new Color(40, 120, 40));
        generateArrow(img + "downArrow.png", 40, 40, true);
        generateArrow(img + "upArrow.png", 40, 40, false);

        // Level 3: Tokyo platforms
        System.out.println("\n--- Level 3: Tokyo Platforms ---");
        generateLevelSign(img + "sign3.png", 200, 1000, "TOKYO", new Color(180, 50, 80));
        generatePagoda(img + "pagota1.png", 225, 800);
        generatePagodaLarge(img + "pagota2.png", 200, 200);
        generateCityLedge(img + "ledge.png", 560, 800);
        generateCityPillar(img + "pillar.png", 30, 80);
        generateDragonStatue(img + "dragonbird.png", 80, 60);
        generateEvilDoor(img + "evil door.png", 80, 120);

        // Backgrounds
        System.out.println("\n--- Backgrounds ---");
        generateOsakaBackground(bg + "mountain_background1-2.jpg", 3200, 1600);
        generateSewerBackground(bg + "sewer2.jpg", 3200, 1600);
        generateTokyoBackground(bg + "city2.jpg", 3200, 1600);
        generateDojoBackground(bg + "Dojo_Background2.jpg", 3200, 1600);

        // Story pages
        System.out.println("\n--- Story Pages ---");
        generateTitlePage(img + "Page1-2.PNG");
        generateProloguePage1(img + "Page2.PNG");
        generateProloguePage2(img + "Page3.PNG");
        generateChapterPage(img + "Page4.PNG", "CHAPTER II", "The Sewers", "Katsuya descends into the depths below Osaka...", new Color(30, 50, 30));
        generateChapterPage(img + "PAGE5.PNG", "CHAPTER III", "Tokyo Night", "The neon-lit streets hide darker secrets...", new Color(15, 15, 40));
        generateChapterPage(img + "PAGE6.PNG", "CHAPTER IV", "The Dojo", "At last, the stronghold of the shadow clan...", new Color(40, 15, 15));
        generateEndingPage(img + "Page7.PNG");

        System.out.println("\n=== All game art generated! ===");
    }

    // ==================== NINJA SPRITE DRAWING ====================

    static void generatePlayerSprite(String path) throws Exception {
        int fw = 82, fh = 82;
        int[] frameCounts = {5, 2, 8, 2, 5};
        int maxCols = 8;
        BufferedImage sheet = new BufferedImage(maxCols * fw, frameCounts.length * fh, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = sheet.createGraphics();
        enableAA(g);

        // Row 0: RUN_RIGHT (5 frames)
        for (int i = 0; i < 5; i++) drawNinja(g, i * fw, 0, fw, fh, new Color(30, 30, 35), true, "run", i, false);
        // Row 1: THROW (2 frames)
        for (int i = 0; i < 2; i++) drawNinja(g, i * fw, fh, fw, fh, new Color(30, 30, 35), true, "throw", i, false);
        // Row 2: IDLE (8 frames)
        for (int i = 0; i < 8; i++) drawNinja(g, i * fw, 2 * fh, fw, fh, new Color(30, 30, 35), true, "idle", i, false);
        // Row 3: DIE (2 frames)
        for (int i = 0; i < 2; i++) drawNinja(g, i * fw, 3 * fh, fw, fh, new Color(30, 30, 35), true, "die", i, false);
        // Row 4: RUN_LEFT (5 frames)
        for (int i = 0; i < 5; i++) drawNinja(g, i * fw, 4 * fh, fw, fh, new Color(30, 30, 35), false, "run", i, false);

        g.dispose();
        ImageIO.write(sheet, "png", new File(path));
        System.out.println("Created: pcNewAttempt2.png (player sprite sheet)");
    }

    static void drawNinja(Graphics2D g, int ox, int oy, int w, int h, Color bodyColor, boolean faceRight, String anim, int frame, boolean isEnemy) {
        int cx = ox + w / 2;
        int cy = oy + h / 2;
        int dir = faceRight ? 1 : -1;

        // Animation offsets
        int legOffset = 0, armOffset = 0, bodyTilt = 0;
        double rotation = 0;
        boolean throwFrame = false;

        if (anim.equals("run")) {
            legOffset = (int)(Math.sin(frame * 1.3) * 10);
            armOffset = -legOffset;
            bodyTilt = (int)(Math.sin(frame * 1.3) * 2);
        } else if (anim.equals("idle")) {
            bodyTilt = (int)(Math.sin(frame * 0.8) * 1);
            armOffset = (int)(Math.sin(frame * 0.5) * 2);
        } else if (anim.equals("throw")) {
            throwFrame = true;
            armOffset = frame == 0 ? -15 : 20;
        } else if (anim.equals("die")) {
            rotation = frame == 0 ? 0.3 : 1.2;
        }

        Graphics2D g2 = (Graphics2D) g.create();
        if (rotation != 0) {
            g2.rotate(rotation, cx, cy);
        }

        Color darkBody = bodyColor;
        Color lightBody = brighter(bodyColor, 30);

        // Shadow
        g2.setColor(new Color(0, 0, 0, 40));
        g2.fillOval(cx - 15, oy + h - 10, 30, 8);

        // Legs
        g2.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setColor(darkBody);
        g2.drawLine(cx - 4 * dir, cy + 12, cx - 8 * dir + legOffset, oy + h - 8);
        g2.drawLine(cx + 4 * dir, cy + 12, cx + 8 * dir - legOffset, oy + h - 8);

        // Feet
        g2.setColor(new Color(60, 50, 40));
        g2.fillOval(cx - 12 * dir + legOffset - 4, oy + h - 12, 10, 6);
        g2.fillOval(cx + 4 * dir - legOffset - 4, oy + h - 12, 10, 6);

        // Body
        g2.setColor(darkBody);
        g2.fillRoundRect(cx - 14, cy - 8 + bodyTilt, 28, 24, 6, 6);
        // Belt/sash
        g2.setColor(isEnemy ? new Color(80, 20, 20) : new Color(60, 60, 70));
        g2.fillRect(cx - 14, cy + 6 + bodyTilt, 28, 5);

        // Arms
        g2.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setColor(darkBody);
        if (throwFrame && frame == 1) {
            // Extended throw arm
            g2.drawLine(cx + 10 * dir, cy - 2, cx + 30 * dir, cy - 8);
        } else {
            g2.drawLine(cx - 12 * dir, cy - 2, cx - 18 * dir + armOffset, cy + 10);
            g2.drawLine(cx + 12 * dir, cy - 2, cx + 18 * dir - armOffset, cy + 10);
        }

        // Head
        g2.setColor(lightBody);
        g2.fillOval(cx - 12, oy + 10 + bodyTilt, 24, 22);
        // Mask/hood wrapping
        g2.setColor(darkBody);
        g2.fillOval(cx - 13, oy + 8 + bodyTilt, 26, 14);
        // Face slit (eyes visible)
        g2.setColor(new Color(200, 180, 150)); // Skin tone
        g2.fillRect(cx - 10, oy + 18 + bodyTilt, 20, 6);
        // Eyes
        g2.setColor(Color.WHITE);
        g2.fillOval(cx - 7 + dir * 2, oy + 18 + bodyTilt, 5, 5);
        g2.fillOval(cx + 3 + dir * 2, oy + 18 + bodyTilt, 5, 5);
        g2.setColor(isEnemy ? Color.RED : new Color(40, 40, 40));
        g2.fillOval(cx - 6 + dir * 3, oy + 19 + bodyTilt, 3, 3);
        g2.fillOval(cx + 4 + dir * 3, oy + 19 + bodyTilt, 3, 3);

        // Headband tail
        g2.setStroke(new BasicStroke(2));
        g2.setColor(isEnemy ? new Color(180, 30, 30) : new Color(200, 30, 30));
        g2.drawLine(cx + 12, oy + 14 + bodyTilt, cx + 22 - dir * 5, oy + 10 + bodyTilt + (int)(Math.sin(frame) * 3));
        g2.drawLine(cx + 12, oy + 16 + bodyTilt, cx + 25 - dir * 5, oy + 14 + bodyTilt + (int)(Math.cos(frame) * 3));

        // Sword on back (only for player)
        if (!isEnemy) {
            g2.setStroke(new BasicStroke(3));
            g2.setColor(new Color(120, 120, 130));
            g2.drawLine(cx - 8 * dir, oy + 12, cx - 2 * dir, cy + 18);
            g2.setColor(new Color(80, 60, 40));
            g2.fillRect(cx - 4 * dir - 2, cy + 16, 6, 3);
        }

        g2.dispose();
    }

    static void generateRedNinja(String path, int w, int h) throws Exception {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        enableAA(g);
        drawNinja(g, 0, 0, w, h, new Color(140, 30, 30), true, "idle", 0, true);
        g.dispose();
        ImageIO.write(img, "png", new File(path));
        System.out.println("Created: enemyRanger.png (red ninja)");
    }

    static void generatePurpleNinja(String path, int w, int h) throws Exception {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        enableAA(g);
        drawNinja(g, 0, 0, w, h, new Color(80, 20, 120), true, "idle", 0, true);
        g.dispose();
        ImageIO.write(img, "png", new File(path));
        System.out.println("Created: boss.png (purple ninja)");
    }

    static void generateEvilSamurai(String path, int w, int h) throws Exception {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        enableAA(g);
        int cx = w / 2, cy = h / 2;

        // Shadow
        g.setColor(new Color(0, 0, 0, 50));
        g.fillOval(cx - 18, h - 10, 36, 8);

        // Legs - armored
        g.setStroke(new BasicStroke(6, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.setColor(new Color(40, 20, 20));
        g.drawLine(cx - 6, cy + 12, cx - 10, h - 8);
        g.drawLine(cx + 6, cy + 12, cx + 10, h - 8);
        // Armored boots
        g.setColor(new Color(50, 50, 55));
        g.fillRect(cx - 16, h - 13, 14, 8);
        g.fillRect(cx + 4, h - 13, 14, 8);

        // Body - heavy armor
        g.setColor(new Color(50, 15, 15));
        g.fillRoundRect(cx - 18, cy - 12, 36, 28, 8, 8);
        // Armor plates
        g.setColor(new Color(70, 25, 25));
        g.fillRect(cx - 16, cy - 8, 32, 4);
        g.fillRect(cx - 16, cy, 32, 4);
        g.fillRect(cx - 16, cy + 8, 32, 4);
        // Gold trim
        g.setColor(new Color(200, 170, 50));
        g.setStroke(new BasicStroke(1));
        g.drawRect(cx - 18, cy - 12, 36, 28);

        // Shoulder pads
        g.setColor(new Color(60, 20, 20));
        g.fillOval(cx - 24, cy - 12, 16, 12);
        g.fillOval(cx + 8, cy - 12, 16, 12);
        g.setColor(new Color(200, 170, 50));
        g.drawOval(cx - 24, cy - 12, 16, 12);
        g.drawOval(cx + 8, cy - 12, 16, 12);

        // Arms
        g.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.setColor(new Color(50, 15, 15));
        g.drawLine(cx - 18, cy - 4, cx - 24, cy + 14);
        g.drawLine(cx + 18, cy - 4, cx + 24, cy + 8);

        // Sword in hand
        g.setStroke(new BasicStroke(3));
        g.setColor(new Color(180, 180, 190));
        g.drawLine(cx + 24, cy + 8, cx + 34, cy - 20);
        g.setColor(new Color(200, 170, 50));
        g.fillRect(cx + 22, cy + 5, 6, 3);

        // Helmet
        g.setColor(new Color(50, 15, 15));
        g.fillArc(cx - 16, 4, 32, 24, 0, 180);
        g.fillRect(cx - 16, 14, 32, 10);
        // Helmet crest
        g.setColor(new Color(200, 170, 50));
        g.fillRect(cx - 2, 0, 4, 12);
        g.setColor(new Color(180, 30, 30));
        g.fillOval(cx - 5, 0, 10, 8);
        // Face mask
        g.setColor(new Color(40, 40, 45));
        g.fillRect(cx - 12, 20, 24, 12);
        // Glowing RED eyes
        g.setColor(new Color(255, 0, 0));
        g.fillOval(cx - 8, 22, 6, 5);
        g.fillOval(cx + 2, 22, 6, 5);
        // Eye glow effect
        g.setColor(new Color(255, 50, 50, 100));
        g.fillOval(cx - 10, 20, 10, 9);
        g.fillOval(cx, 20, 10, 9);
        // Inner pupil
        g.setColor(new Color(200, 0, 0));
        g.fillOval(cx - 6, 23, 3, 3);
        g.fillOval(cx + 4, 23, 3, 3);

        g.dispose();
        ImageIO.write(img, "png", new File(path));
        System.out.println("Created: boss2.png (evil samurai with red eyes)");
    }

    // ==================== WEAPONS & ITEMS ====================

    static void generateThrowingKnife(String path, int w, int h, boolean right) throws Exception {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        enableAA(g);
        // Shuriken/kunai style
        int tipX = right ? w - 2 : 2;
        int baseX = right ? 12 : w - 12;
        // Blade
        g.setColor(new Color(190, 195, 200));
        int[] bx, by;
        if (right) {
            bx = new int[]{12, w - 2, 12};
            by = new int[]{1, h / 2, h - 1};
        } else {
            bx = new int[]{w - 12, 2, w - 12};
            by = new int[]{1, h / 2, h - 1};
        }
        g.fillPolygon(bx, by, 3);
        // Blade shine
        g.setColor(new Color(220, 225, 230));
        if (right) {
            g.drawLine(14, h/2 - 2, w - 8, h/2);
        } else {
            g.drawLine(w - 14, h/2 - 2, 8, h/2);
        }
        // Handle wrap
        g.setColor(new Color(80, 40, 20));
        int hx = right ? 2 : w - 12;
        g.fillRoundRect(hx, 2, 10, h - 4, 3, 3);
        g.setColor(new Color(180, 30, 30));
        for (int i = 0; i < 3; i++) {
            g.drawLine(hx, 4 + i * 4, hx + 9, 4 + i * 4);
        }
        g.dispose();
        ImageIO.write(img, "png", new File(path));
        System.out.println("Created: " + new File(path).getName());
    }

    static void generateKnifeAmmo(String path, int w, int h) throws Exception {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        enableAA(g);
        // Pouch with knives sticking out
        g.setColor(new Color(80, 60, 40));
        g.fillRoundRect(5, 15, w - 10, h - 20, 10, 10);
        g.setColor(new Color(60, 40, 25));
        g.drawRoundRect(5, 15, w - 10, h - 20, 10, 10);
        // Knives poking out
        g.setColor(new Color(190, 195, 200));
        for (int i = 0; i < 3; i++) {
            int kx = 14 + i * 10;
            g.fillRect(kx, 5, 3, 18);
            g.setColor(new Color(220, 225, 230));
            g.drawLine(kx, 5, kx + 1, 3);
            g.setColor(new Color(190, 195, 200));
        }
        // Label
        g.setColor(new Color(200, 170, 50));
        g.setFont(new Font("SansSerif", Font.BOLD, 10));
        g.drawString("KUNAI", 8, h - 6);
        g.dispose();
        ImageIO.write(img, "png", new File(path));
        System.out.println("Created: throwingKnifeAmmo.png");
    }

    static void generateHealthPickup(String path, int w, int h) throws Exception {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        enableAA(g);
        // Onigiri (rice ball) style health
        g.setColor(new Color(245, 245, 240));
        int[] tx = {w/2, 5, w - 5};
        int[] ty = {5, h - 8, h - 8};
        g.fillPolygon(tx, ty, 3);
        g.fillOval(8, h/2, w - 16, h/2 - 4);
        // Nori (seaweed wrap)
        g.setColor(new Color(30, 50, 30));
        g.fillRect(w/2 - 10, h/2 + 2, 20, h/2 - 8);
        // Healing glow
        g.setColor(new Color(100, 255, 100, 60));
        g.fillOval(3, 3, w - 6, h - 6);
        // Cross
        g.setColor(new Color(50, 200, 50));
        g.setStroke(new BasicStroke(3));
        g.drawLine(w/2, 10, w/2, 22);
        g.drawLine(w/2 - 6, 16, w/2 + 6, 16);
        g.dispose();
        ImageIO.write(img, "png", new File(path));
        System.out.println("Created: health.png");
    }

    static void generateCrosshair(String path, int w, int h) throws Exception {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        enableAA(g);
        g.setColor(new Color(255, 50, 50, 200));
        g.setStroke(new BasicStroke(2));
        g.drawOval(4, 4, w - 8, h - 8);
        g.drawLine(w/2, 1, w/2, h - 1);
        g.drawLine(1, h/2, w - 1, h/2);
        g.setColor(new Color(255, 50, 50, 100));
        g.fillOval(w/2 - 2, h/2 - 2, 4, 4);
        g.dispose();
        ImageIO.write(img, "png", new File(path));
        System.out.println("Created: crosshair.png");
    }

    // ==================== LEVEL 1: OSAKA PLATFORMS ====================

    static void generateOsakaGround(String path, int w, int h) throws Exception {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        enableAA(g);
        // Earth/dirt base
        GradientPaint gp = new GradientPaint(0, 0, new Color(120, 85, 50), 0, h, new Color(80, 55, 30));
        g.setPaint(gp);
        g.fillRect(0, 0, w, h);
        // Grass top
        g.setColor(new Color(70, 130, 50));
        g.fillRect(0, 0, w, 12);
        g.setColor(new Color(90, 150, 60));
        for (int i = 0; i < w; i += 6) {
            int gh = 8 + rand.nextInt(10);
            g.fillRect(i, 0, 3, gh);
        }
        // Stone texture
        g.setColor(new Color(0, 0, 0, 20));
        for (int i = 0; i < 30; i++) {
            int rx = rand.nextInt(w - 20);
            int ry = 15 + rand.nextInt(h - 20);
            g.fillOval(rx, ry, 10 + rand.nextInt(20), 5 + rand.nextInt(10));
        }
        g.dispose();
        ImageIO.write(img, "png", new File(path));
        System.out.println("Created: " + new File(path).getName());
    }

    static void generateOsakaCliff(String path, int w, int h) throws Exception {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        enableAA(g);
        GradientPaint gp = new GradientPaint(0, 0, new Color(110, 95, 75), 0, h, new Color(75, 60, 40));
        g.setPaint(gp);
        g.fillRoundRect(0, 0, w, h, 10, 10);
        g.setColor(new Color(70, 130, 50));
        g.fillRect(0, 0, w, 10);
        // Rock face lines
        g.setColor(new Color(0, 0, 0, 30));
        g.setStroke(new BasicStroke(1));
        for (int i = 0; i < 15; i++) {
            int y1 = rand.nextInt(h);
            g.drawLine(0, y1, w, y1 + rand.nextInt(20) - 10);
        }
        g.dispose();
        ImageIO.write(img, "png", new File(path));
        System.out.println("Created: " + new File(path).getName());
    }

    static void generateOsakaRock(String path, int w, int h) throws Exception {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        enableAA(g);
        GradientPaint gp = new GradientPaint(0, 0, new Color(100, 90, 80), 0, h, new Color(70, 60, 50));
        g.setPaint(gp);
        g.fillRoundRect(0, 0, w, h, 15, 15);
        g.setColor(new Color(0, 0, 0, 25));
        for (int i = 0; i < 10; i++) {
            g.drawLine(rand.nextInt(w), rand.nextInt(h), rand.nextInt(w), rand.nextInt(h));
        }
        g.dispose();
        ImageIO.write(img, "png", new File(path));
        System.out.println("Created: " + new File(path).getName());
    }

    static void generateSmallRock(String path, int w, int h) throws Exception {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        enableAA(g);
        g.setColor(new Color(140, 130, 115));
        g.fillRoundRect(0, 0, w, h, 10, 10);
        g.setColor(new Color(160, 150, 135));
        g.fillRoundRect(3, 2, w - 10, h/2, 6, 6);
        g.dispose();
        ImageIO.write(img, "png", new File(path));
        System.out.println("Created: " + new File(path).getName());
    }

    static void generateStoneStep(String path, int w, int h) throws Exception {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        enableAA(g);
        g.setColor(new Color(150, 140, 125));
        g.fillRoundRect(0, 0, w, h, 6, 6);
        g.setColor(new Color(130, 120, 105));
        g.drawRoundRect(0, 0, w - 1, h - 1, 6, 6);
        g.setColor(new Color(165, 155, 140));
        g.fillRoundRect(3, 2, w - 6, h/3, 4, 4);
        g.dispose();
        ImageIO.write(img, "png", new File(path));
        System.out.println("Created: " + new File(path).getName());
    }

    static void generateWoodPlatform(String path, int w, int h) throws Exception {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        enableAA(g);
        // Wood planks
        g.setColor(new Color(140, 100, 55));
        g.fillRect(0, 0, w, h);
        g.setColor(new Color(120, 85, 45));
        for (int i = 0; i < w; i += w/5) {
            g.drawLine(i, 0, i, h);
        }
        // Wood grain
        g.setColor(new Color(0, 0, 0, 20));
        for (int y = 3; y < h; y += 6) {
            g.drawLine(0, y, w, y + (rand.nextInt(3) - 1));
        }
        // Top/bottom edge
        g.setColor(new Color(100, 70, 35));
        g.fillRect(0, 0, w, 3);
        g.fillRect(0, h - 3, w, 3);
        g.dispose();
        ImageIO.write(img, "png", new File(path));
        System.out.println("Created: " + new File(path).getName());
    }

    static void generateMovingPlatform(String path, int w, int h) throws Exception {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        enableAA(g);
        g.setColor(new Color(100, 70, 35));
        g.fillRect(0, 0, w, h);
        g.setColor(new Color(80, 55, 25));
        for (int i = 0; i < w; i += w/6) g.drawLine(i, 0, i, h);
        // Rope indicators on sides
        g.setColor(new Color(180, 160, 100));
        g.setStroke(new BasicStroke(3));
        g.drawLine(5, 0, 5, h);
        g.drawLine(w - 5, 0, w - 5, h);
        // Arrows indicating movement
        g.setColor(new Color(200, 200, 100, 150));
        g.setFont(new Font("SansSerif", Font.BOLD, 14));
        g.drawString("\u2194", w/2 - 6, h/2 + 5); // ↔
        g.dispose();
        ImageIO.write(img, "png", new File(path));
        System.out.println("Created: " + new File(path).getName());
    }

    static void generateWoodPillar(String path, int w, int h) throws Exception {
        BufferedImage img = new BufferedImage(Math.max(w, 1), Math.max(h, 1), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        GradientPaint gp = new GradientPaint(0, 0, new Color(120, 80, 45), w, 0, new Color(90, 60, 30));
        g.setPaint(gp);
        g.fillRect(0, 0, w, h);
        // Wood grain lines
        g.setColor(new Color(80, 50, 25, 40));
        for (int y = 0; y < h; y += 8 + rand.nextInt(6)) {
            g.drawLine(0, y, w, y);
        }
        // Knot details (only for taller pillars)
        if (h > 100 && w >= 8) {
            g.setColor(new Color(70, 45, 20, 60));
            for (int k = 0; k < h / 150; k++) {
                int ky = 50 + rand.nextInt(h - 60);
                g.fillOval(w / 4, ky, Math.max(w / 2, 3), 6);
            }
        }
        // Shadow on right edge
        g.setColor(new Color(0, 0, 0, 50));
        g.drawLine(w - 1, 0, w - 1, h);
        if (w > 5) g.drawLine(w - 2, 0, w - 2, h);
        // Highlight on left edge
        g.setColor(new Color(255, 255, 255, 20));
        g.drawLine(0, 0, 0, h);
        g.dispose();
        ImageIO.write(img, "png", new File(path));
        System.out.println("Created: " + new File(path).getName());
    }

    static void generateLevelSign(String path, int w, int h, String text, Color color) throws Exception {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        enableAA(g);
        // Post
        g.setColor(new Color(90, 60, 30));
        g.fillRect(w/2 - 6, 80, 12, h - 80);
        // Sign board
        g.setColor(color);
        g.fillRoundRect(5, 10, w - 10, 75, 8, 8);
        g.setColor(color.darker());
        g.drawRoundRect(5, 10, w - 10, 75, 8, 8);
        // Text
        g.setColor(Color.WHITE);
        g.setFont(new Font("Serif", Font.BOLD, 22));
        FontMetrics fm = g.getFontMetrics();
        g.drawString(text, (w - fm.stringWidth(text))/2, 55);
        g.dispose();
        ImageIO.write(img, "png", new File(path));
        System.out.println("Created: " + new File(path).getName());
    }

    static void generateMarioGreenPipe(String path, int w, int h) throws Exception {
        BufferedImage img = new BufferedImage(Math.max(w, 1), Math.max(h, 1), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        enableAA(g);
        Color brightGreen = new Color(33, 173, 33);
        Color darkGreen = new Color(20, 120, 20);
        Color highlightGreen = new Color(80, 210, 80);
        Color veryDark = new Color(10, 80, 10);
        int rimH = Math.min(28, h / 6);
        int rimExtra = Math.min(12, w / 8);
        int bodyX = (w < 30) ? 0 : rimExtra / 2;
        int bodyW = (w < 30) ? w : w - rimExtra;
        // Pipe body - cylindrical gradient
        GradientPaint bodyGrad = new GradientPaint(bodyX, 0, darkGreen, bodyX + bodyW / 2, 0, brightGreen);
        g.setPaint(bodyGrad);
        g.fillRect(bodyX, (w < 30) ? 0 : rimH, bodyW, h);
        GradientPaint bodyGrad2 = new GradientPaint(bodyX + bodyW / 2, 0, new Color(33, 173, 33, 0), bodyX + bodyW, 0, darkGreen);
        g.setPaint(bodyGrad2);
        g.fillRect(bodyX + bodyW / 2, (w < 30) ? 0 : rimH, bodyW / 2, h);
        // Specular highlight stripe
        if (w >= 15) {
            g.setColor(new Color(120, 240, 120, 70));
            int hlX = bodyX + bodyW / 4;
            g.fillRect(hlX, (w < 30) ? 0 : rimH, Math.max(2, bodyW / 10), h);
        }
        // Horizontal band lines for structure
        g.setColor(new Color(15, 100, 15, 80));
        g.setStroke(new BasicStroke(2));
        for (int y = ((w < 30) ? 0 : rimH) + 60; y < h - 10; y += 70) {
            g.drawLine(bodyX, y, bodyX + bodyW, y);
        }
        // Dark edge lines
        g.setColor(veryDark);
        g.drawLine(bodyX, (w < 30) ? 0 : rimH, bodyX, h);
        g.drawLine(bodyX + bodyW - 1, (w < 30) ? 0 : rimH, bodyX + bodyW - 1, h);
        // Rim/lip at top (skip for very thin pipes)
        if (w >= 30) {
            GradientPaint rimGrad = new GradientPaint(0, 0, darkGreen, w / 2, 0, highlightGreen);
            g.setPaint(rimGrad);
            g.fillRoundRect(0, 0, w, rimH, 6, 6);
            GradientPaint rimGrad2 = new GradientPaint(w / 2, 0, new Color(80, 210, 80, 0), w, 0, darkGreen);
            g.setPaint(rimGrad2);
            g.fillRoundRect(w / 2, 0, w / 2, rimH, 4, 4);
            // Rim highlight top edge
            g.setColor(highlightGreen);
            g.drawLine(3, 1, w - 3, 1);
            // Rim bottom edge shadow
            g.setColor(veryDark);
            g.drawLine(0, rimH - 1, w, rimH - 1);
            // Rim specular
            g.setColor(new Color(150, 255, 150, 50));
            g.fillRect(w / 4, 2, Math.max(2, w / 8), rimH - 4);
        }
        g.dispose();
        ImageIO.write(img, "png", new File(path));
        System.out.println("Created: " + new File(path).getName());
    }

    // ==================== LEVEL 2: SEWER PLATFORMS ====================

    static void generateSewerGround(String path, int w, int h) throws Exception {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        enableAA(g);
        // Concrete base
        g.setColor(new Color(55, 60, 50));
        g.fillRect(0, 0, w, h);
        // Brick pattern
        g.setColor(new Color(45, 50, 40));
        for (int y = 0; y < h; y += 20) {
            int offset = (y / 20) % 2 == 0 ? 0 : 25;
            for (int x = offset; x < w; x += 50) {
                g.drawRect(x, y, 48, 18);
            }
        }
        // Slime/moss
        g.setColor(new Color(50, 100, 30, 80));
        g.fillRect(0, 0, w, 15);
        // Radioactive glow
        g.setColor(new Color(80, 200, 50, 30));
        g.fillRect(0, h - 20, w, 20);
        g.dispose();
        ImageIO.write(img, "png", new File(path));
        System.out.println("Created: " + new File(path).getName());
    }

    static void generateSewerWall(String path, int w, int h) throws Exception {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setColor(new Color(50, 55, 45));
        g.fillRect(0, 0, w, h);
        g.setColor(new Color(40, 45, 35));
        for (int y = 0; y < h; y += 15) {
            g.drawLine(0, y, w, y);
        }
        // Drip stains
        g.setColor(new Color(60, 120, 40, 60));
        for (int i = 0; i < 5; i++) {
            int dx = rand.nextInt(w - 5);
            g.fillRect(dx, 0, 3, 30 + rand.nextInt(50));
        }
        g.dispose();
        ImageIO.write(img, "png", new File(path));
        System.out.println("Created: " + new File(path).getName());
    }

    static void generateSewerGrate(String path, int w, int h) throws Exception {
        BufferedImage img = new BufferedImage(w, Math.max(h, 1), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        enableAA(g);
        // Dark metal base
        g.setColor(new Color(55, 60, 50));
        g.fillRect(0, 0, w, h);
        // Vertical grate bars
        g.setColor(new Color(75, 80, 70));
        for (int x = 0; x < w; x += 10) {
            g.fillRect(x, 0, 2, h);
        }
        // Horizontal cross bars
        g.setColor(new Color(70, 75, 65));
        g.fillRect(0, 0, w, 2);
        g.fillRect(0, h - 2, w, 2);
        if (h > 12) g.fillRect(0, h / 2, w, 2);
        // Rivet dots
        g.setColor(new Color(90, 95, 85));
        for (int x = 8; x < w; x += 30) {
            g.fillOval(x, 1, 3, 3);
            if (h > 12) g.fillOval(x, h - 4, 3, 3);
        }
        // Green slime on underside
        g.setColor(new Color(60, 180, 40, 50));
        g.fillRect(0, h - 3, w, 3);
        g.dispose();
        ImageIO.write(img, "png", new File(path));
        System.out.println("Created: " + new File(path).getName());
    }

    static void generateSewerConcrete(String path, int w, int h) throws Exception {
        BufferedImage img = new BufferedImage(w, Math.max(h, 1), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        enableAA(g);
        // Light gray concrete base
        g.setColor(new Color(75, 80, 70));
        g.fillRect(0, 0, w, h);
        // Top bright edge
        g.setColor(new Color(95, 100, 90));
        g.fillRect(0, 0, w, 2);
        // Bottom shadow
        g.setColor(new Color(50, 55, 45));
        g.fillRect(0, h - 2, w, 2);
        // Caution stripes (diagonal yellow-black)
        for (int x = -h; x < w; x += 16) {
            g.setColor(new Color(200, 180, 40, 100));
            g.fillRect(x, 0, 8, h);
        }
        // Overlay concrete texture speckles
        g.setColor(new Color(60, 65, 55, 60));
        for (int i = 0; i < w / 5; i++) {
            int sx = rand.nextInt(w);
            int sy = rand.nextInt(h);
            g.fillRect(sx, sy, 2, 2);
        }
        g.dispose();
        ImageIO.write(img, "png", new File(path));
        System.out.println("Created: " + new File(path).getName());
    }

    static void generateArrow(String path, int w, int h, boolean down) throws Exception {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        enableAA(g);
        // Outer glow
        g.setColor(new Color(60, 200, 40, 40));
        g.fillOval(0, 0, w, h);
        // Mid glow
        g.setColor(new Color(80, 220, 60, 60));
        g.fillOval(4, 4, w - 8, h - 8);
        // Arrow with bright Mario green
        g.setColor(new Color(100, 255, 60));
        if (down) {
            int[] xp = {4, w/2, w - 4};
            int[] yp = {6, h - 6, 6};
            g.fillPolygon(xp, yp, 3);
            // Pipe opening hint at top
            g.setColor(new Color(33, 173, 33));
            g.fillRoundRect(w/2 - 10, 0, 20, 6, 3, 3);
        } else {
            int[] xp = {4, w/2, w - 4};
            int[] yp = {h - 6, 6, h - 6};
            g.fillPolygon(xp, yp, 3);
            // Pipe opening hint at bottom
            g.setColor(new Color(33, 173, 33));
            g.fillRoundRect(w/2 - 10, h - 6, 20, 6, 3, 3);
        }
        g.dispose();
        ImageIO.write(img, "png", new File(path));
        System.out.println("Created: " + new File(path).getName());
    }

    // ==================== LEVEL 3: TOKYO PLATFORMS ====================

    static void generatePagoda(String path, int w, int h) throws Exception {
        // Narrow skyscraper (225x800) - rooftop at top, building facade below
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        enableAA(g);

        // Building body - dark glass/steel gradient
        GradientPaint bodyGrad = new GradientPaint(0, 0, new Color(35, 40, 55), 0, h, new Color(22, 25, 38));
        g.setPaint(bodyGrad);
        g.fillRect(0, 0, w, h);

        // Vertical edge lines (building borders)
        g.setColor(new Color(50, 55, 70));
        g.fillRect(0, 0, 3, h);
        g.fillRect(w - 3, 0, 3, h);

        // Horizontal floor lines every ~40px
        g.setColor(new Color(30, 33, 45));
        for (int fy = 30; fy < h; fy += 40) {
            g.fillRect(0, fy, w, 2);
        }

        // Window grid - starting below rooftop section
        for (int wy = 35; wy < h - 5; wy += 40) {
            for (int wx = 10; wx < w - 15; wx += 30) {
                int bright = rand.nextInt(4);
                if (bright == 0) g.setColor(new Color(255, 220, 100, 100 + rand.nextInt(80)));
                else if (bright == 1) g.setColor(new Color(100, 200, 255, 70 + rand.nextInt(60)));
                else if (bright == 2) g.setColor(new Color(255, 180, 80, 50 + rand.nextInt(60)));
                else g.setColor(new Color(30, 35, 50, 80));
                g.fillRect(wx, wy, 20, 28);
                // Window frame
                g.setColor(new Color(25, 28, 40));
                g.drawRect(wx, wy, 20, 28);
            }
        }

        // Rooftop section (top 25px) - concrete surface
        g.setColor(new Color(60, 62, 72));
        g.fillRect(0, 0, w, 22);
        // Roof ledge lip
        g.setColor(new Color(80, 83, 95));
        g.fillRect(0, 0, w, 4);
        // AC unit on rooftop
        g.setColor(new Color(70, 72, 82));
        g.fillRect(w / 2 - 15, 5, 30, 14);
        g.setColor(new Color(50, 52, 60));
        g.drawRect(w / 2 - 15, 5, 30, 14);
        // Antenna
        g.setColor(new Color(75, 78, 88));
        g.fillRect(w - 20, 2, 3, 18);
        // Red warning light
        g.setColor(new Color(255, 50, 50, 160));
        g.fillOval(w - 20, 1, 5, 5);
        g.setColor(new Color(255, 40, 40, 40));
        g.fillOval(w - 24, -3, 13, 13);

        g.dispose();
        ImageIO.write(img, "png", new File(path));
        System.out.println("Created: " + new File(path).getName());
    }

    static void generatePagodaLarge(String path, int w, int h) throws Exception {
        // Large skyscraper top section (200x200)
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        enableAA(g);
        // Building body - dark glass/steel
        GradientPaint bodyGrad = new GradientPaint(0, 0, new Color(30, 35, 50), w, 0, new Color(45, 50, 65));
        g.setPaint(bodyGrad);
        g.fillRect(0, 0, w, h);
        // Window grid
        for (int wy = 8; wy < h - 10; wy += 18) {
            for (int wx = 8; wx < w - 10; wx += 22) {
                int bright = rand.nextInt(3);
                if (bright == 0) g.setColor(new Color(255, 220, 100, 80 + rand.nextInt(80)));
                else if (bright == 1) g.setColor(new Color(100, 200, 255, 60 + rand.nextInt(60)));
                else g.setColor(new Color(40, 45, 60, 80));
                g.fillRect(wx, wy, 14, 10);
            }
        }
        // Roof edge at top
        g.setColor(new Color(70, 75, 90));
        g.fillRect(0, 0, w, 5);
        // Rooftop features
        // Water tank
        g.setColor(new Color(55, 55, 65));
        g.fillRect(w / 2 - 20, 8, 40, 25);
        g.setColor(new Color(45, 45, 55));
        g.drawRect(w / 2 - 20, 8, 40, 25);
        // Antenna
        g.setColor(new Color(70, 70, 80));
        g.fillRect(w - 30, 5, 3, 30);
        g.setColor(new Color(255, 40, 40));
        g.fillOval(w - 30, 4, 4, 4);
        // AC units
        g.setColor(new Color(60, 60, 70));
        g.fillRect(15, 10, 25, 15);
        g.fillRect(15, 28, 18, 12);
        // Building edge lines
        g.setColor(new Color(25, 25, 35));
        g.drawLine(0, 0, 0, h);
        g.drawLine(w - 1, 0, w - 1, h);
        g.dispose();
        ImageIO.write(img, "png", new File(path));
        System.out.println("Created: " + new File(path).getName());
    }

    static void generateCityLedge(String path, int w, int h) throws Exception {
        // Wide skyscraper (560x800) - rooftop at top, glass facade below
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        enableAA(g);

        // Building body - dark steel/glass gradient
        GradientPaint bodyGrad = new GradientPaint(0, 0, new Color(32, 36, 52), 0, h, new Color(20, 22, 35));
        g.setPaint(bodyGrad);
        g.fillRect(0, 0, w, h);

        // Vertical structural columns
        g.setColor(new Color(40, 44, 58));
        g.fillRect(0, 0, 4, h);
        g.fillRect(w - 4, 0, 4, h);
        g.fillRect(w / 3, 25, 3, h);
        g.fillRect(2 * w / 3, 25, 3, h);

        // Horizontal floor lines
        g.setColor(new Color(28, 31, 42));
        for (int fy = 28; fy < h; fy += 36) {
            g.fillRect(0, fy, w, 2);
        }

        // Window grid - below rooftop
        for (int wy = 32; wy < h - 5; wy += 36) {
            for (int wx = 8; wx < w - 10; wx += 24) {
                // Skip where structural columns are
                if (Math.abs(wx - w / 3) < 6 || Math.abs(wx - 2 * w / 3) < 6) continue;
                int bright = rand.nextInt(5);
                if (bright == 0) g.setColor(new Color(255, 220, 100, 90 + rand.nextInt(90)));
                else if (bright == 1) g.setColor(new Color(100, 200, 255, 60 + rand.nextInt(70)));
                else if (bright == 2) g.setColor(new Color(255, 180, 80, 50 + rand.nextInt(50)));
                else if (bright == 3) g.setColor(new Color(180, 255, 180, 40 + rand.nextInt(40)));
                else g.setColor(new Color(28, 32, 48, 80));
                g.fillRect(wx, wy, 16, 26);
                // Window frame
                g.setColor(new Color(22, 25, 36));
                g.drawRect(wx, wy, 16, 26);
            }
        }

        // Rooftop section (top 25px)
        g.setColor(new Color(55, 58, 68));
        g.fillRect(0, 0, w, 22);
        // Roof ledge lip
        g.setColor(new Color(78, 82, 95));
        g.fillRect(0, 0, w, 4);
        // Gravel texture on rooftop
        g.setColor(new Color(62, 65, 75, 80));
        for (int i = 0; i < w / 6; i++) {
            g.fillRect(rand.nextInt(w), 5 + rand.nextInt(14), 2 + rand.nextInt(2), 1);
        }
        // AC units on rooftop
        g.setColor(new Color(65, 68, 78));
        g.fillRect(40, 5, 35, 14);
        g.fillRect(120, 5, 25, 14);
        g.fillRect(w - 100, 5, 30, 14);
        g.setColor(new Color(48, 50, 58));
        g.drawRect(40, 5, 35, 14);
        g.drawRect(120, 5, 25, 14);
        g.drawRect(w - 100, 5, 30, 14);
        // Antenna tower
        g.setColor(new Color(70, 73, 83));
        g.fillRect(w / 2 - 2, 2, 4, 20);
        g.fillRect(w / 2 - 8, 8, 16, 2);
        // Red warning lights
        g.setColor(new Color(255, 50, 50, 160));
        g.fillOval(5, 6, 5, 5);
        g.fillOval(w - 12, 6, 5, 5);
        g.fillOval(w / 2 - 2, 0, 5, 5);
        g.setColor(new Color(255, 40, 40, 35));
        g.fillOval(1, 2, 13, 13);
        g.fillOval(w - 16, 2, 13, 13);

        g.dispose();
        ImageIO.write(img, "png", new File(path));
        System.out.println("Created: " + new File(path).getName());
    }

    static void generateCityPillar(String path, int w, int h) throws Exception {
        // Skyscraper body/wall below rooftop (30x80)
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        enableAA(g);
        // Dark building wall
        GradientPaint gp = new GradientPaint(0, 0, new Color(35, 38, 52), w, 0, new Color(28, 30, 42));
        g.setPaint(gp);
        g.fillRect(0, 0, w, h);
        // Windows on the building side
        for (int wy = 5; wy < h - 5; wy += 14) {
            int bright = rand.nextInt(3);
            if (bright == 0) g.setColor(new Color(255, 220, 100, 90));
            else if (bright == 1) g.setColor(new Color(100, 190, 250, 70));
            else g.setColor(new Color(35, 38, 52, 60));
            g.fillRect(5, wy, w - 10, 8);
        }
        // Vertical edge lines
        g.setColor(new Color(22, 22, 30));
        g.drawLine(0, 0, 0, h);
        g.drawLine(w - 1, 0, w - 1, h);
        g.dispose();
        ImageIO.write(img, "png", new File(path));
        System.out.println("Created: " + new File(path).getName());
    }

    static void generateDragonStatue(String path, int w, int h) throws Exception {
        // Rooftop satellite dish / antenna structure (80x60)
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        enableAA(g);
        // Dish mount base
        g.setColor(new Color(55, 55, 65));
        g.fillRect(w / 2 - 5, h / 2, 10, h / 2);
        // Satellite dish (arc)
        g.setColor(new Color(75, 78, 90));
        g.fillArc(5, 5, w - 10, h - 15, 20, 140);
        // Dish inner shadow
        g.setColor(new Color(50, 52, 62));
        g.fillArc(12, 12, w - 24, h - 28, 25, 130);
        // Feed arm
        g.setColor(new Color(65, 65, 75));
        g.setStroke(new BasicStroke(2));
        g.drawLine(w / 2, h / 3, w - 15, 8);
        // Feed point
        g.setColor(new Color(255, 50, 50, 140));
        g.fillOval(w - 18, 5, 6, 6);
        // Blinking light glow
        g.setColor(new Color(255, 40, 40, 35));
        g.fillOval(w - 24, -1, 18, 18);
        g.dispose();
        ImageIO.write(img, "png", new File(path));
        System.out.println("Created: " + new File(path).getName());
    }

    static void generateEvilDoor(String path, int w, int h) throws Exception {
        // Penthouse rooftop entrance / elevator shaft (80x120)
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        enableAA(g);
        // Building structure
        g.setColor(new Color(35, 35, 45));
        g.fillRect(5, 10, w - 10, h - 10);
        // Top overhang
        g.setColor(new Color(50, 50, 60));
        g.fillRect(0, 0, w, 14);
        // Door frame
        g.setColor(new Color(25, 25, 32));
        g.fillRect(15, 30, w - 30, h - 35);
        // Door - slightly lighter
        g.setColor(new Color(40, 40, 50));
        g.fillRect(18, 33, w - 36, h - 40);
        // Door split line
        g.setColor(new Color(25, 25, 32));
        g.drawLine(w / 2, 33, w / 2, h - 7);
        // Red warning symbol above door
        g.setColor(new Color(180, 30, 30));
        g.fillOval(w / 2 - 8, 16, 16, 12);
        g.setColor(new Color(255, 50, 50));
        g.fillOval(w / 2 - 5, 18, 4, 4);
        g.fillOval(w / 2 + 1, 18, 4, 4);
        // Glowing red edge lights
        g.setColor(new Color(255, 30, 30, 60));
        g.fillRect(5, 14, 3, h - 24);
        g.fillRect(w - 8, 14, 3, h - 24);
        // Red glow
        g.setColor(new Color(255, 20, 20, 20));
        g.fillRect(0, 10, 12, h);
        g.fillRect(w - 12, 10, 12, h);
        g.dispose();
        ImageIO.write(img, "png", new File(path));
        System.out.println("Created: " + new File(path).getName());
    }

    // ==================== BACKGROUNDS ====================

    static void generateOsakaBackground(String path, int w, int h) throws Exception {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        enableAA(g);

        // Sky - warm gradient dawn
        GradientPaint sky = new GradientPaint(0, 0, new Color(100, 150, 210), 0, h/2, new Color(210, 170, 130));
        g.setPaint(sky);
        g.fillRect(0, 0, w, h);

        // Sun with rays
        int sunX = w / 3, sunY = 140;
        drawSunRays(g, sunX, sunY, 400, 14);
        g.setColor(new Color(255, 210, 120, 90));
        g.fillOval(sunX - 65, sunY - 65, 130, 130);
        g.setColor(new Color(255, 230, 160, 140));
        g.fillOval(sunX - 45, sunY - 45, 90, 90);
        g.setColor(new Color(255, 240, 200, 180));
        g.fillOval(sunX - 25, sunY - 25, 50, 50);

        // Clouds
        g.setColor(new Color(255, 255, 255, 55));
        for (int i = 0; i < 10; i++) {
            int cx = rand.nextInt(w);
            int cy = 30 + rand.nextInt(180);
            drawCloud(g, cx, cy, 100 + rand.nextInt(150), 30 + rand.nextInt(25));
        }

        // Far distant mountains with snow caps
        drawMountainRangeWithSnow(g, w, h/2 - 120, 220, 90, 0.0018, new Color(140, 155, 190, 160), true);
        // Mist between layers
        g.setColor(new Color(180, 200, 230, 35));
        g.fillRect(0, h/2 - 60, w, 50);
        drawMountainRangeWithSnow(g, w, h/2 - 50, 180, 100, 0.003, new Color(100, 130, 170, 190), true);
        g.setColor(new Color(170, 190, 220, 30));
        g.fillRect(0, h/2 - 10, w, 40);
        drawMountainRangeWithSnow(g, w, h/2 + 10, 150, 110, 0.004, new Color(70, 105, 140, 210), false);

        // Mid-ground rolling green hills
        g.setColor(new Color(60, 110, 50, 230));
        drawMountainRange(g, w, h/2 + 100, 90, 55, 0.005);
        g.setColor(new Color(55, 100, 45, 240));
        drawMountainRange(g, w, h/2 + 130, 70, 45, 0.006);

        // Trees on hills (layered foliage)
        for (int i = 0; i < 50; i++) {
            int tx = rand.nextInt(w);
            int ty = h/2 + 50 + rand.nextInt(100);
            drawJapaneseTree(g, tx, ty, 14 + rand.nextInt(18));
        }

        // Small town buildings (Osaka feel)
        for (int i = 0; i < 30; i++) {
            int bx = rand.nextInt(w);
            int by = h/2 + 150 + rand.nextInt(80);
            int bw = 28 + rand.nextInt(45);
            int bh = 35 + rand.nextInt(65);
            drawJapaneseBuilding(g, bx, by - bh, bw, bh);
        }

        // Torii gate accents
        drawToriiGate(g, w/5, h/2 + 115, 55);
        drawToriiGate(g, w/2 + 100, h/2 + 130, 50);
        drawToriiGate(g, 4*w/5, h/2 + 140, 48);

        // Winding river in foreground
        g.setColor(new Color(80, 120, 60));
        g.fillRect(0, h/2 + 230, w, h);
        Path2D river = new Path2D.Double();
        river.moveTo(0, h/2 + 280);
        river.curveTo(w * 0.15, h/2 + 260, w * 0.3, h/2 + 300, w * 0.5, h/2 + 275);
        river.curveTo(w * 0.7, h/2 + 250, w * 0.85, h/2 + 295, w, h/2 + 270);
        river.lineTo(w, h/2 + 290);
        river.curveTo(w * 0.85, h/2 + 315, w * 0.7, h/2 + 270, w * 0.5, h/2 + 295);
        river.curveTo(w * 0.3, h/2 + 320, w * 0.15, h/2 + 280, 0, h/2 + 300);
        river.closePath();
        g.setColor(new Color(70, 130, 190, 110));
        ((Graphics2D) g).fill(river);
        // River shimmer
        g.setColor(new Color(140, 190, 240, 50));
        for (int i = 0; i < 30; i++) {
            int rx = rand.nextInt(w);
            g.fillOval(rx, h/2 + 270 + rand.nextInt(25), 12 + rand.nextInt(20), 3);
        }

        g.dispose();
        ImageIO.write(img, "jpg", new File(path));
        System.out.println("Created: mountain_background1-2.jpg (Osaka)");
    }

    static void generateSewerBackground(String path, int w, int h) throws Exception {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        enableAA(g);

        // Dark sewer walls
        g.setColor(new Color(22, 27, 22));
        g.fillRect(0, 0, w, h);

        // Brick pattern with individual color variation
        for (int y = 0; y < h; y += 25) {
            int offset = (y / 25) % 2 == 0 ? 0 : 30;
            for (int x = offset; x < w; x += 60) {
                int rv = rand.nextInt(12);
                g.setColor(new Color(28 + rv, 33 + rv, 25 + rv));
                g.fillRect(x + 1, y + 1, 56, 21);
                g.setColor(new Color(18, 20, 16));
                g.drawRect(x, y, 58, 23);
            }
        }

        // Wall-mounted lights
        int waterY = h * 2 / 3;
        for (int lx = 250; lx < w; lx += 400) {
            // Bracket
            g.setColor(new Color(50, 50, 40));
            g.fillRect(lx - 4, waterY / 3, 8, 12);
            // Warm light glow (large)
            g.setColor(new Color(200, 170, 60, 25));
            g.fillOval(lx - 50, waterY / 3 - 45, 100, 90);
            // Medium glow
            g.setColor(new Color(220, 190, 80, 40));
            g.fillOval(lx - 25, waterY / 3 - 20, 50, 40);
            // Flame
            g.setColor(new Color(255, 210, 90, 150));
            g.fillOval(lx - 4, waterY / 3 - 10, 8, 12);
        }

        // Water level
        g.setColor(new Color(15, 35, 15));
        g.fillRect(0, waterY, w, h - waterY);

        // Radioactive glow puddles (brighter)
        for (int i = 0; i < 20; i++) {
            int gx = rand.nextInt(w);
            g.setColor(new Color(40, 190, 35, 35 + rand.nextInt(45)));
            g.fillOval(gx - 60, waterY - 15, 120, 50);
            // Bright center spot
            g.setColor(new Color(80, 255, 60, 30));
            g.fillOval(gx - 15, waterY + 5, 30, 15);
        }

        // Wavy water surface
        g.setColor(new Color(50, 170, 40, 70));
        for (int x = 0; x < w; x += 2) {
            int wy = waterY - 5 + (int)(4 * Math.sin(x * 0.02));
            g.fillRect(x, wy, 2, 8);
        }
        // Secondary wave shimmer
        g.setColor(new Color(70, 200, 50, 35));
        for (int x = 0; x < w; x += 3) {
            int wy = waterY - 2 + (int)(3 * Math.sin(x * 0.03 + 1));
            g.fillRect(x, wy, 2, 4);
        }

        // Radioactive barrels (more)
        for (int i = 0; i < 10; i++) {
            int bx = 150 + rand.nextInt(w - 300);
            drawRadioactiveBarrel(g, bx, waterY - 32, 28, 38);
        }

        // Dripping effects with splash pools
        for (int i = 0; i < 25; i++) {
            int dx = rand.nextInt(w);
            int dy = rand.nextInt(waterY - 50);
            int dripLen = 15 + rand.nextInt(50);
            // Drip streak
            g.setColor(new Color(50, 140, 30, 90));
            g.fillRect(dx, dy, 3, dripLen);
            // Splash at bottom
            g.setColor(new Color(60, 160, 40, 50));
            g.fillOval(dx - 4, dy + dripLen - 2, 10, 5);
            g.fillOval(dx - 2, dy + dripLen + 2, 6, 3);
        }

        // GREEN Mario-style pipes along ceiling
        Color pipeGreen = new Color(33, 173, 33);
        Color pipeDark = new Color(20, 120, 20);
        Color pipeHL = new Color(80, 210, 80);
        // Main horizontal pipe
        GradientPaint pGrad = new GradientPaint(0, 40, pipeHL, 0, 70, pipeDark);
        g.setPaint(pGrad);
        g.fillRect(0, 40, w, 28);
        g.setColor(new Color(10, 80, 10));
        g.drawLine(0, 40, w, 40);
        g.drawLine(0, 68, w, 68);
        g.setColor(new Color(120, 240, 120, 50));
        g.drawLine(0, 48, w, 48);
        // Second smaller pipe
        GradientPaint pGrad2 = new GradientPaint(0, 95, pipeHL, 0, 112, pipeDark);
        g.setPaint(pGrad2);
        g.fillRect(0, 95, w, 16);
        g.setColor(new Color(10, 80, 10));
        g.drawLine(0, 95, w, 95);
        g.drawLine(0, 111, w, 111);
        // Pipe brackets (green)
        g.setColor(pipeDark);
        for (int x = 0; x < w; x += 80) {
            g.fillRect(x, 35, 10, 38);
            g.fillRect(x, 90, 8, 26);
        }
        // Bracket highlights
        g.setColor(pipeGreen);
        for (int x = 0; x < w; x += 80) {
            g.drawLine(x + 1, 36, x + 1, 72);
            g.drawLine(x + 1, 91, x + 1, 115);
        }

        g.dispose();
        ImageIO.write(img, "jpg", new File(path));
        System.out.println("Created: sewer2.jpg (Sewers with radioactive elements)");
    }

    static void generateTokyoBackground(String path, int w, int h) throws Exception {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        enableAA(g);

        // Night sky - deeper gradient
        GradientPaint sky = new GradientPaint(0, 0, new Color(5, 5, 20), 0, h/3, new Color(15, 12, 45));
        g.setPaint(sky);
        g.fillRect(0, 0, w, h);

        // Stars with twinkle
        for (int i = 0; i < 200; i++) {
            int sx = rand.nextInt(w);
            int sy = rand.nextInt(h / 3);
            int brightness = 100 + rand.nextInt(155);
            int ss = 1 + rand.nextInt(3);
            g.setColor(new Color(brightness, brightness, brightness - rand.nextInt(30), 140 + rand.nextInt(115)));
            g.fillOval(sx, sy, ss, ss);
            // Twinkle cross on larger stars
            if (ss >= 3) {
                g.setColor(new Color(255, 255, 255, 50));
                g.drawLine(sx - 2, sy + ss / 2, sx + ss + 2, sy + ss / 2);
                g.drawLine(sx + ss / 2, sy - 2, sx + ss / 2, sy + ss + 2);
            }
        }

        // Moon with craters
        int moonX = w - 300, moonY = 60;
        g.setColor(new Color(240, 240, 220, 210));
        g.fillOval(moonX, moonY, 100, 100);
        // Moon glow
        g.setColor(new Color(200, 200, 180, 25));
        g.fillOval(moonX - 15, moonY - 15, 130, 130);
        // Crescent shadow
        g.setColor(new Color(5, 5, 20));
        g.fillOval(moonX + 20, moonY - 10, 100, 100);
        // Craters on visible part
        g.setColor(new Color(210, 210, 195, 70));
        g.fillOval(moonX + 5, moonY + 20, 14, 11);
        g.fillOval(moonX + 15, moonY + 55, 9, 7);
        g.fillOval(moonX + 2, moonY + 70, 16, 12);
        g.fillOval(moonX + 12, moonY + 38, 7, 5);

        // Atmospheric haze at horizon
        int skylineY = h / 3;
        g.setColor(new Color(20, 15, 50, 60));
        g.fillRect(0, skylineY - 30, w, 60);

        // Tokyo skyline - varied buildings
        for (int i = 0; i < 70; i++) {
            int bx = rand.nextInt(w);
            int bw = 20 + rand.nextInt(55);
            int bh = 80 + rand.nextInt(220);
            int by = skylineY + 100 - bh / 2;
            int bc = 12 + rand.nextInt(22);
            g.setColor(new Color(bc, bc - 2, bc + 15));
            g.fillRect(bx, by, bw, bh + 200);

            // Rooftop features
            int feature = rand.nextInt(6);
            g.setColor(new Color(bc + 8, bc + 6, bc + 20));
            if (feature == 0) { // Antenna
                g.fillRect(bx + bw / 2 - 1, by - 18, 2, 18);
                g.setColor(new Color(255, 50, 50, 120));
                g.fillOval(bx + bw / 2 - 2, by - 20, 4, 4);
            } else if (feature == 1) { // Satellite dish
                g.fillArc(bx + bw / 2 - 8, by - 10, 16, 12, 0, 180);
            } else if (feature == 2) { // Water tank
                g.fillRect(bx + 4, by - 8, 10, 8);
            } else if (feature == 3 && bw > 35) { // AC unit
                g.fillRect(bx + bw - 14, by - 6, 12, 6);
            }

            // Windows - neon glow
            for (int wy = by + 5; wy < by + bh + 100; wy += 8) {
                for (int wx = bx + 3; wx < bx + bw - 5; wx += 7) {
                    if (rand.nextInt(3) != 0) {
                        Color winColor;
                        int r = rand.nextInt(5);
                        if (r == 0) winColor = new Color(255, 220, 100, 120 + rand.nextInt(100));
                        else if (r == 1) winColor = new Color(100, 200, 255, 80 + rand.nextInt(80));
                        else if (r == 2) winColor = new Color(255, 100, 150, 80 + rand.nextInt(80));
                        else if (r == 3) winColor = new Color(200, 200, 255, 60 + rand.nextInt(60));
                        else winColor = new Color(100, 255, 100, 50 + rand.nextInt(60));
                        g.setColor(winColor);
                        g.fillRect(wx, wy, 4, 4);
                    }
                }
            }
        }

        // Tokyo Tower with lattice detail
        int towerX = w / 2;
        int towerBase = skylineY + 180;
        int towerTop = skylineY - 130;
        // Tower body
        g.setColor(new Color(180, 50, 30));
        int[] ttx = {towerX - 35, towerX, towerX + 35};
        int[] tty = {towerBase, towerTop, towerBase};
        g.fillPolygon(ttx, tty, 3);
        // Glow overlay
        g.setColor(new Color(255, 80, 40, 80));
        g.fillPolygon(ttx, tty, 3);
        // Lattice cross-hatch
        g.setColor(new Color(200, 80, 40, 100));
        g.setStroke(new BasicStroke(1));
        int towerH = towerBase - towerTop;
        for (int ly = towerTop + 15; ly < towerBase; ly += 18) {
            double ratio = (double)(ly - towerTop) / towerH;
            int halfW = (int)(35 * ratio);
            g.drawLine(towerX - halfW, ly, towerX + halfW, ly);
            // Cross diagonals
            if (ly + 18 < towerBase) {
                double nextRatio = (double)(ly + 18 - towerTop) / towerH;
                int nextHalfW = (int)(35 * nextRatio);
                g.drawLine(towerX - halfW, ly, towerX + nextHalfW, ly + 18);
                g.drawLine(towerX + halfW, ly, towerX - nextHalfW, ly + 18);
            }
        }
        // Tower observation deck
        g.setColor(new Color(200, 60, 30));
        double deckRatio = 0.45;
        int deckY = towerTop + (int)(towerH * deckRatio);
        int deckHalfW = (int)(35 * deckRatio) + 5;
        g.fillRect(towerX - deckHalfW, deckY, deckHalfW * 2, 6);
        // Tower tip light
        g.setColor(new Color(255, 50, 50));
        g.fillOval(towerX - 3, towerTop, 6, 6);
        g.setColor(new Color(255, 50, 50, 40));
        g.fillOval(towerX - 20, towerTop - 17, 40, 40);

        // Neon signs with multi-layer glow
        String[] neonTexts = {"\u5175", "\u6b66\u58eb", "\u5fcd\u8005", "\u6771\u4eac", "\u62c9\u9762"};
        Color[] neonColors = {new Color(255, 50, 100), new Color(50, 200, 255), new Color(255, 200, 50),
                              new Color(100, 255, 100), new Color(255, 100, 255)};
        for (int i = 0; i < 12; i++) {
            int nx = 100 + rand.nextInt(w - 200);
            int ny = skylineY + 40 + rand.nextInt(220);
            Color nc = neonColors[rand.nextInt(neonColors.length)];
            // Outer glow
            g.setColor(new Color(nc.getRed(), nc.getGreen(), nc.getBlue(), 25));
            g.fillRoundRect(nx - 18, ny - 18, 86, 66, 12, 12);
            // Mid glow
            g.setColor(new Color(nc.getRed(), nc.getGreen(), nc.getBlue(), 50));
            g.fillRoundRect(nx - 10, ny - 10, 70, 50, 8, 8);
            // Sign face (dark)
            g.setColor(new Color(15, 15, 25));
            g.fillRoundRect(nx, ny, 50, 30, 5, 5);
            // Neon text
            g.setColor(nc);
            g.setFont(new Font("SansSerif", Font.BOLD, 16));
            g.drawString(neonTexts[rand.nextInt(neonTexts.length)], nx + 6, ny + 20);
        }

        // Ground/street
        g.setColor(new Color(25, 25, 35));
        g.fillRect(0, skylineY + 300, w, h);
        // Road lane markings
        g.setColor(new Color(180, 180, 80, 50));
        for (int x = 0; x < w; x += 80) {
            g.fillRect(x, skylineY + 330, 40, 3);
        }
        // Street lights
        for (int x = 120; x < w; x += 320) {
            g.setColor(new Color(50, 50, 60));
            g.fillRect(x, skylineY + 270, 4, 55);
            // Light glow
            g.setColor(new Color(255, 220, 100, 50));
            g.fillOval(x - 15, skylineY + 258, 34, 18);
            g.setColor(new Color(255, 230, 130, 100));
            g.fillOval(x - 4, skylineY + 263, 12, 8);
        }

        g.dispose();
        ImageIO.write(img, "jpg", new File(path));
        System.out.println("Created: city2.jpg (Tokyo night skyline)");
    }

    static void generateDojoBackground(String path, int w, int h) throws Exception {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        enableAA(g);

        // Rich sunset sky
        GradientPaint sky = new GradientPaint(0, 0, new Color(190, 90, 50), 0, h/3, new Color(225, 160, 100));
        g.setPaint(sky);
        g.fillRect(0, 0, w, h);

        // Wispy sunset clouds
        for (int i = 0; i < 8; i++) {
            int cx = rand.nextInt(w);
            int cy = 25 + rand.nextInt(h / 4);
            g.setColor(new Color(255, 170, 110, 35 + rand.nextInt(25)));
            g.fillOval(cx, cy, 180 + rand.nextInt(250), 8 + rand.nextInt(14));
        }

        // Setting sun with glow
        int sunCX = w / 2, sunCY = 150;
        // Outer sun halo
        g.setColor(new Color(255, 140, 60, 40));
        g.fillOval(sunCX - 140, sunCY - 140, 280, 280);
        g.setColor(new Color(255, 130, 50, 120));
        g.fillOval(sunCX - 100, sunCY - 100, 200, 200);
        g.setColor(new Color(255, 160, 80, 180));
        g.fillOval(sunCX - 75, sunCY - 75, 150, 150);
        g.setColor(new Color(255, 200, 130, 220));
        g.fillOval(sunCX - 50, sunCY - 50, 100, 100);

        // Distant mountains (two layers)
        g.setColor(new Color(130, 75, 55, 140));
        drawMountainRange(g, w, h/3 - 70, 160, 90, 0.0025);
        g.setColor(new Color(110, 60, 45, 170));
        drawMountainRange(g, w, h/3 - 30, 120, 70, 0.004);

        // Dojo building with multi-tier pagoda roof
        int dojoX = w / 2 - 220;
        int dojoY = h / 3;
        // Main building walls with panel detail
        g.setColor(new Color(165, 145, 125));
        g.fillRect(dojoX, dojoY, 440, 200);
        // Wall panels
        g.setColor(new Color(150, 130, 110));
        for (int px = dojoX + 20; px < dojoX + 420; px += 55) {
            g.drawRect(px, dojoY + 10, 45, 80);
        }
        // Multi-tiered roof
        for (int tier = 0; tier < 3; tier++) {
            int tierY = dojoY - 25 - tier * 28;
            int tierW = 480 - tier * 50;
            int tierX = dojoX + 220 - tierW / 2;
            g.setColor(new Color(50 + tier * 8, 35 + tier * 5, 25 + tier * 3));
            int[] rX = {tierX - 18, tierX + tierW / 2, tierX + tierW + 18};
            int[] rY = {tierY + 28, tierY, tierY + 28};
            g.fillPolygon(rX, rY, 3);
            // Gold trim
            g.setColor(new Color(200, 170, 50, 150));
            g.setStroke(new BasicStroke(1.5f));
            g.drawPolygon(rX, rY, 3);
        }
        // Roof peak ornament
        g.setColor(new Color(200, 170, 50));
        g.fillOval(dojoX + 214, dojoY - 95, 12, 12);
        // Entrance
        g.setColor(new Color(45, 28, 18));
        g.fillRect(dojoX + 175, dojoY + 80, 90, 120);
        // Sliding door lines
        g.setColor(new Color(60, 40, 25));
        g.drawLine(dojoX + 220, dojoY + 80, dojoX + 220, dojoY + 200);
        // Wall lanterns
        g.setColor(new Color(255, 180, 80, 80));
        g.fillOval(dojoX + 140, dojoY + 90, 20, 25);
        g.fillOval(dojoX + 280, dojoY + 90, 20, 25);
        g.setColor(new Color(255, 200, 100, 160));
        g.fillOval(dojoX + 145, dojoY + 95, 10, 15);
        g.fillOval(dojoX + 285, dojoY + 95, 10, 15);

        // Stone courtyard with individual tile variation
        g.setColor(new Color(160, 150, 140));
        g.fillRect(0, h / 3 + 200, w, h);
        for (int y = h / 3 + 200; y < h; y += 40) {
            int xoff = (y / 40) % 2 == 0 ? 0 : 20;
            for (int x = xoff; x < w; x += 40) {
                int rv = rand.nextInt(14) - 7;
                g.setColor(new Color(155 + rv, 145 + rv, 135 + rv));
                g.fillRect(x + 1, y + 1, 36, 36);
                // Moss between some tiles
                if (rand.nextInt(5) == 0) {
                    g.setColor(new Color(55, 95, 35, 70));
                    g.fillRect(x - 1, y - 1, 40, 3);
                }
            }
            // Tile outlines
            g.setColor(new Color(130, 120, 110));
            for (int x = xoff; x < w; x += 40) {
                g.drawRect(x, y, 38, 38);
            }
        }

        // Zen garden area
        int zenX = w / 6, zenY = h / 3 + 310, zenW = 320, zenH = 160;
        g.setColor(new Color(195, 185, 165));
        g.fillRoundRect(zenX, zenY, zenW, zenH, 12, 12);
        g.setColor(new Color(175, 165, 145));
        g.setStroke(new BasicStroke(1));
        for (int ly = zenY + 12; ly < zenY + zenH - 12; ly += 6) {
            // Wavy raked lines
            for (int lx = zenX + 10; lx < zenX + zenW - 10; lx += 2) {
                int ry = ly + (int)(2 * Math.sin(lx * 0.04));
                g.drawLine(lx, ry, lx + 1, ry);
            }
        }
        // Zen rocks
        g.setColor(new Color(85, 80, 75));
        g.fillOval(zenX + 80, zenY + 45, 35, 22);
        g.setColor(new Color(75, 70, 65));
        g.fillOval(zenX + 190, zenY + 65, 28, 18);
        g.setColor(new Color(90, 85, 80));
        g.fillOval(zenX + 140, zenY + 80, 22, 15);
        // Rock shadows
        g.setColor(new Color(0, 0, 0, 20));
        g.fillOval(zenX + 82, zenY + 62, 32, 8);
        g.fillOval(zenX + 192, zenY + 78, 25, 6);

        // Training dummy
        int dummyX = 2 * w / 3 + 100, dummyY = h / 3 + 290;
        g.setColor(new Color(140, 100, 55));
        g.fillRect(dummyX - 4, dummyY, 8, 65);
        g.fillRect(dummyX - 22, dummyY + 18, 44, 7);
        g.setColor(new Color(120, 85, 40));
        g.fillOval(dummyX - 10, dummyY - 14, 20, 18);
        // Shadow
        g.setColor(new Color(0, 0, 0, 20));
        g.fillOval(dummyX - 15, dummyY + 60, 30, 8);

        // Cherry blossom trees (richer)
        for (int i = 0; i < 14; i++) {
            int tx = 80 + rand.nextInt(w - 160);
            int ty = h / 3 + 90 + rand.nextInt(160);
            drawCherryBlossomTree(g, tx, ty, 42 + rand.nextInt(35));
        }

        // Falling petals (varied colors)
        Color[] petalColors = {
            new Color(255, 180, 200, 140), new Color(255, 160, 190, 130),
            new Color(255, 200, 215, 150), new Color(255, 140, 180, 120),
            new Color(255, 220, 230, 100)
        };
        for (int i = 0; i < 80; i++) {
            g.setColor(petalColors[rand.nextInt(petalColors.length)]);
            int px = rand.nextInt(w);
            int py = rand.nextInt(h);
            g.fillOval(px, py, 3 + rand.nextInt(4), 2 + rand.nextInt(3));
        }

        // Stone lanterns with warm glow
        drawStoneLantern(g, w / 4, h / 3 + 250);
        drawStoneLantern(g, 3 * w / 4, h / 3 + 230);
        drawStoneLantern(g, w / 2 - 300, h / 3 + 260);
        drawStoneLantern(g, w / 2 + 300, h / 3 + 245);

        g.dispose();
        ImageIO.write(img, "jpg", new File(path));
        System.out.println("Created: Dojo_Background2.jpg (Dojo with cherry blossoms)");
    }

    // ==================== STORY PAGES ====================

    static void generateTitlePage(String path) throws Exception {
        BufferedImage img = new BufferedImage(1600, 800, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        enableAA(g);
        enableTextAA(g);

        // Dark Japanese-themed background
        GradientPaint bg = new GradientPaint(0, 0, new Color(10, 5, 15), 0, 800, new Color(30, 10, 10));
        g.setPaint(bg);
        g.fillRect(0, 0, 1600, 800);

        // Blood moon
        g.setColor(new Color(150, 20, 20, 80));
        g.fillOval(700, 50, 200, 200);
        g.setColor(new Color(180, 30, 30, 120));
        g.fillOval(720, 70, 160, 160);

        // Bamboo silhouettes
        g.setColor(new Color(20, 15, 25));
        for (int i = 0; i < 15; i++) {
            int bx = rand.nextInt(1600);
            g.fillRect(bx, 0, 4, 800);
            for (int l = 0; l < 5; l++) {
                int ly = 100 + l * 150;
                g.fillOval(bx - 8, ly, 20, 6);
            }
        }

        // Title
        g.setColor(new Color(200, 30, 30));
        g.setFont(new Font("Serif", Font.BOLD, 80));
        FontMetrics fm = g.getFontMetrics();
        String title = "KATSUYA'S REVENGE";
        g.drawString(title, (1600 - fm.stringWidth(title))/2, 380);
        // Subtitle
        g.setColor(new Color(200, 180, 150));
        g.setFont(new Font("Serif", Font.ITALIC, 28));
        fm = g.getFontMetrics();
        String sub = "A Shadow Ninja's Tale of Vengeance";
        g.drawString(sub, (1600 - fm.stringWidth(sub))/2, 440);

        // Ninja silhouette
        drawNinjaSilhouette(g, 250, 500, 100, false);
        drawNinjaSilhouette(g, 1250, 500, 100, true);

        // Decorative border
        g.setColor(new Color(200, 170, 50, 80));
        g.setStroke(new BasicStroke(2));
        g.drawRect(40, 40, 1520, 720);
        g.drawRect(48, 48, 1504, 704);

        // Play prompt
        g.setColor(new Color(200, 180, 150, 180));
        g.setFont(new Font("SansSerif", Font.PLAIN, 20));
        fm = g.getFontMetrics();
        String play = "Click to Begin";
        g.drawString(play, (1600 - fm.stringWidth(play))/2, 700);

        g.dispose();
        ImageIO.write(img, "png", new File(path));
        System.out.println("Created: Page1-2.PNG (Title)");
    }

    static void generateProloguePage1(String path) throws Exception {
        generateStoryPageWithText(path, "THE LEGEND BEGINS",
            new String[]{
                "In the shadow-veiled mountains of old Japan,",
                "a lone ninja named Katsuya trained in silence.",
                "",
                "When the Shadow Clan betrayed his master,",
                "Katsuya swore an oath of vengeance.",
                "",
                "Armed with nothing but his blade and kunai,",
                "he sets out through the streets of Osaka..."
            }, new Color(15, 10, 25));
    }

    static void generateProloguePage2(String path) throws Exception {
        generateStoryPageWithText(path, "THE OATH",
            new String[]{
                "\"I will find them in the darkness",
                "where they hide like cowards.\"",
                "",
                "\"Through sewers and city streets,",
                "across rooftops under moonlight.\"",
                "",
                "\"Until I reach their dojo",
                "and end this shadow war forever.\""
            }, new Color(10, 15, 25));
    }

    static void generateChapterPage(String path, String title, String subtitle, String desc, Color bg) throws Exception {
        BufferedImage img = new BufferedImage(1600, 800, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        enableAA(g);
        enableTextAA(g);

        GradientPaint gp = new GradientPaint(0, 0, bg, 0, 800, bg.darker());
        g.setPaint(gp);
        g.fillRect(0, 0, 1600, 800);

        // Decorative border
        g.setColor(new Color(200, 170, 50, 60));
        g.setStroke(new BasicStroke(2));
        g.drawRect(50, 50, 1500, 700);

        // Title
        g.setColor(new Color(200, 170, 50));
        g.setFont(new Font("Serif", Font.BOLD, 56));
        FontMetrics fm = g.getFontMetrics();
        g.drawString(title, (1600 - fm.stringWidth(title))/2, 300);

        // Subtitle
        g.setColor(new Color(200, 180, 160));
        g.setFont(new Font("Serif", Font.ITALIC, 36));
        fm = g.getFontMetrics();
        g.drawString(subtitle, (1600 - fm.stringWidth(subtitle))/2, 370);

        // Description
        g.setColor(new Color(180, 170, 160, 200));
        g.setFont(new Font("Serif", Font.PLAIN, 22));
        fm = g.getFontMetrics();
        g.drawString(desc, (1600 - fm.stringWidth(desc))/2, 450);

        // Continue prompt
        g.setColor(new Color(200, 180, 150, 150));
        g.setFont(new Font("SansSerif", Font.PLAIN, 18));
        fm = g.getFontMetrics();
        String cont = "Click 'Next' to continue";
        g.drawString(cont, (1600 - fm.stringWidth(cont))/2, 700);

        g.dispose();
        ImageIO.write(img, "png", new File(path));
        System.out.println("Created: " + new File(path).getName());
    }

    static void generateEndingPage(String path) throws Exception {
        BufferedImage img = new BufferedImage(1600, 800, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        enableAA(g);
        enableTextAA(g);

        // Dawn sky
        GradientPaint sky = new GradientPaint(0, 0, new Color(30, 20, 50), 0, 800, new Color(200, 140, 80));
        g.setPaint(sky);
        g.fillRect(0, 0, 1600, 800);

        // Sunrise
        g.setColor(new Color(255, 180, 80, 100));
        g.fillOval(700, 400, 200, 200);

        // Cherry blossom petals
        g.setColor(new Color(255, 180, 200, 120));
        for (int i = 0; i < 50; i++) {
            g.fillOval(rand.nextInt(1600), rand.nextInt(800), 5, 4);
        }

        g.setColor(new Color(200, 170, 50));
        g.setFont(new Font("Serif", Font.BOLD, 72));
        FontMetrics fm = g.getFontMetrics();
        String title = "THE END";
        g.drawString(title, (1600 - fm.stringWidth(title))/2, 300);

        g.setColor(new Color(220, 200, 180));
        g.setFont(new Font("Serif", Font.ITALIC, 28));
        fm = g.getFontMetrics();
        String sub = "Katsuya's honor has been restored.";
        g.drawString(sub, (1600 - fm.stringWidth(sub))/2, 380);

        g.setColor(new Color(200, 180, 160, 180));
        g.setFont(new Font("Serif", Font.PLAIN, 22));
        fm = g.getFontMetrics();
        String thanks = "Thank you for playing Katsuya's Revenge!";
        g.drawString(thanks, (1600 - fm.stringWidth(thanks))/2, 440);

        g.dispose();
        ImageIO.write(img, "png", new File(path));
        System.out.println("Created: Page7.PNG (Ending)");
    }

    static void generateStoryPageWithText(String path, String title, String[] lines, Color bg) throws Exception {
        BufferedImage img = new BufferedImage(1600, 800, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        enableAA(g);
        enableTextAA(g);

        GradientPaint gp = new GradientPaint(0, 0, bg, 0, 800, bg.darker());
        g.setPaint(gp);
        g.fillRect(0, 0, 1600, 800);

        // Border
        g.setColor(new Color(200, 170, 50, 50));
        g.setStroke(new BasicStroke(2));
        g.drawRect(50, 50, 1500, 700);

        // Title
        g.setColor(new Color(200, 170, 50));
        g.setFont(new Font("Serif", Font.BOLD, 44));
        FontMetrics fm = g.getFontMetrics();
        g.drawString(title, (1600 - fm.stringWidth(title))/2, 180);

        // Horizontal divider
        g.setColor(new Color(200, 170, 50, 80));
        g.drawLine(500, 210, 1100, 210);

        // Story text
        g.setColor(new Color(200, 190, 175));
        g.setFont(new Font("Serif", Font.PLAIN, 24));
        fm = g.getFontMetrics();
        int startY = 280;
        for (String line : lines) {
            if (!line.isEmpty()) {
                g.drawString(line, (1600 - fm.stringWidth(line))/2, startY);
            }
            startY += 38;
        }

        // Continue prompt
        g.setColor(new Color(200, 180, 150, 150));
        g.setFont(new Font("SansSerif", Font.PLAIN, 18));
        fm = g.getFontMetrics();
        String cont = "Click 'Next' to continue";
        g.drawString(cont, (1600 - fm.stringWidth(cont))/2, 700);

        g.dispose();
        ImageIO.write(img, "png", new File(path));
        System.out.println("Created: " + new File(path).getName());
    }

    // ==================== HELPER DRAWING METHODS ====================

    static void drawMountainRange(Graphics2D g, int w, int baseY, int maxHeight, int variance, double freq) {
        int[] xp = new int[w + 2];
        int[] yp = new int[w + 2];
        for (int i = 0; i < w; i++) {
            xp[i] = i;
            yp[i] = baseY - (int)(maxHeight * (0.5 + 0.5 * Math.sin(i * freq)) + variance * Math.sin(i * freq * 3.7) * 0.3);
        }
        xp[w] = w;
        yp[w] = baseY + 500;
        xp[w + 1] = 0;
        yp[w + 1] = baseY + 500;
        g.fillPolygon(xp, yp, w + 2);
    }

    static void drawMountainRangeWithSnow(Graphics2D g, int w, int baseY, int maxHeight, int variance, double freq, Color color, boolean snowCaps) {
        int[] xp = new int[w + 2];
        int[] yp = new int[w + 2];
        for (int i = 0; i < w; i++) {
            xp[i] = i;
            yp[i] = baseY - (int)(maxHeight * (0.5 + 0.5 * Math.sin(i * freq)) + variance * Math.sin(i * freq * 3.7) * 0.3);
        }
        xp[w] = w;
        yp[w] = baseY + 500;
        xp[w + 1] = 0;
        yp[w + 1] = baseY + 500;
        g.setColor(color);
        g.fillPolygon(xp, yp, w + 2);
        // Snow caps on peaks
        if (snowCaps) {
            g.setColor(new Color(240, 245, 255, 140));
            for (int i = 1; i < w - 1; i++) {
                // Find local maxima (peaks)
                if (yp[i] < yp[i - 1] && yp[i] < yp[i + 1] && yp[i] < baseY - maxHeight * 0.3) {
                    int peakY = yp[i];
                    int[] sx = {i - 18, i, i + 18};
                    int[] sy = {peakY + 16, peakY, peakY + 16};
                    g.fillPolygon(sx, sy, 3);
                }
            }
        }
    }

    static void drawSunRays(Graphics2D g, int cx, int cy, int maxRadius, int rayCount) {
        for (int i = 0; i < rayCount; i++) {
            double angle = (2 * Math.PI * i) / rayCount;
            double nextAngle = angle + Math.PI / rayCount * 0.5;
            int[] rx = {cx, cx + (int)(maxRadius * Math.cos(angle)), cx + (int)(maxRadius * Math.cos(nextAngle))};
            int[] ry = {cy, cy + (int)(maxRadius * Math.sin(angle)), cy + (int)(maxRadius * Math.sin(nextAngle))};
            g.setColor(new Color(255, 220, 140, 20 + rand.nextInt(15)));
            g.fillPolygon(rx, ry, 3);
        }
    }

    static void drawCloud(Graphics2D g, int x, int y, int w, int h) {
        g.fillOval(x, y, w / 2, h);
        g.fillOval(x + w / 4, y - h / 3, w / 2, h);
        g.fillOval(x + w / 2, y, w / 2, h);
        g.fillOval(x + w / 6, y + h / 6, w / 3, h * 2 / 3);
    }

    static void drawJapaneseTree(Graphics2D g, int x, int y, int size) {
        // Trunk with shadow
        g.setColor(new Color(55, 35, 20));
        g.fillRect(x - 2, y, 5, size);
        g.setColor(new Color(70, 45, 28));
        g.fillRect(x - 2, y, 3, size);
        // Layered canopy (multiple overlapping ovals)
        int gr = 70 + rand.nextInt(40);
        g.setColor(new Color(35 + rand.nextInt(25), gr, 25 + rand.nextInt(18)));
        g.fillOval(x - size / 2, y - size / 3, size, size * 2 / 3);
        g.setColor(new Color(30 + rand.nextInt(20), gr + 10, 22 + rand.nextInt(15), 200));
        g.fillOval(x - size / 3, y - size / 2, size * 2 / 3, size / 2);
        g.setColor(new Color(40 + rand.nextInt(15), gr - 5, 28 + rand.nextInt(12), 180));
        g.fillOval(x - size / 4 - 3, y - size / 4, size * 3 / 4, size / 2);
    }

    static void drawJapaneseBuilding(Graphics2D g, int x, int y, int w, int h) {
        // Wall
        int wr = 175 + rand.nextInt(35);
        g.setColor(new Color(wr, wr - 10, wr - 25));
        g.fillRect(x, y, w, h);
        // Roof (slightly curved via arc)
        g.setColor(new Color(48, 38, 32));
        int[] rx = {x - 6, x + w / 2, x + w + 6};
        int[] ry = {y, y - 18, y};
        g.fillPolygon(rx, ry, 3);
        // Roof ridge
        g.setColor(new Color(60, 48, 38));
        g.fillRect(x - 6, y - 2, w + 12, 3);
        // Windows
        g.setColor(new Color(70, 45, 25, 120));
        if (w > 30) {
            g.fillRect(x + 5, y + 8, w / 4 - 2, h / 4);
            g.fillRect(x + w - w / 4 - 3, y + 8, w / 4 - 2, h / 4);
        }
        // Door
        g.setColor(new Color(75, 48, 28));
        g.fillRect(x + w / 3, y + h / 2, w / 3, h / 2);
        // Door lines (sliding)
        g.setColor(new Color(90, 60, 35));
        g.drawLine(x + w / 2, y + h / 2, x + w / 2, y + h);
    }

    static void drawToriiGate(Graphics2D g, int x, int y, int size) {
        g.setColor(new Color(180, 40, 30));
        // Pillars
        g.fillRect(x - size / 2, y - size, 7, size);
        g.fillRect(x + size / 2 - 7, y - size, 7, size);
        // Top beam (kasagi)
        g.setColor(new Color(190, 45, 32));
        g.fillRect(x - size / 2 - 10, y - size, size + 20, 9);
        // Curved top
        g.fillArc(x - size / 2 - 12, y - size - 4, size + 24, 10, 0, 180);
        // Second beam (nuki)
        g.setColor(new Color(170, 38, 28));
        g.fillRect(x - size / 2 + 2, y - size + 16, size - 4, 5);
    }

    static void drawCherryBlossomTree(Graphics2D g, int x, int y, int size) {
        // Trunk
        g.setColor(new Color(75, 45, 28));
        g.fillRect(x - 5, y, 10, size);
        // Trunk highlight
        g.setColor(new Color(90, 55, 35));
        g.fillRect(x - 5, y, 4, size);
        // Main branches
        g.setColor(new Color(70, 42, 25));
        g.setStroke(new BasicStroke(3));
        g.drawLine(x, y + size / 3, x - size / 2, y - size / 3);
        g.drawLine(x, y + size / 3, x + size / 2, y - size / 4);
        g.drawLine(x, y + size / 2, x - size / 3, y);
        g.drawLine(x, y + size / 4, x + size / 3, y + size / 8);
        // Sub-branches
        g.setStroke(new BasicStroke(1.5f));
        g.drawLine(x - size / 3, y - size / 6, x - size / 2 - 10, y - size / 2);
        g.drawLine(x + size / 3, y - size / 8, x + size / 2 + 8, y - size / 3);
        g.setStroke(new BasicStroke(1));
        // Blossoms (more, varied pinks + some white)
        Color[] pinks = {
            new Color(255, 175, 200, 175), new Color(255, 155, 185, 160),
            new Color(255, 195, 210, 170), new Color(255, 140, 175, 150),
            new Color(255, 230, 240, 130)  // near-white
        };
        for (int i = 0; i < 40; i++) {
            g.setColor(pinks[rand.nextInt(pinks.length)]);
            int bx = x + rand.nextInt(size + 10) - size / 2 - 5;
            int by = y - rand.nextInt(size + 5);
            g.fillOval(bx, by, 5 + rand.nextInt(6), 4 + rand.nextInt(5));
        }
    }

    static void drawStoneLantern(Graphics2D g, int x, int y) {
        // Warm glow radius
        g.setColor(new Color(255, 200, 100, 20));
        g.fillOval(x - 35, y - 40, 70, 70);
        g.setColor(new Color(255, 200, 100, 15));
        g.fillOval(x - 50, y - 55, 100, 100);
        // Stone parts
        g.setColor(new Color(140, 135, 125));
        // Base
        g.fillRect(x - 14, y + 22, 28, 10);
        // Pillar
        g.fillRect(x - 6, y - 10, 12, 32);
        // Lantern top
        g.fillRect(x - 16, y - 22, 32, 14);
        // Roof
        int[] rx = {x - 20, x, x + 20};
        int[] ry = {y - 22, y - 36, y - 22};
        g.fillPolygon(rx, ry, 3);
        // Light (brighter)
        g.setColor(new Color(255, 210, 110, 160));
        g.fillOval(x - 8, y - 18, 16, 10);
        g.setColor(new Color(255, 240, 180, 80));
        g.fillOval(x - 12, y - 22, 24, 18);
    }

    static void drawRadioactiveBarrel(Graphics2D g, int x, int y, int w, int h) {
        // Glow behind barrel
        g.setColor(new Color(80, 200, 40, 35));
        g.fillOval(x - 8, y - 8, w + 16, h + 16);
        // Barrel body
        g.setColor(new Color(70, 70, 40));
        g.fillRoundRect(x, y, w, h, 6, 6);
        // Barrel bands
        g.setColor(new Color(55, 55, 30));
        g.fillRect(x, y + 3, w, 3);
        g.fillRect(x, y + h - 6, w, 3);
        g.drawRoundRect(x, y, w, h, 6, 6);
        // Hazard symbol (larger trefoil)
        int cx = x + w / 2, cy = y + h / 2;
        g.setColor(new Color(220, 210, 30));
        g.fillOval(cx - 7, cy - 7, 14, 14);
        // Trefoil blades
        for (int a = 0; a < 3; a++) {
            double ang = Math.PI / 2 + a * 2.0 * Math.PI / 3.0;
            int bx = cx + (int)(8 * Math.cos(ang));
            int by = cy - (int)(8 * Math.sin(ang));
            g.fillOval(bx - 4, by - 4, 8, 8);
        }
        g.setColor(new Color(70, 70, 40));
        g.fillOval(cx - 3, cy - 3, 6, 6);
    }

    static void drawNinjaSilhouette(Graphics2D g, int x, int y, int size, boolean flip) {
        int dir = flip ? -1 : 1;
        g.setColor(new Color(10, 5, 15));
        // Body
        g.fillOval(x - 15, y, 30, 40);
        // Head
        g.fillOval(x - 10, y - 18, 20, 20);
        // Sword
        g.setStroke(new BasicStroke(3));
        g.drawLine(x + 10 * dir, y - 10, x + 35 * dir, y - 40);
        // Legs
        g.fillRect(x - 8, y + 35, 6, 25);
        g.fillRect(x + 2, y + 35, 6, 25);
    }

    // ==================== UTILITIES ====================

    static void enableAA(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }

    static void enableTextAA(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }

    static Color brighter(Color c, int amount) {
        return new Color(
            Math.min(255, c.getRed() + amount),
            Math.min(255, c.getGreen() + amount),
            Math.min(255, c.getBlue() + amount)
        );
    }
}
