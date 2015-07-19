/*
 * ds - a declarative swing library
 * Copyright 2014-2015 MeBigFatGuy.com
 * Copyright 2014-2015 Dave Brosius
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations
 * under the License.
 */
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

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.RootPaneContainer;
import javax.swing.border.Border;

public class DSContentHandler<T extends RootPaneContainer> extends DefaultHandler {

    
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
        } catch (Exception e) {
            throw new SAXException(String.format("Failure to build component: %s with attributes: %s", qName, attributesToString(attributes)), e); 
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
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
