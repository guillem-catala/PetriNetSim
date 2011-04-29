/* Copyright Guillem Catala. www.guillemcatala.com/petrinetsim. Licensed http://creativecommons.org/licenses/by-nc-sa/3.0/ */
package presentation.figures;

/**
 *
 * @author Guillem
 */
public interface ConnectionFigure {

    public void setConnectionStart(AbstractFigure start);

    public AbstractFigure getStartConnector();

    public void setConnectionEnd(AbstractFigure end);

    public AbstractFigure getEndConnector();
}
