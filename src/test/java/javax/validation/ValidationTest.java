/*
* JBoss, Home of Professional Open Source
* Copyright 2012, Red Hat, Inc. and/or its affiliates, and individual contributors
* by the @authors tag. See the copyright.txt in the distribution for a
* full listing of individual contributors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
* http://www.apache.org/licenses/LICENSE-2.0
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package javax.validation;


import org.junit.Test;

import java.net.URL;
import java.net.URLClassLoader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author Hardy Ferentschik
 */
public class ValidationTest {

	// BVAL-298
	@Test
	public void testCurrentClassLoaderIsUsedInCaseContextClassLoaderCannotLoadServiceFile() {
		ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
		ClassLoader dummyClassLoader = new URLClassLoader( new URL[] { }, null );
		Thread.currentThread().setContextClassLoader( dummyClassLoader );
		try {
			Validation.buildDefaultValidatorFactory();
			fail();
		}
		catch ( ValidationException e ) {
			assertEquals(
					"Unable to load Bean Validation provider non.existent.ValidationProvider",
					e.getMessage()
			);
		}
		finally {
			Thread.currentThread().setContextClassLoader( contextClassLoader );
		}
	}

	// BVAL-298
	@Test
	public void testCurrentClassLoaderIsUsedInCaseContextClassLoaderIsNull() {
		ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
		Thread.currentThread().setContextClassLoader( null );
		try {
			Validation.buildDefaultValidatorFactory();
			fail();
		}
		catch ( ValidationException e ) {
			assertEquals(
					"Unable to load Bean Validation provider non.existent.ValidationProvider",
					e.getMessage()
			);
		}
		finally {
			Thread.currentThread().setContextClassLoader( contextClassLoader );
		}
	}
}


