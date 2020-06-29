/*Such threads constantly read BUL and transfer the list into an IUT (for the first time) or a part of IUT, which shall support efficiently search for the existence of a specific URL in our result. The BUL has a size limit of 1K urls. Once an IBT find a BUL reaches its limit, it transfer it into (a part of) the tree and clear BUL. 
-When a CT finds a BUL is full, it waits until BUL is cleared. 
-When an IBT finds a BUL is not full, it waits until BUL is full.*/

import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

public class IBT implements Runnable {
	private static Logger logger = Util.getLogger(IBT.class);

	private BUL bul;
	Thread t;
	private IUT iut;
	private HashMap<String, String> parenturl = new HashMap<String, String>();

	public IBT(String name, BUL bul, IUT iut,HashMap<String, String> parenturl){
		this.bul = bul;
		t = new Thread(this, name);
		this.iut = iut;
		this.parenturl=parenturl;
	}

	@Override
	public void run(){

		//clears bul when it is full, transferring contents to its own data struct
		while (true) {
			List<String> urls = bul.remove();
			for (String url: urls){
				iut.add(parenturl,url);
			}

		}

	}
}
