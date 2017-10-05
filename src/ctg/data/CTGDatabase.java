package ctg.data;

import java.awt.Image;
import java.util.ArrayList;
import mhframework.media.MHImageGroup;

public class CTGDatabase
{
    private static CTGDatabase instance = null;
    private static ArrayList<CTGShipInfo> ships;
    
    private CTGDatabase()
    {
        ships = new ArrayList<CTGShipInfo>(13);
        ships.add(0, new CTGShipInfo( 0, "Classic Blue",   6, 6, 6, "ClassicBlue.gif"));
        ships.add(1, new CTGShipInfo( 1, "Classic Red",    6, 6, 6, "ClassicRed.gif"));
        ships.add(2, new CTGShipInfo( 2, "Classic Green",  6, 6, 6, "ClassicGreen.gif"));
        ships.add(3, new CTGShipInfo( 3, "Classic Yellow", 6, 6, 6, "ClassicYellow.gif"));
        ships.add(4, new CTGShipInfo( 4, "Arrow",         10, 7, 1, "Arrow.gif"));
        ships.add(5, new CTGShipInfo( 5, "BGM",            1, 7,10, "BGM.gif"));
        ships.add(6, new CTGShipInfo( 6, "Dart",           5, 5, 8, "Dart.gif"));
        ships.add(7, new CTGShipInfo( 7, "Model E",        2, 9, 7, "E.gif"));
        ships.add(8, new CTGShipInfo( 8, "Interceptor",    8, 3, 7, "Interceptor.gif"));
        ships.add(9, new CTGShipInfo( 9, "Luna",           9, 7, 2, "Luna.gif"));
        ships.add(10,new CTGShipInfo(10, "Parhelia",       4, 5, 9, "Parhelia.gif"));
        ships.add(11,new CTGShipInfo(11, "Star Warrior",   7, 7, 4, "StarWarrior.gif"));
        ships.add(12,new CTGShipInfo(12, "Terra",          3, 6, 9, "Terra.gif"));
    }
    
    
    public CTGShipInfo getShipInfo(int shipID)
    {
        return ships.get(shipID);
    }
    
    
    public static CTGDatabase getInstance()
    {
        if (instance == null)
            instance = new CTGDatabase();
        
        return instance;
    }


    public int getNumShips()
    {
        return ships.size();
    }

    
    public class CTGShipInfo
    {
        public static final String SHIP_IMAGE_DIRECTORY = "images/ships/";

        private int id;
        private String name;
        private int armorClass;
        private int speedClass;
        private int agilityClass;
        private String fileName;
        private Image image;
        
        public CTGShipInfo(int id, String name, int speedClass, int agilityClass, int armorClass, String filename)
        {
            this.id = id;
            this.name = name;
            this.speedClass = speedClass;
            this.agilityClass = agilityClass;
            this.armorClass = armorClass;
            this.fileName = filename;
        }
        
        public Image getImage()
        {
            if (image == null)
                image = MHImageGroup.loadImage(SHIP_IMAGE_DIRECTORY+fileName);
            
            return image;
        }

        public int getID()
        {
            return id;
        }

        public String getName()
        {
            return name;
        }

        public int getArmorClass()
        {
            return armorClass;
        }

        public int getSpeedClass()
        {
            return speedClass;
        }

        public int getAgilityClass()
        {
            return agilityClass;
        }

        public String getFileName()
        {
            return fileName;
        }
    }
    
    
    public class CTGScoreValues
    {
        public static final int GOT_DESTROYED = 25;
        public static final int DESTROYED_ENEMY = 10;
        public static final int WON_ROUND = 10;
        public static final int DIRECT_MISSILE_HIT = 50;
        public static final int LASER_HIT = 1;
    }
}


