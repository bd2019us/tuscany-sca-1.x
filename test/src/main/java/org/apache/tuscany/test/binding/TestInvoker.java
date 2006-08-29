package org.apache.tuscany.test.binding;

import java.lang.reflect.InvocationTargetException;

import org.apache.tuscany.spi.wire.TargetInvoker;
import org.apache.tuscany.spi.wire.Message;
import org.apache.tuscany.spi.wire.InvocationRuntimeException;

/**
 * @version $Rev$ $Date$
 */
public class TestInvoker implements TargetInvoker {

    private boolean cacheable;

    public boolean isCacheable() {
        return cacheable;
    }

    public void setCacheable(boolean cacheable) {
        this.cacheable = cacheable;
    }

    public boolean isOptimizable() {
        return isCacheable();
    }

    public Object invokeTarget(final Object payload) throws InvocationTargetException {
        // echo back the result, a real binding would invoke some API for flowing the request
        return ((Object[])payload)[0];
    }

    public Message invoke(Message msg) throws InvocationRuntimeException {
        try {
            Object resp = invokeTarget(msg.getBody());
            msg.setBody(resp);
        } catch (InvocationTargetException e) {
            msg.setBody(e.getCause());
        } catch (Throwable e) {
            msg.setBody(e);
        }
        return msg;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
