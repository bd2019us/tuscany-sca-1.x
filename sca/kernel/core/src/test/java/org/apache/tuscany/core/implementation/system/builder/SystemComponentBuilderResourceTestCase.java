package org.apache.tuscany.core.implementation.system.builder;

import junit.framework.TestCase;

import org.apache.tuscany.core.implementation.system.model.SystemImplementation;
import org.apache.tuscany.spi.component.AtomicComponent;
import org.apache.tuscany.spi.component.CompositeComponent;
import org.apache.tuscany.spi.component.ScopeContainer;
import org.apache.tuscany.spi.component.ScopeRegistry;
import org.apache.tuscany.spi.deployer.DeploymentContext;
import org.apache.tuscany.spi.implementation.java.ConstructorDefinition;
import org.apache.tuscany.spi.implementation.java.PojoComponentType;
import org.apache.tuscany.spi.implementation.java.Resource;
import org.apache.tuscany.spi.model.ComponentDefinition;
import org.apache.tuscany.spi.model.ComponentTypeReferenceDefinition;
import org.apache.tuscany.spi.model.Property;
import org.apache.tuscany.spi.model.Scope;
import org.apache.tuscany.spi.model.ServiceDefinition;
import org.apache.tuscany.spi.wire.InboundWire;
import org.easymock.EasyMock;

/**
 * @version $Rev$ $Date$
 */
public class SystemComponentBuilderResourceTestCase extends TestCase {

    @SuppressWarnings("unchecked")
    public void testResourceInjection() throws Exception {
        ScopeContainer container = EasyMock.createNiceMock(ScopeContainer.class);
        DeploymentContext ctx = EasyMock.createNiceMock(DeploymentContext.class);
        ScopeRegistry registry = EasyMock.createMock(ScopeRegistry.class);
        EasyMock.expect(registry.getScopeContainer(Scope.STATELESS)).andReturn(container);
        EasyMock.replay(registry);
        SystemComponentBuilder builder = new SystemComponentBuilder();
        builder.setScopeRegistry(registry);
        ConstructorDefinition<Foo> ctorDef = new ConstructorDefinition<SystemComponentBuilderResourceTestCase.Foo>(
            SystemComponentBuilderResourceTestCase.Foo.class.getConstructor());
        PojoComponentType<ServiceDefinition, ComponentTypeReferenceDefinition, Property<?>> type =
            new PojoComponentType<ServiceDefinition, ComponentTypeReferenceDefinition, Property<?>>();
        Resource resource = new Resource();
        resource.setType(String.class);
        resource.setName("resource");
        resource.setMember(SystemComponentBuilderResourceTestCase.Foo.class.getDeclaredField("resource"));
        type.add(resource);
        type.setImplementationScope(Scope.STATELESS);
        type.setConstructorDefinition(ctorDef);
        SystemImplementation impl = new SystemImplementation();
        impl.setImplementationClass(SystemComponentBuilderResourceTestCase.Foo.class);
        impl.setComponentType(type);
        ComponentDefinition<SystemImplementation> definition =
            new ComponentDefinition<SystemImplementation>("foo", impl);

        InboundWire wire = EasyMock.createMock(InboundWire.class);
        EasyMock.expect(wire.getTargetService()).andReturn("result");
        EasyMock.replay(wire);

        CompositeComponent parent = EasyMock.createMock(CompositeComponent.class);
        EasyMock.expect(parent.resolveSystemAutowire(String.class)).andReturn(wire);
        EasyMock.replay(parent);
        AtomicComponent component = builder.build(parent, definition, ctx);
        SystemComponentBuilderResourceTestCase.Foo foo =
            (SystemComponentBuilderResourceTestCase.Foo) component.createInstance();
        assertEquals("result", foo.resource);
        EasyMock.verify(parent);
    }

    private static class Foo {

        protected String resource;

        public Foo() {
        }

    }
}
