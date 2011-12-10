package com.bbr.www.factory;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.MemcachedClient;
import org.springframework.beans.factory.FactoryBean;

/**
 * Spring-memcached
 * User: chenjx
 * Date: 2011-9-28
 */

public class SpyMemcachedBean implements FactoryBean {
    public String ipList = "";
    public Object getObject() throws Exception {
        MemcachedClient mcc = new MemcachedClient(AddrUtil.getAddresses(ipList.replaceAll(","," ")));
        return mcc;
    }

    public void setIpList(String ipList) {
        this.ipList = ipList;
    }

    @SuppressWarnings("unchecked")
	public Class getObjectType() {
		return MemcachedClient.class;
	}

	public boolean isSingleton() {
		return true;
	}

    public void shutdown() {
        System.out.println("SpyMemcachedBean.shutdown() " + System.currentTimeMillis());
    }
}
