/* Copyright Guillem Catala. www.guillemcatala.com/petrinetsim. Licensed http://creativecommons.org/licenses/by-nc-sa/3.0/ */
package business;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 *
 * @author Guillem
 */
public class TokenSet extends AbstractCollection {

    private ArrayList tokenList = new ArrayList();

    public TokenSet() {
    }

    public TokenSet(Object object) {
        if (object instanceof Token) {
            tokenList.add(object);
        } else if (object instanceof TokenSet) {
            tokenList.addAll((TokenSet) object);
        } else {
            tokenList.add(new Token(object));
        }
    }

    public TokenSet(Object object, long time) {
        if (object instanceof TokenSet) {
            this.addAll((TokenSet) object);
        } else {
            tokenList.add(new Token(object, time));
        }
    }

    public TokenSet(Object object, String initialMarkingExpression) {
        tokenList.add(new Token(object, 0, initialMarkingExpression));
    }

    public TokenSet(Object object, long timestamp, String initialMarkingExpression) {
        tokenList.add(new Token(object, timestamp, initialMarkingExpression));
    }

    @Override
    public Iterator iterator() {
        return tokenList.iterator();
    }

    @Override
    public int size() {
        return tokenList.size();
    }

    @Override
    public boolean add(Object tokenSet) {
        return tokenList.add(tokenSet);

    }

    @Override
    public boolean addAll(Collection tokenSet) {
        boolean b = tokenList.addAll(((TokenSet) tokenSet).getTokenList());
        return b;
    }

    public ArrayList getTokenList() {
        return this.tokenList;
    }

    public Token get(int id) {
        return (Token) tokenList.get(id);
    }

    @Override
    public boolean remove(Object o) {
        return tokenList.remove(o);
    }

    /**
     * if(exists at least one timed token and his time <= GLOBALTIME) true
     */
    public boolean containsTime(long timestamp) {
        boolean found = false;
        boolean allzero = true;
        Iterator it = tokenList.iterator();
        while (it.hasNext()) {
            Token token = (Token) it.next();
            if (token.getTimestamp() <= timestamp) {
                found = true;
            }
            if (token.getTimestamp() != 0) {
                allzero = false;
            }
        }

        if (allzero || found) {
            return true;
        } else {
            return false;
        }
    }

    /** Increments timed tokens by a fixed amount */
    public void incrementTime(long timestamp) {
        Iterator it = tokenList.iterator();
        while (it.hasNext()) {
            Token token = (Token) it.next();
            if (token.getTimestamp() != 0) {
                token.setTimestamp(token.getTimestamp() + timestamp);
            }
        }
    }

    @Override
    public boolean removeAll(Collection c) {
        Iterator it = tokenList.iterator();
        while (it.hasNext()) {
            Token i = (Token) it.next();
            Iterator it2 = c.iterator();
            while (it2.hasNext()) {
                Token j = (Token) it2.next();
                if (j.equals(i)) {
                    it.remove();
                    it2.remove();
                }
            }
        }
        return true;
    }

    @Override
    public void clear() {
        tokenList.clear();
    }
}
