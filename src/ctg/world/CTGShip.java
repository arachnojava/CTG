package ctg.world;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import mhframework.MHActor;
import mhframework.MHPoint;
import mhframework.io.MHKeyboardState;
import ctg.data.CTGDatabase;
import ctg.ui.CTGKeyControls;
import ctg.ui.CTGKeyControls.Command;
import ctg.ui.audio.CTGSounds;
import ctg.ui.screens.CTGGameScreen;

public class CTGShip extends MHActor
{
    private boolean doneShopping = false;
    private int shipID = 0;
    private String playerName;
    private CTGKeyControls controls, altControls;
    private Image image;
    double maxSpeed, turnSpeed;
    int firingDelay = 20, firingTimer = 0, missileDelay=50, missileTimer = 0;
    private boolean dead = false;
    private int laserDamage = 1, missileDamage = 20;
    private double missileRadius = 100;
    private ArrayList<Laser> lasers = new ArrayList<Laser>();
    private ArrayList<CTGMissile> missiles = new ArrayList<CTGMissile>();

    public final double leftGunX = -10;
    public final double rightGunX = 11;
    public final double gunY = -10;
    private int maxWarheads = 3;
    private int warheadCount;
    private int score;

    public int getScore()
    {
        return score;
    }


    public void setScore(int score)
    {
        this.score = score;
    }


    public int getWarheadCount()
    {
        return warheadCount;
    }

    
    public void setKeyControls(CTGKeyControls c)
    {
        controls = c;
    }
    
    
    public void setAltKeyControls(CTGKeyControls c)
    {
        altControls = c;
    }
    
    
    @Override
    public void setHealth(int health)
    {
        if (health == 0)
        {
            setDead(true);
        }
        else if (health < 0)
        {
            CTGSounds.play(CTGSounds.deathSound);
            CTGParticleSystem.getInstance().emitExplosion((int)getCenterX(), (int)getCenterY());
            health = 0;
            setDead(true);
            score += CTGDatabase.CTGScoreValues.GOT_DESTROYED;
        }
        else 
            setDead(false);
        
        super.setHealth(health);
    }


    @Override
    public void advance(long elapsedTime)
    {
        for (final Laser l:lasers)
            l.advance(elapsedTime);

        for (final CTGMissile m:missiles)
            m.advance(elapsedTime);

        if (isDead()) return;
        
        firingTimer++;
        missileTimer++;
        
        if (MHKeyboardState.isKeyPressed(controls.getKeyCommand(Command.MOVE)))
        {
            setSpeed(maxSpeed);
            MHPoint nextPoint = this.getLocation().clone();
            nextPoint.translate(getRotation(), getSpeed()*elapsedTime);
            MHPoint myCenter = nextPoint.clone();
            myCenter.setLocation(myCenter.getX()+20, myCenter.getY()+20);

            // Check for collisions.
            if (!(CTGGameScreen.board.canMoveTo((int)((nextPoint.getY()+5)/CTGLevel.TILE_SIZE), (int)((nextPoint.getX()+5)/CTGLevel.TILE_SIZE))
                &&CTGGameScreen.board.canMoveTo((int)((nextPoint.getY()+5)/CTGLevel.TILE_SIZE), (int)((nextPoint.getX()+35)/CTGLevel.TILE_SIZE))
                &&CTGGameScreen.board.canMoveTo((int)((nextPoint.getY()+35)/CTGLevel.TILE_SIZE), (int)((nextPoint.getX()+5)/CTGLevel.TILE_SIZE))
                &&CTGGameScreen.board.canMoveTo((int)((nextPoint.getY()+35)/CTGLevel.TILE_SIZE), (int)((nextPoint.getX()+35)/CTGLevel.TILE_SIZE))
                &&CTGGameScreen.board.canMoveTo((int)(myCenter.getY()/CTGLevel.TILE_SIZE), (int)(myCenter.getX()/CTGLevel.TILE_SIZE))))
                setSpeed(0);

            
            for (int i = 0; i < CTGGameScreen.players.length; i++)
            {
                if (CTGGameScreen.players[i] != this && !CTGGameScreen.players[i].isDead())
                {
                    MHPoint otherCenter = CTGGameScreen.players[i].getLocation().clone();
                    otherCenter.setLocation(otherCenter.getX()+20, otherCenter.getY()+20);
                    double dist = myCenter.distanceTo(otherCenter);

                    if (dist < 30)
                        setSpeed(0);
                }
            }
        }
        else
            setSpeed(0);
        
        if (MHKeyboardState.isKeyPressed(controls.getKeyCommand(Command.TURN_LEFT)))
            setRotationSpeed(turnSpeed * -1.0);
        else if (MHKeyboardState.isKeyPressed(controls.getKeyCommand(Command.TURN_RIGHT)))
            setRotationSpeed(turnSpeed);
        else
            setRotationSpeed(0);
        
        if (MHKeyboardState.isKeyPressed(controls.getKeyCommand(Command.FIRE_LASERS)) && firingTimer > firingDelay)
        {
            firingTimer = 0;

            fireLasers();
        }

        
        if (warheadCount > 0 && MHKeyboardState.isKeyPressed(controls.getKeyCommand(Command.LAUNCH_MISSILE)) && missileTimer > missileDelay)
        {
            missileTimer = 0;
            warheadCount--;

            fireMissile();
        }

        
        super.advance(elapsedTime);
    }


