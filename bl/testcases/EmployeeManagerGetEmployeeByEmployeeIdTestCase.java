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

public class EmployeeManagerGetEmployeeByEmployeeIdTestCase {
    public static void main(String[] args) {
        try {
            String employeeId = args[0];
            EmployeeInterface employee = new Employee();
            EmployeeManagerInterface employeeManager = EmployeeManager.getEmployeeManager();
            employee = employeeManager.getEmployeeByEmployeeId(employeeId);
            System.out.println("EmployeeId : " + employee.getEmployeeId());
            System.out.println("Name  : " + employee.getName());
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            System.out.println("D.O.B. : " + sdf.format(employee.getDateOfBirth()));
            System.out.println("Designation " + employee.getDesignation().getTitle());
            System.out.println("Gender " + employee.getGender());
            System.out.println("Basic Salary " + employee.getBasicSalary());
            System.out.println("PAN number " + employee.getPANNumber());
            System.out.println("Aadhar number " + employee.getAadharCardNumber());
        } catch (BLException blException) {
            List<String> properties = blException.getProperties();
            properties.forEach((property) -> {
                System.out.println(blException.getException(property));
            });
        }
    }
}
