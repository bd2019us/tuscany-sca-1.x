package org.apache.tuscany.core.context.scope;

import java.util.List;
import java.util.ArrayList;

import junit.framework.TestCase;
import junit.framework.Assert;
import org.apache.tuscany.core.wire.jdk.JDKWireFactoryFactory;
import org.apache.tuscany.core.context.WorkContextImpl;
import org.apache.tuscany.core.system.context.SystemAtomicContext;
import org.apache.tuscany.core.mock.MockFactory;
import org.apache.tuscany.core.mock.component.StatelessComponentImpl;
import org.apache.tuscany.core.mock.component.StatelessComponent;
import org.apache.tuscany.spi.context.WorkContext;

/**
 * Unit tests for the module scope container
 *
 * @version $Rev: 396284 $ $Date: 2006-04-23 08:27:42 -0700 (Sun, 23 Apr 2006) $
 */
public class BasicStatelessScopeTestCase extends TestCase {

    /**
     * Tests instance identity is properly maintained
     */
    public void testInstanceManagement() throws Exception {
        WorkContext ctx = new WorkContextImpl();
        StatelessScopeContext scope = new StatelessScopeContext(ctx);
        scope.start();
        SystemAtomicContext context1 = MockFactory.createSystemAtomicContext("comp1",StatelessComponentImpl.class);
        context1.setScopeContext(scope);
        scope.register(context1);
        SystemAtomicContext context2 = MockFactory.createSystemAtomicContext("comp2",StatelessComponentImpl.class);
        context2.setScopeContext(scope);
        scope.register(context2);
        StatelessComponentImpl comp1 = (StatelessComponentImpl) scope.getInstance(context1);
        Assert.assertNotNull(comp1);
        StatelessComponentImpl comp2 = (StatelessComponentImpl) scope.getInstance(context2);
        Assert.assertNotNull(comp2);
        Assert.assertNotSame(comp1, comp2);
        scope.stop();
    }

    public void testRegisterContextAfterRequest() throws Exception {
        WorkContext ctx = new WorkContextImpl();
        StatelessScopeContext scope = new StatelessScopeContext(ctx);

        scope.start();
        SystemAtomicContext context1 = MockFactory.createSystemAtomicContext("comp1",StatelessComponentImpl.class);
        context1.setScopeContext(scope);
        scope.register(context1);
        StatelessComponentImpl comp1 = (StatelessComponentImpl) scope.getInstance(context1);
        Assert.assertNotNull(comp1);
        SystemAtomicContext context2 = MockFactory.createSystemAtomicContext("comp2",StatelessComponentImpl.class);
        context2.setScopeContext(scope);
        scope.register(context2);
        StatelessComponentImpl comp2 = (StatelessComponentImpl) scope.getInstance(context2);
        Assert.assertNotNull(comp2);
        scope.stop();
    }


    /**
     * Tests setting no components in the scope
     */
    public void testSetNullComponents() throws Exception {
        WorkContext ctx = new WorkContextImpl();
        StatelessScopeContext scope = new StatelessScopeContext(ctx);
        scope.start();
        scope.stop();
    }


}
