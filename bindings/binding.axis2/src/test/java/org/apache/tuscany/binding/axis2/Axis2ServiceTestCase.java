package org.apache.tuscany.binding.axis2;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.Servlet;
import javax.wsdl.Definition;
import javax.wsdl.Port;
import javax.wsdl.Service;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import javax.xml.namespace.QName;

import org.xml.sax.InputSource;

import org.apache.tuscany.spi.host.ServletHost;
import org.apache.tuscany.spi.wire.InboundWire;
import org.apache.tuscany.spi.wire.WireService;

import junit.framework.TestCase;
import org.easymock.classextension.EasyMock;

public class Axis2ServiceTestCase extends TestCase {

    public void testInvokeService() throws Exception {
        TestServletHost tomcatHost = new TestServletHost();
        Axis2Service axis2Service = createAxis2Service("testWebAppName", "testServiceName", tomcatHost);
        axis2Service.start();

        Servlet servlet = tomcatHost.getMapping("testWebAppName/services/testServiceName");
        assertNotNull(servlet);

        //Create mocked HttpRequest and HttpResponse object to test the Axis2Servlet
        //To be done:

    }

    private Axis2Service createAxis2Service(String webAppName, String serviceName, ServletHost tomcatHost)
        throws Exception {
        //Create WebServiceBinding
        String wsdlLocation = "/wsdl/hello_world_doc_lit.wsdl";
        URL url = getClass().getResource(wsdlLocation);
        assertNotNull("Could not find wsdl " + url.toString(), url);

        WSDLFactory factory = WSDLFactory.newInstance();
        WSDLReader reader = factory.newWSDLReader();
        reader.setFeature("javax.wsdl.verbose", false);
        InputSource input = new InputSource(url.openStream());
        Definition wsdlDef = reader.readWSDL(url.toString(), input);
        Service wsdlService = wsdlDef.getService(new QName("http://objectweb.org/hello_world_soap_http",
            "SOAPService"));
        Port port = wsdlService.getPort("SoapPort");
        WebServiceBinding wsBinding = new WebServiceBinding(wsdlDef, port, "uri", "portURI", wsdlService);
        wsBinding.setWebAppName(webAppName);

        //Create a mocked WireService, make the call of ServiceExtension.getServiceInstance() returns a proxy instance.
        WireService wireService = EasyMock.createNiceMock(WireService.class);
        wireService.createProxy(EasyMock.isA(InboundWire.class));
        EasyMock.expectLastCall().andReturn(null);
        EasyMock.replay(wireService);

        //Create a mocked InboundWire, make the call of ServiceExtension.getInterface() returns a Class
        InboundWire inboundWire = EasyMock.createNiceMock(InboundWire.class);
        inboundWire.getBusinessInterface();
        EasyMock.expectLastCall().andReturn(Greeter.class).anyTimes();
        EasyMock.replay(inboundWire);

        Axis2Service<?> axis2Service = new Axis2Service(serviceName, null, wireService, wsBinding, tomcatHost);
        axis2Service.setInboundWire(inboundWire);

        return axis2Service;
    }

    protected class TestServletHost implements ServletHost {
        private Map<String, Servlet> mappings = new HashMap<String, Servlet>();

        public void registerMapping(String mapping, Servlet servlet) {
            mappings.put(mapping, servlet);
        }

        public void unregisterMapping(String mapping) {
        }

        public Servlet getMapping(String mapping) {
            return mappings.get(mapping);
        }

    }

}
