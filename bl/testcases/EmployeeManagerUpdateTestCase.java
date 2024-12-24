import com.thinking.machines.hr.bl.interfaces.managers.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.hr.bl.managers.*;
import com.thinking.machines.hr.bl.exceptions.*;
import java.math.BigDecimal;
import java.text.*;
import com.thinking.machines.enums.*;
import com.thinking.machines.hr.bl.exceptions.*;
import java.util.*;

public class EmployeeManagerUpdateTestCase
{
public static void main(String[] args) 
{
try
{
EmployeeInterface employee = new Employee();
employee.setEmployeeId("A10000002");
employee.setName("Vansh Umava");
DesignationInterface designation = new Designation();
designation.setCode(1);
employee.setDesignation(designation);
Date dateOfBirth = new Date();
SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
try
{
dateOfBirth = sdf.parse("17/09/2003");
}catch(ParseException pe)
{
// do nothing
}
employee.setDateOfBirth(dateOfBirth);
employee.setGender(GENDER.MALE);
employee.setIsIndian(true);
employee.setBasicSalary(new BigDecimal("9300000"));
employee.setPANNumber("PAN22022003");
employee.setAadharCardNumber("ADN22022003");
EmployeeManagerInterface employeeManager = EmployeeManager.getEmployeeManager();
employeeManager.updateEmployee(employee);
System.out.println("Employee updated");
}catch(BLException blException)
{
if(blException.hasGenericException())
{
System.out.println(blException.getGenericException());
} 
List<String> properties = blException.getProperties();
properties.forEach((property)->{
System.out.println(blException.getException(property));
});
}
}
}