import com.thinking.machines.hr.dl.dao.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import java.util.*;
public class DesignationGetAllTestCase
{
public static void main(String gg[])
{
try
{
Set<DesignationDTOInterface> designations = new TreeSet<>();
DesignationDAOInterface designationDAO = new DesignationDAO();
designations = designationDAO.getAll();
designations.forEach((d)->{
System.out.println("Code : "+d.getCode()+" ,Designation : "+d.getTitle());
});
}
catch(DAOException de)
{
System.out.println("Error");
System.out.println(de.getMessage());
}
}
} 
