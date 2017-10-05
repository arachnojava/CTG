package ctg.ui.command;

import ctg.world.CTGShip;
import mhframework.gui.MHCommand;

public class CTGUpgradeLaserDamageCommand implements MHCommand
{
    public static final int COST = 10;
    private CTGShip ship;

    public CTGUpgradeLaserDamageCommand(CTGShip ship)
    {
        this.ship = ship;
    }

    @Override
    public void execute()
    {
        if (ship.getScore() < COST)
            return;

        ship.setScore(ship.getScore() - COST);
        ship.setLaserDamage(ship.getLaserDamage()+1);
    }
}
