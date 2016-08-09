package com.learn.Hibernate;

import java.util.Date;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class XMLMappingBased {
	public static void main(String[] args) {
		Emp emp = new Emp();
		emp.setName("Umashankar");
		emp.setRole("Standard Developer");
		emp.setInsertTime(new Date());

		// Get Session
		Session session = HibernateXMLUtil.getSessionFactory().getCurrentSession();
		// start transaction
		session.beginTransaction();
		// Save the Model object
		session.save(emp);
		// Commit transaction
		session.getTransaction().commit();
		System.out.println("Employee ID=" + emp.getId());

		// terminate session factory, otherwise program won't end
		HibernateXMLUtil.getSessionFactory().close();
	}
}

class HibernateXMLUtil {

	// XML based configuration
	private static SessionFactory sessionFactory;

	public static SessionFactory getSessionFactory() {
		if(sessionFactory == null)
			sessionFactory = buildSessionFactory();
		return sessionFactory;
	}

	private static SessionFactory buildSessionFactory() {
		try {
			// Create the SessionFactory from hibernate.cfg.xml
			Configuration configuration = new Configuration();
			configuration.configure("resources/hibernate.cfg.xml");
			System.out.println("Hibernate Configuration loaded");

			ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
			System.out.println("Hibernate serviceRegistry created");

			SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);

			return sessionFactory;
		}
		catch(Throwable ex) {
			// Make sure you log the exception, as it might be swallowed
			System.err.println("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}
}