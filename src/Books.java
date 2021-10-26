//package
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

        DocumentBuilderFactory books = DocumentBuilderFactory.newInstance();  //созд. построителя
        DocumentBuilder b = books.newDocumentBuilder();

        int totalPage = 0;
        int type1 = 0;
        int type2 = 0;
        try {
            Document doc = b.parse("books.xml");         //парсинг файла, получение стр-ры документа из файла XML
            doc.getDocumentElement().normalize();    //опция
            Element eroot = doc.getDocumentElement();   // получение  root-элемента XML-файла
            System.out.println("Root Element: " + eroot.getNodeName());
            NodeList list = eroot.getChildNodes();  // просмтриваем подэлементы root-элемента
            for (int i = 0; i < list.getLength(); i++) {
                Node book = list.item(i);             //проверяем все элементы
                if (book.getNodeType() == Node.ELEMENT_NODE) {    // если тип ноды  - элемент, то получаем элементы
                    Element element = (Element) book;    //
                    String id = element.getAttribute("id");    // получаем аттрибут id
                    String title = element.getElementsByTagName("Title").item(0).getTextContent();
                    String author = element.getElementsByTagName("Author").item(0).getTextContent();
                    String pages = element.getElementsByTagName("Pages").item(0).getTextContent();
                    String type = element.getElementsByTagName("Type").item(0).getTextContent();
                    totalPage += Integer.valueOf(pages); // подсчет общего кол-ва страниц, перевод в Integer

                    NodeList costlist = element.getElementsByTagName("Cost");
                    String cost = costlist.item(0).getTextContent();

                    String currency = costlist.item(0).getAttributes().getNamedItem("currency").getTextContent();

                    if (element.getElementsByTagName("Type").item(0).getTextContent().equals("Науч.лит.")) //сравнение последовательности символов в строке
                        type1 += 1;
                    else if (element.getElementsByTagName("Type").item(0).getTextContent().equals("Худ.лит."))
                        type2 += 1;
                    else
                        System.out.println("Книга неизвеcтного Type");

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
            System.out.println("Total books 'Науч. лит.' : " + type1 + " шт.");
            System.out.println("Total books 'Худ.лит.' : " + type2 + " шт.");
        } catch (SAXException | IOException e) {
            e.printStackTrace();
        }
//запись
        DocumentBuilderFactory xml = DocumentBuilderFactory.newInstance();  //созд. построителя
        DocumentBuilder docBuilder = xml.newDocumentBuilder();
        Document task = docBuilder.newDocument();
// сюда записать файлы.....
        Element rootElement = task.createElement("Books");
        task.appendChild(rootElement);

        Element type = task.createElement("Type");
        type.setTextContent("Науч.лит.");
        String type01 = Integer.toString(type1); // преобразование int в String
        type.setAttribute("quantity", type01);
        rootElement.appendChild(type);






// сюда записать файлы.....











      try (FileOutputStream output = new FileOutputStream("G:\\ПРОГА\\IdeaProjects\\test\\books_test.xml")) {
            writeXml(task, output); // output или System.out -  тогда в консоль запись
          output.close();
        }
      catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeXml(Document task, OutputStream output)
            throws TransformerException {

        TransformerFactory transformerFactory = TransformerFactory.newInstance();   //созд. трансформер
        Transformer transformer = transformerFactory.newTransformer();

        transformer.setOutputProperty(OutputKeys.INDENT, "yes");  //красивая печать
        DOMSource source = new DOMSource(task);
        StreamResult result = new StreamResult(output);

        transformer.transform(source, result);
    }
}





