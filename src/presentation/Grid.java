/* Copyright Guillem Catala. www.guillemcatala.com/petrinetsim. Licensed http://creativecommons.org/licenses/by-nc-sa/3.0/ */
package presentation;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;

/**
 *
 * @author Guillem
 */
public class Grid {

    /** Grid width.*/
    private int width;
    /** Grid height.*/
    private int height;
    /** Size of the foreground cell.*/
    public static int cellSize = 50;
    /** Color of foreground cell.*/
    private Color strongColor = new Color(235, 235, 235);
    /** Color of the background cell.*/
    private Color weakColor = new Color(220, 220, 220);
    /** Background grid.*/
    private GeneralPath backgroundGrid;
    /** Foreground grid.*/
    private GeneralPath foregroundGrid;

    /** Constructor for a new Grid given a width and height.*/
    public Grid(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /** Generates a new grid with a predefined number of columns.*/
    public GeneralPath generateGrid(int numCells) {
        GeneralPath grid = new GeneralPath();
        for (float i = 0; i <= width; i += cellSize / numCells) {
            grid.moveTo(i, 2);
            grid.lineTo(i, height);
        }

        for (float i = 0; i <= height; i += cellSize / numCells) {
            grid.moveTo(2, i);
            grid.lineTo(width, i);
        }

        return grid;
    }

    /** Generates the background and foreground grids.*/
    public void drawGrid(Graphics2D g2) {
        if (backgroundGrid == null) {
            backgroundGrid = generateGrid(5);
        }
        g2.setPaint(strongColor);
        g2.draw(backgroundGrid);

        if (foregroundGrid == null) {
            foregroundGrid = generateGrid(1);
        }
        g2.setPaint(weakColor);
        g2.draw(foregroundGrid);
    }
}
