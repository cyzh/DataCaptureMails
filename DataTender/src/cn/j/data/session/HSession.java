package cn.j.data.session;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HSession {

	private static Configuration config = new Configuration().configure();
	private static SessionFactory factory = config.buildSessionFactory();

	public static Session getSession() {
		return factory.openSession();
	}

	public static void closeSession() {
		factory.close();
	}

}
