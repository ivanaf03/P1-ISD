package es.udc.ws.app.model.util.service;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

public class ServiceFactory {
    private static Service service = null;

    private ServiceFactory() {
    }

    @SuppressWarnings("rawtypes")
    private static Service getInstance() {
        try {
            String serviceClassName = ConfigurationParametersManager.getParameter("ServiceFactory.className");
            Class serviceClass = Class.forName(serviceClassName);
            return (Service) serviceClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized static Service getService() {
        if (service == null) {
            service = getInstance();
        }
        return service;
    }
}
