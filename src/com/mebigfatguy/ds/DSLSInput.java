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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.w3c.dom.ls.LSInput;

public class DSLSInput implements LSInput {

	private URL dsResource;
	private String dsSystemId;
	private String dsPublicId;
	private String dsBaseURI;
	
	public DSLSInput(URL resource, String systemId, String publicId, String baseURI) {
		dsResource = resource;
		dsSystemId = systemId;
		dsPublicId = publicId;
		dsBaseURI = baseURI;
	}
	
	@Override
	public Reader getCharacterStream() {
		try {
			return new BufferedReader(new InputStreamReader(dsResource.openStream(), StandardCharsets.UTF_8));
		} catch (IOException e) {
			return null;
		}
	}

	@Override
	public void setCharacterStream(Reader characterStream) {
	}

	@Override
	public InputStream getByteStream() {
		return null;
	}

	@Override
	public void setByteStream(InputStream byteStream) {
	}

	@Override
	public String getStringData() {
		return null;
	}

	@Override
	public void setStringData(String stringData) {
	}

	@Override
	public String getSystemId() {
		return dsSystemId;
	}

	@Override
	public void setSystemId(String systemId) {
	}

	@Override
	public String getPublicId() {
		return dsPublicId;
	}

	@Override
	public void setPublicId(String publicId) {
	}

	@Override
	public String getBaseURI() {
		return dsBaseURI;
	}

	@Override
	public void setBaseURI(String baseURI) {
	}

	@Override
	public String getEncoding() {
		return StandardCharsets.UTF_16.name();
	}

	@Override
	public void setEncoding(String encoding) {
	}

	@Override
	public boolean getCertifiedText() {
		return false;
	}

	@Override
	public void setCertifiedText(boolean certifiedText) {
	}
}
