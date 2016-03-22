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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.LayoutManager;

import javax.swing.BoxLayout;
import javax.xml.XMLConstants;

import org.xml.sax.Attributes;

import com.mebigfatguy.ds.DSFactory;

public class ContainerDSProvider extends AbstractDSProvider {

	private static final String CONTAINER_NAMESPACE = "http://com.mebigfatguy/ds/container";
	private static final String CONTAINER_SCHEMA_RESOURCE = "/com/mebigfatguy/ds/xsd/container.xsd";

	private static final String LAYOUT_MANAGER = "layoutManager";

	private static final String BORDER_LAYOUT = "BorderLayout";
	private static final String BOX_LAYOUT = "BoxLayout";

	private static final String NAME = "name";
	private static final String CHILD_COMPONENT = "childComponent";

	private String layoutType;

	public ContainerDSProvider() {
		super(CONTAINER_NAMESPACE, CONTAINER_SCHEMA_RESOURCE);
	}

	@Override
	public void startComponent(String uri, String localName, String qName, Attributes attributes, Component activeComponent) {
		String[] parts = attributes.getValue(XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI, DSFactory.XSI_TYPE).split(":");
		if (parts.length < 2) {
			layoutType = null;
		} else {
			layoutType = parts[1];
		}
	}

	@Override
	public void endComponent(String uri, String localName, String qName, Component activeComponent) {
		switch (localName) {

		case LAYOUT_MANAGER:
			LayoutManager l = null;
			switch (layoutType) {
			case BORDER_LAYOUT:
				l = new BorderLayout();
				break;
			case BOX_LAYOUT:
				l = new BoxLayout((Container) activeComponent, BoxLayout.X_AXIS);
				break;
			}

			if (l != null) {
				((Container) activeComponent).setLayout(l);
			}
			break;
		}
	}
}