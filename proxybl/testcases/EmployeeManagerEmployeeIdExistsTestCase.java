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

public class EmployeeManagerEmployeeIdExistsTestCase {
    public static void main(String[] args) {
        try {
            EmployeeManagerInterface employeeManager = EmployeeManager.getEmployeeManager();
            System.out.println("EmployeeID exists ? : " +employeeManager.employeeIdExists(null));
        } catch (BLException blException) {
            List<String> properties = blException.getProperties();
            properties.forEach((property) -> {
                System.out.println(blException.getException(property));
            });
        }
    }
}
