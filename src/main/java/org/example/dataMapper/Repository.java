package org.example.dataMapper;

import org.example.dataMapper.utils.GetterOfFiled;
import org.example.dataMapper.utils.SetterOfField;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Repository {
    public static <T> List<T> load(Class<T> clazz, String path) {
        List<T> listOfObjects = new ArrayList<>();
        try {
            File file = new File(path);
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);
            Element root = document.getDocumentElement();

            NodeList childNodes = root.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node node = childNodes.item(i);

                if (node.getNodeType() == 1) {
                    List<Field> declaredFieldsOfModel = getDeclaredFieldsOfModel(clazz);
                    addElementToList((Element)node,declaredFieldsOfModel, listOfObjects, clazz);
                }
            }
            return listOfObjects;
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> void addElementToList(Element element, List<Field> fields, List<T> listOfObjects, Class<T> clazz) {
        try {
            T instance = clazz.newInstance();
            fields.forEach(field -> {
                Element item = (Element) element.getElementsByTagName(field.getName()).item(0);
                SetterOfField.setAttribute(field, instance, item.getTextContent());
            });
            listOfObjects.add(instance);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> void save(List<T> data, String path) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            System.out.println(data.get(0).getClass().getName() + "s");
            Element racine = document.createElement(data.get(0).getClass().getSimpleName() + "s");
            document.appendChild(racine);
            List<Field> declaredFieldsOfModel = getDeclaredFieldsOfModel(data.get(0).getClass());

            data.forEach(e -> {
                Element element = document.createElement(data.get(0).getClass().getSimpleName());
                racine.appendChild(element);
                addObjectToXml(element,e, declaredFieldsOfModel, document);
            });

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(path));
            transformer.transform(source, result);
        } catch (ParserConfigurationException | TransformerException e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> void addObjectToXml(Element root, T t, List<Field> fields, Document document) {
        fields.forEach(field -> {
            Element element = document.createElement(field.getName());
            element.setTextContent((String) GetterOfFiled.getAttributeValue(field, t)+"");
            root.appendChild(element);
        });
    }

    private static List<Field> getDeclaredFieldsOfModel(Class model) {
        Field[] fields = model.getDeclaredFields();
        return Arrays.asList(fields);
    }
}
