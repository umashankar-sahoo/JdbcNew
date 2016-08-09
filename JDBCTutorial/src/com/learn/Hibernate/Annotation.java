package com.learn.Hibernate;

import java.util.Date;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class Annotation {

	public static void main(String[] args) {
		Employee emp = new Employee();
		emp.setName("Hello");
		emp.setRole("Tester");
		emp.setInsertTime(new Date());

		// Get Session
		SessionFactory sessionFactory = HibernateUtil.getSessionAnnotationFactory();
		Session session = sessionFactory.getCurrentSession();

		// start transaction
		session.beginTransaction();

		// Save the Model object
		session.save(emp);

		// Commit transaction
		session.getTransaction().commit();
		System.out.println("Employee ID=" + emp.getId());

		// terminate session factory, otherwise program won't end
		sessionFactory.close();
	}
}

class HibernateUtil {
	// Annotation based configuration
	private static SessionFactory sessionAnnotationFactory = null;

	public static SessionFactory getSessionAnnotationFactory() {
		if(sessionAnnotationFactory == null)
			sessionAnnotationFactory = buildSessionAnnotationFactory();
		return sessionAnnotationFactory;
	}

	private static SessionFactory buildSessionAnnotationFactory() {
		try {
			Configuration configuration = new Configuration();
			configuration.configure("resources/hibernate-annotation.cfg.xml");
			System.out.println("Hibernate Annotation Configuration loaded");

			ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
			System.out.println("Hibernate Annotation serviceRegistry created");

			SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);

			return sessionFactory;
		}
		catch(Throwable ex) {
			System.err.println("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}
}
