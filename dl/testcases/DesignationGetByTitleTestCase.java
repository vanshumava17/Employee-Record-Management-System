import com.thinking.machines.hr.dl.dao.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
public class DesignationGetByTitleTestCase
{
public static void main(String gg[])
{
String title = gg[0];
try
{
DesignationDAOInterface designationDAO = new DesignationDAO();
DesignationDTOInterface designationDTO = new DesignationDTO();
designationDTO = designationDAO.getByTitle(title);
System.out.println("Code is " + designationDTO.getCode()+" ,Title is " + designationDTO.getTitle());
}
catch(DAOException de)
{
System.out.println(de.getMessage());
}
}
}