package com.MobShop.app;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlSerializer;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by Segarceanu Calin on 3/9/14.
 */
public class Cart{

    private Context context;
    private String filename = "shoppingcart.txt";
    private ArrayList<Product> xmlDataList;

    // XML node keys
    static final String KEY_PRODUCT = "product"; // parent node
    static final String KEY_NAME = "name";
    static final String KEY_ID = "id";
    static final String KEY_PRICE = "price";
    static final String KEY_QUANTITY = "quantity";
    static final String KEY_DESC = "description";
    static final String KEY_PHOTOURL = "photourl";

    Cart(Context ctxt){
        this.context = ctxt;
    }

    public void addProduct(Product product) throws IOException{

        FileOutputStream fos;

        fos = context.openFileOutput(filename, Context.MODE_APPEND);


        XmlSerializer serializer = Xml.newSerializer();
        serializer.setOutput(fos, "UTF-8");
        serializer.startDocument(null, Boolean.valueOf(true));
        serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
        Log.d("URL", "eixta1" + this.fileExistance(filename));
        serializer.startTag("", "cart");

        serializer.startTag("", "product");

        serializer.startTag("", "name");

            serializer.text(product.getProductName());

        serializer.endTag("", "name");

        serializer.startTag("", "id");

            serializer.text(product.getProductId().toString());

        serializer.endTag("", "id");

        serializer.startTag("", "description");

           serializer.text(product.getDescription());

        serializer.endTag("", "description");

        serializer.startTag("", "quantity");

            serializer.text(product.getProductQuantity().toString());

        serializer.endTag("", "quantity");

        serializer.startTag("", "price");

            serializer.text(String.valueOf(product.getPrice() * product.getProductQuantity()));

        serializer.endTag("", "price");


        serializer.endTag("", "product");

        serializer.endDocument();
        Log.d("URL", serializer.toString());
        serializer.flush();

        fos.close();
    }



    public ArrayList<Product> getData() throws IOException, SAXException {
        FileInputStream fis = null;
        InputStreamReader isr = null;

        fis = context.openFileInput(filename);
        isr = new InputStreamReader(fis);
        char[] inputBuffer = new char[fis.available()];
        isr.read(inputBuffer);
        String data = new String(inputBuffer);
        isr.close();
        fis.close();
        Log.d("URL", "eixta2" + this.fileExistance(filename));
        /*
        * converting the String data to XML format
        * so that the DOM parser understand it as an XML input.
        */
        InputStream is = new ByteArrayInputStream(data.getBytes("UTF-8"));
        XMLDOMParser parser = new XMLDOMParser();
        Document doc = parser.getDocument(is);
        xmlDataList = new ArrayList<Product>();
        try{
            NodeList nodeList = doc.getElementsByTagName(KEY_PRODUCT);
            Log.d("URL", " " + nodeList.getLength());
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element e = (Element) nodeList.item(i);
                String name = parser.getValue(e, KEY_NAME);
                String id = parser.getValue(e, KEY_ID);
                String description = parser.getValue(e, KEY_DESC);
                String price = parser.getValue(e, KEY_PRICE);
                String quantity = parser.getValue(e, KEY_QUANTITY);

                Product prod = new Product(Integer.valueOf(id), name);
                prod.setPrice(Double.parseDouble(price));
                prod.setDescription(description);
                prod.setQuantity(Integer.valueOf(quantity));
                xmlDataList.add(prod);
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        return xmlDataList;

    }

    public Integer getNumberOfProducts(){
        return xmlDataList.size();
    }

    public void deleteProduct(int i) throws IOException, TransformerException {
        FileInputStream fis = null;
        FileOutputStream fis2 = null;
        InputStreamReader isr = null;
        OutputStreamWriter outr = null;

        fis = context.openFileInput(filename);
        isr = new InputStreamReader(fis);
        char[] inputBuffer = new char[fis.available()];
        isr.read(inputBuffer);
        String data = new String(inputBuffer);
        isr.close();
        fis.close();

        /*
        * converting the String data to XML format
        * so that the DOM parser understand it as an XML input.
        */
        InputStream is = new ByteArrayInputStream(data.getBytes("UTF-8"));
        XMLDOMParser parser = new XMLDOMParser();
        Document doc = parser.getDocument(is);
        NodeList nodeList = doc.getElementsByTagName(KEY_PRODUCT);
        Element e = (Element) nodeList.item(i);
        String name = parser.getValue(e, KEY_NAME);
        Log.d("URL", name);
        Node node = e.getParentNode();
        node.removeChild(e);
        doc.normalize();

        Transformer tf = TransformerFactory.newInstance().newTransformer();
        tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        context.deleteFile(filename);
        fis2 = context.openFileOutput(filename, Context.MODE_APPEND);
        outr= new OutputStreamWriter(fis2);
        tf.transform(new DOMSource(doc), new StreamResult(outr));


    }

    public boolean fileExistance(String fname){
        File file = context.getFileStreamPath(fname);
        return file.exists();
    }

}
