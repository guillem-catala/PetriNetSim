/* Copyright Guillem Catala. www.guillemcatala.com/petrinetsim. Licensed http://creativecommons.org/licenses/by-nc-sa/3.0/ */
package business;

/**
 *
 * @author Guillem
 */
public interface Inscription {

    /** Gives the hability to evaluate */
    public boolean evaluate();

    /** Gives the hability to execute */
    public TokenSet execute();

    /** Gives the hability to obtain a TokenSet */
    public TokenSet getTokenSet();
}
