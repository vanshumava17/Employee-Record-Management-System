package com.thinking.machines.hr.bl.managers;

import com.thinking.machines.hr.bl.interfaces.managers.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.enums.Managers.Employee;
import com.thinking.machines.hr.bl.exceptions.*;

import java.math.BigDecimal;

import java.util.*;
import com.thinking.machines.network.common.*;
import com.thinking.machines.network.client.*;
import com.thinking.machines.network.common.exceptions.*;

public class EmployeeManager implements EmployeeManagerInterface {
    private static EmployeeManager employeeManager = null;

    private EmployeeManager() {
    }

    public static EmployeeManager getEmployeeManager() throws BLException {
        if (employeeManager == null)
            employeeManager = new EmployeeManager();
        return employeeManager;
    }

    public void addEmployee(EmployeeInterface employee) throws BLException {
        BLException blException = new BLException();
        // validations
        if (employee == null) {
            blException.setGenericException("Employee required");
            throw blException;
        }
        // validating employeeId
        String employeeId = employee.getEmployeeId();
        if (employeeId != null) {
            employeeId = employeeId.trim();
            if (employeeId.length() > 0) {
                blException.addException("employeeId", "Employee Id should be null");
            }
        }

        // validating name
        String name = employee.getName();
        if (name == null) {
            blException.addException("name", "Employee name required");
            name = "";
        } else {
            name = name.trim();
            if (name.length() == 0) {
                blException.addException("name", "Employee name required");
            }
        }

        // validating designation
        DesignationInterface designation = employee.getDesignation();
        DesignationManagerInterface designationManager = DesignationManager.getDesignationManager();
        int designationCode;
        if (designation == null) {
            blException.addException("designation", "Designation required");
        }
        // else {
        // designationCode = designation.getCode();
        // if (designationManager.designationCodeExists(designationCode) == false) {
        // blException.addException("designation", "Invalid Designation");
        // }
        // }

        // validating dateOfBirth
        Date dateOfBirth = employee.getDateOfBirth();
        if (dateOfBirth == null) {
            blException.addException("dateOfBirth", "Date of Birth required");
        }

        // validating gender
        char gender = employee.getGender();
        if (gender == ' ') {
            blException.addException("gender", "Gender required");
        }

        // validating isIndian
        boolean isIndian = employee.getIsIndian();

        // validating basicSalary
        BigDecimal basicSalary = employee.getBasicSalary();
        if (basicSalary == null) {
            blException.addException("basicSalary", "Basic Salary required");
        } else {
            if (basicSalary.signum() == -1) {
                blException.addException("basicSalary", "Basic Salary cannot be negative");
            }
        }

        // validating panNumber
        String panNumber = employee.getPANNumber();
        if (panNumber == null) {
            blException.addException("panNumber", "PAN Number required");
            panNumber = "";
        } else {
            if (panNumber.length() == 0) {
                blException.addException("panNumber", "PAN Number required");
            }
        }
        if (panNumber.length() > 0) {
            panNumber = panNumber.trim();
            // if (panNumberWiseEmployeesMap.containsKey(panNumber.toUpperCase())) {
            // blException.addException("panNumber", "PAN Number " + panNumber + " exists");
            // }
        }

        // validation aadharCardNumber
        String aadharCardNumber = employee.getAadharCardNumber();
        if (aadharCardNumber == null) {
            blException.addException("aadharNumber", "Aadhar Card Number required");
            aadharCardNumber = "";
        } else {
            if (aadharCardNumber.length() == 0) {
                blException.addException("aadharCardNumber", "Aadhar Card Number required");
            }
        }
        if (aadharCardNumber.length() > 0) {
            aadharCardNumber = aadharCardNumber.trim();
            // if
            // (aadharCardNumberWiseEmployeesMap.containsKey(aadharCardNumber.toUpperCase()))
            // {
            // blException.addException("aadharCardNumber", "Aadhar Card Number " +
            // aadharCardNumber + " exists");
            // }
        }
        // validations completed
        if (blException.hasExceptions()) {
            throw blException;
        }

        // preparing request
        Request request = new Request();
        request.setManager(Managers.getManagerType(Managers.EMPLOYEE));
        request.setAction(Managers.getActionType(Managers.Employee.ADD));
        request.setArguments(employee);
        NetworkClient client = new NetworkClient();
        try {
            Response response = client.send(request);
            if (response.hasException()) {
                blException = (BLException) response.getException();
                throw blException;
            }
            EmployeeInterface e = (EmployeeInterface) response.getResult();
            employee.setEmployeeId(e.getEmployeeId());
        } catch (NetworkException networkException) {
            blException.setGenericException(networkException.getMessage());
            throw blException;
        }

    }

