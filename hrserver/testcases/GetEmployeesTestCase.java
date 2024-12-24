import com.thinking.machines.hr.server.*;

import com.thinking.machines.hr.bl.managers.*;
import com.thinking.machines.hr.server.RequestHandler;
import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.network.common.*;
import com.thinking.machines.hr.bl.interfaces.managers.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.network.server.*;

import java.util.*;

public class GetEmployeesTestCase {
    public static void main(String[] args) {
    try{
        RequestHandler rh = new RequestHandler();
        Request r = new Request();
        r.setManager("EmployeeManager");
        r.setAction("getEmployees");
        Response response = rh.process(r);
        Set<EmployeeInterface> set = (Set<EmployeeInterface>) response.getResult();

        for(EmployeeInterface emp : set){
            System.out.println(emp.getName());
        }
    }catch(Exception e) {
        System.out.println(e);
    }
    }
}
