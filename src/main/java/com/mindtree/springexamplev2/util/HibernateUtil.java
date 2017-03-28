package com.mindtree.springexamplev2.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * @author M1036004
 *  This is class for creating session factory 
 */
public class HibernateUtil {
	private static SessionFactory factory;

	private HibernateUtil() {

	}

	/**
	 * This is making session factory object
	 * 
	 * @return session factory object
	 */
	@SuppressWarnings("deprecation")
	private static SessionFactory makeSessionFactoryObject() {

		return (new Configuration()).configure().buildSessionFactory();
	}

	/**
	 * This is public method for getting session factory object
	 * 
	 * @return session factory object
	 */
	public static SessionFactory getSessionFactoryObject() {
		if (factory == null) {
			factory = makeSessionFactoryObject();
		}

		return factory;
	}

}
