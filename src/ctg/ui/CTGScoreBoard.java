package ctg.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import mhframework.MHRenderable;
import mhframework.media.MHFont;
import ctg.world.CTGShip;

public class CTGScoreBoard implements MHRenderable
{
    public static final int WIDTH = 200;
    public static final int HEIGHT = 80;
    private static final MHFont font = new MHFont("Tahoma", Font.BOLD, 14);
    private CTGShip ship;
    int x;
    public int y;
    private Image image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    private Graphics2D graphics = (Graphics2D) image.getGraphics();

    public CTGScoreBoard(CTGShip player, int x, int y)
    {
        ship = player;
        this.x = x;
        this.y = y;
    }

    
    @Override
    public void advance(long timeElapsed)
    {
        // TODO Auto-generated method stub
        
    }

    
    @Override
    public void render(Graphics2D g)
    {
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, WIDTH, HEIGHT);
        graphics.setColor(Color.LIGHT_GRAY);
        graphics.fillRoundRect(5, 5, WIDTH-10, HEIGHT-10, 10, 10);
        graphics.setColor(Color.BLACK);
        graphics.drawImage(ship.getImage(), 10, 30, null);
        font.drawString(graphics, ship.getPlayerName(), 10, 20);
        font.drawString(graphics, "Money: " + ship.getScore(), 60, 40);
        font.drawString(graphics, "Health: " + ship.getHealth(), 60, 55);
        font.drawString(graphics, "Warheads: " + ship.getWarheadCount(), 60, 70);
        
        g.drawImage(image, x, y, null);
    }
}
