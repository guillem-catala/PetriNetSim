/* Copyright Guillem Catala. www.guillemcatala.com/petrinetsim. Licensed http://creativecommons.org/licenses/by-nc-sa/3.0/ */
package presentation.figures;

import business.Global;
import business.Place;
import business.TokenSet;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

/**
 *
 * @author Guillem
 */
public class TokenSetFigure extends TextFigure {

    private String placeId;
    private Ellipse2D ellipse;
    /** Distance from parent Figure*/
    private Point2D offsetToParent = new Point2D.Double(0, 0);
    final private static int DIAMETER = 30;

    public TokenSetFigure(PlaceFigure parent) {
        super(parent);
        this.position = new Point2D.Double(offsetToParent.getX() + parent.getPosition().getX(), offsetToParent.getY() + parent.getPosition().getY());
        this.placeId = parent.getElementId();
    }

    @Override
    public void draw(Graphics2D g) {
        this.ellipse = generateEllipse();
        drawFill(g);
        drawStroke(g);
    }

    @Override
    public void drawFill(Graphics2D g) {
        g.setPaint(fillColor);
    //g.fill(ellipse);
    }

    @Override
    public void drawStroke(Graphics2D g) {
        super.drawStroke(g);
    //g.draw(ellipse);
    }

    public Ellipse2D generateEllipse() {
        return new Ellipse2D.Double(position.getX() - DIAMETER / 2, position.getY() - DIAMETER / 2, DIAMETER, DIAMETER);
    }

    @Override
    public void setRelativePosition(Point2D parentPosition) {
        position = new Point2D.Double(parentPosition.getX() +
                offsetToParent.getX(), parentPosition.getY() + offsetToParent.getY());
    }

    @Override
    public String getText() {
        Place place = (Place) Global.petriNet.getNetElement(placeId);
        return "" + place.getTokens().size();
    }
}
