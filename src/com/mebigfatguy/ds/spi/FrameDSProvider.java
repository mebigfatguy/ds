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
package com.mebigfatguy.ds.spi;

import java.awt.Component;

import javax.swing.JFrame;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class FrameDSProvider extends AbstractDSProvider {

	private static final String FRAME_NAMESPACE = "http://com.mebigfatguy/ds/frame";
	private static final String FRAME_SCHEMA_RESOURCE = "/com/mebigfatguy/ds/xsd/frame.xsd";
	
	private JFrame frame;
	
	public FrameDSProvider() {
		super(FRAME_NAMESPACE, FRAME_SCHEMA_RESOURCE);
	}

	@Override
	public Component getComponent() {
		return frame;
	}
	
	@Override
	public void startComponent(String uri, String localName, String qName, Attributes attributes, Component activeComponent) {
		if ("frame".equals(localName)) {
			frame = new JFrame();
		}
	}
}
