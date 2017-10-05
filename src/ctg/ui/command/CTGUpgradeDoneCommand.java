package ctg.ui.command;

import ctg.world.CTGShip;
import mhframework.gui.MHCommand;

public class CTGUpgradeDoneCommand implements MHCommand
{
    CTGShip ship;
    
    public CTGUpgradeDoneCommand(CTGShip ship)
    {
        this.ship = ship;
    }

    @Override
    public void execute()
    {
        ship.setDoneShopping(true);
    }

}
