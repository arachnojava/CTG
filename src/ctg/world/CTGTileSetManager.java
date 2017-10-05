package ctg.world;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import mhframework.media.MHImageGroup;

public class CTGTileSetManager //extends MHTileSetManager
{
    public static final String TILE_SET_DIRECTORY = "images/tiles/";
    private static CTGTileSetManager instance;
    private ArrayList<Image> tiles;
    
    private CTGTileSetManager()
    {
        tiles = new ArrayList<Image>();
        for (int tileID = 0; tileID < 100; tileID++)
        {
            File f = new File(TILE_SET_DIRECTORY+tileID+".jpg");
            if (f.exists())
                loadTileImage(tileID, f);

            f = new File(TILE_SET_DIRECTORY+tileID+".gif");
            if (f.exists())
                loadTileImage(tileID, f);
            
            f = new File(TILE_SET_DIRECTORY+tileID+".png");
            if (f.exists())
                loadTileImage(tileID, f);
        }
    }
    
    private void loadTileImage(int tileID, File f)
    {
        try
        {
            tiles.add(tileID, MHImageGroup.loadImage(f.getCanonicalPath()));
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public static CTGTileSetManager getInstance()
    {
        if (instance == null)
            instance = new CTGTileSetManager();
        
        return instance;
    }
    
    public Image getTile(int tileID)
    {
        return tiles.get(tileID);
    }
}
