/* Copyright Guillem Catala. www.guillemcatala.com/petrinetsim. Licensed http://creativecommons.org/licenses/by-nc-sa/3.0/ */
package business;

/**
 *
 * @author Guillem
 */
public class OutputArc extends Arc implements Inscription {

    /** Text string that represents the expression this arc will execute by default */
    private String executeText = "1";

    /** Constructor to create an output arc. */
    public OutputArc(Place place, Transition transition) {
        this.id = "o" + this.id;
        this.place = place;
        this.transition = transition;
    }

    /** Constructor to create a new output arc with specific attributes. */
    public OutputArc(String id, Place place, Transition transition, String action) {
        this.id = id;
        this.place = place;
        this.transition = transition;
        this.executeText = action;
    }

    public boolean evaluate() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /** Default method that is executed when a transition fires. */
    public TokenSet execute() {
        return new TokenSet(executeText);
    }

    /** Returns the TokenSet of the place this arc is connected*/
    public TokenSet getTokenSet() {
        return this.getPlace().getTokens();
    }

    /**
     * @return the executeText
     */
    public String getExecuteText() {
        return executeText;
    }

    /**
     * @param executeText the executeText to set
     */
    public void setExecuteText(String executeText) {
        this.executeText = executeText;
    }
}
