package es.udc.ws.app.model.util.compra;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

public class SqlSaleDaoFactory {
    private static SqlSaleDao dao = null;

    private SqlSaleDaoFactory() {
    }

    private static SqlSaleDao getInstance() {
        try {
            String daoClassName = ConfigurationParametersManager.getParameter("SqlSaleDaoFactory.className");
            Class daoClass = Class.forName(daoClassName);
            return (SqlSaleDao) daoClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized static SqlSaleDao getDao() {
        if (dao == null) {
            dao = getInstance();
        }
        return dao;
    }
}