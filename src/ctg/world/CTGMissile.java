package ctg.world;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import mhframework.MHActor;
import mhframework.MHPoint;
import mhframework.media.MHImageGroup;
import ctg.ui.audio.CTGSounds;
import ctg.ui.screens.CTGGameScreen;

public class CTGMissile extends MHActor
{
    final static double MAX_SPEED = 0.5;
    public boolean dead = false;
    private int damage = 20;
    private double blastRadius = 100;
    static Image image = MHImageGroup.loadImage("images/weapons/Missile.gif");
    private ArrayList<CTGMissileTrailParticle> trail = new ArrayList<CTGMissileTrailParticle>();
    
    
    public CTGMissile(final double startX, final double startY, final double direction, double speed, int missileDamage, double missileRadius)
    {
        super();

        init(startX, startY, direction, speed, missileDamage, missileRadius);
        
    }


    public void init(final double startX, final double startY, final double direction, double speed, int missileDamage, double missileRadius)
    {
        this.setLocation(startX-getWidth()/2, startY-getHeight()/2);
        this.setRotation(direction);
        this.setSpeed(Math.max(0.05, speed));
        dead = false;
        damage = missileDamage;
        blastRadius = missileRadius;
    }


    @Override
    public void advance(long elapsedTime)
    {
        for (CTGMissileTrailParticle p : trail)
        {
            if (!p.dead)
            {
                p.color = p.color.darker();
                p.size *= 0.95;
                if (p.size <= 1.0)
                    p.dead = true;
            }
        }

        if (dead) return;

        final int x = (int)getCenterX();
        final int y = (int)getCenterY();

        if (!(CTGGameScreen.board.canMoveTo(y/CTGLevel.TILE_SIZE, x/CTGLevel.TILE_SIZE)))
        {
            dead = true; // Kill the missile.
            // Spawn particles.
            CTGParticleSystem.getInstance().emitShockWave((int)getCenterX(), (int)getCenterY(), blastRadius);
            CTGParticleSystem.getInstance().emitSparks(x, y);
            
            for (CTGShip player : CTGGameScreen.players)
            {
                MHPoint enemyCtr = new MHPoint(player.getCenterX(), player.getCenterY());
                double dist = enemyCtr.distanceTo(getLocation());
                if (dist < blastRadius)
                {
                    // TODO: Add explosion sound,
                    
                    double diff = blastRadius - dist;
                    double dmg = (diff/blastRadius) * getDamage();
                    player.setHealth((int)(player.getHealth()-dmg));
                    CTGSounds.play(CTGSounds.takeDamage);
                    CTGParticleSystem.getInstance().emitSparks((int)player.getCenterX(), (int)player.getCenterY());
                }
            }
        }
        else 
        {
            double speed = Math.min(getSpeed() * 1.1, MAX_SPEED);
            setSpeed(speed);
            findDeadTrailParticle().init(x, y);
            
            super.advance(elapsedTime);
        }
    }

    
    private CTGMissileTrailParticle findDeadTrailParticle()
    {
        for (CTGMissileTrailParticle p : trail)
        {
            if (p.dead)
                return p;
        }
        
        CTGMissileTrailParticle n = new CTGMissileTrailParticle();
        trail.add(n);
        return n;
    }


    @Override
    public void render(final Graphics2D g)
    {
        for (CTGMissileTrailParticle p : trail)
        {
            if (!p.dead)
            {
                g.setColor(p.color);
                g.fillOval((int)p.pos.getX(), (int)p.pos.getY(), (int)p.size, (int)p.size);
            }
        }

        if (dead) return;

        super.render(g);
    }

    
    public Image getImage()
    {
        return image;
    }
    

    public int getDamage()
    {
        return damage;
    }
}


class CTGMissileTrailParticle
{
    public final MHPoint pos = new MHPoint();
    public double size;
    public boolean dead;
    public Color color;
    
    public CTGMissileTrailParticle()
    {
    }
    
    public CTGMissileTrailParticle init(int x, int y)
    {
        size = 10;
        pos.setLocation(x-size/2, y-size/2);
        dead = false;
        color = Color.YELLOW;
        
        return this;
    }
}

