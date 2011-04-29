/* Copyright Guillem Catala. www.guillemcatala.com/petrinetsim. Licensed http://creativecommons.org/licenses/by-nc-sa/3.0/ */
package business;

import java.util.Iterator;
import presentation.GUI;

/**
 *
 * @author Guillem
 */
public class Transition extends NetObject implements Inscription {

    private String guardText = "return true;";
    /** Global clock when the transition fires.*/
    private long globalClock;

    public Transition() {
        this.id = "t" + this.id;
    }

    public Transition(String id) {
        this.id = id;
    }

    public Transition(String id, String guardText) {
        this.id = id;
        this.guardText = guardText;
    }

    /** Fires a transition. */
    public void fire(GUI gui, long globalClock) {
        this.globalClock = globalClock;

        // Highlight places ON
        gui.getCanvas().highlightPlaces(Global.petriNet.getInputArcs(), id, true, false);

        // Highlight inputArcs ON
        gui.getCanvas().highlightArcs(Global.petriNet.getInputArcs(), id, true, true);

        //remove all tokens from places
        Iterator it = Global.petriNet.getInputArcs().iterator();
        while (it.hasNext()) {
            InputArc arc = (InputArc) it.next();
            if (arc.getTransition().getId().equals(getId())) {
                arc.getPlace().removeTokens(arc.execute());
                gui.getJTextArea1().append("- " + arc.getExecuteText() + "\n");
                gui.getJTextArea1().setCaretPosition(gui.getJTextArea1().getText().length());
            }
        }

        // Highlight places OFF
        gui.getCanvas().highlightPlaces(Global.petriNet.getInputArcs(), id, false, false);

        // Highlight inputArcs OFF
        gui.getCanvas().highlightArcs(Global.petriNet.getInputArcs(), id, false, false);

        // Highlight transition ON
        gui.getCanvas().highlightTransition(id, true, true);
        if (!this.getLabel().equals(this.getId())) {
            gui.getJTextArea1().append(this.getLabel() + " (" + this.getId() + ") fired!\n");
        } else {
            gui.getJTextArea1().append(this.getId() + " fired.\n");
        }
        gui.getJTextArea1().setCaretPosition(gui.getJTextArea1().getText().length());


        // Highlight transition OFF
        gui.getCanvas().highlightTransition(id, false, false);

        // Highlight outputArcs ON
        gui.getCanvas().highlightArcs(Global.petriNet.getOutputArcs(), id, true, false);


        // Highlight places ON
        gui.getCanvas().highlightPlaces(Global.petriNet.getOutputArcs(), id, true, true);

        //create all tokens to output places
        it = Global.petriNet.getOutputArcs().iterator();
        while (it.hasNext()) {
            OutputArc arc = (OutputArc) it.next();
            if (arc.getTransition().getId().equals(getId())) {
                TokenSet tokenSet = arc.execute();
                tokenSet.incrementTime(globalClock);// set time of all new tokens of the tokenSet
                arc.getPlace().addToken(tokenSet);

                gui.getJTextArea1().append("+ " + arc.getExecuteText() + "\n");
                gui.getJTextArea1().setCaretPosition(gui.getJTextArea1().getText().length());

            }
        }

        // Highlight outputArcs OFF
        gui.getCanvas().highlightArcs(Global.petriNet.getOutputArcs(), id, false, false);

        // Highlight places OFF
        gui.getCanvas().highlightPlaces(Global.petriNet.getOutputArcs(), id, false, false);

        gui.getJTextArea1().append("----------------------------\n");
        gui.getCanvas().repaint();
    }

    public boolean enabled(long time) {
        // transition guard evaluation
        boolean enabled = evaluate();

        // input arc guards
        if (enabled && !Global.petriNet.getInputArcs().isEmpty()) {
            Iterator it = Global.petriNet.getInputArcs().iterator();
            while (enabled && it.hasNext()) {
                InputArc arc = (InputArc) it.next();
                if (arc.getTransition().getId().equals(getId())) {
                    TokenSet tokensList = arc.getPlace().getTokens();
                    enabled = tokensList.containsTime(time);
                    // check arc's evaluation expression
                    enabled = enabled & arc.evaluate();
                }
            }
        }

        // check output arc place capacity restriction
        if (enabled && !Global.petriNet.getOutputArcs().isEmpty()) {
            Iterator it = Global.petriNet.getOutputArcs().iterator();
            while (enabled && it.hasNext()) {
                OutputArc arc = (OutputArc) it.next();
                if (arc.getTransition().getId().equals(getId())) {
                    TokenSet tokensList = arc.getPlace().getTokens();
                    // check if places have capacity limit
                    if (arc.getPlace().getCapacity() != 0) {
                        enabled = enabled & arc.getPlace().getCapacity() > tokensList.size();
                    }
                }
            }
        }

        return enabled;
    }

    public boolean evaluate() {
        return true;
    }

    public TokenSet execute() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public TokenSet getTokenSet() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @return the inscriptionText
     */
    public String getGuardText() {
        return guardText;
    }

    /**
     * @param inscriptionText the inscriptionText to set
     */
    public void setGuardText(String guardText) {
        this.guardText = guardText;
    }

    /**
     * @return the globalClock
     */
    public long getGlobalClock() {
        return globalClock;
    }
}


