package com.mebigfatguy.ds;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

import com.mebigfatguy.ds.service.DSHandlerProvider;

public class DSResourceResolver implements LSResourceResolver {
	private static final String HANDLER_RESOURCE = "META-INF/Services/com.mebigfatguy.ds.service.DSHandlerProvider";

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
