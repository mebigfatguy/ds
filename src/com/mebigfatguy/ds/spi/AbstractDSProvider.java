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
package com.mebigfatguy.ds.spi;

import java.awt.Component;
import java.net.URL;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.DefaultHandler;

import com.mebigfatguy.ds.service.DSHandlerProvider;

public abstract class AbstractDSProvider implements DSHandlerProvider {

	private String xsdDSNamespace;
	private String xsdDSResource;
	
	protected AbstractDSProvider(String xsdNamespace, String xsdResource) {
		xsdDSNamespace = xsdNamespace;
		xsdDSResource = xsdResource;
	}
	
	@Override
	public String getXSDNamespace() {
		return xsdDSNamespace;
	}
	
	@Override
	public URL getSchema() {
		return FrameDSProvider.class.getResource(xsdDSResource);
	}

	@Override
	public Component getComponent() {
		return null;
	}

	@Override
	public void startComponent(String uri, String localName, String qName, Attributes atts, Component activeComponent) {
	}

	@Override
	public void endComponent(String uri, String localName, String qName, Component activeComponent) {
	}
	
	@Override
	public void endChildComponent(String uri, String localName, String qName, Component childComponent) {
	}

	@Override
	public void content(String content) {
	}
	
	
}