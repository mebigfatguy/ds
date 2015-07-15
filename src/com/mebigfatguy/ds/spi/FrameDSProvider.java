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
