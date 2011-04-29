/* Copyright Guillem Catala. www.guillemcatala.com/petrinetsim. Licensed http://creativecommons.org/licenses/by-nc-sa/3.0/ */
package presentation.figures;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;

/**
 *
 * @author Guillem
 */
public class NormalArcFigure extends AbstractArcFigure {

    @Override
    public boolean contains(Point2D position) {
        double flatness = 0.01;
        boolean intersect = false;
        PathIterator pit = path.getPathIterator(null, flatness);
        double[] coords = new double[6];
        double lastX = 0, lastY = 0;
        while (!intersect && !pit.isDone()) {
            int type = pit.currentSegment(coords);
            switch (type) {
                case PathIterator.SEG_MOVETO:
                    lastX = coords[0];
                    lastY = coords[1];
                    break;
                case PathIterator.SEG_LINETO:
                    BasicStroke stroke = new BasicStroke(10.0f);
                    Line2D.Double line = new Line2D.Double(lastX, lastY,
                            coords[0], coords[1]);
                    Shape shape = stroke.createStrokedShape(line);
                    if (shape.contains(position)) {
                        intersect = true;
                    }

                    lastX = coords[0];
                    lastY = coords[1];
            }
            pit.next();
        }
        return intersect;
    }

    @Override
    public Rectangle2D getBounds() {
        return null;
    }

    @Override
    public void draw(Graphics2D g) {
        generatePath();
        drawStroke(g);       
    }

    @Override
    public void drawFill(Graphics2D g) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void drawStroke(Graphics2D g) {
        g.setStroke(new BasicStroke(1f));
        if (highlighted) {
            g.setStroke(new BasicStroke(2f));
            g.setPaint(highlightedColor);
        } else if (selected) {
            g.setPaint(selectedColor);
        } else {
            g.setPaint(strokeColor);
        }

        g.draw(path);
    }

    @Override
    public String getElementId() {
        return this.arcId;
    }

    @Override
    public void setElementId(String id) {
        this.arcId = id;
    }
}
