/**
 *
 *  Copyright 2005 The Apache Software Foundation or its licensors, as applicable.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package helloworld;

import junit.framework.TestCase;

import org.apache.tuscany.core.client.TuscanyRuntime;
import org.osoa.sca.CompositeContext;
import org.osoa.sca.CurrentCompositeContext;

/**
 * This shows how to test the HelloWorld service component.
 */
public class HelloWorldTestCase extends TestCase {
    
    private TuscanyRuntime tuscany;
    
    protected void setUp() throws Exception {
        super.setUp();
        
        // Create a Tuscany runtime
        tuscany = new TuscanyRuntime("HelloWorldWebSample", "webapp");

        // Start the Tuscany runtime and associate it with this thread
        tuscany.start();
    }
    
    public void testGeetings() throws Exception {

        // Get the SCA composite context.
        CompositeContext compositeContext = CurrentCompositeContext.getContext();

        // Locate the HelloWorld service
        HelloWorldService helloworldService = compositeContext.locateService(HelloWorldService.class, "HelloWorldServiceComponent");
        
        // Invoke the HelloWorld service
        String value = helloworldService.getGreetings("World");
        
        assertEquals(value, "Hello World");
    }
    
    protected void tearDown() throws Exception {
        
        // Stop the Tuscany runtime
        tuscany.stop();
        
        super.tearDown();
    }
}
