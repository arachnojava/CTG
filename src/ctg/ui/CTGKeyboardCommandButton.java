package ctg.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import mhframework.MHPoint;
import mhframework.MHRenderable;
import mhframework.gui.MHCommand;
import mhframework.media.MHFont;

public class CTGKeyboardCommandButton implements MHRenderable
{
    private MHCommand cmd;
    private ButtonProperties props;
    private ButtonProperties normalProps, selectedProps;
    private MHPoint center = new MHPoint();
    private Rectangle2D bounds = new Rectangle2D.Double();
    private String text;
    

    public CTGKeyboardCommandButton(MHCommand c)
    {
        cmd = c;
        
        normalProps = new ButtonProperties();
        normalProps.backgroundColor = new Color(64, 64, 64, 64);
        normalProps.textColor = Color.WHITE;
        normalProps.borderColor = new Color(0, 0, 0, 64);
        normalProps.font = new MHFont("Monospaced", Font.BOLD, 16);
        normalProps.size = new MHPoint(200, 50);

        selectedProps = new ButtonProperties();
        selectedProps.backgroundColor = new Color(64, 64, 64, 180);
        selectedProps.textColor = Color.GREEN;
        selectedProps.borderColor = new Color(0, 255, 0, 255);
        selectedProps.font = new MHFont("Monospaced", Font.BOLD, 20);
        selectedProps.size = new MHPoint(220, 55);

        deselect();
    }
    
    
    public void setCenterPosition(int x, int y)
    {
        center.setLocation(x, y);
    }

    
    public String getText()
    {
        return text;
    }


    public void setText(String text)
    {
        this.text = text;
    }


    public void select()
    {
        props = selectedProps;
        calculateBounds();
    }

    
    private void calculateBounds()
    {
        double w = props.size.getX();
        double h = props.size.getY();
        double x = center.getX() - w/2;
        double y = center.getY() - h/2;
        bounds.setRect(x, y, w, h);
    }
    
    
    public void deselect()
    {
        props = normalProps;
        calculateBounds();
    }

    
    public void execute()
    {
        cmd.execute();
    }


    @Override
    public void advance(long timeElapsed)
    {
        // TODO Auto-generated method stub
        
    }



    @Override
    public void render(Graphics2D g)
    {
        g.setColor(props.backgroundColor);
        g.fill(bounds);//(int)center.getX(), (int)center.getY(), (int)props.size.getX(), (int)props.size.getY());
        g.setColor(props.textColor);
        MHPoint tp = props.font.centerOn(bounds, g, text);
        props.font.drawString(g, text, tp.getX(), tp.getY());
        g.setColor(props.borderColor);
        g.draw(bounds);
    }


    public void setSize(int w, int h)
    {
        normalProps.size.setLocation(w, h);
        selectedProps.size.setLocation(w, h);
    }
}


class ButtonProperties
{
    public Color backgroundColor;
    public Color textColor;
    public Color borderColor;
    public MHFont font;
    public MHPoint size;
}

