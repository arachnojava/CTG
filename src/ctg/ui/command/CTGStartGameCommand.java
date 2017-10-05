package ctg.ui.command;

import mhframework.MHScreen;
import mhframework.gui.MHCommand;
import ctg.ui.screens.CTGVehicleSelectionScreen;

public class CTGStartGameCommand implements MHCommand
{
    private static CTGStartGameCommand instance = null;
    private MHScreen screen;
    
    
    private CTGStartGameCommand(MHScreen screen)
    {
        this.screen = screen;
    }
    
    
    public static CTGStartGameCommand getInstance(MHScreen screen)
    {
        if (instance == null)
            instance = new CTGStartGameCommand(screen);
        
        return instance;
    }
    
    
    @Override
    public void execute()
    {
        screen.setFinished(true);
        screen.setNextScreen(new CTGVehicleSelectionScreen());
    }

}
