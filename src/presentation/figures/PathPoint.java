/* Copyright Guillem Catala. www.guillemcatala.com/petrinetsim. Licensed http://creativecommons.org/licenses/by-nc-sa/3.0/ */
package presentation.figures;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;

/**
 *
 * @author Guillem
 */
public class PathPoint extends AbstractFigure {

    /** Stroke color of the abstract figure  */
    protected Color strokeColor = new Color(204, 204, 204);
    public static int POINTSIZE = 6;
    private Rectangle2D rectangle;
    private String id;

    public PathPoint(Point2D point, String id) {
        point = new Point2D.Double((int) point.getX(), (int) point.getY());
        this.position = point;
        this.id = id;
    }

    @Override
    public boolean contains(Point2D position) {
        return getBounds().contains(position);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PathPoint)) {
            return false;
        }
        PathPoint pathPoint = (PathPoint) obj;
        if (this.id.equals(pathPoint.getElementId()) && this.position.equals(pathPoint.getPosition())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void draw(Graphics2D g) {
        if (selected) {
            rectangle = (Rectangle2D) getBounds();
            drawFill(g);
            drawStroke(g);
        }
    }

    @Override
    public void drawFill(Graphics2D g) {
        g.setPaint(fillColor);
        g.fill(rectangle);
        g.setPaint(new Color(0, 0, 0));
        g.fill(new Rectangle2D.Double(position.getX() - POINTSIZE / 2 + 2, position.getY() - POINTSIZE / 2 + 2, POINTSIZE - 3, POINTSIZE - 3));

    }

    @Override
    public void drawStroke(Graphics2D g) {
        g.setStroke(new java.awt.BasicStroke(1f));
        g.setPaint(strokeColor);
        g.draw(rectangle);
    }

    @Override
    public void setElementId(String id) {
        this.id = id;
    }

    @Override
    public RectangularShape getBounds() {
        return new Rectangle2D.Double(position.getX() - POINTSIZE / 2, position.getY() - POINTSIZE / 2, POINTSIZE, POINTSIZE);
    }

    @Override
    public String getElementId() {
        return this.id;
    }
}
