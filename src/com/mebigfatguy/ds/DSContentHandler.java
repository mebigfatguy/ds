package com.mebigfatguy.ds;

import java.awt.Container;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.swing.RootPaneContainer;

public class DSContentHandler<T extends RootPaneContainer> extends DefaultHandler {

    private static final String CONTAINER = "container";
    private static final String CONTROL = "control";
    
    private static final String ATTR_NAME = "name";
    private static final String ATTR_TYPE = "type";
    
    private DSLocalizer localizer;
    private RootPaneContainer window;
    private List<Container> containerStack = new ArrayList<>();
    
    public DSContentHandler(DSTypeReference<T> typeRef, DSLocalizer l10n) throws DSException {
        try {
            Type superclass = typeRef.getClass().getGenericSuperclass();
            Type windowClass = ((ParameterizedType) superclass).getActualTypeArguments()[0];
                    
            Class<?> rawType = windowClass instanceof Class<?> ? (Class<?>) windowClass : (Class<?>) ((ParameterizedType) windowClass).getRawType();
            window = (RootPaneContainer) rawType.getConstructor().newInstance();

            localizer = l10n;
        } catch (Exception e) {
            throw new DSException("Failed initializing the root pane component", e);
        }
    }
    
    public T getView() {
        return (T)window;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        Container c;
        try {
            if (localName.equals(CONTAINER)) {
                if (containerStack.isEmpty()) {
                    c = (Container) window;
                } else {
                    String type = attributes.getValue(ATTR_TYPE);
                    Class<?> cls = Class.forName(type);
                    c = (Container) cls.newInstance(); 
                }
                containerStack.add(c);
            } else if (localName.equals(CONTROL)) {
                
            }
            
        } catch (Exception e) {
            //what to do?
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (localName.equals(CONTAINER)) {
            containerStack.remove(containerStack.size() - 1);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
    }
}
