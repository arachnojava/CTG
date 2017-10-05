package ctg.ui.command;

import ctg.world.CTGShip;
import mhframework.gui.MHCommand;

public class CTGUpgradeAmmoCommand implements MHCommand
{
    public static final int COST = 10;
    private CTGShip ship;
    
    public CTGUpgradeAmmoCommand(CTGShip ship)
    {
        this.ship = ship;
    }

    @Override
    public void execute()
    {
        if (ship.getScore() >= COST)
        {
            ship.setScore(ship.getScore()-COST);
            ship.setMaxWarheads(ship.getMaxWarheads()+1);
        }
    }
}
