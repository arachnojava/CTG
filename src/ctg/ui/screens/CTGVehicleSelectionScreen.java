package ctg.ui.screens;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import mhframework.MHPoint;
import mhframework.MHRandom;
import mhframework.MHRenderable;
import mhframework.media.MHFont;
import ctg.data.CTGDatabase;
import ctg.data.CTGDatabase.CTGShipInfo;


public class CTGVehicleSelectionScreen extends mhframework.MHScreen
{
    private CTGVehicleSelector player1Selector;
    private CTGVehicleSelector player2Selector;
    
    
    public CTGVehicleSelectionScreen()
    {
        player1Selector = new CTGVehicleSelector(0, 50, "Player 1");
        player2Selector = new CTGVehicleSelector(400, 50, "Player 2");
    }
    
    
    public void advance(long elapsedTime)
    {
        player1Selector.advance(elapsedTime);
        player2Selector.advance(elapsedTime);
    }
    
    public void render(Graphics2D g)
    {
        fill(g, Color.BLACK);
        
        g.setColor(Color.YELLOW);
        String instructions1 = "Cycle through available craft with the movement keys.";
        String instructions2 = "Press the SPACE BAR when both players have selected a craft.";
        Rectangle2D r = new Rectangle2D.Double(0,0,800,50);
        MHPoint p = player1Selector.dataFont.centerOn(r, g, instructions1);
        player1Selector.dataFont.drawString(g, instructions1, p.getX(), 20);
        p = player1Selector.dataFont.centerOn(r, g, instructions2);
        player1Selector.dataFont.drawString(g, instructions2, p.getX(), 45);
        
        player1Selector.render(g);
        player2Selector.render(g);
        
        super.render(g);
    }
    
    
    
    
    @Override
    public void keyPressed(KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_S)
            player1Selector.nextVehicle();
        else if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_W)
            player1Selector.prevVehicle();
        else if (e.getKeyCode() == KeyEvent.VK_NUMPAD6 || e.getKeyCode() == KeyEvent.VK_NUMPAD2 || e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_DOWN)
            player2Selector.nextVehicle();
        else if (e.getKeyCode() == KeyEvent.VK_NUMPAD4 || e.getKeyCode() == KeyEvent.VK_NUMPAD8 || e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_UP)
            player2Selector.prevVehicle();
        else if (e.getKeyCode() == KeyEvent.VK_SPACE)
        {
            CTGGameScreen scr = new CTGGameScreen();
            scr.setP1Ship(player1Selector.selectedVehicleID);
            scr.setP2Ship(player2Selector.selectedVehicleID);
            setNextScreen(scr);
            setFinished(true);
            setDisposable(true);
        }
        else
            super.keyTyped(e);
    }


    @Override
    public void actionPerformed(ActionEvent e)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void load()
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void unload()
    {
        // TODO Auto-generated method stub
        
    }

    class CTGVehicleSelector implements MHRenderable
    {
        public static final int WIDTH = 400;
        public static final int HEIGHT = 550;
        
        private int x, y;
        private int selectedVehicleID = 0;
        private double rotation;
        private double rotationSpeed = 0.1; // per millisecond
        private String playerName;
        
        public MHFont dataFont = new MHFont("Monospaced", Font.BOLD, 20);
        private MHFont titleFont = new MHFont("Monospaced", Font.BOLD, 40);
        private MHFont shipNameFont = new MHFont("Monospaced", Font.BOLD, 30);
        
        private Rectangle2D shipNameRect;
        private Rectangle2D titleRect;
        
        public CTGVehicleSelector(int x, int y, String playerName)
        {
            this.x = x;
            this.y = y;
            this.playerName = playerName;
            
            shipNameRect = new Rectangle2D.Double(x, 250, WIDTH, 40);
            titleRect = new Rectangle2D.Double(x, y, WIDTH, 100);
            
            rotation = (double)MHRandom.random(0, 180);
            selectedVehicleID = MHRandom.random(0, CTGDatabase.getInstance().getNumShips()-1);
        }
        
        
        public void nextVehicle()
        {
            selectedVehicleID++;
            if (selectedVehicleID >= CTGDatabase.getInstance().getNumShips())
                selectedVehicleID = 0;
        }


        public void prevVehicle()
        {
            selectedVehicleID--;
            if (selectedVehicleID < 0)
                selectedVehicleID = CTGDatabase.getInstance().getNumShips()-1;
        }


        @Override
        public void advance(long timeElapsed)
        {
            rotation += rotationSpeed * timeElapsed;
        }

        @Override
        public void render(Graphics2D g)
        {
            CTGShipInfo ship = CTGDatabase.getInstance().getShipInfo(selectedVehicleID);
            
            // Draw the background.
            g.setColor(Color.BLACK);
            g.fillRoundRect(x+10, y+10, WIDTH-20, HEIGHT-20, 40, 40);
            g.setColor(Color.LIGHT_GRAY);
            g.drawRoundRect(x+10, y+10, WIDTH-20, HEIGHT-20, 40, 40);
            g.drawRoundRect(x+15, y+15, WIDTH-30, HEIGHT-30, 40, 40);

            g.setColor(Color.WHITE);
            //g.drawRect((int)titleRect.getX(), (int)titleRect.getY(), (int)titleRect.getWidth(), (int)titleRect.getHeight());
            MHPoint tp = titleFont.centerOn(titleRect, g, playerName);
            titleFont.drawString(g, playerName, (int)tp.getX(), (int)tp.getY());
            
            // Draw the stats.
            int labelX = x + 80;
            int labelY = HEIGHT/2 + 50;
            int lineSpacing = 30;
            g.setColor(Color.WHITE);
            dataFont.drawString(g, "  Speed Rating:", labelX, labelY);
            labelY += lineSpacing;
            dataFont.drawString(g, "Agility Rating:", labelX, labelY);
            labelY += lineSpacing;
            dataFont.drawString(g, "  Armor Rating:", labelX, labelY);

            labelX = x + 280;
            labelY = HEIGHT/2 + 50;
            g.setColor(Color.GREEN);
            tp = this.shipNameFont.centerOn(shipNameRect, g, ship.getName());
            shipNameFont.drawString(g, ship.getName(), tp.getX(), tp.getY());
            dataFont.drawString(g, ship.getSpeedClass()+"", labelX, labelY);
            labelY += lineSpacing;
            dataFont.drawString(g, ship.getAgilityClass()+"", labelX, labelY);
            labelY += lineSpacing;
            dataFont.drawString(g, ship.getArmorClass()+"", labelX, labelY);

            
            // Draw the ship.
            int shipX = (x + WIDTH/2) - (ship.getImage().getWidth(null)/2);
            int shipY = y + HEIGHT/4;
            final AffineTransform originalTransform = g.getTransform();
            g.rotate(rotation * (Math.PI / 180.0), shipX + (ship.getImage().getWidth(null)/2), shipY+(ship.getImage().getHeight(null)/2));  //.rotate(rotation * Math.PI / 180.0, w / 2.0, h / 2.0);
            g.drawImage(ship.getImage(), shipX, shipY, null);
            g.setTransform(originalTransform);
        }
    }
}
