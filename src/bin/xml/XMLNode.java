package bin.xml;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

public class XMLNode {
    private String name;
    private ArrayList<XMLNode> children;
    private Dictionary<String,String> attributes;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    private String content;

    public XMLNode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ArrayList<XMLNode> getChildren() {
        if (children == null) {
            children =  new ArrayList<>();
        }
        return children;
    }

    public void addChild(XMLNode child) {
        getChildren().add(child);
    }

    public Dictionary<String, String> getAttributes() {
        if (attributes == null) {
            attributes =  new Hashtable();
        }
        return attributes;
    }

    public void addAttribute(String key, String value) {
        getAttributes().put(key,value);
    }

    public XMLNode GetChild(String name) {
        for (int i = 0; i < getChildren().size(); i++) {
            if (getChildren().get(i).getName() == name)
                return getChildren().get(i);
        }
        return null;
    }

    public XMLNode GetChildByAttributeValue(String attrName, String attrValue) {
        for (int i = 0; i < getChildren().size(); i++) {
            Dictionary attributes = getChildren().get(i).getAttributes();
            String c = (String)attributes.get(attrName);

            if (c.contentEquals(attrValue))
                return getChildren().get(i);
        }
        return null;
    }
}
