package com.bbr.tools;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.MemcachedClient;

import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

//import net.spy.memcached.transcoders.ByteTranscoder;

/**
 * SpyMemcached client
 * User: chenjx
 * Date: 2011-3-29
 */
public class SpyMemcached {
    public static final Hashtable<String, MemcachedClient> MCC_LIST;
    public static final Hashtable<String, SpyMemcached> INSTANCE_LIST;

    private String modName = null;

    static {
        MCC_LIST = new Hashtable<String, MemcachedClient>(32);
        INSTANCE_LIST = new Hashtable<String, SpyMemcached>(32);
    }

    public static synchronized SpyMemcached getInstance(String modName) {
        return getInstance(modName, false);
    }

    public static synchronized SpyMemcached getInstance(String modName, boolean is_byte) {
        String ipList = AppConfigs.getInstance().get(modName);

        if (ipList == null) {
            System.err.println("ALERT: SpyMemcached.getInstance(" + modName + ") is invalid! check memcached.property .");
            return null;
        } else {
            ipList = ipList.trim();
            //System.out.println("DEBUG: SpyMemcached.getInstance(" + modName + ") : " + ipList);
        }
        SpyMemcached instance = (SpyMemcached) INSTANCE_LIST.get(modName);
        if (instance == null) {
            try {
                MemcachedClient mcc = null;
                if (!is_byte) {
                    mcc = new MemcachedClient(AddrUtil.getAddresses(ipList.replaceAll(",", " ")));
                } else {
                    //mcc = new MemcachedClient(new DefaultConnectionFactory(),AddrUtil.getAddresses(ipList),new ByteTranscoder());
                }
                //System.out.println(ipList);
                MCC_LIST.put(modName, mcc);
                instance = new SpyMemcached(modName);
                INSTANCE_LIST.put(modName, instance);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("INFO: Memcached.getInstance created:" + modName
                    + " -> " + ipList
                    + " module_count=" + INSTANCE_LIST.size());
        }
        return instance;
    }

    public static synchronized SpyMemcached getInstance(String modName, String ipList, boolean is_byte) {
        SpyMemcached instance = (SpyMemcached) INSTANCE_LIST.get(modName);
        if (instance == null) {
            try {
                MemcachedClient mcc = null;
                if (!is_byte) {
                    mcc = new MemcachedClient(AddrUtil.getAddresses(ipList));
                } else {
                    //mcc = new MemcachedClient(new DefaultConnectionFactory(),AddrUtil.getAddresses(ipList),new ByteTranscoder());
                }

                MCC_LIST.put(modName, mcc);
                instance = new SpyMemcached(modName);
                INSTANCE_LIST.put(modName, instance);
            } catch (Exception e) {
            }
            System.out.println("INFO: Memcached.getInstance created:" + modName + " module_count=" + INSTANCE_LIST.size());
        }
        return instance;
    }

    private SpyMemcached(String modName) {
        this.modName = modName;
    }

    public MemcachedClient getClient() {
        return MCC_LIST.get(modName);
    }

    public void set(String key, int timeout, Object v) {
        if (timeout == -1) timeout = 0;
        MemcachedClient mcc = getClient();
        mcc.set(key, timeout, v);
    }

    public void set(String key, int timeout, int v) {
        if (timeout == -1) timeout = 0;
        MemcachedClient mcc = getClient();
        mcc.set(key, timeout, String.valueOf(v));
    }

    public void set(String key, int timeout, long v) {
        if (timeout == -1) timeout = 0;
        MemcachedClient mcc = getClient();
        mcc.set(key, timeout, String.valueOf(v));
    }

    public Object get(String key) {
        MemcachedClient mcc = getClient();
        // Try to get a value, for up to 5 seconds, and cancel if it doesn't return
        Object myObj = null;
        Future<Object> f = mcc.asyncGet(key);
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

    public Map<String, Object> getBulk(String key) {
        MemcachedClient mcc = getClient();
        // Try to get a value, for up to 5 seconds, and cancel if it doesn't return
        Map<String, Object> myObj = null;
        Future<Map<String, Object>> f = mcc.asyncGetBulk(key.split(" "));

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

    public long getIncrLong(String key) {
        Object obj = get(key);
        if (obj == null) return -1;
        return Long.parseLong((String) obj);
    }

    public long getIncrInt(String key) {
        Object obj = get(key);
        if (obj == null) return -1;
        return Integer.parseInt((String) obj);
    }

    public void remove(String key) {
        MemcachedClient mcc = getClient();
        mcc.delete(key);
    }

    public long incr(String key) {
        return incr(key, 1);
    }

    public long incr(String key, int incr) {
        MemcachedClient mcc = getClient();
        long ret = mcc.incr(key, incr);
        return ret;
    }

    public static void main(String[] args) {
        SpyMemcached m = SpyMemcached.getInstance("Twitter");
        m.getClient().flush();

        m.getClient().shutdown();
    }

}