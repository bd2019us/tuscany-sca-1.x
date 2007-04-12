package org.apache.tuscany.databinding.sdo2om;

import java.io.StringWriter;

import javax.xml.stream.XMLStreamException;

import junit.framework.Assert;

import org.apache.axiom.om.OMElement;
import org.apache.tuscany.interfacedef.DataType;
import org.apache.tuscany.interfacedef.impl.DataTypeImpl;
import org.apache.tuscany.interfacedef.util.XMLType;

import commonj.sdo.DataObject;

/**
 * 
 */
public class DataObject2OMElementTestCase extends SDOTransformerTestCaseBase {

    @Override
    protected DataType<?> getSourceDataType() {
        return new DataTypeImpl<XMLType>(DataObject.class.getName(), DataObject.class, new XMLType(ORDER_QNAME, null));
    }

    @Override
    protected DataType<?> getTargetDataType() {
        return new DataTypeImpl<XMLType>(OMElement.class.getName(), OMElement.class, new XMLType(ORDER_QNAME, null));
    }

    public final void testTransform() throws XMLStreamException {
        OMElement element = new DataObject2OMElement().transform(dataObject, context);
        Assert.assertEquals(ORDER_QNAME.getNamespaceURI(), element.getNamespace().getNamespaceURI());
        Assert.assertEquals(ORDER_QNAME.getLocalPart(), element.getLocalName());
        StringWriter writer = new StringWriter();
        element.serialize(writer);
    }

}
