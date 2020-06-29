import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IUT {
    private static Logger logger = Util.getLogger(IUT.class);
    private List<Node> seenUrlNodes = new ArrayList<>();
    private List<Node> roots = new ArrayList<>();
    private Set<String> seenUrls = new HashSet<>();
    private Writer output;
    private HtmlStorageManager hsm;

    public IUT(String outFile, int numStoredPages) throws IOException {
        output = new FileWriter(outFile);
        hsm = new HtmlStorageManager(numStoredPages);  //this is for pulling and storing HTML content from a url
    }

    //Adds the urls encountered to the IUT
    public synchronized void add(HashMap<String, String> parenturl , String url) {
        if (!seenUrls.contains(url)) {
            seenUrls.add(url);
            Node urlnode = new Node(url);
            seenUrlNodes.add(urlnode);
            if (parenturl.get(url) == null) {
                roots.add(urlnode);
            } else {
                if (!seenUrls.contains(parenturl.get(url))) {
                    add(parenturl, parenturl.get(url));
                } else {
                    for (Node node : seenUrlNodes) {
                        if (node.getNode().equals(parenturl.get(url))) {
                            node.addChild(urlnode);
                            urlnode.setParent(node);
                        }
                    }
                    try {
                        logger.fine(url);
                        logger.fine("Beginning to find HTML");
                        output.write(url + " --> " + parenturl.get(url) + " : " + hsm.storeHTML(url));
                        output.append('\n');
                        output.flush();
                    } catch (IOException ioe) {
                        logger.log(Level.WARNING, ioe.getMessage(), ioe);
                    }

                }
            }
        }
    }
    public boolean contains(String url){
        return seenUrls.contains(url);
    }

}

