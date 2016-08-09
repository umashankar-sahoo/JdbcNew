package com.learn.Hibernate;

import java.util.Date;
import java.util.Properties;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class JavaConfiguration {

	public static void main(String[] args) {
		Employee emp = new Employee();
		emp.setName("Lisa");
		emp.setRole("Manager");
		emp.setInsertTime(new Date());

		// Get Session
		SessionFactory sessionFactory = HibernateConfigUtil.getSessionJavaConfigFactory();
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

class HibernateConfigUtil {

	// Property based configuration
	private static SessionFactory sessionJavaConfigFactory;

	public static SessionFactory getSessionJavaConfigFactory() {
		if(sessionJavaConfigFactory == null)
			sessionJavaConfigFactory = buildSessionJavaConfigFactory();
		return sessionJavaConfigFactory;
	}

	private static SessionFactory buildSessionJavaConfigFactory() {
		try {
			Configuration configuration = new Configuration();

			// Create Properties, can be read from property files too
			Properties props = new Properties();
			props.put("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
			props.put("hibernate.connection.url", "jdbc:mysql://localhost/mydb");
			props.put("hibernate.connection.username", "root");
			props.put("hibernate.connection.password", "Uss@123");
			props.put("hibernate.current_session_context_class", "thread");

			configuration.setProperties(props);

			// we can set mapping file or class with annotation addClass(Employee1.class) will look for resource
			// com/bsil/hibernate/model/Employee1.hbm.xml (not good)
			configuration.addAnnotatedClass(Employee.class);

			ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
			System.out.println("Hibernate Java Config serviceRegistry created");

			SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);

			return sessionFactory;
		}
		catch(Throwable ex) {
			System.err.println("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}
}
