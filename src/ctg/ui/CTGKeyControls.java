package ctg.ui;

import java.util.HashMap;

public class CTGKeyControls
{
    public enum Command
    {
        MOVE, TURN_RIGHT, TURN_LEFT, FIRE_LASERS, LAUNCH_MISSILE;
    }
    
    private HashMap<Command, Integer> keys = new HashMap<Command, Integer>();
    
    public void setKeyCommand(Command cmd, int keyCode)
    {
        keys.put(cmd, keyCode);
    }
    
    public int getKeyCommand(Command cmd)
    {
        return keys.get(cmd);
    }
}
