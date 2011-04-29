/* Copyright Guillem Catala. www.guillemcatala.com/petrinetsim. Licensed http://creativecommons.org/licenses/by-nc-sa/3.0/ */
package presentation.figures;

import business.Global;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author Guillem
 */
public class TextFigure extends AbstractFigure {

    /** Distance from parent Figure*/
    private Point2D offsetToParent = new Point2D.Double(50, 0);
    /** The parent of this figure*/
    private AbstractFigure parent;
    /** Shape of this figure*/
    private Rectangle2D rectangle;

    public TextFigure(AbstractFigure parent) {
        this.position = new Point2D.Double(offsetToParent.getX() + parent.getPosition().getX(), offsetToParent.getY() + parent.getPosition().getY());
        this.parent = parent;
    }

    @Override
    public boolean contains(Point2D position) {
        return this.rectangle.contains(position);
    }

    @Override
    public void draw(Graphics2D g) {
        drawStroke(g);
    }

    @Override
    public void drawFill(Graphics2D g) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void drawStroke(Graphics2D g) {
        drawText(g);
    }

    public String getText() {
        String lbl=Global.petriNet.getNetElement(parent.getElementId()).getLabel();
        if(!lbl.equals(parent.getElementId())){
            lbl = parent.getElementId()+":"+lbl;
        }
        return lbl;
    }
    
    public String getTextLabel(){
        return Global.petriNet.getNetElement(parent.getElementId()).getLabel();
    }

    public void drawText(Graphics2D g) {

        g.setStroke(new java.awt.BasicStroke(1f));
        g.setPaint(strokeColor);

        Font font = new Font(null, java.awt.Font.BOLD, 12);
        FontRenderContext fontRenderContext = g.getFontRenderContext();
        TextLayout textLayout = new TextLayout(getText(),
                font, fontRenderContext);

        Rectangle2D rectangle2D = textLayout.getBounds();

        rectangle = new Rectangle2D.Double(position.getX() -
                rectangle2D.getWidth() / 2, position.getY() -
                rectangle2D.getHeight() / 2, rectangle2D.getWidth(),
                rectangle2D.getHeight());

        g.drawString(getText(),
                (float) (position.getX() - rectangle2D.getWidth() / 2),
                (float) (position.getY() + rectangle2D.getHeight() / 2));

    }

    @Override
    public void setPosition(Point2D position) {
        offsetToParent = new Point2D.Double(position.getX() - parent.getPosition().getX(),
                position.getY() - parent.getPosition().getY());
        this.position = position;
    }

    public void setRelativePosition(Point2D parentPosition) {
        position = new Point2D.Double(parentPosition.getX() +
                offsetToParent.getX(), parentPosition.getY() + offsetToParent.getY());
    }

    public Rectangle2D getBounds() {
        return this.rectangle;
    }

    @Override
    public String getElementId() {
        return this.parent.getElementId() + "label";
    }

    /**
     * @return the offsetToParent
     */
    public Point2D getOffsetToParent() {
        return offsetToParent;
    }

    public void setOffsetToParent(Point2D offset) {
        offsetToParent = offset;
    }

    @Override
    public void setElementId(String id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
