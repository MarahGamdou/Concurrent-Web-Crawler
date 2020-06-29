//import java.io.FileWriter;
//import java.io.Writer;
import java.util.*;
import java.util.logging.Logger;
import java.util.HashMap;
/**
 * Testing Instructions:
 * Please change NUM_BUL to the desired number.
 *
 * The commandline required to be entered is of the format:
 * java Crawler -time 24h -input url.txt -output output.txt
 * so please run the program accordingly
 *
 * Our current configuration is for each BUL we have 2 CTs and 1 IBT. To change these parameters go into the CREATION for loop
 *
 * Table of Results
 *
 * 1. Configuration 1 -> 1 BUL with 2 IBT and 2 CT each
 * Number of URL found in 1 hour:
 *
 *
 *
 */

public class Main {
	static final int NUM_BUL = 1;
	static final int NUM_CRAWLTHREAD = 2 * NUM_BUL;
	static final Logger logger = Util.getLogger(Main.class);


	public static void main(String[] args) throws Exception {
		//Reform command
		StringBuilder s = new StringBuilder();
		for (String i:args){
			s.append(i);
			s.append(" ");
		}
		Parser p = new Parser(s.toString());
		int hoursSet = p.getNumHours();
		int numStoredPages = p.getnumPages();
		String seedFile = p.getinFile();
		String outFile = p.getoutFile();
		System.out.println(hoursSet);
		System.out.println(seedFile);
		System.out.println(outFile);



		UrlList urlList = UrlList.CreateUrlList(seedFile);
		HashMap<String, String> parenturl = new HashMap<String, String>();
		IUT iut = new IUT(outFile,numStoredPages);
		List<IBT> ibts = new ArrayList<>();
		List<CrawlingThread> cts = new ArrayList<>();

		//CREATION
		//For every BUL, we create x IBT and y CT , where x and y are optimal values for the crawler to explore as many unique urls
		for(int i=0; i<NUM_BUL; i++) {
			BUL bul = new BUL();
			IBT ibt = new IBT(String.valueOf(i), bul, iut,parenturl);

			CrawlingThread ct1 = new CrawlingThread(String.valueOf(i)+"A", 2*i+1, urlList, bul, iut,parenturl);  //threadnumber is used for deciding how to allocate the split urllist
			CrawlingThread ct2 = new CrawlingThread(String.valueOf(i)+"B", 2*i+2, urlList, bul, iut,parenturl);

			ibts.add(ibt);
			cts.add(ct1);
			cts.add(ct2);
		}


		//boot up all the IBTs
		for(IBT ibt: ibts) {
			ibt.t.start();
		}

		//boot up all the threads
		for(CrawlingThread ct: cts) {
			ct.t.start();
		}

		//start timer for program according to time specified by user
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new ProgramTimer(), hoursSet *60 * 60 * 1000, hoursSet * 60 * 60 * 1000);

		// This should not return - the program will be ended by the ProgramTimer
		cts.get(0).t.join();



		logger.severe("Something bad happened - this should never be hit");

	}

	static class ProgramTimer extends TimerTask {
		@Override
		public void run() {
			logger.info("End of time set. Stopping crawler.");
			System.exit(0);
		}
	}
}

