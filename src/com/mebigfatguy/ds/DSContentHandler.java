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

import javax.swing.JScrollPane;
import javax.swing.RootPaneContainer;

public class DSContentHandler<T extends RootPaneContainer> extends DefaultHandler {

    private static final String CONTAINER = "container";
    private static final String CONTROL = "control";
    private static final String LAYOUT = "layout";
    private static final String PREFERRED_SIZE = "preferredSize";
    
    private static final String ATTR_NAME = "name";
    private static final String ATTR_TEXT = "text";
    private static final String ATTR_TYPE = "type";
    private static final String ATTR_POSITION = "position";
    private static final String ATTR_WIDTH = "width";
    private static final String ATTR_HEIGHT = "height";
    private static final String ATTR_SELECTED = "selected";
    
    private DSLocalizer localizer;
    private Component topComponent;
    private List<Container> containerStack = new ArrayList<>();
    private Component activeComponent = null;
    
    public DSContentHandler(DSLocalizer l10n) {
        localizer = l10n;
    }
    
    public T getView() {
        return (T)topComponent;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        try {
            switch (qName) {
                case CONTAINER:
                case CONTROL: {
                    String type = attributes.getValue(ATTR_TYPE);
                    Class<?> cls = Class.forName(type);
                    activeComponent = (Component) cls.newInstance();
                    
                    String name = attributes.getValue(ATTR_NAME);
                    if (name != null) {
                        setName(activeComponent, name);
                    }
                    
                    String text = attributes.getValue(ATTR_TEXT);
                    if (text != null) {
                        setText(activeComponent, localizer.getLocalString(text));
                    }
                    
                    String selected = attributes.getValue(ATTR_SELECTED);
                    if (selected != null) {
                        try {
                            Method m = activeComponent.getClass().getMethod("setSelected", boolean.class);
                            m.invoke(activeComponent, Boolean.parseBoolean(selected));
                        } catch (Exception e) {
                        }
                    }
                    
                    Container parent = null;
                    if (!containerStack.isEmpty()) {
                        parent = containerStack.get(containerStack.size() - 1);
                    }
        
                    if (qName.equals(CONTAINER)) {
                        containerStack.add((Container) activeComponent);
                    }
                    
                    if (parent != null) {
                        addChild(parent, activeComponent, attributes.getValue(ATTR_POSITION));
                    }
                }
                break;
                
                case LAYOUT: {
                    String type = attributes.getValue(ATTR_TYPE);
                    if (type != null) {
                        Class<?> cls = Class.forName(type);
                        ((Container) activeComponent).setLayout((LayoutManager) cls.newInstance());
                    }
                }
                break;
                
                case PREFERRED_SIZE: {
                    activeComponent.setPreferredSize(new Dimension(Integer.parseInt(attributes.getValue(ATTR_WIDTH)), Integer.parseInt(attributes.getValue(ATTR_HEIGHT))));
                }
                break;
            }
        } catch (Exception e) {
            throw new SAXException(String.format("Failure to build component: %s with attributes: %s", qName, attributesToString(attributes))); 
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equals(CONTAINER)) {
            topComponent = containerStack.remove(containerStack.size() - 1);
            activeComponent = topComponent;
        } else if (qName.equals(CONTROL)) {
            activeComponent = containerStack.get(containerStack.size() - 1);
        }
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
            if (parent instanceof JScrollPane)
                ((JScrollPane) parent).setViewportView(child);
            else
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
    
    private void setText(Component c, String text) {
        try {
            Method m = c.getClass().getMethod("setText", String.class);
            if (m != null) {
                m.invoke(c,  text);
            }
        } catch (Exception e1) {
            try {
                Method m = c.getClass().getMethod("setTitle", String.class);
                if (m != null) {
                    m.invoke(c,  text);
                }
            } catch (Exception e2) {
            }
        }
    }
    
    private String attributesToString(Attributes attributes) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        String comma = "";
        for (int i = 0; i < attributes.getLength(); i++) {
            sb.append(comma);
            comma = ",";
            sb.append(attributes.getQName(i)).append("=").append(attributes.getValue(i));
        }
        sb.append("}");
        
        return sb.toString();
    }
}
