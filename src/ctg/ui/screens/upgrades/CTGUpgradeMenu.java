package ctg.ui.screens.upgrades;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import mhframework.MHPoint;
import mhframework.MHRandom;
import mhframework.MHRenderable;
import mhframework.media.MHFont;
import ctg.data.CTGDatabase;
import ctg.data.CTGDatabase.CTGShipInfo;
import ctg.ui.CTGKeyboardCommandButton;
import ctg.ui.command.CTGUpgradeAmmoCommand;
import ctg.ui.command.CTGUpgradeBlastRadiusCommand;
import ctg.ui.command.CTGUpgradeDoneCommand;
import ctg.ui.command.CTGUpgradeLaserDamageCommand;
import ctg.ui.command.CTGUpgradeLaserRateCommand;
import ctg.ui.command.CTGUpgradeMissileDamageCommand;
import ctg.ui.command.CTGUpgradeMissileRateCommand;
import ctg.world.CTGShip;

public class CTGUpgradeMenu implements MHRenderable
{
        public static final int WIDTH = 400;
        public static final int HEIGHT = 550;
        
        private int x, y;
        private int selectedUpgrade = 0;
        private String playerName;

        CTGKeyboardCommandButton[] menu;

        public MHFont dataFont = new MHFont("Monospaced", Font.BOLD, 16);
        private MHFont titleFont = new MHFont("Monospaced", Font.BOLD, 30);
        //private MHFont shipNameFont = new MHFont("Monospaced", Font.BOLD, 30);
        
        private Rectangle2D shipNameRect;
        private Rectangle2D titleRect;
        
        private CTGShip ship;
        
        
        public CTGUpgradeMenu(int x, int y, String playerName, CTGShip ship)
        {
            this.x = x;
            this.y = y;
            this.playerName = playerName;
            this.ship = ship;

            shipNameRect = new Rectangle2D.Double(x+80, y+70, 40, 20);
            titleRect = new Rectangle2D.Double(x+110, y, WIDTH-100, 90);
            
            menu = new CTGKeyboardCommandButton[7];
            menu[0] = new CTGKeyboardCommandButton(new CTGUpgradeLaserRateCommand(ship));
            menu[0].setText("Laser Fire Rate ($"+CTGUpgradeLaserRateCommand.COST+")");
            menu[1] = new CTGKeyboardCommandButton(new CTGUpgradeLaserDamageCommand(ship));
            menu[1].setText("Laser Damage ($"+ CTGUpgradeLaserDamageCommand.COST +")");
            menu[2] = new CTGKeyboardCommandButton(new CTGUpgradeAmmoCommand(ship));
            menu[2].setText("Warhead Ammo ($"+CTGUpgradeAmmoCommand.COST+")");
            menu[3] = new CTGKeyboardCommandButton(new CTGUpgradeMissileRateCommand(ship));
            menu[3].setText("Warhead Fire Rate ($"+CTGUpgradeMissileRateCommand.COST+")");
            menu[4] = new CTGKeyboardCommandButton(new CTGUpgradeMissileDamageCommand(ship));
            menu[4].setText("Warhead Damage ($" + CTGUpgradeMissileDamageCommand.COST + ")");
            menu[5] = new CTGKeyboardCommandButton(new CTGUpgradeBlastRadiusCommand(ship));
            menu[5].setText("Blast Radius ($"+CTGUpgradeBlastRadiusCommand.COST + ")");
            menu[6] = new CTGKeyboardCommandButton(new CTGUpgradeDoneCommand(ship));
            menu[6].setText("Done");

            int by = y + 260;
            
            for (int i = 0; i < menu.length; i++)
            {
                menu[i].setCenterPosition(x+200, by+i*35);
                menu[i].setSize(350, 30);
            }
            changeSelection(0);
        }
        
        
        public void nextUpgrade()
        {
            selectedUpgrade++;
            if (selectedUpgrade >= CTGDatabase.getInstance().getNumShips())
                selectedUpgrade = 0;
            
            changeSelection(selectedUpgrade);
        }


