/* Copyright Guillem Catala. www.guillemcatala.com/petrinetsim. Licensed http://creativecommons.org/licenses/by-nc-sa/3.0/ */
package business;

import java.util.ArrayList;
import java.util.Iterator;
import presentation.GUI;

/**
 *
 * @author Guillem
 */
public class TimedSimulation extends Simulation {

    private long time = 0;

    public TimedSimulation(boolean step, GUI gui) {
        super(step, gui);
        gui.getTxtClock().setText(String.valueOf(time));
    }

    @Override
    public void run() {
        super.run();
    }

    @Override
    public boolean isFinished() {
        ArrayList enabledTransitions = this.enabledTransitionList();
        boolean isDead = this.enabledTransitionList().isEmpty();
        if (enabledTransitions.isEmpty()) {
            incrementTime();
            enabledTransitions = this.enabledTransitionList();
            if (!enabledTransitions.isEmpty()) {
                isDead = false;
            }
        } else {
            isDead = false;
        }
        return isDead;
    }

    @Override
    public ArrayList enabledTransitionList() {
        Iterator it = Global.petriNet.getTransitions().iterator();
        ArrayList enabledTransitions = new ArrayList();
        while (it.hasNext()) {
            Transition transition = (Transition) it.next();
            if (transition.enabled(time)) {
                enabledTransitions.add(transition);
            }
        }
        return enabledTransitions;
    }

    @Override
    protected void fireTransition() {
        enabledTransitionList();
        if (!this.enabledTransitionList().isEmpty()) {
            getRandomTransition().fire(this.gui, this.time);
            pauseResumeSimulation();
        }
    }

    public void incrementTime() {
        // Visit all places tokens and check whether they have timestamp>0 and less than globalclock
        // assign the global clock to the mimnimum found
        long minTime = 999999999;
        ArrayList places = Global.petriNet.getPlaces();
        for (int i = 0; i < places.size(); i++) {
            Place place = (Place) places.get(i);
            TokenSet tokenList = place.getTokens();
            if (tokenList.size() > 0) {
                for (int j = 0; j < tokenList.size(); j++) {
                    Token token = (Token) tokenList.get(j);
                    if (token.getTimestamp() != 0 && token.getTimestamp() < minTime) {
                        minTime = token.getTimestamp();
                    }
                }
            }
        }
        if (minTime != 999999999) {
            this.time = minTime;
        }
        this.gui.getTxtClock().setText(String.valueOf(this.time));
    }

    /**
     * @return the time
     */
    public long getTime() {
        return time;
    }

    /**
     * @param time the time to set
     */
    public void setTime(long time) {
        this.time = time;
    }
}
