import com.thinking.machines.hr.bl.interfaces.managers.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.hr.bl.managers.*;
import com.thinking.machines.hr.bl.exceptions.*;
import java.math.BigDecimal;
import java.text.*;
import com.thinking.machines.enums.*;
import com.thinking.machines.hr.bl.exceptions.*;
import java.util.*;

public class EmployeeManagerRemoveEmployeeTestCase {
    public static void main(String[] args) {
        try {
            String employeeId = args[0];
            EmployeeManagerInterface employeeManager = EmployeeManager.getEmployeeManager();
            employeeManager.removeEmployee(employeeId);
            System.out.println("Employee with EmployeeId : "+employeeId+" removed");
        } catch (BLException blException) {
            List<String> properties = blException.getProperties();
            properties.forEach((property) -> {
                System.out.println(blException.getException(property));
            });
        }
    }
}
