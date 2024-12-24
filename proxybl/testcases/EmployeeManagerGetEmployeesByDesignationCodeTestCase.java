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

public class EmployeeManagerGetEmployeesByDesignationCodeTestCase {
    public static void main(String[] args) {
        int desigantionCode = Integer.parseInt(args[0]);
        try {
            EmployeeManagerInterface employeeManager = EmployeeManager.getEmployeeManager();
            Set<EmployeeInterface> employees = employeeManager.getEmployeesByDesignationCode(desigantionCode);
            if(employees == null)
            {
                System.out.println("Employees not found against DesignationCode "+desigantionCode );
            }
            employees.forEach((employee)->{
                System.out.println("EmployeeId : " + employee.getEmployeeId());
                System.out.println("Name  : " + employee.getName());
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                System.out.println("D.O.B. : " + sdf.format(employee.getDateOfBirth()));
                System.out.println("Designation " + employee.getDesignation().getTitle());
                System.out.println("Gender " + employee.getGender());
                System.out.println("Is Indian " + employee.getIsIndian());
                System.out.println("Basic Salary " + employee.getBasicSalary());
                System.out.println("PAN number " + employee.getPANNumber());
                System.out.println("Aadhar number " + employee.getAadharCardNumber());
                System.out.println();
                System.out.println("________________________________________________________");
                System.out.println();
            });
        } catch (BLException blException) {
            List<String> properties = blException.getProperties();
            properties.forEach((property) -> {
                System.out.println(blException.getException(property));
            });
        }
    }
}
