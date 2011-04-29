/* Copyright Guillem Catala. www.guillemcatala.com/petrinetsim. Licensed http://creativecommons.org/licenses/by-nc-sa/3.0/ */
package presentation.figures;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import presentation.Grid;

/**
 *
 * @author Guillem
 */
public abstract class AbstractArcFigure extends AbstractFigure implements ConnectionFigure {

    /** Color of the figure when highlighted  */
    protected Color highlightedColor = new Color(0, 0, 128);
    private AbstractFigure start;
    private AbstractFigure end;
    protected String arcId;
    protected GeneralPath path = new GeneralPath();
    protected LinkedList pathPoints = new LinkedList();
    final int BARB = 10;
    final double PHI = Math.PI / 8;
    // protected Point2D selectedPoint;
    protected HashMap selectedPoints = new HashMap();

    public void addPoint(Point2D point) {        
        PathPoint p = new PathPoint(point, "_pathpoint_" + this.getPoints().size());
        pathPoints.add(p);
    }

    public void removePoint(PathPoint pathPoint) {
        pathPoints.remove(pathPoint);
    }

    public void setConnectionStart(AbstractFigure start) {
        this.start = start;
    }

    public AbstractFigure getStartConnector() {
        return start;
    }

    public void setConnectionEnd(AbstractFigure end) {
        this.end = end;
    }

    public AbstractFigure getEndConnector() {
        return end;
    }

    public void setLabel() {
        this.label = new TextFigure(this);
    }

    public void setSelectedPoint(Point2D position) {
        this.selectedPoints.put(position, pathPoints.indexOf(position));
    }

    public void removeSelectedPoints() {
        this.selectedPoints = new HashMap();
    }

    private Point2D getDefaultLabelPosition() {
        Point2D startPoint = (Point2D) pathPoints.get((pathPoints.size()) / 2 - 1);
        Point2D endPoint = (Point2D) pathPoints.get((pathPoints.size()) / 2);
        return new Point2D.Double(startPoint.getX() + (endPoint.getX() - startPoint.getX()) / 2,
                startPoint.getY() + (endPoint.getY() - startPoint.getY()) / 2);

    }

    /** Check if a Point2D is near a path point*/
    public Point2D containsPoint(Point2D position) {
        boolean intersect = false;
        Point2D point = null;
        Iterator it = this.getPoints().iterator();
        while (!intersect && it.hasNext()) {
            PathPoint pathPoint = (PathPoint) it.next();
            point = pathPoint.getPosition();
            Rectangle2D rect = (Rectangle2D) pathPoint.getBounds();
            if (rect.contains(position)) {
                intersect = true;
            }
        }
        if (!intersect) {
            point = null;
        }

        return point;
    }

    private Point2D.Double getIntersectingPoint(double theta, Rectangle2D r) {
        double cx = r.getCenterX();
        double cy = r.getCenterY();
        double W = r.getWidth() / 2;
        double H = r.getHeight() / 2;
        double R = Point2D.distance(cx, cy, cx + W, cy + H);
        double x = cx + R * Math.cos(theta);
        double y = cy + R * Math.sin(theta);
        Point2D.Double p = new Point2D.Double();
        int outcode = r.outcode(x, y);
        switch (outcode) {
            case Rectangle2D.OUT_TOP:
                p.x = cx - H * ((x - cx) / (y - cy));
                p.y = cy - H;
                break;
            case Rectangle2D.OUT_LEFT:
                p.x = cx - W;
                p.y = cy - W * ((y - cy) / (x - cx));
                break;
            case Rectangle2D.OUT_BOTTOM:
                p.x = cx + H * ((x - cx) / (y - cy));
                p.y = cy + H;
                break;
            case Rectangle2D.OUT_RIGHT:
                p.x = cx + W;
                p.y = cy + W * ((y - cy) / (x - cx));
                break;
            default:
                p.x = cx + W;
                p.y = cy + W * ((y - cy) / (x - cx));

        }
        return p;
    }

