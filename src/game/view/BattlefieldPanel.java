package game.view;

import game.model.Enemy;
import game.model.Hero;
import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

/** draws the battle sprites and runs their animations. */
public class BattlefieldPanel extends JPanel {

    private BufferedImage backgroundImage;
    private BufferedImage enemyImage;
    private BufferedImage keyImage;
    private List<BufferedImage> heroImages = new ArrayList<>();
    private int enemyOffset;
    private float enemyScale = 1.0f;
    private float enemyAlpha = 1.0f;
    private float keyScale = 1.0f;
    private boolean showAncientKey;

    public BattlefieldPanel(List<Hero> heroes, Enemy enemy) {
        setPreferredSize(new Dimension(760, 330));
        loadImages(heroes, enemy);
    }

    /** shakes the enemy when the party lands an attack. */
    public void animateEnemyHit() {
        Timer timer = new Timer(60, null);
        timer.addActionListener(new ActionListener() {
            private int frame;

            @Override
            public void actionPerformed(ActionEvent event) {
                enemyOffset = frame % 2 == 0 ? -8 : 8;
                frame++;
                if (frame == 6) {
                    enemyOffset = 0;
                    timer.stop();
                }
                repaint();
            }
        });
        timer.start();
    }

    /** plays the key animation followed by the final boss reveal. */
    public void startFinalEntrance(boolean hasKey, Runnable onKeyStart,
            Runnable onKeyUsed, Runnable onBossStart, Runnable onFinished) {
        enemyScale = 0.25f;
        enemyAlpha = 0.0f;
        if (hasKey && keyImage != null) {
            animateKey(onKeyStart, onKeyUsed, onBossStart, onFinished);
        } else {
            revealBoss(onBossStart, onFinished);
        }
    }

    private void animateKey(Runnable onStart, Runnable onUsed,
            Runnable onBossStart, Runnable onFinished) {
        showAncientKey = true;
        keyScale = 0.7f;
        onStart.run();
        Timer timer = new Timer(90, null);
        timer.addActionListener(new ActionListener() {
            private int frame;

            @Override
            public void actionPerformed(ActionEvent event) {
                keyScale = frame % 2 == 0 ? 1.15f : 0.85f;
                frame++;
                repaint();
                if (frame == 12) {
                    timer.stop();
                    showAncientKey = false;
                    onUsed.run();
                    revealBoss(onBossStart, onFinished);
                }
            }
        });
        timer.start();
    }

    private void revealBoss(Runnable onStart, Runnable onFinished) {
        onStart.run();
        Timer timer = new Timer(70, null);
        timer.addActionListener(event -> {
            enemyScale = Math.min(1.0f, enemyScale + 0.07f);
            enemyAlpha = Math.min(1.0f, enemyAlpha + 0.09f);
            repaint();
            if (enemyScale >= 1.0f && enemyAlpha >= 1.0f) {
                timer.stop();
                onFinished.run();
            }
        });
        timer.start();
    }

    private void loadImages(List<Hero> heroes, Enemy enemy) {
        try {
            backgroundImage = ImageIO.read(new File("assets/backgrounds/forest_battle.png"));
            keyImage = ImageIO.read(new File("assets/items/ancient_key.png"));
            String enemyName = enemy.getClass().getSimpleName()
                    .replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
            enemyImage = ImageIO.read(new File("assets/enemies/" + enemyName + ".png"));
            for (Hero hero : heroes) {
                String fileName = hero.getName().toLowerCase() + ".png";
                heroImages.add(ImageIO.read(new File("assets/heroes/" + fileName)));
            }
        } catch (IOException error) {
            JOptionPane.showMessageDialog(this, "Battle images could not be loaded.");
        }
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D graphics2d = (Graphics2D) graphics.create();
        graphics2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        if (backgroundImage != null) {
            graphics2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
        }
        drawHeroes(graphics2d);
        drawKey(graphics2d);
        drawEnemy(graphics2d);
        graphics2d.dispose();
    }

    private void drawHeroes(Graphics2D graphics) {
        int heroSize = Math.min(150, getWidth() / 5);
        for (int i = 0; i < heroImages.size(); i++) {
            int x = 25 + (i * (heroSize + 15));
            graphics.drawImage(heroImages.get(i), x, getHeight() - heroSize - 15,
                    heroSize, heroSize, null);
        }
    }

    private void drawKey(Graphics2D graphics) {
        if (showAncientKey && keyImage != null) {
            int keySize = Math.round(100 * keyScale);
            graphics.drawImage(keyImage, (getWidth() - keySize) / 2,
                    (getHeight() - keySize) / 2, keySize, keySize, null);
        }
    }

    private void drawEnemy(Graphics2D graphics) {
        if (enemyImage == null) {
            return;
        }
        int fullSize = Math.min(260, getHeight() - 30);
        int enemySize = Math.round(fullSize * enemyScale);
        graphics.setComposite(AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER, enemyAlpha));
        graphics.drawImage(enemyImage, getWidth() - enemySize - 35 + enemyOffset,
                getHeight() - enemySize - 15, enemySize, enemySize, null);
    }
}
