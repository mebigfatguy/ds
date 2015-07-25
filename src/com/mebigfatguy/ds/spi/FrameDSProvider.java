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

public class FrameDSProvider extends AbstractDSProvider {

	private static final String FRAME_NAMESPACE = "http://mebigfatguy.com/ds/frame";
	private static final String FRAME_SCHEMA_RESOURCE = "/com/mebigfatguy/ds/xsd/frame.xsd";
	
	private static final String FRAME = "frame";
	private static final String TITLE = "title";
	
	private JFrame frame;
	private String contents;
	private boolean inFrame;
	
	public FrameDSProvider() {
		super(FRAME_NAMESPACE, FRAME_SCHEMA_RESOURCE);
	}

	@Override
	public Component getComponent() {
		return inFrame ? frame : null;
	}
	
	@Override
	public void startComponent(String uri, String localName, String qName, Attributes attributes, Component activeComponent) {
		inFrame = false;
		switch (localName) {
			case FRAME:
				frame = new JFrame();
				inFrame = true;
			break;
		}
	}

	@Override
	public void endComponent(String uri, String localName, String qName, Component activeComponent) {
		switch (localName) {		
			case TITLE:
				frame.setTitle(contents);
			break;
		}
	}

	@Override
	public void content(String content) {
		contents = content;
	}
	
	
}