    @Override
    public void render(Graphics2D g)
    {
        for (final Laser l:lasers)
            l.render(g);
        for (final CTGMissile m:missiles)
            m.render(g);
        if (!isDead())
            super.render(g);
    }

    
    public Image getImage()
    {
        if (image == null)
            image = CTGDatabase.getInstance().getShipInfo(shipID).getImage();
        
        return image;
    }
    
    
    public void setSelectedShip(int shipID)
    {
        this.shipID = shipID;
        image = CTGDatabase.getInstance().getShipInfo(shipID).getImage();
        maxSpeed = CTGDatabase.getInstance().getShipInfo(shipID).getSpeedClass()*0.025;
        turnSpeed = (CTGDatabase.getInstance().getShipInfo(shipID).getAgilityClass()*0.033);
        setMaxHealth(10 * CTGDatabase.getInstance().getShipInfo(shipID).getArmorClass());
        setHealth(getMaxHealth());
    }

    public String getPlayerName()
    {
        return playerName;
    }

    public void setPlayerName(String playerName)
    {
        this.playerName = playerName;
    }
    
    
    public int getFiringDelay()
    {
        return firingDelay;
    }


    public void setFiringDelay(int firingDelay)
    {
        this.firingDelay = firingDelay;
    }


    public int getMissileDelay()
    {
        return missileDelay;
    }


    public void setMissileDelay(int missileDelay)
    {
        this.missileDelay = missileDelay;
    }


    public void fireLasers()
    {
        CTGSounds.play(CTGSounds.fireLaser);
        
        final MHPoint leftGun = new MHPoint(leftGunX, gunY);
        leftGun.rotate(getCenterX(), getCenterY(), getRotation());

        final MHPoint rightGun = new MHPoint(rightGunX, gunY);
        rightGun.rotate(getCenterX(), getCenterY(), getRotation());

        // Find a dead laser to reuse.
        Laser left = findDeadLaser();
        if (left == null)
            lasers.add(new Laser(leftGun.getX(), leftGun.getY(), getRotation(), laserDamage));
        else 
            left.init(leftGun.getX(), leftGun.getY(), getRotation(), laserDamage);

        Laser right = findDeadLaser();
        if (right == null)
            lasers.add(new Laser(rightGun.getX(), rightGun.getY(), getRotation(), laserDamage));
        else 
            right.init(rightGun.getX(), rightGun.getY(), getRotation(), laserDamage);
    }

    
    public void fireMissile()
    {
        //CTGSounds.play(CTGSounds.fireMissile);
        
        final MHPoint missileLauncher = new MHPoint(0, 0);
        missileLauncher.rotate(getCenterX(), getCenterY(), getRotation());

        // Find a dead missile to reuse.
        CTGMissile missile = findDeadMissile();
        if (missile == null)
            missiles.add(new CTGMissile(missileLauncher.getX(), missileLauncher.getY(), getRotation(), getSpeed(), missileDamage, missileRadius));
        else 
            missile.init(missileLauncher.getX(), missileLauncher.getY(), getRotation(), getSpeed(), missileDamage, missileRadius);
    }

    
    private Laser findDeadLaser()
    {
        for (Laser l : lasers)
        {
            if (l.dead)
                return l;
        }
        
        return null;
    }
    
    
    private CTGMissile findDeadMissile()
    {
        for (CTGMissile m : missiles)
        {
            if (m.dead)
                return m;
        }
        
        return null;
    }
    
    
    class Laser extends MHActor
    {
        int beamLength = 30;
        double beamX, beamY;
        public boolean dead = false;
        private int laserDamage = 1;

        public Laser(final double startX, final double startY, final double direction, int laserDamage)
        {
            super();

            init(startX, startY, direction, laserDamage);
        }

        
        public void init(final double startX, final double startY, final double direction, int laserDamage)
        {
            this.setLocation(startX, startY);
            this.setRotation(direction);
            this.setSpeed(1.0);
            this.laserDamage = laserDamage;
            dead = false;
            
            // Calculate the laser's end points given the beam length.
            final MHPoint endPoint = new MHPoint(0, 0);
            endPoint.translate(getRotation(), beamLength);
            beamX = endPoint.getX();
            beamY = endPoint.getY();
        }
        
        
        @Override
        public void advance(long elapsedTime)
        {
            if (dead) return;
            //getLocation().translate(getRotation(), getSpeed()*elapsedTime);
            
            final int x = (int)getX();
            final int y = (int)getY();
            final int endX = (int) (x + beamX);
            final int endY = (int) (y + beamY);

            if (!(CTGGameScreen.board.canMoveTo(y/CTGLevel.TILE_SIZE, x/CTGLevel.TILE_SIZE)
                    && CTGGameScreen.board.canMoveTo(endY/CTGLevel.TILE_SIZE, endX/CTGLevel.TILE_SIZE)
                    && CTGGameScreen.board.canMoveTo(((y+endY)/2)/CTGLevel.TILE_SIZE, ((x+endX)/2)/CTGLevel.TILE_SIZE)))
            {
                dead = true; // Kill the laser.
                // Spawn particles.
                CTGParticleSystem.getInstance().emitSparks(x, y);
            }
            else 
                super.advance(elapsedTime);
        }

