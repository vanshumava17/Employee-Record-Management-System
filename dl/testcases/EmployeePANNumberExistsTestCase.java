import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.dao.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.exceptions.*;
public class EmployeePANNumberExistsTestCase
{
public static void main(String gg[])
{
String panNumber = gg[0];
try
{
EmployeeDAOInterface employeeDAO = new EmployeeDAO();
EmployeeDTOInterface employeeDTO = new EmployeeDTO();
System.out.println("Employee Id Exists ? : "+employeeDAO.panNumberExists(panNumber));
}catch(DAOException daoException)
{
System.out.println(daoException.getMessage());
}
}
}