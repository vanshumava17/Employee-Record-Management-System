import com.thinking.machines.hr.bl.interfaces.managers.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.hr.bl.managers.*;

import java.math.BigDecimal;
import java.text.*;
import com.thinking.machines.enums.*;
import com.thinking.machines.hr.bl.exceptions.*;
import java.util.*;

public class EmployeeManagerAddTestCase
{
public static void main(String[] args) 
{
try
{
EmployeeInterface employee = new Employee();
employee.setName("Vidhi Chourasiya");
DesignationManager designationManager = DesignationManager.getDesignationManager();
DesignationInterface designation = designationManager.getDesignationByCode(3); 
employee.setDesignation(designation);
Date dateOfBirth = new Date();
SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
try
{
dateOfBirth = sdf.parse("24/10/2002");
}catch(ParseException pe)
{
// do nothing
}
employee.setDateOfBirth(dateOfBirth);
employee.setGender(GENDER.FEMALE);
employee.setIsIndian(true);
employee.setBasicSalary(new BigDecimal("1200000"));
employee.setPANNumber("PAN24102002");
employee.setAadharCardNumber("ADN24102002");
EmployeeManagerInterface employeeManager = EmployeeManager.getEmployeeManager();
employeeManager.addEmployee(employee);
System.out.println("Employee added");
}catch(BLException blException)
{
List<String> properties = blException.getProperties();
properties.forEach((property)->{
System.out.println(blException.getException(property));
});
}
}
}