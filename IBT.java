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
