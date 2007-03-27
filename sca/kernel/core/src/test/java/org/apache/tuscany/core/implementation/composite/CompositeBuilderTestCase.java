/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.    
 */
package org.apache.tuscany.core.implementation.composite;

import java.net.URI;

import org.apache.tuscany.spi.component.AtomicComponent;
import org.apache.tuscany.spi.component.CompositeComponent;
import org.apache.tuscany.spi.component.ScopeContainerMonitor;
import org.apache.tuscany.spi.component.Service;
import org.apache.tuscany.spi.deployer.DeploymentContext;
import org.apache.tuscany.spi.idl.java.JavaInterfaceProcessorRegistry;
import org.apache.tuscany.spi.idl.java.JavaServiceContract;
import org.apache.tuscany.spi.implementation.java.ConstructorDefinition;
import org.apache.tuscany.spi.implementation.java.JavaMappedProperty;
import org.apache.tuscany.spi.implementation.java.JavaMappedReference;
import org.apache.tuscany.spi.implementation.java.JavaMappedService;
import org.apache.tuscany.spi.implementation.java.PojoComponentType;
import org.apache.tuscany.spi.model.ComponentDefinition;
import org.apache.tuscany.spi.model.ComponentReferenceDefinition;
import org.apache.tuscany.spi.model.ComponentTypeReferenceDefinition;
import org.apache.tuscany.spi.model.CompositeComponentType;
import org.apache.tuscany.spi.model.CompositeImplementation;
import org.apache.tuscany.spi.model.AbstractReferenceDefinition;
import org.apache.tuscany.spi.model.CompositeReferenceDefinition;
import org.apache.tuscany.spi.model.ReferenceTarget;
import org.apache.tuscany.spi.model.Scope;
import org.apache.tuscany.spi.model.ServiceContract;
import org.apache.tuscany.spi.model.ServiceDefinition;
import org.apache.tuscany.spi.wire.WireService;

import junit.framework.TestCase;
import org.apache.tuscany.core.binding.local.LocalBindingBuilder;
import org.apache.tuscany.core.binding.local.LocalBindingDefinition;
import org.apache.tuscany.core.builder.BuilderRegistryImpl;
import org.apache.tuscany.core.component.scope.CompositeScopeContainer;
import org.apache.tuscany.core.deployer.RootDeploymentContext;
import org.apache.tuscany.core.idl.java.JavaInterfaceProcessorRegistryImpl;
import org.apache.tuscany.core.implementation.java.JavaComponentBuilder;
import org.apache.tuscany.core.implementation.java.JavaImplementation;
import org.apache.tuscany.core.mock.component.OtherTarget;
import org.apache.tuscany.core.mock.component.Source;
import org.apache.tuscany.core.mock.component.SourceImpl;
import org.apache.tuscany.core.mock.component.Target;
import org.apache.tuscany.core.mock.component.TargetImpl;
import org.apache.tuscany.core.wire.jdk.JDKWireService;
import org.easymock.EasyMock;

/**
 * @version $$Rev$$ $$Date$$
 */
public class CompositeBuilderTestCase extends TestCase {
    private DeploymentContext deploymentContext;

    @SuppressWarnings("unchecked")
    public void testBuild() throws Exception {
        CompositeComponent parent = new CompositeComponentImpl(null, null, null, null);

        CompositeBuilder builder = new CompositeBuilder();
        WireService wireService = new JDKWireService();
        builder.setWireService(wireService);
        BuilderRegistryImpl builderRegistry = new BuilderRegistryImpl(null, wireService);
        JavaComponentBuilder jBuilder = new JavaComponentBuilder();
        jBuilder.setWireService(wireService);
        builderRegistry.register(JavaImplementation.class, jBuilder);
        builderRegistry.register(CompositeImplementation.class, builder);
        builderRegistry.register(LocalBindingDefinition.class, new LocalBindingBuilder());
        builder.setBuilderRegistry(builderRegistry);
        CompositeComponent component =
            (CompositeComponent) builder.build(parent, createTopComponentDef(), deploymentContext);

        component.start();
        CompositeComponent sourceComponent = (CompositeComponent) component.getChild("SourceComponent");
        assertTrue(sourceComponent.getChild("InnerSourceService") instanceof Service);
        AtomicComponent innerSourceComponent = (AtomicComponent) sourceComponent.getChild("InnerSourceComponent");
        Source innerSourceInstance = (Source) deploymentContext.getCompositeScope().getInstance(innerSourceComponent);
        assertNotNull(innerSourceInstance);
        component.stop();
    }

    private ComponentDefinition createTopComponentDef() throws Exception {

        CompositeComponentType<JavaMappedService, CompositeReferenceDefinition, JavaMappedProperty<?>> outerType =
            new CompositeComponentType<JavaMappedService, CompositeReferenceDefinition, JavaMappedProperty<?>>();
        outerType.add(createSourceComponentDef());
        outerType.add(createTargetComponentDef());

        CompositeImplementation outerImpl = new CompositeImplementation();
        outerImpl.setComponentType(outerType);

        return new ComponentDefinition<CompositeImplementation>(outerImpl);
    }