    public void updateEmployee(EmployeeInterface employee) throws BLException {
        BLException blException = new BLException();
        // validations
        if (employee == null) {
            blException.setGenericException("Employee required");
            throw blException;
        }
        // Extracting fields in employee
        String employeeId = employee.getEmployeeId();
        String name = employee.getName();
        DesignationInterface designation = employee.getDesignation();
        DesignationManagerInterface designationManager = DesignationManager.getDesignationManager();
        int designationCode;
        Date dateOfBirth = employee.getDateOfBirth();
        char gender = employee.getGender();
        boolean isIndian = employee.getIsIndian();
        BigDecimal basicSalary = employee.getBasicSalary();
        String panNumber = employee.getPANNumber();
        String aadharCardNumber = employee.getAadharCardNumber();

        // validating employeeId
        if (employeeId == null) {
            blException.addException("employeeId", "Employee Id required");
        } else {
            employeeId = employeeId.trim();
            if (employeeId.length() == 0) {
                blException.addException("employeeId", "Employee Id required");
            }
        }
        // validating name
        if (name == null) {
            blException.addException("name", "Employee name required");
            name = "";
        } else {
            name = name.trim();
            if (name.length() == 0) {
                blException.addException("name", "Employee name required");
            }
        }
        // validating designation
        if (designation == null) {
            blException.addException("designation", "Designation required");
        }
        // validating dateOfBirth
        if (dateOfBirth == null) {
            blException.addException("dateOfBirth", "Date of Birth required");
        }
        // validating gender
        if (gender == ' ') {
            blException.addException("gender", "Gender required");
        }
        // validating isIndian
        // validating basicSalary
        if (basicSalary == null) {
            blException.addException("basicSalary", "Basic Salary required");
        } else {
            if (basicSalary.signum() == -1) {
                blException.addException("basicSalary", "Basic Salary cannot be negative");
            }
        }
        // validating panNumber
        if (panNumber == null) {
            blException.addException("panNumber", "PAN Number required");
            panNumber = "";
        } else {
            panNumber = panNumber.trim();
            if (panNumber.length() == 0) {
                blException.addException("panNumber", "PAN Number required");
            }
        }

        // validation aadharCardNumber
        if (aadharCardNumber == null) {
            blException.addException("aadharNumber", "Aadhar Card Number required");
            aadharCardNumber = "";
        } else {
            aadharCardNumber = aadharCardNumber.trim();
            if (aadharCardNumber.length() == 0) {
                blException.addException("aadharCardNumber", "Aadhar Card Number required");
            }
        }
        // validations completed
        if (blException.hasExceptions()) {
            throw blException;
        }

        // preparing request to update
        Request request = new Request();
        request.setManager(Managers.getManagerType(Managers.EMPLOYEE));
        request.setAction(Managers.getActionType(Managers.Employee.UPDATE));
        request.setArguments(employee);
        NetworkClient client = new NetworkClient();
        try {
            Response response = client.send(request);
            if (response.hasException()) {
                blException = (BLException) response.getException();
                throw blException;
            }
        } catch (NetworkException networkException) {
            blException.setGenericException(networkException.getMessage());
            throw blException;
        }
    }

    public void removeEmployee(String employeeId) throws BLException {
        BLException blException = new BLException();
        if (employeeId == null) {
            blException.addException("employeeId", "Employee Id required");
            throw blException;
        } else {
            employeeId = employeeId.trim();
            if (employeeId.length() == 0) {
                blException = new BLException();
                blException.addException("employeeId", "Employee Id required");
                throw blException;
            }
        }

        // preparing request
        Request request = new Request();
        request.setManager(Managers.getManagerType(Managers.EMPLOYEE));
        request.setAction(Managers.getActionType(Managers.Employee.REMOVE));
        request.setArguments(employeeId);
        NetworkClient client = new NetworkClient();
        try {
            Response response = client.send(request);
            if (response.hasException()) {
                blException = (BLException) response.getException();
                throw blException;
            }
        } catch (NetworkException networkException) {
            blException.setGenericException(networkException.getMessage());
            throw blException;
        }
    }

    public EmployeeInterface getEmployeeByEmployeeId(String employeeId) throws BLException {
        BLException blException = new BLException();
        if (employeeId == null) {
            blException.addException("employeeId", "EmployeeId required");
            throw blException;
        } else {
            employeeId = employeeId.trim();
            if (employeeId.length() == 0) {
                blException.addException("employeeId", "Employee Id required");
                throw blException;
            }
        }

        // preparing request
        Request request = new Request();
        request.setManager(Managers.getManagerType(Managers.EMPLOYEE));
        request.setAction(Managers.getActionType(Managers.Employee.GET_BY_ID));
        request.setArguments(employeeId);
        NetworkClient client = new NetworkClient();
        try {
            Response response = client.send(request);
            if (response.hasException()) {
                blException = (BLException) response.getException();
                throw blException;
            }
            EmployeeInterface employee = (EmployeeInterface) response.getResult();
            return employee;
        } catch (NetworkException networkException) {
            blException.setGenericException(networkException.getMessage());
            throw blException;
        }
    }

