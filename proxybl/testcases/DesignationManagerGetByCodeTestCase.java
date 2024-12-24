import com.thinking.machines.hr.bl.interfaces.managers.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.hr.bl.managers.*;
import com.thinking.machines.hr.bl.pojo.*;
import java.util.*;
public class DesignationManagerGetByCodeTestCase
{
public static  void main(String gg[])
{
int code = Integer.parseInt(gg[0]);
try
{
DesignationManagerInterface designationManager = DesignationManager.getDesignationManager();
DesignationInterface designation = designationManager.getDesignationByCode(code);
System.out.println("Code : "+designation.getCode()+" Designation : "+designation.getTitle());
}catch(BLException blException)
{
List<String> properties = blException.getProperties();
properties.forEach((property)->{
System.out.println(blException.getException(property));
});
}
}
}