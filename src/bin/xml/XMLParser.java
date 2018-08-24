package bin.xml;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public final class XMLParser {
    private XMLParser() { // private constructor
    }

    public static XMLNode Parse(String filePath) {
        File xmlFile = new File(filePath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            Node rootnode = doc.getDocumentElement().getFirstChild().getParentNode();
            XMLNode root = ParseRecursively(rootnode);
            return root;

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return null;
        } catch (SAXException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static XMLNode ParseRecursively(Node node) {
        if (node.getNodeName() == "#text")
            return null;

        XMLNode newnode =  new XMLNode(node.getNodeName());

        if (node.hasAttributes()) {
            NamedNodeMap attributes = node.getAttributes();
            for (int i = 0; i < attributes.getLength(); i++) {
                newnode.addAttribute(attributes.item(i).getNodeName(),attributes.item(i).getNodeValue());
            }
        }

        if (node.hasChildNodes()) {
            NodeList children = node.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                XMLNode child = ParseRecursively(children.item(i));
                if (child != null)
                    newnode.addChild(child);
            }
        }

        newnode.setContent(format(node.getTextContent()));

        return newnode;
    }

    private static String format(String s) {
        s = s.replace('\t',' ').replace('\n',' ');
        s = s.trim();
        while (s.contains("  "))
            s = s.replace("  ", " ");

        return s;
    }
}
