package utilidades;

import entidades.Producto;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UtilidadesXML {
    /**
     * Cabecera: public static void exportarXML(String nombreFichero, ArrayList<Producto> productos)
     *
     * Descripcion: Este metodo se encarga de exportar los xml con el nombre dle fichero pasado por parametros
     * y una lista de productos.
     * @param nombreFichero String
     * @param productos ArrayList<Producto>
     */
    public static void exportarXML(String nombreFichero, ArrayList<Producto> productos){
        transformer(documentBuilder(productos),nombreFichero);
    }
    /**
     * Cabecera: private static Document documentBuilder(ArrayList<Producto> productos)
     *
     * Descripcion: Este metodo se encarga de generar el doc y de llamar al metodo para generar las etiquetas.
     *
     * Precondciiones: Ninguna
     * Postcondiciones: Devuelve el documento creado
     * @param productos ArrayList<Productos>
     * @return Document doc
     */
    private static Document documentBuilder(ArrayList<Producto> productos){
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;

        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document doc = db.newDocument();
        doc.setXmlVersion("1.0");
        Element elProductos = doc.createElement("productos");
        doc.appendChild(elProductos);
        return generarEtiquetas(doc,elProductos,productos);
    }

    /**
     * Cabecera: private static Document generarEtiquetas(Document doc,Element elProductos,ArrayList<Producto> productos)
     *
     * Descripcion: Este metodo se encarga de generar las eqtiquetas en el Document pasado por parametros.
     *
     * Precondiciones: Ninguno
     *
     * Postcondiciones: Generas las etiquetas
     * @param doc Document
     * @param elProductos Element
     * @param productos ArrayList<Producto>
     * @return Document doc
     */
    private static Document generarEtiquetas(Document doc,Element elProductos,ArrayList<Producto> productos){
        for (Producto p : productos) {
            Element elProducto = doc.createElement("producto");
            elProducto.setAttribute("id",String.valueOf(p.getId()));
            Element elDesc = doc.createElement("descripcion");
            elDesc.appendChild(doc.createTextNode(p.getDescripcion()));
            elProducto.appendChild(elDesc);
            Element elStock = doc.createElement("stock");
            elStock.appendChild(doc.createTextNode(String.valueOf(p.getStock())));
            elProducto.appendChild(elStock);
            Element elPrecio = doc.createElement("precio");
            elPrecio.appendChild(doc.createTextNode(String.valueOf(p.getPrecio())));
            elProducto.appendChild(elPrecio);
            elProductos.appendChild(elProducto);
        }
        return doc;
    }

    /**
     * Cabecera: private static void transformer(Document doc, String nombreFichero)
     *
     * Descripcion: Este metodo se encarga de realizar el transformer con el document y el nombre del fichero.
     *
     * Precondiciones: Ninguna
     * Postcondiciones: Transforma el document pasado a XML
     *
     * @param doc Document
     * @param nombreFichero String
     */
    private static void transformer(Document doc, String nombreFichero){
        try {
            DOMSource domSource = new DOMSource(doc);
            Transformer transformer = TransformerFactory.newInstance().
                    newTransformer();
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(
                    "{http://xml.apache.org/xslt}indent-amount", "4");
            StreamResult sr = new StreamResult(nombreFichero);
            transformer.transform(domSource, sr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Cabecera: public static void imprimirXMLSAX(String nombreFichero)
     *
     * Descripcion: Este metodo se encarga de imprimir por consola el contenido de un xml utilizado SAX.
     * Para ello utilizamos la clase HnadlerSAX para mostrarlo en forma de tabla.
     *
     * Precondiciones: Que el fichero exista y tenga contenidos
     *
     * Postcondiciones: Muestra por consola el contenido del xml
     * @param nombreFichero String
     */
    public static void imprimirXMLSAX(String nombreFichero) {
        HandlerSAX gestor;
        XMLReader procesadorXML;
        InputSource archivoXML;
        try {
            SAXParserFactory parserFactory = SAXParserFactory.newInstance();
            SAXParser parser = parserFactory.newSAXParser();
            procesadorXML = parser.getXMLReader();
            gestor = new HandlerSAX();
            procesadorXML.setContentHandler(gestor);
            archivoXML = new InputSource(nombreFichero);
            procesadorXML.parse(archivoXML);
        } catch (SAXException | ParserConfigurationException ex) {
            Logger.getLogger(UtilidadesXML.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
