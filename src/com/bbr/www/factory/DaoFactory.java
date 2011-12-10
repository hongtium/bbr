package com.bbr.www.factory;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import javax.sql.DataSource;

public class DaoFactory {
	public static ApplicationContext ctx = null;
    public static long SysStartTime = System.currentTimeMillis();
	static {
		init();
	}

 	public static DataSource getDB() {
		return (DataSource) ctx.getBean("dataSource");
	}

    /**
     * 根据Spring bean配置文件获得命名对象的通用方法
     */
    public static Object getBean(String beanName) {
        return ctx.getBean(beanName);
    }

	public static synchronized void init() {
        String[] locations = {"applicationContext.xml"};
        ctx = new ClassPathXmlApplicationContext(locations);
	}


    /*
	public static MsgDaoImpl getMsgDao() {
		return (MsgDaoImpl) bf.getBean("MsgDao");
	}

	public static DocumentDaoImpl getDocumentDao() {
		return (DocumentDaoImpl) bf.getBean("DocumentDao");
	}

	public static HotDocumentDaoImpl getHotDocumentDao() {

		return (HotDocumentDaoImpl) bf.getBean("HotDocumentDao");
	}

	public static OrganizeDaoImpl getOrganizeDao() {
		return (OrganizeDaoImpl) bf.getBean("OrganizeDao");
	}
	*/

}
