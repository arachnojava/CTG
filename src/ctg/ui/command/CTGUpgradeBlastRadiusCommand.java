package ctg.ui.command;

import mhframework.gui.MHCommand;
import ctg.world.CTGShip;

public class CTGUpgradeBlastRadiusCommand implements MHCommand
{
    public static final int COST = 10;
    private CTGShip ship;

    public CTGUpgradeBlastRadiusCommand(CTGShip ship)
    {
        this.ship = ship;
    }
   
    
    @Override
    public void execute()
    {
        if (ship.getScore() < COST)
            return;

        ship.setScore(ship.getScore() - COST);
        
        int val = (int)Math.ceil(ship.getFiringDelay() * 0.9);
        if (val == ship.getFiringDelay())
            val--;
        
        if (val < 5)
            val = 5;
        
        ship.setMissileRadius(ship.getMissileRadius() * 1.1);
    }

}
