import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.logging.Logger;

public class UrlList extends ArrayList<String> {
	private static Logger logger = Util.getLogger(UrlList.class);

	private UrlList() {
		super();
	}

	public static UrlList CreateUrlList(String filename) throws Exception {
		UrlList urllist = new UrlList();
		File file = new File(filename);
		BufferedReader br = new BufferedReader(new FileReader(file));
		String url;
		String line = br.readLine();
		url = line;

		while ((line = br.readLine()) != null) {
			try {
				if (line.substring(0, 4).equals("http")) {
					urllist.add(url);
					url = line;
				} else {
					url = url + line;
				}
			} catch (IndexOutOfBoundsException iob) {
				url = url + line;
			}
		}
		urllist.add(url);
		return (urllist);

	}

} 		  
  			  
     

