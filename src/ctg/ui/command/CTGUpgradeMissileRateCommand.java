package ctg.ui.command;

import mhframework.gui.MHCommand;
import ctg.world.CTGShip;

public class CTGUpgradeMissileRateCommand implements MHCommand
{
    public static final int COST = 10;
    private CTGShip ship;

    public CTGUpgradeMissileRateCommand(CTGShip ship)
    {
        this.ship = ship;
    }
   
    
    @Override
    public void execute()
    {
        if (ship.getScore() < COST)
            return;

        ship.setScore(ship.getScore() - COST);
        
        int val = (int)Math.ceil(ship.getMissileDelay() * 0.9);
        if (val == ship.getMissileDelay())
            val--;
        
        if (val < 10)
            val = 10;
        
        ship.setMissileDelay(val);
    }

}
