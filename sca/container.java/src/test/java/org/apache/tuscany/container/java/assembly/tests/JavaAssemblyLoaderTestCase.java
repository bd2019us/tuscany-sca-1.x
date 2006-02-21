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
package org.apache.tuscany.container.java.assembly.tests;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.tuscany.common.resource.loader.ResourceLoaderFactory;
import org.apache.tuscany.container.java.assembly.tests.bigbank.account.services.accountdata.AccountDataService;
import org.apache.tuscany.model.assembly.AssemblyModelContext;
import org.apache.tuscany.model.assembly.Component;
import org.apache.tuscany.model.assembly.ConfiguredService;
import org.apache.tuscany.model.assembly.EntryPoint;
import org.apache.tuscany.model.assembly.Module;
import org.apache.tuscany.model.assembly.impl.AssemblyModelContextImpl;
import org.apache.tuscany.model.assembly.loader.AssemblyLoader;
import org.apache.tuscany.model.assembly.loader.impl.AssemblyLoaderImpl;

/**
 */
public class JavaAssemblyLoaderTestCase extends TestCase {

    private AssemblyModelContext modelContext;

    /**
     *
     */
    public JavaAssemblyLoaderTestCase() {
        super();
    }

    public void testLoader() {

        AssemblyLoader loader = modelContext.getAssemblyLoader();
        Module module = loader.getModule(getClass().getResource("sca.module").toString());
        module.initialize(modelContext);

        Assert.assertTrue(module.getName().equals("tuscany.container.java.assembly.tests.bigbank.account"));

        Component component = module.getComponent("AccountServiceComponent");
        Assert.assertTrue(component != null);

        EntryPoint entryPoint = module.getEntryPoint("AccountService");
        Assert.assertTrue(entryPoint != null);

        Object value = component.getConfiguredProperty("currency").getValue();
        Assert.assertTrue(value.equals("EURO"));

        ConfiguredService configuredService = component.getConfiguredReference("accountDataService").getTargetConfiguredServices().get(0);
        Assert.assertTrue(configuredService.getAggregatePart().getName().equals("AccountDataServiceComponent"));

        Class interfaceClass = configuredService.getService().getServiceContract().getInterface();
        Assert.assertTrue(interfaceClass == AccountDataService.class);

    }

    protected void setUp() throws Exception {
        super.setUp();

        Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
        modelContext = new AssemblyModelContextImpl(new AssemblyLoaderImpl(), ResourceLoaderFactory.getResourceLoader(getClass().getClassLoader()));
    }

}
