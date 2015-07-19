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

import java.awt.Window;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.swing.RootPaneContainer;
import javax.xml.XMLConstants;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.InputSource;

import com.mebigfatguy.ds.service.DSHandlerProvider;

public class DSFactory {

	private static final String HANDLER_RESOURCE = "META-INF/Services/com.mebigfatguy.ds.service.DSHandlerProvider";
	
    private static SAXParserFactory SPF;
    private static Schema SCHEMA;
    
    static {
        try {
            SPF = SAXParserFactory.newInstance();
            try {
                SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
                SCHEMA = schemaFactory.newSchema();
                SPF.setValidating(true);
                SPF.setNamespaceAware(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            NoClassDefFoundError er = new NoClassDefFoundError("Failed to initialize DSFactory's sax parser");
            er.initCause(e);
            throw er;
        }
    }
    
    public static <T extends RootPaneContainer> T getView(String name, DSLocalizer localizer) throws DSException {
        
        DSErrorHandler eh = null;
        try (BufferedInputStream bis = new BufferedInputStream(DSFactory.class.getResourceAsStream(name))) {
            Validator validator = SCHEMA.newValidator();
            validator.setResourceResolver(new DSResourceResolver());
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
    
    static class DSResourceResolver implements LSResourceResolver {
    	private static Map<String, Class<? extends DSHandlerProvider>> handlers;
    	
    	static {
    		try {
    			loadHandlers();
    		} catch (IOException ioe) {	
    		}
    	}
    	
		@Override
		public LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId, String baseURI) {
			try {
				Class<? extends DSHandlerProvider> cls = handlers.get(namespaceURI);
				if (cls != null) {
					DSHandlerProvider p = cls.newInstance();
					URL u = p.getSchema();
					return new DSLSInput(u, systemId, publicId, baseURI);
				}
				return null;
			} catch (IllegalAccessException | InstantiationException e) {
				return null;
			}
		}
		
		private static void loadHandlers() throws IOException {
			handlers = new HashMap<>();
			Enumeration<URL> e = Thread.currentThread().getContextClassLoader().getResources(HANDLER_RESOURCE);
			while (e.hasMoreElements()) {
				URL u = e.nextElement();
				try (InputStream is = new BufferedInputStream(u.openStream())) {
					Properties p = new Properties();
					p.load(is);
					for (Map.Entry<?, ?> entry : p.entrySet()) {
						Class<? extends DSHandlerProvider> handler = (Class<? extends DSHandlerProvider>) Class.forName(entry.getValue().toString());
						handlers.put(entry.getKey().toString(), handler);
					}
				} catch (ClassNotFoundException cnfe) {
					
				}
			}
		}
    }
}
