package com.mebigfatguy.ds;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.lang.reflect.Field;
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
    private static final String PREFERRED_SIZE = "preferredSize";
    
    private static final String ATTR_NAME = "name";
    private static final String ATTR_TITLE = "title";
    private static final String ATTR_TYPE = "type";
    private static final String ATTR_LAYOUT = "layout";
    private static final String ATTR_POSITION = "position";
    
    private DSLocalizer localizer;
    private Component topComponent;
    private List<Container> containerStack = new ArrayList<>();
    private Component activeComponent = null;
    private StringBuilder pcData = new StringBuilder();
    
    public DSContentHandler(DSLocalizer l10n) {
        localizer = l10n;
    }
    
    public T getView() {
        return (T)topComponent;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        try {
            if (qName.equals(CONTAINER) || qName.equals(CONTROL)) {
                String type = attributes.getValue(ATTR_TYPE);
                Class<?> cls = Class.forName(type);
                activeComponent = (Component) cls.newInstance();
                
                String name = attributes.getValue(ATTR_NAME);
                if (name != null) {
                    setName(activeComponent, name);
                }
                
                String title = attributes.getValue(ATTR_TITLE);
                if (title != null) {
                    setTitle(activeComponent, localizer.getLocalString(title));
                }
                
                Container parent = null;
                if (!containerStack.isEmpty()) {
                    parent = containerStack.get(containerStack.size() - 1);
                }
    
                if (qName.equals(CONTAINER)) {
                    String layout = attributes.getValue(ATTR_LAYOUT);
                    if (layout != null) {
                        cls = Class.forName(layout);
                        ((Container) activeComponent).setLayout((LayoutManager) cls.newInstance());
                    }
                    containerStack.add((Container) activeComponent);
                }
                
                if (parent != null) {
                    addChild(parent, activeComponent, attributes.getValue(ATTR_POSITION));
                }
            } else {
                pcData.setLength(0);
            }
        } catch (Exception e) {
            //what to do?
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equals(CONTAINER)) {
            topComponent = containerStack.remove(containerStack.size() - 1);
            activeComponent = topComponent;
        } else if (qName.equals(CONTROL)) {
            activeComponent = containerStack.get(containerStack.size() - 1);
        } else {
            if (qName.equals(PREFERRED_SIZE)) {
                String[] sizes = pcData.toString().split(",");
                Dimension d = new Dimension(Integer.parseInt(sizes[0]), Integer.parseInt(sizes[1]));
                activeComponent.setPreferredSize(d);
            }
            pcData.setLength(0);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        pcData.append(ch, start, length);
    }
    
    private void addChild(Container parent, Component child, String position) {
        Object positionObject = null;
        
        if (position != null) {
            int dotPos = position.lastIndexOf('.');
            if (dotPos >= 0) {
                try {
                    String clsName = position.substring(0, dotPos);
                    String fieldName = position.substring(dotPos + 1);
                    Class<?> cls = Class.forName(clsName);
                    Field f = cls.getField(fieldName);
                    positionObject = f.get(null);
                } catch (Exception e) {
                }
            }
        }
        
        if (positionObject != null) {
            if (positionObject instanceof String)
                parent.add(child, positionObject);
            else if (positionObject instanceof Integer)
                parent.add(child, ((Integer) positionObject).intValue());
        } else {
            parent.add(child);
        }
    }
    
    private void setName(Component c, String name) {
        try {
            Method m = c.getClass().getMethod("setName", String.class);
            if (m != null) {
                m.invoke(c,  name);
            }
        } catch (Exception e) {
        }
    }
    
    private void setTitle(Component c, String title) {
        try {
            Method m = c.getClass().getMethod("setText", String.class);
            if (m != null) {
                m.invoke(c,  title);
            }
        } catch (Exception e1) {
            try {
                Method m = c.getClass().getMethod("setTitle", String.class);
                if (m != null) {
                    m.invoke(c,  title);
                }
            } catch (Exception e2) {
            }
        }
    }
}
