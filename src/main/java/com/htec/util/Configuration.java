package com.htec.util;

import com.htec.dao.CityDao;

import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Provides access to some of frequently used configuration data.
 */
public class Configuration {
    private static Configuration _instance = null;
    public static int NUMBER_OF_TRANSITES = 2;
    public static String DB_URL = "jdbc:h2:mem:flightadvisordb";

    Logger lgr = Logger.getLogger(Configuration.class.getName());

    protected Configuration(){
        try{
            InputStream file = getClass().getClassLoader().getResourceAsStream("config.properties") ;
            Properties props = new Properties();
            props.load(file);
            NUMBER_OF_TRANSITES = Integer.parseInt(props.getProperty("NUMBER_OF_TRANSITES"));
            DB_URL = props.getProperty("DB_URL");
        }
        catch(Exception e){
            lgr.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    static public Configuration instance(){
        if (_instance == null) {
            _instance = new Configuration();
        }
        return _instance;
    }
}