    public EmployeeInterface getEmployeeByPANNumber(String panNumber) throws BLException {
        BLException blException = new BLException();
        if (panNumber == null) {
            blException.addException("panNumber", "PAN Number required");
            throw blException;
        } else {
            panNumber = panNumber.trim();
            if (panNumber.length() == 0) {
                blException.addException("panNumber", "PAN Number required");
                throw blException;
            }
        }

        // preparing request
        Request request = new Request();
        request.setManager(Managers.getManagerType(Managers.EMPLOYEE));
        request.setAction(Managers.getActionType(Managers.Employee.GET_BY_PAN_NUMBER));
        request.setArguments(panNumber);
        NetworkClient client = new NetworkClient();
        try {
            Response response = client.send(request);
            if (response.hasException()) {
                blException = (BLException) response.getException();
                throw blException;
            }
            EmployeeInterface employee = (EmployeeInterface) response.getResult();
            return employee;
        } catch (NetworkException networkException) {
            blException.setGenericException(networkException.getMessage());
            throw blException;
        }
    }

    public EmployeeInterface getEmployeeByAadharCardNumber(String aadharCardNumber) throws BLException {
        BLException blException = new BLException();
        if (aadharCardNumber == null) {
            blException.addException("aadharCardNumber", "Aadhar Card Number required");
            throw blException;
        } else {
            aadharCardNumber = aadharCardNumber.trim();
            if (aadharCardNumber.length() == 0) {
                blException.addException("aadharCardNumber", "Aadhar Card Number required");
                throw blException;
            }
        }
        // preparing request
        Request request = new Request();
        request.setManager(Managers.getManagerType(Managers.EMPLOYEE));
        request.setAction(Managers.getActionType(Managers.Employee.GET_BY_AADHAR_CARD_NUMBER));
        request.setArguments(aadharCardNumber);
        NetworkClient client = new NetworkClient();
        try {
            Response response = client.send(request);
            if (response.hasException()) {
                blException = (BLException) response.getException();
                throw blException;
            }
            EmployeeInterface employee = (EmployeeInterface) response.getResult();
            return employee;
        } catch (NetworkException networkException) {
            blException.setGenericException(networkException.getMessage());
            throw blException;
        }
    }

    public int getEmployeeCount() throws BLException {
        BLException blException =new BLException();
        // preparing request
        Request request = new Request();
        request.setManager(Managers.getManagerType(Managers.EMPLOYEE));
        request.setAction(Managers.getActionType(Managers.Employee.GET_EMPLOYEE_COUNT));
        NetworkClient client = new NetworkClient();
        try {
            Response response = client.send(request);
            if (response.hasException()) {
                blException = (BLException) response.getException();
                throw blException;
            }
            int employeeCount = (Integer) response.getResult();
            return employeeCount;
        } catch (NetworkException networkException) {
            blException.setGenericException(networkException.getMessage());
            throw blException;
        } catch (Exception exception) {
            blException.setGenericException(exception.getMessage());
            throw blException;
        }
    }

    public boolean employeeIdExists(String employeeId) {
        if (employeeId == null)
            return false;
        employeeId = employeeId.trim();
        if (employeeId.length() == 0)
            return false;

        Request request = new Request();
        request.setManager(Managers.getManagerType(Managers.EMPLOYEE));
        request.setAction(Managers.getActionType(Managers.Employee.EMPLOYEE_ID_EXISTS));
        request.setArguments(employeeId);
        NetworkClient client = new NetworkClient();
        try {
            Response response = client.send(request);
            boolean employeeIdExists = (Boolean) response.getResult();
            return employeeIdExists;
        } catch (NetworkException networkException) {
            System.out.println("ERROR: proxybl : employeeIdExists");
            return false;
        } catch (Exception exception) {
            System.out.println("ERROR: proxybl : employeeIdExists");
            return false;
        }
    }

    public boolean employeePANNumberExists(String panNumber) {
        if (panNumber == null)
            return false;
        panNumber = panNumber.trim();
        if (panNumber.length() == 0)
            return false;

        Request request = new Request();
        request.setManager(Managers.getManagerType(Managers.EMPLOYEE));
        request.setAction(Managers.getActionType(Managers.Employee.EMPLOYEE__PAN_NUMBER_EXISTS));
        request.setArguments(panNumber);
        NetworkClient client = new NetworkClient();
        try {
            Response response = client.send(request);
            boolean panNumberExists = (Boolean) response.getResult();
            return panNumberExists;
        } catch (NetworkException networkException) {
            System.out.println("ERROR: proxybl : panNumberExists");
            return false;
        } catch (Exception exception) {
            System.out.println("ERROR: proxybl : panNumberExists");
            return false;
        }
    }

