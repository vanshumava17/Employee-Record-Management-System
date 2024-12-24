import com.thinking.machines.hr.dl.dao.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
public class DesignationTitleExistsTestCase
{
public static void main(String gg[])
{
String title = gg[0];
try
{
DesignationDAOInterface designationDAO = new DesignationDAO();
System.out.println("Title Exists : " + designationDAO.titleExists(title));
}
catch(DAOException de)
{
System.out.println(de.getMessage());
}
}
}