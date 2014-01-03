package com.mebigfatguy.ds;

import java.awt.Window;
import java.io.BufferedInputStream;
import java.io.InputStream;

import org.xml.sax.InputSource;

import javax.swing.RootPaneContainer;
import javax.xml.XMLConstants;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

public class DSFactory {

    private static SAXParserFactory SPF;
    private static Schema SCHEMA;
    
    static {
        try {
            SPF = SAXParserFactory.newInstance();
            try (InputStream xsdIs = new BufferedInputStream(DSFactory.class.getResourceAsStream("/com/mebigfatguy/ds/ds.xsd"))) {
                SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
                SAXSource source = new SAXSource(new InputSource(xsdIs));
                SCHEMA = schemaFactory.newSchema(source);
                SPF.setSchema(SCHEMA);
                SPF.setValidating(true);
                //SPF.setNamespaceAware(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            throw new NoClassDefFoundError("Failed to initialize DSFactory's sax parser");
        }
    }
    
    public static <T extends RootPaneContainer> T getView(String name, DSLocalizer localizer) throws DSException {
        
        DSErrorHandler eh = null;
        try (BufferedInputStream bis = new BufferedInputStream(DSFactory.class.getResourceAsStream(name))) {
            Validator validator = SCHEMA.newValidator();
            eh = new DSErrorHandler();
            validator.setErrorHandler(eh);
            DSContentHandler<T> ch = new DSContentHandler<T>(localizer);
            validator.validate(new SAXSource(new InputSource(bis)), new SAXResult(ch));
            
            if (eh.hasErrors()) {
                throw new DSException(String.format("Failed to fetch view: %s with info%n%s", name, eh));
            }
            return pack(ch.getView());
        } catch (Exception e) {
            throw new DSException(String.format("Failed to fetch view: %s with info%n%s", name, eh), e);
        }
    }
    
    private static <T extends RootPaneContainer> T pack(T t) {
        if (t instanceof Window) {
            ((Window) t).pack();
        }
        
        return t;
    }
}
