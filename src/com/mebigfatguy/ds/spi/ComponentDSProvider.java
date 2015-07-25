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
import java.awt.Dimension;
import java.awt.Window;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ComponentDSProvider extends AbstractDSProvider {

	private static final String COMPONENT_NAMESPACE = "http://com.mebigfatguy/ds/component";
	private static final String COMPONENT_SCHEMA_RESOURCE = "/com/mebigfatguy/ds/xsd/component.xsd";
	
	private static final String NAME = "name";
	private static final String PREFERRED_SIZE = "preferredSize";
	
	private static final Pattern SIZE_PATTERN = Pattern.compile("(\\d+)\\s*,\\s*(\\d+)");
	
	private String content;
	
	public ComponentDSProvider() {
		super(COMPONENT_NAMESPACE, COMPONENT_SCHEMA_RESOURCE);
	}

	@Override
	public void endComponent(String uri, String localName, String qName, Component activeComponent) {
		switch (localName) {
			case NAME:
				activeComponent.setName(content);
			break;
			
			case PREFERRED_SIZE:
				Matcher m = SIZE_PATTERN.matcher(content.toString());
				if (m.matches()) {
					int width = Integer.parseInt(m.group(1));
					int height = Integer.parseInt(m.group(2));
					activeComponent.setPreferredSize(new Dimension(width, height));
				}
			break;
		}
	}
	
	@Override
	public void content(String contents) {
		content = contents;
	}
}
