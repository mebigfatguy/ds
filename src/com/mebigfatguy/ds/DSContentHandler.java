package com.mebigfatguy.ds;

import java.awt.Component;
import java.awt.Container;
import java.awt.LayoutManager;
import java.lang.reflect.Method;
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
    private static final String ATTR_LAYOUT = "layout";
    
    private DSLocalizer localizer;
    private Component topComponent;
    private List<Container> containerStack = new ArrayList<>();
    
    public DSContentHandler(DSLocalizer l10n) {
        localizer = l10n;
    }
    
    public T getView() {
        return (T)topComponent;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        Component c;
        try {
            String type = attributes.getValue(ATTR_TYPE);
            Class<?> cls = Class.forName(type);
            c = (Component) cls.newInstance();
            
            String name = attributes.getValue(ATTR_NAME);
            if (name != null) {
                Method m = c.getClass().getMethod("setTitle", String.class);
                if (m != null) {
                    m.invoke(c, name);
                } else {
                    m = c.getClass().getMethod("setName", String.class);
                    if (m != null) {
                        m.invoke(c,  name);
                    }
                }
            }

            if (qName.equals(CONTAINER)) {
                String layout = attributes.getValue(ATTR_LAYOUT);
                cls = Class.forName(layout);
                ((Container) c).setLayout((LayoutManager) cls.newInstance());
                
                containerStack.add((Container) c);
            }
        } catch (Exception e) {
            //what to do?
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equals(CONTAINER)) {
            topComponent = containerStack.remove(containerStack.size() - 1);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
    }
}
