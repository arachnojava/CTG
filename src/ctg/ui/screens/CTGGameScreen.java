package ctg.ui.screens;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import mhframework.MHDisplayModeChooser;
import mhframework.MHScreen;
import ctg.data.CTGDatabase;
import ctg.ui.CTGKeyControls;
import ctg.ui.CTGKeyControls.Command;
import ctg.ui.CTGScoreBoard;
import ctg.world.CTGLevel;
import ctg.world.CTGParticleSystem;
import ctg.world.CTGShip;

public class CTGGameScreen extends MHScreen
{
    public static final CTGLevel board = new CTGLevel();
    public static CTGShip[] players;// player1, player2;
    private CTGScoreBoard[] scores = new CTGScoreBoard[2];
    private boolean roundOver = false;
    private long roundOverTime;
    
    public CTGGameScreen()
    {
        players = new CTGShip[2];
        
        players[0] = new CTGShip();
        players[0].setPlayerName("Player 1");
        CTGKeyControls p1Controls = new CTGKeyControls();
        p1Controls.setKeyCommand(Command.MOVE, KeyEvent.VK_W);
        p1Controls.setKeyCommand(Command.TURN_LEFT, KeyEvent.VK_A);
        p1Controls.setKeyCommand(Command.TURN_RIGHT, KeyEvent.VK_D);
        p1Controls.setKeyCommand(Command.FIRE_LASERS, KeyEvent.VK_CONTROL);
        p1Controls.setKeyCommand(Command.LAUNCH_MISSILE, KeyEvent.VK_SHIFT);
        players[0].setKeyControls(p1Controls);
        players[0].setLocation(30,240);
        players[0].setRotation(90.0);
        
        players[1] = new CTGShip();
        players[1].setPlayerName("Player 2");
        CTGKeyControls p2Controls = new CTGKeyControls();
        p2Controls.setKeyCommand(Command.MOVE, KeyEvent.VK_NUMPAD8);
        p2Controls.setKeyCommand(Command.TURN_LEFT, KeyEvent.VK_NUMPAD4);
        p2Controls.setKeyCommand(Command.TURN_RIGHT, KeyEvent.VK_NUMPAD6);
        p2Controls.setKeyCommand(Command.FIRE_LASERS, KeyEvent.VK_ENTER);
        p2Controls.setKeyCommand(Command.LAUNCH_MISSILE, KeyEvent.VK_BACK_SPACE);
        players[1].setKeyControls(p2Controls);
        players[1].setLocation(730, 240);
        players[1].setRotation(-90.0);
        
        scores[0] = new CTGScoreBoard(players[0], 0, MHDisplayModeChooser.getHeight()-CTGScoreBoard.HEIGHT);
        scores[1] = new CTGScoreBoard(players[1], 3*CTGScoreBoard.WIDTH, scores[0].y);
    }

    public void setP1Ship(int id)
    {
        players[0].setSelectedShip(id);
    }
    
    public void setP2Ship(int id)
    {
        players[1].setSelectedShip(id);
    }
    
    public void advance(long timeElapsed)
    {
        int alive = 0;
        for (int i = 0; i < players.length; i++)
            if (!players[i].isDead())
                alive++;

        if (alive == 1 && !roundOver)
        {
            for (int i = 0; i < players.length; i++)
                if (!players[i].isDead())
                    players[i].setScore(players[i].getScore()+CTGDatabase.CTGScoreValues.WON_ROUND);
            
            roundOver = true;
            roundOverTime = System.currentTimeMillis();
        }
        if (alive == 0 && !roundOver)
        {
            roundOver = true;
            roundOverTime = System.currentTimeMillis();
        }
        else if (roundOver && System.currentTimeMillis() - roundOverTime > 3000)
        {
            setFinished(true);
            
            // TODO: If game is over, dispose of game screen.
            
            // TODO: Show standings screen.
            setNextScreen(new CTGUpgradeScreen(players[0], players[1]));
        }
        
        players[0].advance(timeElapsed);
        players[1].advance(timeElapsed);
        
        //Check collisions.
        players[0].checkCollisions(players[1]);
        players[1].checkCollisions(players[0]);
        
        CTGParticleSystem.getInstance().advance(timeElapsed);
    }
    
    public void render(Graphics2D g)
    {
        fill(g, Color.BLACK);
        
        board.render(g);
        for (int i = 0; i < players.length; i++)
            players[i].render(g);
        
        for (int i = 0; i < scores.length; i++)
            scores[i].render(g);
        
        CTGParticleSystem.getInstance().render(g);
        
        super.render(g);
    }
    

    @Override
    public void actionPerformed(ActionEvent e)
    {
        // TODO Auto-generated method stub

    }
    
    
    @Override
    public void keyPressed(KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_D)
            ;
        else if (e.getKeyCode() == KeyEvent.VK_A)
            ;
        else if (e.getKeyCode() == KeyEvent.VK_W)
            ;
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
            ;
        else if (e.getKeyCode() == KeyEvent.VK_LEFT)
            ;
        else if (e.getKeyCode() == KeyEvent.VK_UP)
            ;
        else
            super.keyTyped(e);
    }

    
    @Override
    public void load()
    {
        this.setFinished(false);
        roundOver = false;
        
        players[0].setLocation(30,240);
        players[0].setRotation(90.0);
        players[0].init();
        
        players[1].setLocation(730, 240);
        players[1].setRotation(-90.0);
        players[1].init();
    }


    @Override
    public void unload()
    {
        // TODO Auto-generated method stub

    }
}
