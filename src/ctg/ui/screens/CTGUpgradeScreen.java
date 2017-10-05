package ctg.ui.screens;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import mhframework.MHPoint;
import ctg.ui.screens.upgrades.CTGUpgradeMenu;
import ctg.world.CTGShip;


public class CTGUpgradeScreen extends mhframework.MHScreen
{
    private CTGUpgradeMenu player1Menu;
    private CTGUpgradeMenu player2Menu;
    private CTGShip p1Ship, p2Ship;
    
    
    public CTGUpgradeScreen(CTGShip p1Ship, CTGShip p2Ship)
    {
        this.p1Ship = p1Ship;
        this.p2Ship = p2Ship;
        player1Menu = new CTGUpgradeMenu(0, 50, "Player 1", p1Ship);
        player2Menu = new CTGUpgradeMenu(400, 50, "Player 2", p2Ship);
    }
    
    
    public void advance(long elapsedTime)
    {
        player1Menu.advance(elapsedTime);
        player2Menu.advance(elapsedTime);
        
        if (p1Ship.isDoneShopping() && p2Ship.isDoneShopping())
        {
            setNextScreen(null);
            setFinished(true);
            setDisposable(true);
        }   
    }
    
    public void render(Graphics2D g)
    {
        fill(g, Color.BLACK);
        
        g.setColor(Color.YELLOW);
        String instructions1 = "Use the movement keys to navigate the upgrades.";
        String instructions2 = "Press the fire key to select.";
        Rectangle2D r = new Rectangle2D.Double(0,0,800,50);
        MHPoint p = player1Menu.dataFont.centerOn(r, g, instructions1);
        player1Menu.dataFont.drawString(g, instructions1, p.getX(), 20);
        p = player1Menu.dataFont.centerOn(r, g, instructions2);
        player1Menu.dataFont.drawString(g, instructions2, p.getX(), 45);
        
        player1Menu.render(g);
        player2Menu.render(g);
        
        super.render(g);
    }
    
    
    
    
    @Override
    public void keyPressed(KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_S)
            player1Menu.nextUpgrade();
        else if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_W)
            player1Menu.prevUpgrade();
        else if (e.getKeyCode() == KeyEvent.VK_CONTROL || e.getKeyCode() == KeyEvent.VK_SHIFT)
            player1Menu.execute();
        else if (e.getKeyCode() == KeyEvent.VK_NUMPAD6 || e.getKeyCode() == KeyEvent.VK_NUMPAD2 || e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_DOWN)
            player2Menu.nextUpgrade();
        else if (e.getKeyCode() == KeyEvent.VK_NUMPAD4 || e.getKeyCode() == KeyEvent.VK_NUMPAD8 || e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_UP)
            player2Menu.prevUpgrade();
        else if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_BACK_SPACE)
            player2Menu.execute();
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


}
