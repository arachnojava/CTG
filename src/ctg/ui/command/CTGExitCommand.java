package ctg.ui.command;

import mhframework.MHGame;
import mhframework.gui.MHCommand;

public class CTGExitCommand implements MHCommand
{

    @Override
    public void execute()
    {
        MHGame.setProgramOver(true);
    }

}