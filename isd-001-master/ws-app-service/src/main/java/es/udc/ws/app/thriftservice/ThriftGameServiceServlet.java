package es.udc.ws.app.thriftservice;

import es.udc.ws.app.thrift.ThriftGameService;
import es.udc.ws.util.servlet.ThriftHttpServletTemplate;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocolFactory;

public class ThriftGameServiceServlet extends ThriftHttpServletTemplate {
    public ThriftGameServiceServlet() {
        super(createProcessor(), createProtocolFactory());
    }

    private static TProcessor createProcessor() {

        return new ThriftGameService.Processor<ThriftGameService.Iface>(
                new ThriftGameServiceImplementation());

    }

    private static TProtocolFactory createProtocolFactory() {
        return new TBinaryProtocol.Factory();
    }
}
