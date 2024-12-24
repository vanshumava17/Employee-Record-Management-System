import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.dao.*;
import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.enums.*;
import java.util.*;
import java.math.*;
import java.text.*;
public class EmployeeAddTestCase 
{
public static void main(String gg[])
{
String name = gg[0];
int designationCode = Integer.parseInt(gg[1]);
Date dateOfBirth = new Date();
SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
try
{
dateOfBirth = sdf.parse(gg[2]);
}catch(ParseException pe)
{
//nothing to do here
}
char fGender = (gg[3].charAt(0));
GENDER gender;
if(fGender == 'M' || fGender == 'm')gender = GENDER.MALE;
else gender = GENDER.FEMALE;
boolean isIndian = Boolean.parseBoolean(gg[4]);
BigDecimal basicSalary = new BigDecimal(gg[5]);
String panNumber = gg[6];
String aadharCardNumber = gg[7];
try
{
EmployeeDTOInterface employeeDTO = new EmployeeDTO();
employeeDTO.setName(name);
employeeDTO.setDesignationCode(designationCode);
employeeDTO.setDateOfBirth(dateOfBirth);
employeeDTO.setGender(gender);
employeeDTO.setIsIndian(isIndian);
employeeDTO.setBasicSalary(basicSalary);
employeeDTO.setPANNumber(panNumber);
employeeDTO.setAadharCardNumber(aadharCardNumber);

EmployeeDAOInterface employeeDAO =new EmployeeDAO();
employeeDAO.add(employeeDTO);
System.out.println("Employee added, EmployeeId is "+employeeDTO.getEmployeeId());


}catch(DAOException daoException)
{
System.out.println(daoException.getMessage());
}
}
}