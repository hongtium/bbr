package com.bbr.www.factory;

import net.spy.memcached.MemcachedClient;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * User: chenjx
 * Date: 2011-9-28
 */
public class SpyMemcachedFactory {
    static Resource resource = null;
    static BeanFactory ctx = null;

    static {
        init();
    }

    public static synchronized void init() {
        resource = new ClassPathResource("cacheContext.xml");
        ctx = new XmlBeanFactory(resource);
    }
    //static ApplicationContext ctx = new ClassPathXmlApplicationContext("cacheContext.xml");

    MemcachedClient client = null;

    public SpyMemcachedFactory(String module) {
        client = (MemcachedClient) ctx.getBean(module);
    }

    public void set(String key, int timeout, Object v) {
        if (timeout == -1) timeout = 0;

        client.set(key, timeout, v);
    }

    public void incr(String key) {
        incr(key, 0, 1);
    }

    public void incr(String key, int timeout) {
        incr(key, timeout, 1);
    }

    public void incr(String key, int timeout, int v) {
        if (timeout == -1) timeout = 0;

        client.incr(key, v);

    }

    public void incrNew(String key, int timeout, int v) {
        if (timeout == -1) timeout = 0;

        if (client.incr(key, v) == -1) {
            client.set(key, timeout, String.valueOf(v));
        }
    }

    public void incr(String key, int timeout, long v) {
        if (timeout == -1) timeout = 0;

        client.incr(key, Integer.parseInt(v + ""));
    }

    public Object get(String key) {

        // Try to get a value, for up to 1 seconds, and cancel if it doesn't return
        Object myObj = null;
        Future<Object> f = client.asyncGet(key);
        try {
            myObj = f.get(1, TimeUnit.SECONDS);
        } catch (Exception e) {
            // Since we don't need this, go ahead and cancel the operation.  This
            // is not strictly necessary, but it'll save some work on the server.
            e.printStackTrace();
            f.cancel(false);
            // Do other timeout related stuff
        }
        return myObj;
    }

    public Map<String, Object> getBulk(String key) {

        // Try to get a value, for up to 5 seconds, and cancel if it doesn't return
        Map<String, Object> myObj = null;
        Future<Map<String, Object>> f = client.asyncGetBulk(key.split(" "));

        try {
            myObj = f.get(5, TimeUnit.SECONDS);
        } catch (Exception e) {
            // Since we don't need this, go ahead and cancel the operation.  This
            // is not strictly necessary, but it'll save some work on the server.
            f.cancel(false);
            // Do other timeout related stuff
        }
        return myObj;
    }

    public String getString(String key) {
        Object obj = get(key);
        if (obj == null) return null;
        return (String) obj;
    }

    public long getLong(String key) {
        Object obj = get(key);
        if (obj == null) return -1;
        return (Long) obj;
    }

    public int getInt(String key) {
        Object obj = get(key);
        if (obj == null) return -1;
        return Integer.parseInt((String) obj);
    }

    public void remove(String key) {

        client.delete(key);
    }

    public MemcachedClient getClient() {
        return client;
    }
}
