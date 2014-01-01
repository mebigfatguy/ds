package com.mebigfatguy.ds;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class DSErrorHandler implements ErrorHandler {

    StringBuilder error = new StringBuilder();
    
    @Override
    public void warning(SAXParseException exception) throws SAXException {
        error.append(exception.getMessage()).append("\n");
    }

    @Override
    public void error(SAXParseException exception) throws SAXException {
        error.append(exception.getMessage()).append("\n");
    }

    @Override
    public void fatalError(SAXParseException exception) throws SAXException {
        error.append(exception.getMessage()).append("\n");
    }
    
    @Override
    public String toString() {
        return error.toString();
    }

}
