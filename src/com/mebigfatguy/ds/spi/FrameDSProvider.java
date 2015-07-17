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

import java.net.URI;
import java.net.URISyntaxException;

import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.DefaultHandler;

import com.mebigfatguy.ds.service.DSHandlerProvider;

public class FrameDSProvider extends DefaultHandler implements DSHandlerProvider {

	private static final URI FRAME_NAMESPACE;
	
	static {
		try {
			FRAME_NAMESPACE = new URI("http://com.mebigfatguy/ds/frame");
		} catch (URISyntaxException e) {
			throw new Error("Unable to define namespace", e);
		}
	}
	
	@Override
	public URI getXSDNamespace() {
		return FRAME_NAMESPACE;
	}

	@Override
	public ContentHandler getDSHandler() {
		return this;
	}
}
