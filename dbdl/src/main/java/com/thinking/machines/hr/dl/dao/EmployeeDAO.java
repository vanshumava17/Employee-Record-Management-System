package com.thinking.machines.hr.dl.dao;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.enums.*;
import java.util.*;

import javax.print.attribute.standard.PresentationDirection;

import java.io.*;
import java.math.*;
import java.sql.Connection;
import java.text.*;
import java.sql.*;
public class EmployeeDAO implements EmployeeDAOInterface
{
private static final String FILE_NAME = "employee.data";
public void add(EmployeeDTOInterface employeeDTO) throws DAOException
{
// validations starts here
if(employeeDTO == null)throw new DAOException("EmployeeDTO is null");
String employeeId;
String name = employeeDTO.getName();
if(name == null)throw new DAOException("Name is null");
name = name.trim();
if(name.length() == 0)throw new DAOException("Length of name is zerO");
int designationCode = employeeDTO.getDesignationCode();
// if we use DesignationDAO to check codes existence then in DesignationDAO connection establish and close then
// again we create connection and add employee and close so to avoid two time connection establishment we will 
// check designation codes existence here by using sql statement (JDBC)
Connection connection;
PreparedStatement preparedStatement;
ResultSet resultSet;
try
{
connection  = DAOConnection.getConnection();
preparedStatement = connection.prepareStatement("select code from designation where code=?");
preparedStatement.setInt(1,designationCode);
resultSet = preparedStatement.executeQuery();
if(resultSet.next() == false)
{
resultSet.close();
preparedStatement.close();
connection.close();
throw new DAOException("Invalid designation code : "+designationCode);
}
resultSet.close();
preparedStatement.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}

java.util.Date dateOfBirth = employeeDTO.getDateOfBirth();
if(dateOfBirth == null)
{
try
{
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
throw new DAOException("Date of birth is null");
}
char gender = employeeDTO.getGender();
boolean isIndian = employeeDTO.getIsIndian();
BigDecimal basicSalary = employeeDTO.getBasicSalary();
if(basicSalary == null)
{
try
{
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
throw new DAOException("Basic salary is null");
}
if(basicSalary.signum()==-1) 
{
try
{
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
throw new DAOException("Basic salary is negative");
}
String panNumber = employeeDTO.getPANNumber();
if(panNumber == null)
{
try
{
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
throw new DAOException("PAN number is null");
}
panNumber = panNumber.trim();
if(panNumber.length() == 0)
{
try
{
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
throw new DAOException("Length of PAN number is zero");
}
String aadharCardNumber = employeeDTO.getAadharCardNumber();
if(aadharCardNumber == null)
{
try
{
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
throw new DAOException("Aadhar card number is null");
}
aadharCardNumber = aadharCardNumber.trim();
if(aadharCardNumber.length() == 0)
{
try
{
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
throw new DAOException("Lenght of Aadhar card number is zero");
}
//validations ends here
try
{
// pannumber and aadhar number must be unique so let's check that
boolean panNumberExists = false;
preparedStatement = connection.prepareStatement("select gender from employee where pan_number=?");
preparedStatement.setString(1,panNumber);
resultSet = preparedStatement.executeQuery();
panNumberExists = resultSet.next();
resultSet.close();
preparedStatement.close();

boolean aadharCardNumberExists = false;
preparedStatement = connection.prepareStatement("select gender from employee where aadhar_card_number=?");
preparedStatement.setString(1,aadharCardNumber);
resultSet = preparedStatement.executeQuery();
aadharCardNumberExists = resultSet.next();
resultSet.close();
preparedStatement.close();

if(panNumberExists && aadharCardNumberExists)
{
connection.close();
throw new DAOException("PAN number ("+panNumber+") and Aadhar card number ("+aadharCardNumber+") exists");
}
if(panNumberExists)
{
connection.close();
throw new DAOException("PAN number ("+panNumber+") exists");
}
if(aadharCardNumberExists)
{
connection.close();
throw new DAOException("Aadhar card number ("+aadharCardNumber+") exists");
}
// finally inserting employee into database

preparedStatement = connection.prepareStatement("insert into employee (name,designation_code,date_of_birth,gender,is_indian,basic_salary,pan_number,aadhar_card_number) values (?,?,?,?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS);
preparedStatement.setString(1,name);
preparedStatement.setInt(2,designationCode);
// converting java.util.Date to java.sql.Date 
java.sql.Date sqlDateOfBirth = new java.sql.Date(dateOfBirth.getYear(),dateOfBirth.getMonth(),dateOfBirth.getDate());
preparedStatement.setDate(3, sqlDateOfBirth);
preparedStatement.setString(4,String.valueOf(gender));
preparedStatement.setBoolean(5, isIndian);
preparedStatement.setBigDecimal(6, basicSalary);
preparedStatement.setString(7,panNumber);
preparedStatement.setString(8,aadharCardNumber);
preparedStatement.executeUpdate();
resultSet = preparedStatement.getGeneratedKeys();
resultSet.next();
int generatedEmployeeId = resultSet.getInt(1);
resultSet.close();
preparedStatement.close();
connection.close();
employeeDTO.setEmployeeId("A"+(1000000+generatedEmployeeId));
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
}


public void update(EmployeeDTOInterface employeeDTO) throws DAOException
{
// validations starts here
if(employeeDTO == null)throw new DAOException("EmployeeDTO is null");
String employeeId = employeeDTO.getEmployeeId();
if(employeeId==null)throw new DAOException("Employee id is null");
employeeId = employeeId.trim();
if(employeeId.length() == 0) throw new DAOException("Length of Employee id is zero");
int actualEmployeeId =0;
try
{
actualEmployeeId = Integer.parseInt(employeeId.substring(1)) - 1000000;
}catch(Exception exception)
{
throw new DAOException("Invalid Employee Id");
}

Connection connection;
PreparedStatement preparedStatement;
ResultSet resultSet;

try
{
connection  = DAOConnection.getConnection();
preparedStatement = connection.prepareStatement("select gender from employee where employee_id=?");
preparedStatement.setInt(1,actualEmployeeId);
resultSet = preparedStatement.executeQuery();
if(resultSet.next() == false)
{
resultSet.close();
preparedStatement.close();
connection.close();
throw new DAOException("Invalid Employee Id ");
}
resultSet.close();
preparedStatement.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}

String name = employeeDTO.getName();
if(name == null)
{
try
{
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
throw new DAOException("Name is null");
}
name = name.trim();
if(name.length() == 0)
{
try
{
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
throw new DAOException("Length of name is zero");
}
int designationCode = employeeDTO.getDesignationCode();
try
{
connection  = DAOConnection.getConnection();
preparedStatement = connection.prepareStatement("select code from designation where code=?");
preparedStatement.setInt(1,designationCode);
resultSet = preparedStatement.executeQuery();
if(resultSet.next() == false)
{
resultSet.close();
preparedStatement.close();
connection.close();
throw new DAOException("Invalid designation code : "+designationCode);
}
resultSet.close();
preparedStatement.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}

java.util.Date dateOfBirth = employeeDTO.getDateOfBirth();
if(dateOfBirth == null)
{
try
{
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
throw new DAOException("Date of birth is null");
}
char gender = employeeDTO.getGender();
boolean isIndian = employeeDTO.getIsIndian();
BigDecimal basicSalary = employeeDTO.getBasicSalary();
if(basicSalary == null)
{
try
{
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
throw new DAOException("Basic salary is null");
}
if(basicSalary.signum()==-1) 
{
try
{
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
throw new DAOException("Basic salary is negative");
}
String panNumber = employeeDTO.getPANNumber();
if(panNumber == null)
{
try
{
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
throw new DAOException("PAN number is null");
}
panNumber = panNumber.trim();
if(panNumber.length() == 0)
{
try
{
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
throw new DAOException("Length of PAN number is zero");
}
String aadharCardNumber = employeeDTO.getAadharCardNumber();
if(aadharCardNumber == null)
{
try
{
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
throw new DAOException("Aadhar card number is null");
}
aadharCardNumber = aadharCardNumber.trim();
if(aadharCardNumber.length() == 0)
{
try
{
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
throw new DAOException("Lenght of Aadhar card number is zero");
}
//validations ends here
try
{
// pannumber and aadhar number must be unique so let's check that
boolean panNumberExists = false;
preparedStatement = connection.prepareStatement("select gender from employee where pan_number=? and employee_id<>?");
preparedStatement.setString(1,panNumber);
preparedStatement.setInt(2, actualEmployeeId);
resultSet = preparedStatement.executeQuery();
panNumberExists = resultSet.next();
resultSet.close();
preparedStatement.close();

boolean aadharCardNumberExists = false;
preparedStatement = connection.prepareStatement("select gender from employee where aadhar_card_number=? and employee_id<>?");
preparedStatement.setString(1,aadharCardNumber);
preparedStatement.setInt(2, actualEmployeeId);
resultSet = preparedStatement.executeQuery();
aadharCardNumberExists = resultSet.next();
resultSet.close();
preparedStatement.close();

if(panNumberExists && aadharCardNumberExists)
{
connection.close();
throw new DAOException("PAN number ("+panNumber+") and Aadhar card number ("+aadharCardNumber+") exists");
}
if(panNumberExists)
{
connection.close();
throw new DAOException("PAN number ("+panNumber+") exists");
}
if(aadharCardNumberExists)
{
connection.close();
throw new DAOException("Aadhar card number ("+aadharCardNumber+") exists");
}
// finally inserting employee into database
preparedStatement = connection.prepareStatement("update employee set name=?,designation_code=?,date_of_birth=?,gender=?,is_indian=?,basic_salary=?,pan_number=?,aadhar_card_number=? where employee_id=?");
preparedStatement.setString(1,name);
preparedStatement.setInt(2,designationCode);
// converting java.util.Date to java.sql.Date 
java.sql.Date sqlDateOfBirth = new java.sql.Date(dateOfBirth.getYear(),dateOfBirth.getMonth(),dateOfBirth.getDate());
preparedStatement.setDate(3, sqlDateOfBirth);
preparedStatement.setString(4,String.valueOf(gender));
preparedStatement.setBoolean(5, isIndian);
preparedStatement.setBigDecimal(6, basicSalary);
preparedStatement.setString(7,panNumber);
preparedStatement.setString(8,aadharCardNumber);
preparedStatement.setInt(9, actualEmployeeId);
preparedStatement.executeUpdate();
preparedStatement.close();
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
}


public void delete(String employeeId) throws DAOException
{
// validations starts here
if(employeeId==null)throw new DAOException("Employee id is null");
employeeId = employeeId.trim();
if(employeeId.length() == 0) throw new DAOException("Length of Employee id is zero");
int actualEmployeeId =0;
try
{
actualEmployeeId = Integer.parseInt(employeeId.substring(1)) - 1000000;
}catch(Exception exception)
{
throw new DAOException("Invalid Employee Id");
}
Connection connection;
PreparedStatement preparedStatement;
ResultSet resultSet;
try
{
connection  = DAOConnection.getConnection();
preparedStatement = connection.prepareStatement("select gender from employee where employee_id=?");
preparedStatement.setInt(1,actualEmployeeId);
resultSet = preparedStatement.executeQuery();
if(resultSet.next() == false)
{
resultSet.close();
preparedStatement.close();
connection.close();
throw new DAOException("Invalid Employee Id ");
}
resultSet.close();
preparedStatement.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
//validations ends here
try
{
// deleting employee
preparedStatement = connection.prepareStatement("delete from employee where employee_id=?");
preparedStatement.setInt(1, actualEmployeeId);
preparedStatement.executeUpdate();
preparedStatement.close();
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
}


public Set<EmployeeDTOInterface> getAll() throws DAOException
{
Set<EmployeeDTOInterface> employees = new TreeSet<>();
try
{
Connection connection = DAOConnection.getConnection();
Statement statement = connection.createStatement();
ResultSet resultSet = statement.executeQuery("select * from employee");
EmployeeDTO employee;
String employeeId;
String name;
int designationCode;
java.sql.Date dateOfBirth;
String gender;
boolean isIndian;
BigDecimal basicSalary;
String panNumber;
String aadharCardNumber;

while(resultSet.next())
{
employeeId = "A"+(1000000 + resultSet.getInt("employee_id"));
name = resultSet.getString("name").trim();
designationCode = resultSet.getInt("designation_code");
dateOfBirth = resultSet.getDate("date_of_birth");
gender = resultSet.getString("gender");
isIndian = resultSet.getBoolean("is_indian");
basicSalary = resultSet.getBigDecimal("basic_salary");
panNumber = resultSet.getString("pan_number").trim();
aadharCardNumber = resultSet.getString("aadhar_card_number");
// creating object of EmployeeDTO and setting its properties
employee = new EmployeeDTO();
employee.setEmployeeId(employeeId);
employee.setName(name);
employee.setDesignationCode(designationCode);
employee.setDateOfBirth(dateOfBirth);  // java.sql.Date is derived from java.util.Date
employee.setGender((gender.equalsIgnoreCase("M"))?GENDER.MALE:GENDER.FEMALE);
employee.setIsIndian(isIndian);
employee.setBasicSalary(basicSalary);
employee.setPANNumber(panNumber);
employee.setAadharCardNumber(aadharCardNumber);
employees.add(employee);
}
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
return employees;
}


public Set<EmployeeDTOInterface> getByDesignationCode(int designationCode) throws DAOException
{
if(designationCode <= 0)throw new DAOException("Invalid designationCode");
Set<EmployeeDTOInterface> employees = new TreeSet<>();
try
{
Connection connection = DAOConnection.getConnection();
PreparedStatement preparedStatement = connection.prepareStatement("select code from designation where code=?");
preparedStatement.setInt(1, designationCode);
ResultSet resultSet = preparedStatement.executeQuery();
if(resultSet.next() == false)
{
resultSet.close();
preparedStatement.close();
connection.close();
throw new DAOException("Invalid designation code");
}
resultSet.close();
preparedStatement.close();
// extract all employees from ds for the gived designation code
preparedStatement = connection.prepareStatement("select * from employee where designation_code=?");
preparedStatement.setInt(1, designationCode);
resultSet = preparedStatement.executeQuery();
EmployeeDTO employee;
String employeeId;
String name;
java.sql.Date dateOfBirth;
String gender;
boolean isIndian;
BigDecimal basicSalary;
String panNumber;
String aadharCardNumber;

while(resultSet.next())
{
employeeId = "A"+(1000000 + resultSet.getInt("employee_id"));
name = resultSet.getString("name").trim();
designationCode = resultSet.getInt("designation_code");
dateOfBirth = resultSet.getDate("date_of_birth");
gender = resultSet.getString("gender");
isIndian = resultSet.getBoolean("is_indian");
basicSalary = resultSet.getBigDecimal("basic_salary");
panNumber = resultSet.getString("pan_number").trim();
aadharCardNumber = resultSet.getString("aadhar_card_number");
// creating object of EmployeeDTO and setting its properties
employee = new EmployeeDTO();
employee.setEmployeeId(employeeId);
employee.setName(name);
employee.setDesignationCode(designationCode);
employee.setDateOfBirth(dateOfBirth);  // java.sql.Date is derived from java.util.Date
employee.setGender((gender.equalsIgnoreCase("M"))?GENDER.MALE:GENDER.FEMALE);
employee.setIsIndian(isIndian);
employee.setBasicSalary(basicSalary);
employee.setPANNumber(panNumber);
employee.setAadharCardNumber(aadharCardNumber);
employees.add(employee);
}
resultSet.close();
preparedStatement.close();
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
return employees;
}


public boolean isDesignationAlloted(int designationCode) throws DAOException
{
if(designationCode <= 0)throw new DAOException("Invalid designationCode");
boolean designationCodeAlloted = false;
try
{
Connection connection = DAOConnection.getConnection();
// validating designation code for its existence 
PreparedStatement preparedStatement = connection.prepareStatement("select code from designation where code=?");
preparedStatement.setInt(1, designationCode);
ResultSet resultSet = preparedStatement.executeQuery();
if(resultSet.next() == false)
{
resultSet.close();
preparedStatement.close();
connection.close();
throw new DAOException("Invalid designation code");
}
resultSet.close();
preparedStatement.close();
// check for designation code's alloted or not
preparedStatement = connection.prepareStatement("select gender from employee where designation_code=?");
preparedStatement.setInt(1, designationCode);
resultSet = preparedStatement.executeQuery();
designationCodeAlloted = resultSet.next();
resultSet.close();
preparedStatement.close();
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
return designationCodeAlloted;
}


public EmployeeDTOInterface getByEmployeeId(String employeeId) throws DAOException
{
if(employeeId == null)throw new DAOException("Employee ID is null");
if(employeeId.length() == 0)throw new DAOException("Length of Employee ID is zero");
int actualEmployeeId=0;
try
{
actualEmployeeId = Integer.parseInt(employeeId.substring(1))-1000000;
}catch(Exception exception)
{
throw new DAOException(exception.getMessage());
}
EmployeeDTO employee;
try
{
Connection connection = DAOConnection.getConnection();
PreparedStatement preparedStatement = connection.prepareStatement("select * from employee where employee_id=?");
preparedStatement.setInt(1, actualEmployeeId);
ResultSet resultSet = preparedStatement.executeQuery();
if(resultSet.next() == false)
{
resultSet.close();
preparedStatement.close();
connection.close();
throw new DAOException("Invalid Employee ID");
}

String name;
int designationCode;
java.sql.Date dateOfBirth;
String gender;
boolean isIndian;
BigDecimal basicSalary;
String panNumber;
String aadharCardNumber;

employeeId = "A"+(1000000 + resultSet.getInt("employee_id"));
name = resultSet.getString("name").trim();
designationCode = resultSet.getInt("designation_code");
dateOfBirth = resultSet.getDate("date_of_birth");
gender = resultSet.getString("gender");
isIndian = resultSet.getBoolean("is_indian");
basicSalary = resultSet.getBigDecimal("basic_salary");
panNumber = resultSet.getString("pan_number").trim();
aadharCardNumber = resultSet.getString("aadhar_card_number");
// creating object of EmployeeDTO and setting its properties
employee = new EmployeeDTO();
employee.setEmployeeId(employeeId);
employee.setName(name);
employee.setDesignationCode(designationCode);
employee.setDateOfBirth(dateOfBirth);  // java.sql.Date is derived from java.util.Date
employee.setGender((gender.equalsIgnoreCase("M"))?GENDER.MALE:GENDER.FEMALE);
employee.setIsIndian(isIndian);
employee.setBasicSalary(basicSalary);
employee.setPANNumber(panNumber);
employee.setAadharCardNumber(aadharCardNumber);
resultSet.close();
preparedStatement.close();
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
return employee;
}


public EmployeeDTOInterface getByPANNumber(String panNumber) throws DAOException
{
if(panNumber == null)throw new DAOException("PAN Number is null");
panNumber = panNumber.trim();
if(panNumber.length() == 0)throw new DAOException("Invalid PAN Number : lenght is zero");
EmployeeDTO employee;
try
{
Connection connection = DAOConnection.getConnection();
PreparedStatement preparedStatement = connection.prepareStatement("select * from employee where pan_number=?");
preparedStatement.setString(1, panNumber);
ResultSet resultSet = preparedStatement.executeQuery();
if(resultSet.next() == false)
{
resultSet.close();
preparedStatement.close();
connection.close();
throw new DAOException("Invalid PAN Number");
}
String employeeId;
String name;
int designationCode;
java.sql.Date dateOfBirth;
String gender;
boolean isIndian;
BigDecimal basicSalary;
String aadharCardNumber;

employeeId = "A"+(1000000 + resultSet.getInt("employee_id"));
name = resultSet.getString("name").trim();
designationCode = resultSet.getInt("designation_code");
dateOfBirth = resultSet.getDate("date_of_birth");
gender = resultSet.getString("gender");
isIndian = resultSet.getBoolean("is_indian");
basicSalary = resultSet.getBigDecimal("basic_salary");
panNumber = resultSet.getString("pan_number").trim();
aadharCardNumber = resultSet.getString("aadhar_card_number");
// creating object of EmployeeDTO and setting its properties
employee = new EmployeeDTO();
employee.setEmployeeId(employeeId);
employee.setName(name);
employee.setDesignationCode(designationCode);
employee.setDateOfBirth(dateOfBirth);  // java.sql.Date is derived from java.util.Date
employee.setGender((gender.equalsIgnoreCase("M"))?GENDER.MALE:GENDER.FEMALE);
employee.setIsIndian(isIndian);
employee.setBasicSalary(basicSalary);
employee.setPANNumber(panNumber);
employee.setAadharCardNumber(aadharCardNumber);
resultSet.close();
preparedStatement.close();
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
return employee;
}


public EmployeeDTOInterface getByAadharCardNumber(String aadharCardNumber) throws DAOException
{
if(aadharCardNumber == null)throw new DAOException("Aadhar Card Number is null");
aadharCardNumber = aadharCardNumber.trim();
if(aadharCardNumber.length() == 0)throw new DAOException("Invalid Aadhar Card Number : lenght is zero");
EmployeeDTO employee;
try
{
Connection connection = DAOConnection.getConnection();
PreparedStatement preparedStatement = connection.prepareStatement("select * from employee where aadhar_card_number=?");
preparedStatement.setString(1,aadharCardNumber);
ResultSet resultSet = preparedStatement.executeQuery();
if(resultSet.next() == false)
{
resultSet.close();
preparedStatement.close();
connection.close();
throw new DAOException("Invalid Aadhar Card Number");
}
String employeeId;
String name;
int designationCode;
java.sql.Date dateOfBirth;
String gender;
boolean isIndian;
BigDecimal basicSalary;
String panNumber;

employeeId = "A"+(1000000 + resultSet.getInt("employee_id"));
name = resultSet.getString("name").trim();
designationCode = resultSet.getInt("designation_code");
dateOfBirth = resultSet.getDate("date_of_birth");
gender = resultSet.getString("gender");
isIndian = resultSet.getBoolean("is_indian");
basicSalary = resultSet.getBigDecimal("basic_salary");
panNumber = resultSet.getString("pan_number").trim();
aadharCardNumber = resultSet.getString("aadhar_card_number");
// creating object of EmployeeDTO and setting its properties
employee = new EmployeeDTO();
employee.setEmployeeId(employeeId);
employee.setName(name);
employee.setDesignationCode(designationCode);
employee.setDateOfBirth(dateOfBirth);  // java.sql.Date is derived from java.util.Date
employee.setGender((gender.equalsIgnoreCase("M"))?GENDER.MALE:GENDER.FEMALE);
employee.setIsIndian(isIndian);
employee.setBasicSalary(basicSalary);
employee.setPANNumber(panNumber);
employee.setAadharCardNumber(aadharCardNumber);
resultSet.close();
preparedStatement.close();
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
return employee;
}


public boolean employeeIdExists(String employeeId) throws DAOException
{
if(employeeId==null) return false;
employeeId=employeeId.trim();
if(employeeId.length()==0) return false;
boolean employeeIdExists = false;
int actualEmployeeId = 0;
try
{
actualEmployeeId = Integer.parseInt(employeeId.substring(1)) - 1000000;
}catch(Exception exception)
{
throw new DAOException("Invalid Employee Id");
}
try
{
Connection connection = DAOConnection.getConnection();
PreparedStatement preparedStatement = connection.prepareStatement("select gender from employee where employee_id=?");
preparedStatement.setInt(1, actualEmployeeId);
ResultSet resultSet = preparedStatement.executeQuery();
employeeIdExists = resultSet.next();
resultSet.close();
preparedStatement.close();
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
return employeeIdExists;
}

public boolean panNumberExists(String panNumber) throws DAOException
{
if(panNumber==null) return false;
panNumber=panNumber.trim();
if(panNumber.length()==0) return false;
boolean panNumberExists = false;
try
{
Connection connection = DAOConnection.getConnection();
PreparedStatement preparedStatement = connection.prepareStatement("select gender from employee where pan_number=?");
preparedStatement.setString(1, panNumber);
ResultSet resultSet = preparedStatement.executeQuery();
panNumberExists = resultSet.next();
resultSet.close();
preparedStatement.close();
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
return panNumberExists;
}

public boolean aadharCardNumberExists(String aadharCardNumber) throws DAOException
{
if(aadharCardNumber==null) return false;
aadharCardNumber=aadharCardNumber.trim();
if(aadharCardNumber.length()==0) return false;
boolean aadharCardNumberExists = false;
try
{
Connection connection = DAOConnection.getConnection();
PreparedStatement preparedStatement = connection.prepareStatement("select gender from employee where aadhar_card_number=?");
preparedStatement.setString(1, aadharCardNumber);
ResultSet resultSet = preparedStatement.executeQuery();
aadharCardNumberExists = resultSet.next();
resultSet.close();
preparedStatement.close();
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
return aadharCardNumberExists;
}

public int getCount() throws DAOException
{
int count = 0;
try
{
Connection connection = DAOConnection.getConnection();
Statement statement = connection.createStatement();
ResultSet resultSet = statement.executeQuery("select count(*) as cnt from employee");
resultSet.next();
count = resultSet.getInt("cnt");
resultSet.close();
statement.close();
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
return count;
}

public int getCountByDesignationCode(int designationCode) throws DAOException
{
if(designationCode <= 0)throw new DAOException("Invalid designationCode");
int count = 0;
try
{
Connection connection = DAOConnection.getConnection();
PreparedStatement preparedStatement = connection.prepareStatement("select code from designation where code=?");
preparedStatement.setInt(1, designationCode);
ResultSet resultSet = preparedStatement.executeQuery();
if(resultSet.next() == false)
{
resultSet.close();
preparedStatement.close();
connection.close();
throw new DAOException("Invalid designationCode");
}
resultSet.close();
preparedStatement.close();
preparedStatement = connection.prepareStatement("select count(*) as cnt from employee where designation_code=?");
preparedStatement.setInt(1, designationCode);
resultSet = preparedStatement.executeQuery();
resultSet.next();
count = resultSet.getInt("cnt");
resultSet.close();
preparedStatement.close();
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
return count;
}
}