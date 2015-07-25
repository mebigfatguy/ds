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
package com.mebigfatguy.ds;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class DSErrorHandler implements ErrorHandler {

    private StringBuilder error = new StringBuilder();
    private boolean hasErrors = false;
    
    @Override
    public void warning(SAXParseException exception) {
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
    	PrintStream ps = new PrintStream(baos);
    	exception.printStackTrace(ps);
    	
    	error.append("Warning:\n").append(new String(baos.toByteArray())).append("\n");
    }

    @Override
    public void error(SAXParseException exception) {
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
    	PrintStream ps = new PrintStream(baos);
    	exception.printStackTrace(ps);
    	
    	error.append("Error:\n").append(new String(baos.toByteArray())).append("\n");
        hasErrors = true;
    }

    @Override
    public void fatalError(SAXParseException exception) {
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
    	PrintStream ps = new PrintStream(baos);
    	exception.printStackTrace(ps);
    	
    	error.append("Fatal Error:\n").append(new String(baos.toByteArray())).append("\n");
        hasErrors = true;
    }
    
    public boolean hasErrors() {
        return hasErrors;
    }
    @Override
    public String toString() {
        return error.toString();
    }

}
