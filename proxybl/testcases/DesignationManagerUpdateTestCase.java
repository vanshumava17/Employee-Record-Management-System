import com.thinking.machines.hr.bl.interfaces.managers.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.managers.*;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.hr.bl.exceptions.*;
import java.util.*;
public class DesignationManagerUpdateTestCase
{
public static void main(String gg[])
{
int code = Integer.parseInt(gg[0]);
String title = gg[1];
Designation designation = new Designation();
designation.setCode(code);
designation.setTitle(title);
try
{
DesignationManagerInterface designationManager = DesignationManager.getDesignationManager();
designationManager.updateDesignation(designation);
System.out.println("Designation "+designation.getTitle()+" with code "+designation.getCode()+" Updated");
}catch(BLException blException)
{
if(blException.hasGenericException())
{
System.out.println(blException.getGenericException());
}
List<String> properties = blException.getProperties();
for(String property : properties)
{
System.out.println(property + " : " + blException.getException(property));
}
}
}
}