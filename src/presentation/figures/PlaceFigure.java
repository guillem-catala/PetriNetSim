/* Copyright Guillem Catala. www.guillemcatala.com/petrinetsim. Licensed http://creativecommons.org/licenses/by-nc-sa/3.0/ */
package presentation.figures;

import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.RectangularShape;
import presentation.Grid;

/**
 *
 * @author Guillem
 */
public class PlaceFigure extends AbstractFigure {

    private String placeId;
    private Ellipse2D ellipse;
    final public static int DIAMETER = Grid.cellSize;
    protected TokenSetFigure tokenFigure;

    public PlaceFigure(String placeId, Point2D position) {
        this.placeId = placeId;
        this.position = position;
        this.label = new TextFigure(this);
        this.tokenFigure = new TokenSetFigure(this);
        this.ellipse = generateEllipse();
    }

    @Override
    public boolean contains(Point2D position) {
        return this.ellipse.contains(position);
    }

    @Override
    public RectangularShape getBounds() {
        return new Ellipse2D.Double(position.getX() - DIAMETER / 2, position.getY() - DIAMETER / 2, DIAMETER, DIAMETER);
    }

    @Override
    public void draw(Graphics2D g) {
        this.ellipse = generateEllipse();
        drawFill(g);
        drawStroke(g);
        tokenFigure.draw(g);
    }

    @Override
    public void drawFill(Graphics2D g) {
        if (selected) {
            g.setPaint(selectedColor);
        } else {
            g.setPaint(fillColor);
        }
        g.fill(ellipse);
    }

    @Override
    public void drawStroke(Graphics2D g) {
        g.setStroke(new java.awt.BasicStroke(2f));
        if (highlighted) {
            g.setPaint(highlightedColor);
        } else {
            g.setPaint(strokeColor);
        }
        g.draw(ellipse);
    }

    public Ellipse2D generateEllipse() {
        return new Ellipse2D.Double(position.getX() - DIAMETER / 2, position.getY() - DIAMETER / 2, DIAMETER, DIAMETER);
    }

    @Override
    public void setPosition(Point2D newPosition) {
        position = newPosition;
        label.setRelativePosition(newPosition);
        tokenFigure.setRelativePosition(newPosition);
    }

    @Override
    public String getElementId() {
        return this.placeId;
    }

    @Override
    public void setElementId(String id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