    public boolean employeeAadharCardNumberExists(String aadharCardNumber) {
        if (aadharCardNumber == null)
            return false;
        aadharCardNumber = aadharCardNumber.trim();
        if (aadharCardNumber.length() == 0)
            return false;

        Request request = new Request();
        request.setManager(Managers.getManagerType(Managers.EMPLOYEE));
        request.setAction(Managers.getActionType(Managers.Employee.EMPLOYEE_AADHAR_CARD_NUMBER_EXISTS));
        request.setArguments(aadharCardNumber);

        NetworkClient client = new NetworkClient();
        try {
            Response response = client.send(request);
            boolean aadharCardNumberExists = (Boolean) response.getResult();
            return aadharCardNumberExists;
        } catch (NetworkException networkException) {
            System.out.println("ERROR: proxybl : aadharCardNumberExists");
            return false;
        } catch (Exception exception) {
            System.out.println("ERROR: proxybl : aadharCardNumberExists");
            return false;
        }
    }

    public Set<EmployeeInterface> getEmployees() throws BLException{
        BLException blException = new BLException();
        Request request = new Request();
        request.setManager(Managers.getManagerType(Managers.EMPLOYEE));
        request.setAction(Managers.getActionType(Managers.Employee.GET_ALL));
        NetworkClient client = new NetworkClient();
        Set<EmployeeInterface> employeesSet = new HashSet<>();
        try{
            Response response = client.send(request); 
            if (response.hasException()) {
                blException = (BLException) response.getException();
                throw blException;
            }
            employeesSet = (Set<EmployeeInterface>) response.getResult();
            return employeesSet;
        } catch (NetworkException networkException) {
            blException.setGenericException(networkException.getMessage());
            throw blException;
        } catch (Exception exception) {
            blException.setGenericException(exception.getMessage());
            throw blException;
        }
    }

    public Set<EmployeeInterface> getEmployeesByDesignationCode(int designationCode) throws BLException {
        BLException blException = new BLException();
        Request request = new Request();
        request.setManager(Managers.getManagerType(Managers.EMPLOYEE));
        request.setAction(Managers.getActionType(Managers.Employee.GET_BY_DESIGNATION_CODE));
        request.setArguments(designationCode);
        NetworkClient client = new NetworkClient();
        Set<EmployeeInterface> employeesSet = new HashSet<>();
        try{
            Response response = client.send(request); 
            if (response.hasException()) {
                blException = (BLException) response.getException();
                throw blException;
            }
            employeesSet = (Set<EmployeeInterface>) response.getResult();
            return employeesSet;
        }catch (NetworkException networkException) {
            blException.setGenericException(networkException.getMessage());
            throw blException;
        } catch (Exception exception) {
            blException.setGenericException(exception.getMessage());
            throw blException;
        }
    }

    public int getEmployeeCountByDesignationCode(int designationCode) throws BLException {
        BLException blException = new BLException();
        Request request = new Request();
        request.setManager(Managers.getManagerType(Managers.EMPLOYEE));
        request.setAction(Managers.getActionType(Managers.Employee.GET_EMPLOYEE_COUNT_BY_DESIGNATION_CODE));
        request.setArguments(designationCode);
        NetworkClient client = new NetworkClient();
        try{
            Response response = client.send(request); 
            if(response.hasException()){
                blException = (BLException) response.getException();
                throw blException;
            }
            int count = (Integer) response.getResult();
            return count;
        }catch (NetworkException networkException) {
            blException.setGenericException(networkException.getMessage());
            throw blException;
        } catch (Exception exception) {
            blException.setGenericException(exception.getMessage());
            throw blException;
        }
    }

    public boolean designationAlloted(int designationCode) throws BLException {
        if(designationCode <= 0) return false;
        BLException blException = new BLException();
        Request request = new Request();
        request.setManager(Managers.getManagerType(Managers.EMPLOYEE));
        request.setAction(Managers.getActionType(Managers.Employee.IS_DESIGNATION_ALLOTED));
        request.setArguments(designationCode);
        NetworkClient client = new NetworkClient();
        try{
            Response response = client.send(request);
            if(response.hasException()){
                blException = (BLException) response.getException();
                throw blException;
            }
            boolean isAllotated = (Boolean) response.getResult();
            return isAllotated;
        }catch (NetworkException networkException) {
            blException.setGenericException(networkException.getMessage());
            throw blException;
        } catch (Exception exception) {
            blException.setGenericException(exception.getMessage());
            throw blException;
        }
    }
}
