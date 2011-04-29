/* Copyright Guillem Catala. www.guillemcatala.com/petrinetsim. Licensed http://creativecommons.org/licenses/by-nc-sa/3.0/ */
package business;

/**
 *
 * @author Guillem
 */
public abstract class Arc extends NetObject {

    /** The place where this arc is connected*/
    protected Place place;
    /** The transition where this arc is connected*/
    protected Transition transition;

    /**
     * @return the place
     */
    public Place getPlace() {
        return place;
    }

    /**
     * @param place the place to set
     */
    public void setPlace(Place place) {
        this.place = place;
    }

    /**
     * @return the transition
     */
    public Transition getTransition() {
        return transition;
    }

    /**
     * @param transition the transition to set
     */
    public void setTransition(Transition transition) {
        this.transition = transition;
    }
}
