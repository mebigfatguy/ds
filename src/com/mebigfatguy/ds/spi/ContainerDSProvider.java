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

public class ContainerDSProvider extends AbstractDSProvider {

	private static final String CONTAINER_NAMESPACE = "http://com.mebigfatguy/ds/container";
	private static final String CONTAINER_SCHEMA_RESOURCE = "/com/mebigfatguy/ds/xsd/container.xsd";
	
	public ContainerDSProvider() {
		super(CONTAINER_NAMESPACE, CONTAINER_SCHEMA_RESOURCE);
	}
}