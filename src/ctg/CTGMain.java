package ctg;

import mhframework.MHGameApplication;
import mhframework.MHScreen;
import mhframework.MHVideoSettings;
import ctg.ui.screens.CTGMainMenuScreen;

public class CTGMain
{

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        final MHScreen screen = new CTGMainMenuScreen();

        final MHVideoSettings settings = new MHVideoSettings();
        settings.fullScreen = false;
        settings.displayWidth = 800;
        settings.displayHeight = 600;
        settings.windowCaption = "CTG";

        new MHGameApplication(screen, settings);

        System.exit(0);
    }

}
