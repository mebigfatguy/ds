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

	public DSHandlerProvider getProvider(String xsdSchema) {
		try {
			Class<? extends DSHandlerProvider> cls = handlers.get(xsdSchema);
			if (cls != null) {
				return cls.newInstance();
			}
			return null;
		} catch (IllegalAccessException | InstantiationException e) {
			return null;
		}
	}

	private static void loadHandlers() throws IOException {
		handlers = new HashMap<>();
		Enumeration<URL> e = Thread.currentThread().getContextClassLoader().getResources(HANDLER_RESOURCE);
		Properties p = new Properties();

		while (e.hasMoreElements()) {
			URL u = e.nextElement();
			try (InputStream is = new BufferedInputStream(u.openStream())) {
				p.load(is);
				for (Map.Entry<?, ?> entry : p.entrySet()) {
					Class<? extends DSHandlerProvider> handler = (Class<? extends DSHandlerProvider>) Class.forName(entry.getValue().toString().trim());
					handlers.put((String) entry.getKey(), handler);
				}
			} catch (ClassNotFoundException cnfe) {

			} finally {
				p.clear();
			}
		}
	}
}
