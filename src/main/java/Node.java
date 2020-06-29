import java.util.ArrayList;
import java.util.List;

/*
setNode
getNode
setParent
getParent
addChild
addChildren
getChildren


 */

public class Node {
    private String str = null;
    private Node parent = null;
    private List<Node> children = new ArrayList<>();

    public Node(String str) {
        this.str = str;
    }

    public void setNode(String str) {
        this.str =str;
    }

    public String getNode() {
        return str;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Node getParent() {
        return parent;
    }

    public Node addChild(Node child) {
        child.setParent(this);
        this.children.add(child);
        return child;
    }

    public void addChildren(List<Node> children) {
        children.forEach(each -> each.setParent(this));
        this.children.addAll(children);
    }

    public List<Node> getChildren() {
        return children;
    }

}