/* Copyright Guillem Catala. www.guillemcatala.com/petrinetsim. Licensed http://creativecommons.org/licenses/by-nc-sa/3.0/ */
package presentation.figures;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.RectangularShape;

/**
 *
 * @author Guillem
 */
public abstract class AbstractFigure {

    /** Coords of the abstract figure  */
    protected Point2D position;
    /** Coords of the offset  */
    private Point2D offset;
    /** Text figure assigned to this Figure*/
    protected TextFigure label;
    /** Background color of the abstract figure  */
    protected Color fillColor = new Color(255, 255, 255);
    /** Stroke color of the abstract figure  */
    protected Color strokeColor = new Color(0, 0, 0);
    /** Color of the figure when selected  */
    protected Color selectedColor = new Color(153, 153, 255);
    /** Color of the figure when highlighted  */
    protected Color highlightedColor = new Color(115, 230, 0);
    /** False if the figure is NOT selected. True otherwhise  */
    protected boolean selected = false;
    /** False if the figure is NOT highlighted. True otherwhise  */
    protected boolean highlighted = false;
    /** Check if a given point is contained by this figure*/
    public abstract boolean contains(Point2D position);
    /** Draws the full figure */
    public abstract void draw(java.awt.Graphics2D g);
    /** Paint the interior of the figure*/
    public abstract void drawFill(java.awt.Graphics2D g);
    /** Paint the stroke of the figure*/
    public abstract void drawStroke(java.awt.Graphics2D g);

    public abstract String getElementId();
    
    public abstract void setElementId(String id);
    /** Return the bounds of the shape that represents this figure*/
    public abstract RectangularShape getBounds();

    /**
     * @return the label
     */
    public TextFigure getLabel() {
        return label;
    }

    /**
     * @param label the label to set
     */
    public void setLabel(TextFigure label) {
        this.label = label;
    }

    /**
     * @return the position
     */
    public Point2D getPosition() {
        return position;
    }


    /**
     * @param position the position to set
     */
    public void setPosition(Point2D position) {
        this.position = position;
    }

    /**
     * @return the selected
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * @param selected the selected to set
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /**
     * @return the highlighted
     */
    public boolean isHighlighted() {
        return highlighted;
    }

    /**
     * @param highlighted the highlighted to set
     */
    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }

    /**
     * @return the offset
     */
    public Point2D getOffset() {
        return offset;
    }

    /**
     * @param offset the offset to set
     */
    public void setOffset(Point2D offset) {
        this.offset = offset;
    }

}