        public void prevUpgrade()
        {
            selectedUpgrade--;
            if (selectedUpgrade < 0)
                selectedUpgrade = CTGDatabase.getInstance().getNumShips()-1;
            
            changeSelection(selectedUpgrade);
        }


        @Override
        public void advance(long timeElapsed)
        {
        }

        @Override
        public void render(Graphics2D g)
        {
            // Draw the background.
            g.setColor(Color.BLACK);
            g.fillRoundRect(x+10, y+10, WIDTH-20, HEIGHT-20, 40, 40);
            g.setColor(Color.LIGHT_GRAY);
            g.drawRoundRect(x+10, y+10, WIDTH-20, HEIGHT-20, 40, 40);
            g.drawRoundRect(x+15, y+15, WIDTH-30, HEIGHT-30, 40, 40);

            g.setColor(Color.WHITE);
            //g.drawRect((int)titleRect.getX(), (int)titleRect.getY(), (int)titleRect.getWidth(), (int)titleRect.getHeight());
            MHPoint tp = titleFont.centerOn(titleRect, g, playerName);
            titleFont.drawString(g, playerName, (int)tp.getX(), (int)tp.getY());
            
            // Draw the stats.
            int labelX = x + 120;
            int labelY = y + 110;
            int lineSpacing = 18;
            g.setColor(Color.WHITE);
            dataFont.drawString(g, "           Money:", labelX, labelY);
            labelY += lineSpacing;
            dataFont.drawString(g, "Laser Fire Delay:", labelX, labelY);
            labelY += lineSpacing;
            dataFont.drawString(g, "    Laser Damage:", labelX, labelY);
            labelY += lineSpacing;
            dataFont.drawString(g, "Warhead Capacity:", labelX, labelY);
            labelY += lineSpacing;
            dataFont.drawString(g, "   Warhead Delay:", labelX, labelY);
            labelY += lineSpacing;
            dataFont.drawString(g, "  Warhead Damage:", labelX, labelY);
            labelY += lineSpacing;
            dataFont.drawString(g, "    Blast Radius:", labelX, labelY);

            g.setStroke(new BasicStroke(1.0f));
            g.drawLine(x + 50, labelY+10, x+WIDTH-50, labelY+10);
            
            labelX = x + 300;
            labelY = y + 110;
            g.setColor(Color.GREEN);
            tp = this.dataFont.centerOn(shipNameRect, g, ship.getName());
            dataFont.drawString(g, ship.getName(), tp.getX(), tp.getY());
            dataFont.drawString(g, "$"+ship.getScore(), labelX, labelY);
            labelY += lineSpacing;
            dataFont.drawString(g, ship.getFiringDelay()+"", labelX, labelY);
            labelY += lineSpacing;
            dataFont.drawString(g, ship.getLaserDamage()+"", labelX, labelY);
            labelY += lineSpacing;
            dataFont.drawString(g, ship.getMaxWarheads()+"", labelX, labelY);
            labelY += lineSpacing;
            dataFont.drawString(g, ship.getMissileDelay()+"", labelX, labelY);
            labelY += lineSpacing;
            dataFont.drawString(g, ship.getMissileDamage()+"", labelX, labelY);
            labelY += lineSpacing;
            dataFont.drawString(g, (int)ship.getMissileRadius()+"", labelX, labelY);

            
            // Draw the ship.
            int shipX = x + 80;
            int shipY = y + 30;
            g.drawImage(ship.getImage(), shipX, shipY, null);
            
            
            // Draw the buttons.
            if (!ship.isDoneShopping())
            {
                for (int i = 0; i < menu.length; i++)
                    menu[i].render(g);
            }
        }

        
        private void changeSelection(int s)
        {
            for (int i = 0; i < menu.length; i++)
                menu[i].deselect();
            
            if (s < 0)
                s = menu.length - 1;
            else if (s >= menu.length)
                s = 0;
            
            selectedUpgrade = s;
            menu[s].select();
        }


        public void execute()
        {
            if (!ship.isDoneShopping())
                menu[selectedUpgrade].execute();
        }

}
