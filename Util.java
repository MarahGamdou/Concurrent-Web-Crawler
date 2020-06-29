import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class handles various common utilities such as logging facilities.
 */
public class Util {
	public static Logger getLogger(Class<?> clas) {
		ConsoleHandler ch = new ConsoleHandler();
		ch.setLevel(Level.WARNING);
		Logger l = Logger.getLogger(clas.getName());
		l.setUseParentHandlers(false);
		l.addHandler(ch);
		return l;
	}
}
