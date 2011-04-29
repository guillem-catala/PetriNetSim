/* Copyright Guillem Catala. www.guillemcatala.com/petrinetsim. Licensed http://creativecommons.org/licenses/by-nc-sa/3.0/ */
package presentation;

import business.Global;
import business.NetObject;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Iterator;
import presentation.figures.AbstractArcFigure;
import presentation.figures.AbstractFigure;
import presentation.figures.PathPoint;
import presentation.figures.TextFigure;

/**
 *
 * @author Guillem
 */
public class SelectionManager extends javax.swing.JComponent implements MouseListener, MouseMotionListener, KeyListener {

    /** Selection rectangle starting coordinates.*/
    private Point2D selectionStartPoint;
    /** Selection rectangle ending coordinates.*/
    private Point2D selectionEndPoint;
    /** Selection rectangle used to select multiple elements.*/
    private Rectangle2D selectionRectangle;
    /** If true, enables the ability to select figures. False otherwise.*/
    private boolean selectionEnabled = false;
    /** Contains current selected figures.*/
    private HashMap selectedFigures = new HashMap();
    /** Background color of the selection rectangle.*/
    protected Color fillColor = new Color(175, 203, 229, 80);
    /** Stroke color of the selection rectangle.*/
    protected Color strokeColor = new Color(151, 200, 250);
    /** Parent component that holds SelectionManager*/
    private Canvas canvas;
    /** True if all canvas figures are snapped to Grid. False otherwise.*/
    private boolean snapToGrid = true;
    
    public SelectionManager(Canvas canvas) {
        this.canvas = canvas;
        addMouseListener(this);
        addMouseMotionListener(this);
        this.addKeyListener(this);
    }

    /** Used to update Selection Manager bounds according to canvas size. */
    public void updateBounds() {
        if (selectionEnabled) {
            setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        }
    }

    /**
     * Draws the selection rectangle and the currently selected figures.
     */
    @Override
    public void paintComponent(java.awt.Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g2 = (Graphics2D) graphics;
        this.updateBounds();
        //draw selected figures
        if (!selectedFigures.isEmpty()) {
            Iterator it = selectedFigures.values().iterator();
            while (it.hasNext()) {
                AbstractFigure figure = (AbstractFigure) it.next();
                if (!(figure instanceof TextFigure)) {// prevent double painting
                    figure.draw(g2);
                }
            }
        }

        // Draw selection rectangle
        if (selectionEnabled && selectionRectangle != null) {
            g2.setStroke(new java.awt.BasicStroke(1f));
            g2.setPaint(fillColor);
            g2.fill(selectionRectangle);
            g2.setPaint(strokeColor);
            g2.draw(selectionRectangle);
        }
        canvas.repaint();
    }

    /**
     * Recalculates the size of the selection rectangle given a new end point.
     */
    public void calculateRectangleBounds(Point2D point) {
        if (selectionStartPoint != null) {
            // store the current location
            this.selectionEndPoint = point;
            // calculate the new size of the selection rectangle
            double downX = this.selectionStartPoint.getX();
            double downY = this.selectionStartPoint.getY();
            double hereX = this.selectionEndPoint.getX();
            double hereY = this.selectionEndPoint.getY();

            double l = Math.min(downX, hereX);
            double t = Math.min(downY, hereY);
            double w = Math.abs(downX - hereX);
            double h = Math.abs(downY - hereY);
            this.selectionRectangle = new Rectangle2D.Double(l, t, w, h);

        }
    }

    /** Adds a new figure to the current selected figures. */
    public void addSelectedFigure(AbstractFigure figure) {
        figure.setSelected(true);
        selectedFigures.put(figure.getElementId(), figure);
    }

    /**
     * Adds all elements inside the selection rectangle.
     */
    public void addSelectedElements(HashMap figures) {
        if (selectionRectangle != null) {
            Iterator it = figures.values().iterator();
            while (it.hasNext()) {
                AbstractFigure figure = (AbstractFigure) it.next();
                if (!(figure instanceof AbstractArcFigure)) {
                    if (figure.getBounds().intersects(selectionRectangle)) {
                        this.addSelectedFigure(figure);
                    }
                }
            }
        }
        selectionRectangle = null;
    }

    /**
     * Remove all current selected figures by changing it's selected
     * attribute to false.
     */
    public void removeSelectedFigures() {
        if (!selectedFigures.isEmpty()) {
            Iterator it = selectedFigures.values().iterator();
            while (it.hasNext()) {
                AbstractFigure figure = (AbstractFigure) it.next();
                if (figure instanceof AbstractArcFigure) {
                    AbstractArcFigure arcFigure = (AbstractArcFigure) figure;
                    arcFigure.removeSelectedPoints();
                }
                figure.setSelected(false);
            }
            selectedFigures = new HashMap();
        }
    }

    /**
     * Updates all selected figures distance from a given point
     */
    public void updateOffsets(Point2D offset) {
        // update offsets
        Iterator it = selectedFigures.values().iterator();
        while (it.hasNext()) {
            AbstractFigure element = (AbstractFigure) it.next();
            if (element.getPosition() != null) {// ie: AbstractArcFigure doesn't have position by itself
                Point2D newOffset = new Point2D.Double(element.getPosition().getX() - offset.getX(), element.getPosition().getY() - offset.getY());
                element.setOffset(newOffset);
            }
        }
    }

