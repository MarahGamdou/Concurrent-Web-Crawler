//a list recording newly crawled URLs with their html content
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

//Buffered URL List (BUL)
public class BUL {
	private static Logger logger = Util.getLogger(BUL.class);

	private HashMap<String, String> bufferedUrlList;
	protected boolean isFull;
	final static int CAPACITY = 10; //each BUL according to p1 guidelines has max cap of 1k


	public BUL() {
		bufferedUrlList = new HashMap<>(CAPACITY);
		isFull = false;
	}


	/**
	 * takes in pair of strings(url and html content), parses it and stores it in the hashmap
	 * is synchronized as in current config 2 CTs share access to 1 BUL
	 *
	 * @param url  url as a string
	 * @param body html contents of that url as string
	 */
	public synchronized void add(String url, String body) {
		while (isFull) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		logger.fine("Inserting " + url);
		bufferedUrlList.put(url, body);
		if (bufferedUrlList.size() >= CAPACITY) {
			isFull = true;
		}
		logger.fine("BUL done updating by thread");
		notifyAll();

	}


	/**
	 * clears the BUL (transfers it to IBT)
	 */
	public synchronized List<String> remove() {
		while (!isFull) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		//for now, just clear BUL to see if wait and notify work properly
		logger.fine("Clearing BUL");
		ArrayList<String> urls = new ArrayList<>();
		synchronized (bufferedUrlList) {
			urls.addAll(bufferedUrlList.keySet());
			bufferedUrlList.clear();
			isFull = false;
		}
		logger.fine("BUL cleared");

		notifyAll();
		return urls;
	}
}
