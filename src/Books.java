
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.OutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.OutputKeys;
import org.w3c.dom.Attr;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import java.io.*;

public class Books {
    public static void main(String[] args) throws SAXException, IOException, TransformerException, ParserConfigurationException {

        DocumentBuilderFactory books = DocumentBuilderFactory.newInstance();                                            //create builder
        DocumentBuilder b = books.newDocumentBuilder();

        int totalPage = 0;
        int type1 = 0;
        int type2 = 0;
        try {
            Document doc = b.parse("books.xml");                                                                    //parse file, get structure  of document from XML
            doc.getDocumentElement().normalize();                                                                       //normalize node
            Element eroot = doc.getDocumentElement();                                                                   //get root element  from XML
            System.out.println("Root Element: " + eroot.getNodeName());
            NodeList list = eroot.getChildNodes();                                                                      //see subitem  of root element
            for (int i = 0; i < list.getLength(); i++) {
                Node book = list.item(i);                                                                               //check all elements
                if (book.getNodeType() == Node.ELEMENT_NODE) {                                                          //if types of node is element, get element
                    Element element = (Element) book;
                    String id = element.getAttribute("id");                                                       //get attribute "id"
                    String title = element.getElementsByTagName("Title").item(0).getTextContent();
                    String author = element.getElementsByTagName("Author").item(0).getTextContent();
                    String pages = element.getElementsByTagName("Pages").item(0).getTextContent();
                    String type = element.getElementsByTagName("Type").item(0).getTextContent();
                    totalPage += Integer.valueOf(pages);                                                                //total page count, convert to Integer (object)

                    NodeList costlist = element.getElementsByTagName("Cost");
                    String cost = costlist.item(0).getTextContent();

                    String currency = costlist.item(0).getAttributes().getNamedItem("currency").getTextContent();  //get attribute

                    if (element.getElementsByTagName("Type").item(0).getTextContent().equals("Science lit."))      //comparison of a sequence of characters
                        type1 += 1;
                    else if (element.getElementsByTagName("Type").item(0).getTextContent().equals("Art lit."))
                        type2 += 1;
                    else
                        System.out.println("Unknown type");

                    System.out.println("Current Element: " + book.getNodeName());
                    System.out.println("Position: " + id);
                    System.out.println("Title: " + title);
                    System.out.println("Author: " + author);
                    System.out.println("Pages: " + pages);
                    System.out.println("Type: " + type);
                    System.out.printf("Cost: %,.2f %s \n\n", Float.parseFloat(cost), currency);
                }
            }
            System.out.println("Total: " + totalPage + " pages");
            System.out.println("Total books 'Science lit.' : " + type1 + " шт.");
            System.out.println("Total books 'Art lit.' : " + type2 + " шт.");
        } catch (SAXException | IOException e) {                                                                        //catch an exception
            e.printStackTrace();
        }
                                                                                                                        //write new XML
        DocumentBuilderFactory xml = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = xml.newDocumentBuilder();
        Document task = docBuilder.newDocument();
        Element rootElement = task.createElement("books");                                                      //create root element
        task.appendChild(rootElement);

        Element totalpages = task.createElement("pages");                                                       //create element "pages"
        totalpages.setTextContent("Total pages");
        String totalP = Integer.toString(totalPage);                                                                    //convert to String
        totalpages.setAttribute("quantity", totalP);                                                              //set name and value of attribute
        rootElement.appendChild(totalpages);                                                                            //add child element

        Element typeS = task.createElement("type");
        typeS.setTextContent("Science lit.");
        String type01 = Integer.toString(type1);
        typeS.setAttribute("quantity", type01);
        rootElement.appendChild(typeS);

        Element typeA = task.createElement("type");
        typeA.setTextContent("Art lit.");
        String type02 = Integer.toString(type2);
        typeA.setAttribute("quantity", type02);
        rootElement.appendChild(typeA);
                                                                                                                        //TODO  add files for output
      try (FileOutputStream output = new FileOutputStream("G:\\ПРОГА\\IdeaProjects\\test\\books_test.xml")) {     //open output flow,write document "task" to file
            writeXml(task, output);
          output.close();                                                                                               //close flow
        }
      catch (IOException e) {                                                                                           //catch an exception
            System.out.println("Error writing file");
        }
    }

    private static void writeXml(Document task, OutputStream output)
            throws TransformerException {

        TransformerFactory transformerFactory = TransformerFactory.newInstance();                                       //create transformer, converting
        Transformer transformer = transformerFactory.newTransformer();

        transformer.setOutputProperty(OutputKeys.INDENT, "yes");                                                   //pretty print
        DOMSource source = new DOMSource(task);
        StreamResult result = new StreamResult(output);
        transformer.transform(source, result);
    }
}