    /**
     * @return the mouseDown
     */
    public Point2D getSelectionStartPoint() {
        return selectionStartPoint;
    }

    /**
     * @param mouseDown the mouseDown to set
     */
    public void setSelectionStartPoint(Point2D selectionStartPoint) {
        this.selectionStartPoint = selectionStartPoint;
    }

    /**
     * @return the mouseHere
     */
    public Point2D getSelectionEndPoint() {
        return selectionEndPoint;
    }

    /**
     * @param mouseHere the mouseHere to set
     */
    public void setSelectionEndPoint(Point2D selectionEndPoint) {
        this.selectionEndPoint = selectionEndPoint;
    }

    /**
     * @return the selectionRectangle
     */
    public Rectangle2D getSelectionRectangle() {
        return selectionRectangle;
    }

    /**
     * @param selectionRectangle the selectionRectangle to set
     */
    public void setSelectionRectangle(Rectangle2D selectionRectangle) {
        this.selectionRectangle = selectionRectangle;
    }

    /**
     * @return the selectionEnabled
     */
    public boolean isSelectionEnabled() {
        return selectionEnabled;
    }

    /**
     * @param selectionEnabled the selectionEnabled to set
     */
    public void setSelectionEnabled(boolean selectionEnabled) {
        this.selectionEnabled = selectionEnabled;
        if (selectionEnabled) {
            // to enable listeners in parent component
            updateBounds();
            canvas.add(this);
        } else {
            this.selectionRectangle = null;
            removeSelectedFigures();
            canvas.remove(this);
        }
    }

    /** Snaps all selected figures except PathPoints to grid cells */
    public void snapToGrid(Point2D e) {
        if(snapToGrid){
        int val = Grid.cellSize / 5;// number of parts per cell
        Iterator it = selectedFigures.values().iterator();
        while (it.hasNext()) {
            AbstractFigure figure = (AbstractFigure) it.next();
            if (!(figure instanceof PathPoint) && figure.getPosition() != null) {// ie: AbstractArcFigure doesn't have position by itself
                int x = (int) (((int) (e.getX() + figure.getOffset().getX()) / val) * val + val / 2);
                int y = (int) (((int) (e.getY() + figure.getOffset().getY()) / val) * val + val / 2);
                Point2D point = new Point2D.Double(x, y);
                figure.setPosition(point);
            }
        }}
    }

    /**
     * @return the selectedElements
     */
    public HashMap getSelectedElements() {
        return selectedFigures;
    }

    /**
     * @param selectedElements the selectedElements to set
     */
    public void setSelectedElements(HashMap selectedElements) {
        this.selectedFigures = selectedElements;
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        switch (Global.mode) {
            case Global.SIMULATIONMODE:
            case Global.SELECTMODE:
                AbstractFigure figure = canvas.selectFigure(e.getPoint());
                if (figure != null) {
                    if (!selectedFigures.containsKey(figure.getElementId())) {
                        addSelectedFigure(figure);
                    }
                    updateOffsets(e.getPoint());
                    // right click
                    if (e.getButton() == MouseEvent.BUTTON3) {
                        NetObject netObject = Global.petriNet.getNetElement(canvas.getFigureKey(figure));
                        canvas.showForm(netObject);
                        removeSelectedFigures();
                    }
                } else {
                    setSelectionStartPoint(e.getPoint());
                    removeSelectedFigures();
                    if (e.getButton() == MouseEvent.BUTTON3) {
                        canvas.showForm(Global.petriNet);
                    }
                }
                break;
        }
        this.repaint();
    }

    public void mouseReleased(MouseEvent e) {
        addSelectedElements(canvas.getFigures());
        this.repaint();

    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
        switch (Global.mode) {
            case Global.SELECTMODE:
                if (!selectedFigures.isEmpty()) {
                    Iterator it = selectedFigures.values().iterator();
                    while (it.hasNext()) {
                        AbstractFigure figure = (AbstractFigure) it.next();
                        if (figure instanceof AbstractArcFigure) {
                            AbstractArcFigure arcFigure = (AbstractArcFigure) figure;
                            arcFigure.setPosition(e.getPoint());
                        } else {
                            Point2D offset = figure.getOffset();
                            Point2D newPosition = new Point2D.Double(e.getPoint().getX() + offset.getX(), e.getPoint().getY() + offset.getY());
                            figure.setPosition(newPosition);
                        }
                    }
                    snapToGrid(e.getPoint());

                } else {
                    calculateRectangleBounds(e.getPoint());
                }
                break;
        }

        this.repaint();
    }

    public void mouseMoved(MouseEvent e) {
        requestFocus();//per activar el KeyListener
    }

    public void keyTyped(KeyEvent e) {
    }

    /** Deletes selected figures if the key supr is pressed*/
    public void keyPressed(KeyEvent e) {
        if (Global.mode == Global.SELECTMODE) {
            if (e.getKeyCode() == 127) {//supr
                if (!selectedFigures.isEmpty()) {
                    Iterator i = selectedFigures.values().iterator();
                    while (i.hasNext()) {
                        AbstractFigure figure = (AbstractFigure) i.next();
                        canvas.removeFigure(figure);
                    }
                }
            }
        }
        // reset selected figures
        this.selectedFigures = new HashMap();
        this.repaint();
    }

    public void keyReleased(KeyEvent e) {
    }
}