    private ComponentDefinition<CompositeImplementation> createSourceComponentDef() throws Exception {

        CompositeComponentType<ServiceDefinition, CompositeReferenceDefinition, JavaMappedProperty<?>> innerType =
            new CompositeComponentType<ServiceDefinition, CompositeReferenceDefinition, JavaMappedProperty<?>>();
        innerType.add(createInnerSourceComponentDef());
        CompositeReferenceDefinition reference = new CompositeReferenceDefinition();
        reference.setName("TargetComponentRef");
        JavaInterfaceProcessorRegistry registry = new JavaInterfaceProcessorRegistryImpl();
        JavaServiceContract targetContract = registry.introspect(Target.class);
        reference.setServiceContract(targetContract);
        innerType.add(reference);
        ServiceDefinition service = new ServiceDefinition();
        service.setName("InnerSourceService");
        JavaServiceContract sourceContract = registry.introspect(Source.class);
        service.setServiceContract(sourceContract);
        service.setTarget(new URI("InnerSourceComponent"));
        innerType.add(service);

        CompositeImplementation innerImpl = new CompositeImplementation();
        innerImpl.setComponentType(innerType);

        ComponentDefinition<CompositeImplementation> sourceComponentDefinition =
            new ComponentDefinition<CompositeImplementation>("SourceComponent", innerImpl);
        
        /*ReferenceTarget refTarget = new ReferenceTarget();
        refTarget.setReferenceName("TargetComponentRef");
        refTarget.addTarget(new URI("TargetComponent"));
        sourceComponentDefinition.add(refTarget);*/
        ComponentReferenceDefinition compRef = new ComponentReferenceDefinition(innerType.getReferences().get("TargetComponentRef"));
        compRef.setName("TargetComponentRef");
        compRef.addTarget(new URI("TargetComponent"));
        sourceComponentDefinition.add(compRef);

        return sourceComponentDefinition;
    }

    private ComponentDefinition<JavaImplementation> createInnerSourceComponentDef() throws Exception {

        PojoComponentType<JavaMappedService, JavaMappedReference, JavaMappedProperty<?>> sourceType =
            new PojoComponentType<JavaMappedService, JavaMappedReference, JavaMappedProperty<?>>();
        sourceType.setImplementationScope(Scope.COMPOSITE);
        JavaMappedReference reference = new JavaMappedReference();
        reference.setName("targetReference");
        JavaInterfaceProcessorRegistry registry = new JavaInterfaceProcessorRegistryImpl();
        ServiceContract<?> targetContract = registry.introspect(Target.class);
        targetContract.setCallbackClass(OtherTarget.class);
        targetContract.setCallbackName("OtherTarget");
        reference.setServiceContract(targetContract);
        reference.setMember(SourceImpl.class.getMethod("setTarget", Target.class));
        sourceType.add(reference);

        ServiceContract<?> sourceContract = registry.introspect(Source.class);

        JavaMappedService sourceServiceDefinition = new JavaMappedService();
        sourceServiceDefinition.setName("Source");
        sourceServiceDefinition.setServiceContract(sourceContract);

        sourceType.add(sourceServiceDefinition);
        sourceType.setConstructorDefinition(new ConstructorDefinition<SourceImpl>(SourceImpl.class.getConstructor()));
        JavaImplementation sourceImpl = new JavaImplementation(SourceImpl.class, sourceType);
        ComponentDefinition<JavaImplementation> innerSourceComponentDefinition =
            new ComponentDefinition<JavaImplementation>("InnerSourceComponent", sourceImpl);
        
        ComponentReferenceDefinition compRef = new ComponentReferenceDefinition(sourceType.getReferences().get("targetReference"));
        compRef.setName("targetReference");
        compRef.addTarget(new URI("TargetComponentRef"));
        innerSourceComponentDefinition.add(compRef);
        /*ReferenceTarget refTarget = new ReferenceTarget();
        refTarget.setReferenceName("targetReference");
        refTarget.addTarget(new URI("TargetComponentRef"));
        innerSourceComponentDefinition.add(refTarget);
        */
        return innerSourceComponentDefinition;
    }

    private ComponentDefinition<JavaImplementation> createTargetComponentDef() throws Exception {

        PojoComponentType<JavaMappedService, JavaMappedReference, JavaMappedProperty<?>> targetType =
            new PojoComponentType<JavaMappedService, JavaMappedReference, JavaMappedProperty<?>>();
        targetType.setImplementationScope(Scope.COMPOSITE);

        JavaInterfaceProcessorRegistry registry = new JavaInterfaceProcessorRegistryImpl();
        ServiceContract<?> targetContract = registry.introspect(Target.class);
        targetContract.setCallbackClass(OtherTarget.class);
        targetContract.setCallbackName("OtherTarget");

        JavaMappedService serviceDefinition = new JavaMappedService();
        serviceDefinition.setName("Target");
        serviceDefinition.setServiceContract(targetContract);
        serviceDefinition.setCallbackReferenceName("otherTarget");

        targetType.add(serviceDefinition);
        targetType.setConstructorDefinition(new ConstructorDefinition<TargetImpl>(TargetImpl.class.getConstructor()));
        JavaImplementation targetImpl = new JavaImplementation(TargetImpl.class, targetType);
        return new ComponentDefinition<JavaImplementation>("TargetComponent", targetImpl);
    }

    protected void setUp() throws Exception {
        super.setUp();
        ScopeContainerMonitor monitor = EasyMock.createNiceMock(ScopeContainerMonitor.class);
        CompositeScopeContainer container = new CompositeScopeContainer(monitor);
        container.start();
        deploymentContext = new RootDeploymentContext(null, null, container, null);
    }

}
