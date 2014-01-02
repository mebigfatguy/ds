package com.mebigfatguy.ds;

import java.io.BufferedInputStream;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import javax.swing.RootPaneContainer;
import javax.xml.XMLConstants;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

public class DSFactory {

    private static SAXParserFactory SPF;
    
    static {
        try {
            SPF = SAXParserFactory.newInstance();
            Schema schema = null;
            try {
                SAXSource source = new SAXSource(new InputSource(DSFactory.class.getResourceAsStream("/com/mebigfatguy/ds/ds.xsd")));
                SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
                schema = schemaFactory.newSchema(source);
                SPF.setValidating(true);
                SPF.setNamespaceAware(true);
                SPF.setSchema(schema);
            } catch (Exception e) {
            }
        } catch (Exception e) {
            throw new NoClassDefFoundError("Failed to initialize DSFactory's sax parser");
        }
    }
    
    public static <T extends RootPaneContainer> T getView(String name, DSTypeReference<T> typeRef, DSLocalizer localizer) throws DSException {
        
        DSErrorHandler eh = null;
        try (BufferedInputStream bis = new BufferedInputStream(DSFactory.class.getResourceAsStream(name))) {
            
            SAXParser saxParser = SPF.newSAXParser();
            XMLReader r = saxParser.getXMLReader();

            DSContentHandler<T> ch = new DSContentHandler<T>(typeRef, localizer);
            r.setContentHandler(ch);
            eh = new DSErrorHandler();
            r.setErrorHandler(eh);
            r.parse(new InputSource(bis));
            
            return ch.getView();
            
        } catch (Exception e) {
            throw new DSException(String.format("Failed to fetch view: %s with info%n%s", name, eh), e);
        }
    }
}
