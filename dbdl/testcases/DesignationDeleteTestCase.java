import com.thinking.machines.hr.dl.dao.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
public class DesignationDeleteTestCase
{
public static void main(String gg[])
{
int code = Integer.parseInt(gg[0]);
try
{
DesignationDAOInterface designationDAO = new DesignationDAO();
designationDAO.delete(code);
System.out.println("Designation With Code : "+code+" deleted");
}
catch(DAOException de)
{
System.out.println(de.getMessage());
}
}
}