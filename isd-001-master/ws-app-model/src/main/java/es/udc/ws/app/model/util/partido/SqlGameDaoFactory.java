package es.udc.ws.app.model.util.partido;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

public class SqlGameDaoFactory {
    private static SqlGameDao dao = null;

    public SqlGameDaoFactory() {
    }

    private static SqlGameDao getInstance() {
        try {
            String daoClassName = ConfigurationParametersManager.getParameter("SqlGameDaoFactory.className");
            Class daoClass = Class.forName(daoClassName);
            return (SqlGameDao) daoClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized static SqlGameDao getDao() {
        if (dao == null) {
            dao = getInstance();
        }
        return dao;
    }
}
