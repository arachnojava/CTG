package ctg.ui.audio;

import mhframework.MHDataModel;
import mhframework.media.MHSoundManager;

public class CTGSounds
{
    public static final String SOUND_DIRECTORY = "audio/";
    
    public static final int fireLaser = MHDataModel.loadSoundFile(SOUND_DIRECTORY + "laser2.wav");
    public static final int takeDamage = MHDataModel.loadSoundFile(SOUND_DIRECTORY + "sound1.wav");;
    //public static int laserDies;
    public static final int deathSound = MHDataModel.loadSoundFile(SOUND_DIRECTORY + "Explosion0.wav");
    
    
    public static void play(int soundId)
    {
        MHSoundManager soundMan = MHDataModel.getSoundManager();
        soundMan.stop(soundId);
        soundMan.play(soundId);
    }
}