        @Override
        public void render(final Graphics2D g)
        {
            if (dead) return;
            
            final int x = (int)getX();
            final int y = (int)getY();
            final int endX = (int) (x + beamX);
            final int endY = (int) (y + beamY);

            g.setStroke(new BasicStroke(9, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g.setColor(new Color(255, 0, 0, 64));
            g.drawLine(x, y, endX, endY);

            g.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g.setColor(new Color(255, 0, 0, 100));
            g.drawLine(x, y, endX, endY);

            g.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g.setColor(new Color(255, 255, 255, 200));
            g.drawLine(x, y, endX, endY);
        }


        public int getDamage()
        {
            return laserDamage;
        }
    }


    public void checkCollisions(CTGShip enemy)
    {
        if (enemy.isDead())
            return;
        
        for (Laser l : lasers)
        {
            if (!l.dead)
            {
            double midx = l.getX() + l.beamX/2;
            double midy = l.getY() + l.beamY/2;
            
            if (enemy.getBounds().contains(l.getLocation().toPoint()) ||
                enemy.getBounds().contains(l.getX()+l.beamX, l.getY()+l.beamY) ||
                enemy.getBounds().contains(midx, midy))
            {
                enemy.setHealth(enemy.getHealth() - l.getDamage());
                if (enemy.getHealth() <= 0)
                    score += CTGDatabase.CTGScoreValues.DESTROYED_ENEMY;
                CTGParticleSystem.getInstance().emitSparks((int)midx, (int)midy);
                l.dead = true;
                CTGSounds.play(CTGSounds.takeDamage);
                score += CTGDatabase.CTGScoreValues.LASER_HIT;
            }
            }
        }

        for (CTGMissile m : missiles)
        {
            if (!m.dead)
            {
                if (enemy.getBounds().contains(m.getLocation().toPoint()))
                {
                    enemy.setHealth(enemy.getHealth() - m.getDamage());
                    score += CTGDatabase.CTGScoreValues.DIRECT_MISSILE_HIT;
                    CTGParticleSystem.getInstance().emitSparks((int)m.getLocation().getX(), (int)m.getLocation().getY());
                    CTGParticleSystem.getInstance().emitShockWave((int)m.getLocation().getX(), (int)m.getLocation().getY(), missileRadius);
                    m.dead = true;

                    CTGSounds.play(CTGSounds.deathSound);
                    CTGSounds.play(CTGSounds.takeDamage);
                }
            }
        }
    }


    public String getName()
    {
        return CTGDatabase.getInstance().getShipInfo(shipID).getName();
    }


    public int getSpeedClass()
    {
        return CTGDatabase.getInstance().getShipInfo(shipID).getSpeedClass();
    }


    public int getAgilityClass()
    {
        return CTGDatabase.getInstance().getShipInfo(shipID).getAgilityClass();
    }


    public int getArmorClass()
    {
        return CTGDatabase.getInstance().getShipInfo(shipID).getArmorClass();
    }


    public boolean isDead()
    {
        return dead;
    }


    public void setDead(boolean dead)
    {
        this.dead = dead;
    }


    public void init()
    {
        doneShopping = false;
        dead = false;
        warheadCount = maxWarheads;
        setMaxHealth(10 * CTGDatabase.getInstance().getShipInfo(shipID).getArmorClass());
        setHealth(getMaxHealth());
    }


    public int getLaserDamage()
    {
        return laserDamage;
    }


    public void setLaserDamage(int damage)
    {
        laserDamage = damage;
    }


    public int getMaxWarheads()
    {
        return maxWarheads ;
    }


    public void setMaxWarheads(int count)
    {
        maxWarheads = count;        
    }


    public int getMissileDamage()
    {
        return missileDamage;
    }

    
    public void setMissileDamage(int damage)
    {
        missileDamage = damage;
    }


    public double getMissileRadius()
    {
        return missileRadius;
    }


    public void setMissileRadius(double d)
    {
        this.missileRadius = d;
    }
    


    public boolean isDoneShopping()
    {
        return doneShopping;
    }


    public void setDoneShopping(boolean doneShopping)
    {
        this.doneShopping = doneShopping;
    }

}