    public void generatePath() {
        double bx, by, ex, ey, eFirstx, eFirsty, eLastx, eLasty;
        PathPoint pathPoint;
        if (start == null) {
            if (end == null) {
                pathPoint = (PathPoint) pathPoints.getFirst();
                bx = pathPoint.getPosition().getX();
                by = pathPoint.getPosition().getY();
                pathPoint = (PathPoint) pathPoints.getLast();
                ex = pathPoint.getPosition().getX();
                ey = pathPoint.getPosition().getY();
            } else {
                System.out.println("AbstractArcFigure GeneratePath()");
                bx = position.getX();
                by = position.getX();
                ex = round(end.getPosition().getX());
                ey = round(end.getPosition().getY());
            }
            eFirstx = ex;
            eFirsty = ey;
            eLastx = ex;
            eLasty = ey;
        } else if (end == null) {
            bx = round(start.getPosition().getX());
            by = round(start.getPosition().getY());

            pathPoint = (PathPoint) pathPoints.get(pathPoints.size() - 1);
            ex = round(pathPoint.getPosition().getX());
            ey = round(pathPoint.getPosition().getY());
            eFirstx = bx;
            eFirsty = by;
            eLastx = ex;
            eLasty = ey;
            if (pathPoints.size() > 1) {
                pathPoint = (PathPoint) pathPoints.get(1);
                eFirstx = round(pathPoint.getPosition().getX());
                eFirsty = round(pathPoint.getPosition().getY());
            }
            if (pathPoints.size() > 1) {
                pathPoint = (PathPoint) pathPoints.get(pathPoints.size() - 2);
                eLastx = round(pathPoint.getPosition().getX());
                eLasty = round(pathPoint.getPosition().getY());
            }
        } else {
            bx = round(start.getPosition().getX());
            by = round(start.getPosition().getY());
            ex = round(end.getPosition().getX());
            ey = round(end.getPosition().getY());
            pathPoint = (PathPoint) pathPoints.get(1);
            eFirstx = round(pathPoint.getPosition().getX());
            eFirsty = round(pathPoint.getPosition().getY());
            pathPoint = (PathPoint) pathPoints.get(pathPoints.size() - 2);
            eLastx = round(pathPoint.getPosition().getX());
            eLasty = round(pathPoint.getPosition().getY());
        }

        int boffset = Grid.cellSize / 2;
        int eoffset = Grid.cellSize / 2;

        if (start == null) {
            boffset = 0;
        }

        if (end == null) {
            eoffset = 0;
        }
        path = new GeneralPath(GeneralPath.WIND_NON_ZERO);
        double dx = ex - eLastx;
        double dy = ey - eLasty;
        float v1 = operation(eFirstx, bx, eFirsty, by);
        float v2 = operation(eFirsty, by, eFirstx, bx);
        float v3 = operation(ex, eLastx, ey, eLasty);
        float v4 = operation(ey, eLasty, ex, eLastx);
        double theta = (Math.atan2(dy, dx));


        Point2D p0;
        Point2D p1;

        if (start instanceof PlaceFigure) {
            p0 = getPoint(new Point2D.Double(round(bx + boffset * v1), round(by + boffset * v2)), theta);
        } else if (start instanceof TransitionFigure) {
            dx = eFirstx - bx;
            dy = eFirsty - by;
            double theta1 = Math.atan2(dy, dx);
            TransitionFigure transitionFigure = (TransitionFigure) getStartConnector();
            p0 = getIntersectingPoint(theta1, (Rectangle2D) transitionFigure.getBounds());
        } else {
            pathPoint = (PathPoint) pathPoints.getFirst();
            p0 = (Point2D) pathPoint.getPosition();
        }

        if (end != null) {
            if (end instanceof PlaceFigure) {
                p1 = getPoint(new Point2D.Double(round(ex - eoffset * v3), round(ey - eoffset * v4)), theta + Math.PI);
            } else {
                dx = eLastx - ex;
                dy = eLasty - ey;
                double theta1 = Math.atan2(dy, dx);
                TransitionFigure transitionFigure = (TransitionFigure) getEndConnector();
                p1 = getIntersectingPoint(theta1, (Rectangle2D) transitionFigure.getBounds());
            }
            pathPoint = (PathPoint) pathPoints.get(this.pathPoints.size() - 1);
            pathPoints.set(this.pathPoints.size() - 1, new PathPoint(p1, pathPoint.getElementId()));
        }

        pathPoint = (PathPoint) pathPoints.get(0);
        pathPoints.set(0, new PathPoint(p0, pathPoint.getElementId()));
        Iterator it = pathPoints.iterator();

        int i = 0;
        while (i < pathPoints.size() && it.hasNext()) {
            pathPoint = (PathPoint) it.next();
            if (i == 0) {
                path.moveTo((float) p0.getX(), (float) p0.getY());
            } else {
                this.path.lineTo(pathPoint.getPosition().getX(), pathPoint.getPosition().getY());
            }
            i++;
        }

        if (end == null) {
//            pathPoint = (PathPoint) pathPoints.get(pathPoints.size() - 1);
//            Point2D p = pathPoint.getPosition();
//            dx = p.getX() - ex;
//            dy = p.getY() - ey;
//            theta = Math.atan2(dy, dx);
//            theta += Math.PI;
//            this.path.lineTo(p.getX(), p.getY());
//            path.append(this.getLine(new Point2D.Double(p.getX(), p.getY()), theta - PHI), false);
//            path.append(this.getLine(new Point2D.Double(p.getX(), p.getY()), theta + PHI), false);
        } else {
            theta += Math.PI;
            pathPoint = (PathPoint) pathPoints.getLast();

            Line2D line = this.getLine(pathPoint.getPosition(), theta - PHI);
            Line2D line1 = this.getLine(pathPoint.getPosition(), theta + PHI);
            path.append(line, false);
            path.append(line1, false);
        // Line2D line2 = new Line2D.Double(line.getP2(), line1.getP2());
        // path.append(line2, false);

        }
    }

    private int round(double d) {
        return (int) Math.floor(d + 0.5);
    }

    public float operation(double a, double b, double c, double d) {
        return (float) ((a - b) / Math.sqrt(Math.pow(a - b, 2) + Math.pow(c - d, 2)));
    }

    private Point2D getPoint(Point2D p, double theta) {
        return new Point2D.Double(p.getX() +  Math.cos(theta),
                p.getY() +  Math.sin(theta));
    }

    private Line2D getLine(Point2D p, double theta) {
        double x = p.getX() + BARB * Math.cos(theta);
        double y = p.getY() + BARB * Math.sin(theta);
        return new Line2D.Double(p.getX(), p.getY(), x, y);
    }

    /**
     * @return the points
     */
    public LinkedList getPoints() {
        return pathPoints;
    }

    /**
     * @param points the points to set
     */
    public void setPoints(LinkedList points) {
        this.pathPoints = points;
    }

    public void setPosition(Point2D newPosition) {

    }
}
