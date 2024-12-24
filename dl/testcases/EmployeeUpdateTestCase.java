import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.dao.*;
import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.enums.*;
import java.util.*;
import java.math.*;
import java.text.*;
public class EmployeeUpdateTestCase 
{
public static void main(String gg[])
{
String employeeId  = gg[0];
String name = gg[1];
int designationCode = Integer.parseInt(gg[2]);
Date dateOfBirth = new Date();
SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
try
{
dateOfBirth = sdf.parse(gg[3]);
}catch(ParseException pe)
{
//NOTHEING
}
char fGender = (gg[4].charAt(0));
GENDER gender;
if(fGender == 'M' || fGender == 'm')gender = GENDER.MALE;
else gender = GENDER.FEMALE;
boolean isIndian = Boolean.parseBoolean(gg[5]);
BigDecimal basicSalary = new BigDecimal(gg[6]);
String panNumber = gg[7];
String aadharCardNumber = gg[8];
try
{
EmployeeDTOInterface employeeDTO = new EmployeeDTO();
employeeDTO.setEmployeeId(employeeId);
employeeDTO.setName(name);
employeeDTO.setDesignationCode(designationCode);
employeeDTO.setDateOfBirth(dateOfBirth);
employeeDTO.setGender(gender);
employeeDTO.setIsIndian(isIndian);
employeeDTO.setBasicSalary(basicSalary);
employeeDTO.setPANNumber(panNumber);
employeeDTO.setAadharCardNumber(aadharCardNumber);

EmployeeDAOInterface employeeDAO =new EmployeeDAO();
employeeDAO.update(employeeDTO);
System.out.println("Employee updated, EmployeeId is "+employeeDTO.getEmployeeId());


}catch(DAOException daoException)
{
System.out.println(daoException.getMessage());
}
}
}