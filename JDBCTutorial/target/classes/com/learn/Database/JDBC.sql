create database SqlServerDB;

Use SQLSERVERDB;

--TESTING GIT

create table Users(
  id  int primary key,
  name varchar(20),
  email varchar(20),
  country varchar(20),
  password varchar(20)
);

INSERT INTO Users (id, name, email, country, password) 
VALUES (1, 'Umashankar', 'uss@apple.com', 'India', 'uss@123');

INSERT INTO Users (id, name, email, country, password) 
VALUES (2, 'Aditya', 'adi@apple.com', 'India', 'adi@123');

select * from users;


drop table if exists employees;

CREATE TABLE employees (
  id int NOT NULL  IDENTITY(1,1),
  last_name varchar(64) DEFAULT NULL,
  first_name varchar(64) DEFAULT NULL,
  email varchar(64) DEFAULT NULL,
  department varchar(64) DEFAULT NULL,
  salary DECIMAL(10,2) DEFAULT NULL,
  PRIMARY KEY (id)
);

INSERT INTO employees (last_name,first_name,email, department, salary) VALUES ('Doe','John','john.doe@foo.com', 'HR', 55000.00);
INSERT INTO employees (last_name,first_name,email, department, salary) VALUES ('Public','Mary','mary.public@foo.com', 'Engineering', 75000.00);
INSERT INTO employees (last_name,first_name,email, department, salary) VALUES ('Queue','Susan','susan.queue@foo.com', 'Legal', 130000.00);

INSERT INTO employees (last_name,first_name,email, department, salary) VALUES ('Williams','David','david.williams@foo.com', 'HR', 120000.00);
INSERT INTO employees (last_name,first_name,email, department, salary) VALUES ('Johnson','Lisa','lisa.johnson@foo.com', 'Engineering', 50000.00);
INSERT INTO employees (last_name,first_name,email, department, salary) VALUES ('Smith','Paul','paul.smith@foo.com', 'Legal', 100000.00);

INSERT INTO employees (last_name,first_name,email, department, salary) VALUES ('Adams','Carl','carl.adams@foo.com', 'HR', 50000.00);
INSERT INTO employees (last_name,first_name,email, department, salary) VALUES ('Brown','Bill','bill.brown@foo.com', 'Engineering', 50000.00);
INSERT INTO employees (last_name,first_name,email, department, salary) VALUES ('Thomas','Susan','susan.thomas@foo.com', 'Legal', 80000.00);

INSERT INTO employees (last_name,first_name,email, department, salary) VALUES ('Davis','John','john.davis@foo.com', 'HR', 45000.00);
INSERT INTO employees (last_name,first_name,email, department, salary) VALUES ('Fowler','Mary','mary.fowler@foo.com', 'Engineering', 65000.00);
INSERT INTO employees (last_name,first_name,email, department, salary) VALUES ('Waters','David','david.waters@foo.com', 'Legal', 90000.00);


SELECT *FROM employees

alter table employees add previous_salary decimal(10,2) DEFAULT NULL;

----------------------------------------------------------------------------------
IF EXISTS (SELECT * FROM sys.objects WHERE NAME = 'sp_count_for_department_s' AND TYPE = 'P' AND schema_id = SCHEMA_ID('dbo'))
BEGIN
    PRINT 'Dropping PROCEDURE: sp_count_for_department_s'
    DROP PROC sp_count_for_department_s
END
GO

CREATE PROCEDURE [dbo].sp_count_for_department_s (
	@the_department varchar(64),
	@the_total int output
)
AS
BEGIN
	SELECT @the_total = COUNT(*) FROM employees where department=@the_department; 
END
GO

PRINT 'Created Procedure: sp_count_for_department_s'
----------------------------------------------------------------------------------

----------------------------------------------------------------------------------
IF EXISTS (SELECT * FROM sys.objects WHERE NAME = 'sp_employees_for_department_s' AND TYPE = 'P' AND schema_id = SCHEMA_ID('dbo'))
BEGIN
    PRINT 'Dropping PROCEDURE: sp_employees_for_department_s'
    DROP PROC sp_employees_for_department_s
END
GO

CREATE PROCEDURE [dbo].sp_employees_for_department_s(
	@the_department varchar(64)
)
AS
BEGIN
	SELECT * from employees where department=@the_department;
END
GO

PRINT 'Created Procedure: sp_employees_for_department_s'
----------------------------------------------------------------------------------

----------------------------------------------------------------------------------
IF EXISTS (SELECT * FROM sys.objects WHERE NAME = 'sp_greet_the_department_s' AND TYPE = 'P' AND schema_id = SCHEMA_ID('dbo'))
BEGIN
    PRINT 'Dropping PROCEDURE: sp_greet_the_department_s'
    DROP PROC sp_greet_the_department_s
END
GO

CREATE PROCEDURE [dbo].sp_greet_the_department_s(
	@the_department varchar(64) out
)
AS
BEGIN
	SET @the_department = concat('Hello to the awesome ', @the_department, ' team!');
END
GO

PRINT 'Created Procedure: sp_greet_the_department_s'
----------------------------------------------------------------------------------

----------------------------------------------------------------------------------
IF EXISTS (SELECT * FROM sys.objects WHERE NAME = 'sp_increase_salaries_for_department_u' AND TYPE = 'P' AND schema_id = SCHEMA_ID('dbo'))
BEGIN
    PRINT 'Dropping PROCEDURE: sp_increase_salaries_for_department_u'
    DROP PROC sp_increase_salaries_for_department_u
END
GO

CREATE PROCEDURE [dbo].sp_increase_salaries_for_department_u(
	@the_department varchar(64),
	@increase_amount DECIMAL(10,2)
)
AS
BEGIN
	UPDATE EMPLOYEES SET salary= salary + @increase_amount where department=@the_department;
END
GO

PRINT 'Created Procedure: sp_increase_salaries_for_department_u'
----------------------------------------------------------------------------------