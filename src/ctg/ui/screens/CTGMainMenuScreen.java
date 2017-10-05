package ctg.ui.screens;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import mhframework.MHDisplayModeChooser;
import mhframework.MHGame;
import mhframework.MHScreen;
import mhframework.media.MHImageGroup;
import ctg.ui.CTGKeyboardCommandButton;
import ctg.ui.command.CTGExitCommand;
import ctg.ui.command.CTGStartGameCommand;

public class CTGMainMenuScreen extends MHScreen
{
    private Image logoImg = MHImageGroup.loadImage("images/CTGLogo3DAlpha.png");
    CTGKeyboardCommandButton btnPlay;
    private CTGKeyboardCommandButton btnExit;
    CTGKeyboardCommandButton[] menu = new CTGKeyboardCommandButton[2];
    int selected = 0;
    
    public CTGMainMenuScreen()
    {
        btnPlay = new CTGKeyboardCommandButton(CTGStartGameCommand.getInstance(this));
        btnPlay.setText("Play");
        btnPlay.setCenterPosition(400, 300);

        btnExit = new CTGKeyboardCommandButton(new CTGExitCommand());
        btnExit.setText("Exit");
        btnExit.setCenterPosition(400, 400);

        
        menu[0] = btnPlay;
        menu[1] = btnExit;
        changeSelection(0);
    }
    
    
    private void changeSelection(int s)
    {
        for (int i = 0; i < menu.length; i++)
            menu[i].deselect();
        
        if (s < 0)
            s = menu.length - 1;
        else if (s >= menu.length)
            s = 0;
        
        selected = s;
        menu[s].select();
    }
    
    
    public void render(Graphics2D g)
    {
        fill(g, Color.BLACK);
        g.drawImage(logoImg, 400-logoImg.getWidth(null)/2, 50, null);

        for (int i = 0; i < menu.length; i++)
            menu[i].render(g);
        
        super.render(g);
    }
    
    @Override
    public void keyPressed(KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_UP)
            changeSelection(--selected);
        else if (e.getKeyCode() == KeyEvent.VK_DOWN)
            changeSelection(++selected);
        else if (e.getKeyCode() == KeyEvent.VK_ENTER)
            menu[selected].execute();
        
        super.keyPressed(e);
    }


    @Override
    public void actionPerformed(ActionEvent e)
    {
/*        if (e.getSource() == btnExit)
            MHGame.setProgramOver(true);
        else if (e.getSource() == btnPlay)
        {
            this.setFinished(true);
            this.setNextScreen(new CTGVehicleSelectionScreen());
        }*/
    }


    @Override
    public void load()
    {
/*        centerComponent(btnPlay);
        btnPlay.setY(MHDisplayModeChooser.DISPLAY_Y+300);
        centerComponent(btnExit);
        btnExit.setY(MHDisplayModeChooser.DISPLAY_Y+400);*/
    }


    @Override
    public void unload()
    {
        // TODO Auto-generated method stub

    }

}
