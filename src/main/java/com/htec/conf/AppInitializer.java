package com.htec.conf;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

/**
 * Initialization of required connection, schema and data on in memory H2 database.
 */
@WebListener
public class AppInitializer implements ServletContextListener {
    Logger lgr = Logger.getLogger(AppInitializer.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        lgr.log(Level.INFO, "executing contextInitialized()");

        String url = "jdbc:h2:mem:flightadvisordb;DB_CLOSE_DELAY=-1;"
                + "INIT=RUNSCRIPT FROM 'classpath:/sql/schema.sql'"
                + "\\;RUNSCRIPT FROM 'classpath:/sql/data.sql'";

        SimpleDriverDataSource ds = new SimpleDriverDataSource();
        ds.setDriver(new org.h2.Driver());
        ds.setUrl(url);

        try (Connection con = ds.getConnection()) {

        } catch (SQLException ex) {
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}