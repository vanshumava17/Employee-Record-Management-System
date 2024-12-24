import com.thinking.machines.hr.dl.dao.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
public class DesignationGetByCodeTestCase
{
public static void main(String gg[])
{
int code = Integer.parseInt(gg[0]);
try
{
DesignationDAOInterface designationDAO = new DesignationDAO();
DesignationDTOInterface designationDTO = new DesignationDTO();
designationDTO = designationDAO.getByCode(code);
System.out.println("Designation is "+designationDTO.getTitle());
}
catch(DAOException de)
{
System.out.println(de.getMessage());
}
}
}