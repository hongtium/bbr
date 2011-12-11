package com.bbr.tools;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: chenjx
 * Date: 2011-6-4
 */
public class AppConfigs {
    public static boolean DEBUG = false;
    private final static Log log = LogFactory.getLog(AppConfigs.class);
    private static AppConfigs instance;
    private Map<String, String> configs;
    public final static String configFile = "global.properties";
    private static final Object lock = new Object();
    private static boolean initDone = false;

    private AppConfigs() {
    }

    public static synchronized AppConfigs getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new AppConfigs();
                    instance.init();
                    initDone = true;
                }
            }
        }
        return instance;
    }

    public synchronized void reload() {
        Map<String, String> configsTemp = new ConcurrentHashMap<String, String>();
        log.debug("reload AppConfigs");
        Properties props = new Properties();
        try {
            URL url = this.getClass().getClassLoader().getResource(configFile);
            InputStream in = url.openStream();
            props.load(in);
            Enumeration en = props.propertyNames();
            while (en.hasMoreElements()) {
                String key = (String) en.nextElement();
                String value = props.getProperty(key);
                configsTemp.put(key, value);
            }
        } catch (Exception e) {
            log.error("reload AppConfigs error:" + e.getMessage());
            return;
        }
        configs = configsTemp;
    }

    public void init() {
        if (initDone) {
            return;
        }
        log.info("================== init AppConfigs ... =====================");
        Properties props = new Properties();
        try {
            URL url = this.getClass().getClassLoader().getResource(configFile);
            InputStream in = url.openStream();
            props.load(in);
            Enumeration en = props.propertyNames();
            configs = new ConcurrentHashMap<String, String>();
            while (en.hasMoreElements()) {
                String key = (String) en.nextElement();
                String value = props.getProperty(key);
                if(DEBUG) System.out.println("AppConfigs key=" + key + " value=" + value);
                configs.put(key, value);
            }
        } catch (Exception e) {
            log.error("init AppConfigs error:" + e.getMessage());
        }
    }

    public Map<String, String> getConfigs() {
        return configs;
    }

    public String get(String key) {
        return configs.get(key);
    }

    public String get(String key,String defaultStr) {
        String result = configs.get(key);
        if(result == null){
            result =  defaultStr;
        }
        return result;
    }
}
