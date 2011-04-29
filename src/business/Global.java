/* Copyright Guillem Catala. www.guillemcatala.com/petrinetsim. Licensed http://creativecommons.org/licenses/by-nc-sa/3.0/ */
package business;

/**
 *
 * @author Guillem
 */
public class Global {

    /** The current PetriNet model */
    public static PetriNet petriNet = new PetriNet();
    /** Application mode*/
    public static int mode = 0;
    /** To enable figure selection*/
    public static final int SELECTMODE = 0;
    /** To add places */
    public static final int PLACEMODE = 1;
    /** To add transitions */
    public static final int TRANSITIONMODE = 2;
    /** To add arcs*/
    public static final int NORMALARCMODE = 3;
    /** When simulation occurs */
    public static final int SIMULATIONMODE = 4;
}
