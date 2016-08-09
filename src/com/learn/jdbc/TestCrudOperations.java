package com.learn.jdbc;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Scanner;

public class TestCrudOperations {
	public static void main(String[] args) throws SQLException {
		// SelectQuery sq = new SelectQuery();
		// sq.select();
		// UpdateQuery uq = new UpdateQuery();
		// uq.update();
		// SP sp = new SP();
		// sp.select();
		Metadata md = new Metadata();
		md.resultSet();
	}
}

class Metadata {
	public void resultSet() throws SQLException {
		String catalog = null;
		String schemaPattern = null;
		String tableNamePattern = null;
		String columnNamePattern = null;
		String[] types = null;

		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		try {
			myConn = DBConnection.getConnection();
			myStmt = myConn.createStatement();
			myRs = myStmt.executeQuery("select id, last_name, first_name, salary from employees");
			ResultSetMetaData rsMetaData = myRs.getMetaData();

			int columnCount = rsMetaData.getColumnCount();
			System.out.println("Column count: " + columnCount + "\n");
			for(int column = 1; column <= columnCount; column++) {
				System.out.println("Column name: " + rsMetaData.getColumnName(column));
				System.out.println("Column type name: " + rsMetaData.getColumnTypeName(column));
				System.out.println("Is Nullable: " + rsMetaData.isNullable(column));
				System.out.println("Is Auto Increment: " + rsMetaData.isAutoIncrement(column) + "\n");
			}

			System.out.println();
			DatabaseMetaData databaseMetaData = myConn.getMetaData();

			// 3. Display info about database
			System.out.println("Product name: " + databaseMetaData.getDatabaseProductName());
			System.out.println("Product version: " + databaseMetaData.getDatabaseProductVersion());
			System.out.println();

			// 4. Display info about JDBC Driver
			System.out.println("JDBC Driver name: " + databaseMetaData.getDriverName());
			System.out.println("JDBC Driver version: " + databaseMetaData.getDriverVersion());

			System.out.println();
			System.out.println("List of Tables");
			System.out.println("--------------");
			myRs = databaseMetaData.getTables(catalog, schemaPattern, tableNamePattern, types);
			int counter = 0;
			while(myRs.next()) {
				System.out.println(myRs.getString("TABLE_NAME"));
				if(counter++ > 5)
					break;
			}

			System.out.println("\n\nList of Columns");
			System.out.println("--------------");
			myRs = databaseMetaData.getColumns(catalog, schemaPattern, "employees", columnNamePattern);
			while(myRs.next()) {
				System.out.println(myRs.getString("COLUMN_NAME"));
			}
		}
		catch(SQLException sE) {
			System.out.println(sE.getMessage());
		}
		finally {
			myConn.close();
			myStmt.close();;
		}
	}
}

class SP {
	public void select() throws SQLException {
		Connection myConn = null;
		CallableStatement myStmt = null;
		ResultSet myRs = null;
		try {
			myConn = DBConnection.getConnection();
			String theDepartment = "Legal";

			// Prepare the stored procedure call
			myStmt = myConn.prepareCall("{call sp_count_for_department_s(?, ?)}");

			// Set the parameters
			myStmt.setString(1, theDepartment);
			myStmt.registerOutParameter(2, Types.INTEGER);

			// Call stored procedure
			myStmt.execute();

			// Get the value of the OUT parameter
			System.out.println("\nThe count = " + myStmt.getInt(2));

			myStmt.close();
			System.out.println();
			myStmt = myConn.prepareCall("{call sp_employees_for_department_s(?)}");
			myStmt.setString(1, "Engineering");
			myStmt.execute();
			myRs = myStmt.getResultSet();
			SelectQuery.display(myRs);

			myStmt.close();
			myStmt = myConn.prepareCall("{call sp_greet_the_department_s(?)}");
			myStmt.registerOutParameter(1, Types.VARCHAR);
			myStmt.setString(1, theDepartment);
			myStmt.execute();
			// Get the value of the INOUT parameter
			String theResult = myStmt.getString(1);
			System.out.println("\nThe result = " + theResult);

			myStmt.close();
			int theIncreaseAmount = 12000;
			// showSalaries(myConn, theDepartment);
			myStmt = myConn.prepareCall("{call sp_increase_salaries_for_department_u(?, ?)}");
			myStmt.setString(1, theDepartment);
			myStmt.setDouble(2, theIncreaseAmount);
			myStmt.execute();
			System.out.println("\n\nSalaries AFTER Update\n");
			// showSalaries(myConn, theDepartment);
			PreparedStatement myPrepStmt = myConn.prepareStatement("select * from employees where department=?");
			myPrepStmt.setString(1, theDepartment);
			myRs = myPrepStmt.executeQuery();
			SelectQuery.display(myRs);
			myPrepStmt.close();

		}
		catch(SQLException sE) {
			System.out.println(sE.getMessage());
		}
		finally {
			myConn.close();
			myStmt.close();;
		}
	}
}

class SelectQuery {

	public void select() throws SQLException {
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		try {
			myConn = DBConnection.getConnection();

			myStmt = myConn.prepareStatement("select * from employees where salary > ? and department=?");
			myStmt.setDouble(1, 80000);
			myStmt.setString(2, "Legal");

			myRs = myStmt.executeQuery();
			display(myRs);

			System.out.println("\n\nReuse the prepared statement:  salary > 25000,  department = HR");
			myStmt.setDouble(1, 25000);
			myStmt.setString(2, "HR");

			myRs = myStmt.executeQuery();
			display(myRs);
		}
		catch(Exception exc) {
			exc.printStackTrace();
		}
		finally {
			myRs.close();
			myStmt.close();
			myConn.close();
		}
	}

	protected static void display(ResultSet myRs) throws SQLException {
		while(myRs.next()) {
			String lastName = myRs.getString("last_name");
			String firstName = myRs.getString("first_name");
			double salary = myRs.getDouble("salary");
			String department = myRs.getString("department");

			System.out.printf("%s, %s, %.2f, %s\n", lastName, firstName, salary, department);
		}
	}
}

class UpdateQuery {

	public void update() throws SQLException {
		// read new salary from command line
		Scanner scanner = new Scanner(System.in);
		Connection myConn = DBConnection.getConnection();
		try {
			PreparedStatement myStmt = myConn.prepareStatement("select * from employees");
			ResultSet myRs = myStmt.executeQuery();
			display(myRs);

			System.out.print("Enter new salary for employees: ");
			double newSalary = scanner.nextDouble();

			// perform salary update
			PreparedStatement salaryStmt = myConn.prepareStatement("update employees set salary=? where last_name = ?");
			salaryStmt.setDouble(1, newSalary);
			salaryStmt.setString(2, "Adams");
			salaryStmt.executeUpdate();

			System.out.println("\n\nEmployee Salaries");
			display(myStmt.executeQuery());
			myStmt.close();
			myRs.close();
			salaryStmt.close();
		}
		catch(SQLException s) {
			System.out.println(s.getMessage());
		}
		finally {
			scanner.close();
			myConn.close();
		}
	}

	private static void display(ResultSet myRs) throws SQLException {
		while(myRs.next()) {
			String lastName = myRs.getString("last_name");
			String firstName = myRs.getString("first_name");
			double salary = myRs.getDouble("salary");
			double previousSalary = myRs.getDouble("previous_salary");

			System.out.printf("%-15s\t %-15s Salary=%.2f\t\t Previous Salary=%.2f\n", lastName, firstName, salary, previousSalary);
		}
		System.out.println();
	}
}
