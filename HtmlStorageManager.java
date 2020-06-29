import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HtmlStorageManager {
    private int pageLimit;
    private int currPageCount;
    private static String folderPath = "./crawler";
    private static String fullMsg = "ignored";
    private static String errorMsg = "File unable to be created for some reason";
    private static String deadMsg="dead-url";
    private Writer writer;
    private Logger logger;
    private HashMap<String, String> locations;

    public HtmlStorageManager(int pageLimit) {
        this.pageLimit = pageLimit;
        currPageCount = 0;
        logger = Util.getLogger(HtmlStorageManager.class);
        locations = new HashMap<>();
    }

    public String storeHTML(String url) {
        if(locations.containsKey(url)) {
            return locations.get(url);
        } else {
            String result = "";
            try {
                result = _storeHTML(url);
            } catch(Exception ex) {
                result = "";
            }
            locations.put(url, result);
            return result;
        }
    }

    private String _storeHTML(String url){
        if (currPageCount == pageLimit) {  //if reached page limit
            logger.fine(fullMsg);
            return fullMsg;
        }


        File newFile = new File(currPageCount+".html"); //files will be named according to their parse sequence/order
        try {
            if (isDead(url)){
                return deadMsg;
            }else if (newFile.createNewFile()) {
                String filename = newFile.getName();
                currPageCount++;
                writer = new FileWriter(filename);
                writer.write(getHtmlContents(url));
                writer.flush();
                logger.fine("Stored HTML contents at "+ folderPath+"/"+filename);
                return newFile.getAbsolutePath();
            } else {
                logger.warning(errorMsg);
                return errorMsg;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getHtmlContents(String url) {

        //find any href(and resolves to absolute if needed) using jsoup
        try {
            Document doc = Jsoup.connect(url).ignoreHttpErrors(true).ignoreContentType(true).get();
            return doc.toString();
        } catch (IOException e) {
            logger.log(Level.INFO,e.getMessage(),e);
        } catch (Exception e) {
            logger.log(Level.WARNING,e.getMessage(),e);
        }
        return "";
    }

    private static boolean isDead(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connect = (HttpURLConnection) url.openConnection();
            int code = connect.getResponseCode();
            // link is broken
            return (connect.getResponseCode() != 200);
        } catch (Exception e) {
            return (true);
        }
    }
}
