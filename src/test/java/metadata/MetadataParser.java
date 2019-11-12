package metadata;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import javax.imageio.metadata.IIOMetadata;

/**
 * This class is based in http://johnbokma.com/java/obtaining-image-metadata.html,
 * that is based in https://docs.oracle.com/javase/1.5.0/docs/guide/imageio/spec/apps.fm5.html
 * <p>
 * Thanks for it, John! And well, good job, Oracle. Tell Sun guys I said hi.
 */
public class MetadataParser {

    public int countMarkersByName(IIOMetadata metadata, String nodeOrAttributeName) {
        int count = 0;

        String[] names = metadata.getMetadataFormatNames();
        for (String name : names) {
            System.out.println("Format name: " + name);
            count += countMarkersByName(metadata.getAsTree(name), nodeOrAttributeName);
        }

        return count;
    }

    private int countMarkersByName(Node root, String nodeOrAttributeName) {
        return countMarkersByName(root, 0, nodeOrAttributeName);
    }

    private int countMarkersByName(Node node, int level, String nodeOrAttributeName) {
        int count = 0;

        if (node.getNodeName().equals(nodeOrAttributeName)) {
            count++;
        }

        System.out.print("<" + node.getNodeName());
        NamedNodeMap map = node.getAttributes();
        if (map != null) { // print attribute values
            int length = map.getLength();
            for (int i = 0; i < length; i++) {
                Node attr = map.item(i);

                if (attr.getNodeName().equals(nodeOrAttributeName)) {
                    count++;
                }
            }
        }

        Node child = node.getFirstChild();
        if (child != null) {
            while (child != null) { // emit child tags recursively
                count += countMarkersByName(child, level + 1, nodeOrAttributeName);
                child = child.getNextSibling();
            }
        }

        return count;
    }
}
