/*
 * ds - a declarative swing library
 * Copyright 2014-2016 MeBigFatGuy.com
 * Copyright 2014-2016 Dave Brosius
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
import java.util.ArrayList;
import java.util.List;

import javax.swing.RootPaneContainer;
import javax.xml.XMLConstants;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.NamespaceSupport;

import com.mebigfatguy.ds.service.DSHandlerProvider;

public class DSContentHandler<T extends RootPaneContainer> extends DefaultHandler {

	private NamespaceSupport nsSupport;

	private DSLocalizer localizer;
	private List<DSHandlerProvider> providerStack = new ArrayList<>();
	private List<Component> componentStack = new ArrayList<>();
	private StringBuilder contents = new StringBuilder();

	public DSContentHandler(DSLocalizer l10n) {
		localizer = l10n;
	}

	public T getView() {
		return (T) getTopComponent();
	}

	@Override
	public void startDocument() {
		nsSupport = new NamespaceSupport();
	}

	@Override
	public void startPrefixMapping(String prefix, String uri) throws SAXException {
		nsSupport.pushContext();
		nsSupport.declarePrefix(prefix, uri);
	}

	@Override
	public void endPrefixMapping(String prefix) throws SAXException {
		nsSupport.popContext();
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		try {
			DSHandlerProvider provider = getTopProvider();
			String subTypeURI = getSubTypeURI(attributes.getValue(XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI, DSFactory.XSI_TYPE));
			String actualURI = subTypeURI != null ? subTypeURI : uri;

			if (provider == null || !provider.getXSDNamespace().equals(actualURI)) {
				provider = DSFactory.getProvider(actualURI);
				providerStack.add(provider);
			} else {
				providerStack.add(provider);
			}
			provider.startComponent(actualURI, localName, qName, attributes, getTopComponent());
			Component c = provider.getComponent();
			if (c != null) {
				componentStack.add(c);
			}

			contents.setLength(0);
		} catch (Exception e) {
			throw new SAXException(String.format("Failure to build component: %s with attributes: %s", qName, attributesToString(attributes)), e);
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		DSHandlerProvider provider = getTopProvider();
		provider.content(contents.toString());
		provider.endComponent(uri, localName, qName, getTopComponent());
		providerStack.remove(provider);
		Component childComponent = provider.getComponent();

		if (childComponent != null) {
			provider = getTopProvider();
			if (provider != null) {
				provider.endChildComponent(uri, localName, qName, childComponent);
			}
		}
	}

	@Override
	public void characters(char[] chars, int start, int length) throws SAXException {
		contents.append(chars, start, length);
	}

	private Component getTopComponent() {
		if (componentStack.isEmpty()) {
			return null;
		}

		return componentStack.get(componentStack.size() - 1);
	}

	private DSHandlerProvider getTopProvider() {
		if (providerStack.isEmpty()) {
			return null;
		}

		return providerStack.get(providerStack.size() - 1);
	}

	private String getSubTypeURI(String qName) {
		if (qName == null) {
			return null;
		}

		String[] parts = qName.split(":");
		if (parts.length < 2) {
			return null;
		}

		return nsSupport.getURI(parts[0]);
	}

	private String attributesToString(Attributes attributes) {
		StringBuilder sb = new StringBuilder();
		sb.append('{');
		String comma = "";
		for (int i = 0; i < attributes.getLength(); i++) {
			sb.append(comma);
			comma = ",";
			sb.append(attributes.getQName(i)).append('=').append(attributes.getValue(i));
		}
		sb.append('}');

		return sb.toString();
	}
}
