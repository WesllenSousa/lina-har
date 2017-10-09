/*
 * Created on Dec 3, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package controle.CRF.CRF;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface Constraint {
	int UNION=1;
	int PAIR_DISALLOW=2;
	int ALLOW_ONLY = 3;
	int type();
}
