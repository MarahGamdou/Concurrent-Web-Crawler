import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.HashMap;

public class CrawlingThread implements Runnable {
	private static Logger logger = Util.getLogger(CrawlingThread.class);

	Thread t;
	private String threadName;
	private ConcurrentLinkedQueue<String> urllist;
	private BUL bul;
	private IUT iut;
	private HashMap<String, String> parenturl = new HashMap<String, String>();

	CrawlingThread(String name, int threadNumber, UrlList urllist, BUL bul, IUT iut,HashMap<String, String> parenturl) {
		threadName = name;
		t = new Thread(this, name);
		this.bul = bul;
		this.iut = iut;
		this.parenturl=parenturl;

		//create and split urllist, and allocate a portion to the thread depending on its thread number
		int numDigits = urllist.size() / Main.NUM_CRAWLTHREAD;
		int first = (threadNumber - 1) * numDigits;
		int last = first + numDigits;
		this.urllist = new ConcurrentLinkedQueue<>(urllist.subList(first, last));

		logger.info("Thread " + name + " created!");
	}


	public void run() {
		while (!urllist.isEmpty()) {
			String url = urllist.poll();
			logger.fine(threadName + " is parsing " + url);
			String html = "";

			//get HTML contents
			if(!iut.contains(url)) {
				try {
					//find any href(and resolves to absolute if needed) using jsoup
					Document doc = Jsoup.connect(url).ignoreHttpErrors(true).get();
					html = doc.toString();
					//TODO: add finding links in other tags (not just a tags)
					Elements links = doc.select("a");
					if (links != null) {
						for (Element elem : links) {
							//we want only http urls, not mailto: etc
							//TODO: add a better check for URL-ness
							if (elem.attr("abs:href").contains("http")) {
								String nextUrl = elem.attr("abs:href");
								if (!iut.contains(nextUrl)) {
									urllist.add(nextUrl);
									parenturl.put(nextUrl,url);

								}
							}
						}
					}
				} catch (IOException e) {
					logger.log(Level.FINE, e.getMessage(), e);
				} catch (Exception e) {
					logger.log(Level.WARNING, e.getMessage(), e);
				} finally {
					//update the BUL
					bul.add(url, html);
				}
			}


		}
	}


}
