import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.dao.*;
import com.thinking.machines.hr.dl.exceptions.*;
public class EmployeeGetCountByDesignationCodeTestCase
{
public static void main(String gg[])
{
int designationCode = Integer.parseInt(gg[0]);
try
{
EmployeeDAOInterface employeeDAO = new EmployeeDAO();
System.out.println("Employee Count By designationCode : "+designationCode+" is : "+employeeDAO.getCountByDesignationCode(designationCode));
}catch(DAOException daoException)
{
System.out.println(daoException.getMessage());
}
}
}