package com.mebigfatguy.ds.service;

import java.net.URI;

import org.xml.sax.ContentHandler;

public interface DSHandlerProvider {
	
	URI getXSDNamespace();
	
	ContentHandler getDSHandler();
}
