package ctg.world;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.ArrayList;
import mhframework.MHPoint;
import mhframework.MHRandom;
import mhframework.MHRenderable;

public class CTGParticleSystem implements MHRenderable
{
    public static final Stroke STROKE1 = new BasicStroke(20, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    public static final Stroke STROKE2 = new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

    private static CTGParticleSystem instance = new CTGParticleSystem();
    private ArrayList<SparkParticle> sparks;
    private ArrayList<SparkParticle> explosions;
    private ArrayList<ShockWave> shockWaves;
    
    private CTGParticleSystem()
    {
        sparks = new ArrayList<SparkParticle>();
        explosions = new ArrayList<SparkParticle>();
        shockWaves = new ArrayList<ShockWave>();
    }

    
    public static CTGParticleSystem getInstance()
    {
        return instance;
    }
    

    public void emitSparks(int x, int y)
    {
        for (int i = 0; i < 20; i++)
        {
            SparkParticle s = findDeadSpark();
            s.pos.setLocation(x, y);
            s.direction = Math.random() * 360;
            s.speed = Math.random() / 5;
            s.dead = false;
        }
    }
    
    
    public void emitExplosion(int x, int y)
    {
        for (int i = 0; i < 100; i++)
        {
            SparkParticle s = findDeadExplosion();
            s.pos.setLocation(x, y);
            s.flag = MHRandom.flipCoin();
            s.direction = Math.random() * 360;
            s.speed = Math.random() / 2;
            s.dead = false;
        }
    }
    
    
    private SparkParticle findDeadSpark()
    {
        for (SparkParticle s : sparks)
        {
            if (s.dead)
                return s;
        }
        
        SparkParticle s = new SparkParticle(); 
        sparks.add(s);
        return s;
    }
    
    
    private SparkParticle findDeadExplosion()
    {
        for (SparkParticle s : explosions)
        {
            if (s.dead)
            {
                s.dead = false;
                return s;   
            }
        }
        
        SparkParticle s = new SparkParticle(); 
        explosions.add(s);
        return s;
    }
    
    
    private ShockWave findDeadShockwave()
    {
        for (ShockWave s : shockWaves)
        {
            if (s.dead)
            {
                s.dead = false;
                return s;
            }
        }
        
        ShockWave s = new ShockWave(); 
        shockWaves.add(s);
        return s;
    }
    
    
    @Override
    public void advance(long timeElapsed)
    {
        for (SparkParticle s : sparks)
        {
            s.pos.translate(s.direction, s.speed*timeElapsed);
            s.speed *= 0.8;
            if (s.speed < 0.02)
                s.dead = true;
        }

        for (SparkParticle s : explosions)
        {
            s.pos.translate(s.direction, s.speed*timeElapsed);
            s.speed *= 0.9;
            if (s.speed < 0.01)
                s.dead = true;
        }
        
        for (ShockWave s : shockWaves)
        {
            s.size *= 1.5;
            if (s.size > 200)
                s.dead = true;
        }
    }

    @Override
    public void render(Graphics2D g)
    {
        g.setColor(Color.WHITE);
        for (SparkParticle s : sparks)
        {
            if (!s.dead)
            {
                int x = (int)s.pos.getX();
                int y = (int)s.pos.getY();
                g.fillOval(x, y, 2, 2);
            }
        }
        
        for (SparkParticle s : explosions)
        {
            s.flag = !s.flag;
            
            if (!s.dead)
            {
                if (s.flag)
                    g.setColor(Color.WHITE);
                else
                    g.setColor(Color.RED);
                int x = (int)s.pos.getX();
                int y = (int)s.pos.getY();
                g.fillOval(x, y, 5, 5);
            }
        }

        for (ShockWave s : shockWaves)
        {
            if (!s.dead)
            {
                int x = (int)(s.center.getX() - s.size/2);
                int y = (int)(s.center.getY() - s.size/2);
                g.setStroke(STROKE1);
                g.setColor(new Color(255, 0, 0, 128));
                g.drawOval(x, y, (int)s.size, (int)s.size);
            }
        }
    }

    
    public void emitShockWave(int x, int y, double blastRadius)
    {
        ShockWave s = findDeadShockwave();
        s.center.setLocation(x, y);
        s.dead = false;
        s.size = 10.0;
    }

    
    class SparkParticle
    {
        public MHPoint pos = new MHPoint();
        public double speed;
        public double direction;
        public boolean dead;
        public boolean flag;
    }


    class ShockWave
    {
        public MHPoint center = new MHPoint();
        public double size, maxSize;
        public boolean dead;
    }


}
