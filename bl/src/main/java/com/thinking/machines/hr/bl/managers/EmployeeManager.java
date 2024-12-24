package com.thinking.machines.hr.bl.managers;

import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.interfaces.managers.*;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.dao.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.enums.*;
import java.util.*;
import java.math.*;
import java.text.*;

public class EmployeeManager implements EmployeeManagerInterface {
    private static EmployeeManager employeeManager = null;
    private Map<String, EmployeeInterface> employeeIdWiseEmployeesMap;
    private Map<String, EmployeeInterface> panNumberWiseEmployeesMap;
    private Map<String, EmployeeInterface> aadharCardNumberWiseEmployeesMap;
    private Map<Integer, Set<EmployeeInterface>> designationCodeWiseEmployeesMap;
    private Set<EmployeeInterface> employeesSet;

    private EmployeeManager() throws BLException {
        populateDataStructures();
    }

    private void populateDataStructures() throws BLException {
        this.designationCodeWiseEmployeesMap = new HashMap<>();
        this.employeeIdWiseEmployeesMap = new HashMap<>();
        this.panNumberWiseEmployeesMap = new HashMap<>();
        this.aadharCardNumberWiseEmployeesMap = new HashMap<>();
        this.employeesSet = new TreeSet<>();
        try {
            Set<EmployeeDTOInterface> dlEmployees = new EmployeeDAO().getAll();
            DesignationManagerInterface designationManager = DesignationManager.getDesignationManager();
            EmployeeInterface employee;
            DesignationInterface designation;
            Set<EmployeeInterface> ets;
            for (EmployeeDTOInterface dlEmployee : dlEmployees) {
                employee = new Employee();
                employee.setEmployeeId(dlEmployee.getEmployeeId());
                employee.setName(dlEmployee.getName());
                employee.setDateOfBirth(dlEmployee.getDateOfBirth());
                designation = designationManager.getDesignationByCode(dlEmployee.getDesignationCode());
                employee.setDesignation(designation);
                if (dlEmployee.getGender() == 'M')
                    employee.setGender(GENDER.MALE);
                if (dlEmployee.getGender() == 'F')
                    employee.setGender(GENDER.FEMALE);
                employee.setIsIndian(dlEmployee.getIsIndian());
                employee.setBasicSalary(dlEmployee.getBasicSalary());
                employee.setPANNumber(dlEmployee.getPANNumber());
                employee.setAadharCardNumber(dlEmployee.getAadharCardNumber());
                // now add this employee object in all ds below
                this.employeeIdWiseEmployeesMap.put(employee.getEmployeeId().toUpperCase(), employee);
                this.panNumberWiseEmployeesMap.put(employee.getPANNumber().toUpperCase(), employee);
                this.aadharCardNumberWiseEmployeesMap.put(employee.getAadharCardNumber().toUpperCase(), employee);
                ets = this.designationCodeWiseEmployeesMap.get(dlEmployee.getDesignationCode());
                if (ets == null) {
                    ets = new TreeSet<>();
                    ets.add(employee);
                    this.designationCodeWiseEmployeesMap.put(designation.getCode(), ets);
                } else {
                    ets.add(employee);
                }
                this.employeesSet.add(employee);
            }
        } catch (DAOException daoException) {
            BLException blException = new BLException();
            blException.setGenericException(daoException.getMessage());
            throw blException;
        }
    }

    public static EmployeeManagerInterface getEmployeeManager() throws BLException {
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
        } else {
            designationCode = designation.getCode();
            if (designationManager.designationCodeExists(designationCode) == false) {
                blException.addException("designation", "Invalid Designation");
            }
        }

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
            if (panNumberWiseEmployeesMap.containsKey(panNumber.toUpperCase())) {
                blException.addException("panNumber", "PAN Number " + panNumber + " exists");
            }
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
            if (aadharCardNumberWiseEmployeesMap.containsKey(aadharCardNumber.toUpperCase())) {
                blException.addException("aadharCardNumber", "Aadhar Card Number " + aadharCardNumber + " exists");
            }
        }
        // validations completed

        if (blException.hasExceptions()) {
            throw blException;
        }

        // Now add employee
        try {
            EmployeeDTOInterface dlEmployee = new EmployeeDTO();
            dlEmployee.setEmployeeId(employeeId);
            dlEmployee.setName(name);
            dlEmployee.setDateOfBirth(dateOfBirth);
            dlEmployee.setDesignationCode(designation.getCode());
            if (gender == 'M')
                dlEmployee.setGender(GENDER.MALE);
            else if (gender == 'F')
                dlEmployee.setGender(GENDER.FEMALE);
            dlEmployee.setIsIndian(isIndian);
            dlEmployee.setBasicSalary(basicSalary);
            dlEmployee.setPANNumber(panNumber);
            dlEmployee.setAadharCardNumber(aadharCardNumber);
            new EmployeeDAO().add(dlEmployee);
            employeeId = dlEmployee.getEmployeeId();
            employee.setEmployeeId(employeeId);

            // now add new employee to data structures
            // **** note **** :- we need to add an object of Employee in DS which is not
            // available to user
            // means create a copy of employee as dsEmployee whose reference is not shared
            // with user so he/she can't harm it
            EmployeeInterface dsEmployee = new Employee();
            dsEmployee.setEmployeeId(employeeId);
            dsEmployee.setName(name);
            dsEmployee.setDateOfBirth((Date) dateOfBirth.clone());
            dsEmployee.setDesignation(
                    ((DesignationManager) designationManager).getDSDesignationByCode(designation.getCode()));
            if (gender == 'M')
                dsEmployee.setGender(GENDER.MALE);
            else if (gender == 'F')
                dsEmployee.setGender(GENDER.FEMALE);
            dsEmployee.setIsIndian(isIndian);
            dsEmployee.setBasicSalary(basicSalary);
            dsEmployee.setPANNumber(panNumber);
            dsEmployee.setAadharCardNumber(aadharCardNumber);
            this.employeeIdWiseEmployeesMap.put(dsEmployee.getEmployeeId().toUpperCase(), dsEmployee);
            this.panNumberWiseEmployeesMap.put(dsEmployee.getPANNumber().toUpperCase(), dsEmployee);
            this.aadharCardNumberWiseEmployeesMap.put(dsEmployee.getAadharCardNumber().toUpperCase(), dsEmployee);
            this.employeesSet.add(dsEmployee);
            Set<EmployeeInterface> ets;
            ets = this.designationCodeWiseEmployeesMap.get(dsEmployee.getDesignation().getCode());
            if (ets == null) {
                ets = new TreeSet<>();
                ets.add(dsEmployee); // adding dsEmployee so user can't change value in DS's object note(dsEmployee
                                     // has different address as of employee(sent by user))
                this.designationCodeWiseEmployeesMap.put(dsEmployee.getDesignation().getCode(), ets);
            } else {
                ets.add(dsEmployee);
            }
        } catch (DAOException daoException) {
            blException.setGenericException(daoException.getMessage());
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
            } else {
                if (employeeIdWiseEmployeesMap.containsKey(employeeId.toUpperCase()) == false) {
                    blException.addException("employeeId", "Invalid Employee Id " + employeeId);
                    throw blException;
                }
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
        } else {
            designationCode = designation.getCode();
            if (designationManager.designationCodeExists(designationCode) == false) {
                blException.addException("designation", "Invalid Designation");
            }
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
        if (panNumber.length() > 0) {
            EmployeeInterface ee = this.panNumberWiseEmployeesMap.get(panNumber.toUpperCase());
            if (ee != null && ee.getEmployeeId().equalsIgnoreCase(employeeId) == false) {
                blException.addException("panNumber", "PAN Number " + panNumber + " exists");
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
        if (aadharCardNumber.length() > 0) {
            EmployeeInterface ee = this.aadharCardNumberWiseEmployeesMap.get(aadharCardNumber.toUpperCase());
            if (ee != null && ee.getEmployeeId().equalsIgnoreCase(employeeId) == false) {
                blException.addException("aadharCardNumber", "Aadhar Card Number " + aadharCardNumber + " exists");
            }
        }
        // validations completed
        if (blException.hasExceptions()) {
            throw blException;
        }
        // Now update employee
        try {
            EmployeeInterface dsEmployee = employeeIdWiseEmployeesMap.get(employeeId.toUpperCase());
            String oldPANNumber = dsEmployee.getPANNumber();
            String oldAadharCardNumber = dsEmployee.getAadharCardNumber();
            int oldDesignationCode = dsEmployee.getDesignation().getCode();
            EmployeeDTOInterface dlEmployee = new EmployeeDTO();
            dlEmployee.setEmployeeId(employeeId);
            dlEmployee.setName(name);
            dlEmployee.setDateOfBirth(dateOfBirth);
            dlEmployee.setDesignationCode(designation.getCode());
            if (gender == 'M')
                dlEmployee.setGender(GENDER.MALE);
            else if (gender == 'F')
                dlEmployee.setGender(GENDER.FEMALE);
            dlEmployee.setIsIndian(isIndian);
            dlEmployee.setBasicSalary(basicSalary);
            dlEmployee.setPANNumber(panNumber);
            dlEmployee.setAadharCardNumber(aadharCardNumber);
            new EmployeeDAO().update(dlEmployee);
            // remove old dsEmployee from Data Structures
            this.employeesSet.remove(dsEmployee);
            this.employeeIdWiseEmployeesMap.remove(employeeId.toUpperCase());
            this.panNumberWiseEmployeesMap.remove(oldPANNumber.toUpperCase());
            this.aadharCardNumberWiseEmployeesMap.remove(oldAadharCardNumber.toUpperCase());
            // updating dsEmployee with new data
            dsEmployee.setName(name);
            dsEmployee.setDateOfBirth((Date) dateOfBirth.clone());
            dsEmployee.setDesignation(
                    ((DesignationManager) designationManager).getDSDesignationByCode(designation.getCode()));
            if (gender == 'M')
                dsEmployee.setGender(GENDER.MALE);
            else if (gender == 'F')
                dsEmployee.setGender(GENDER.FEMALE);
            dsEmployee.setIsIndian(isIndian);
            dsEmployee.setBasicSalary(basicSalary);
            dsEmployee.setPANNumber(panNumber);
            dsEmployee.setAadharCardNumber(aadharCardNumber);
            this.employeeIdWiseEmployeesMap.put(dsEmployee.getEmployeeId().toUpperCase(), dsEmployee);
            this.panNumberWiseEmployeesMap.put(dsEmployee.getPANNumber().toUpperCase(), dsEmployee);
            this.aadharCardNumberWiseEmployeesMap.put(dsEmployee.getAadharCardNumber().toUpperCase(), dsEmployee);
            this.employeesSet.add(dsEmployee);
            if (oldDesignationCode != dsEmployee.getDesignation().getCode()) {
                Set<EmployeeInterface> ets;
                ets = this.designationCodeWiseEmployeesMap.get(oldDesignationCode);
                ets.remove(dsEmployee);
                ets = this.designationCodeWiseEmployeesMap.get(dsEmployee.getDesignation().getCode());
                if (ets == null) {
                    ets = new TreeSet<>();
                    ets.add(dsEmployee);
                    this.designationCodeWiseEmployeesMap.put(dsEmployee.getDesignation().getCode(), ets);
                } else {
                    ets.add(dsEmployee);
                }
            }
        } catch (DAOException daoException) {
            blException.setGenericException(daoException.getMessage());
            throw blException;
        }
    }

    public void removeEmployee(String employeeId) throws BLException {
        if (employeeId == null) {
            BLException blException = new BLException();
            blException.addException("employeeId", "Employee Id required");
            throw blException;
        } else {
            employeeId = employeeId.trim();
            if (employeeId.length() == 0) {
                BLException blException = new BLException();
                blException.addException("employeeId", "Employee Id required");
                throw blException;
            } else if (this.employeeIdWiseEmployeesMap.containsKey(employeeId.toUpperCase()) == false) {
                BLException blException = new BLException();
                blException.addException("employeeId", "Invalid EmployeeId " + employeeId);
            }
        }
        try {
            EmployeeInterface dsEmployee = this.employeeIdWiseEmployeesMap.get(employeeId.toUpperCase());
            EmployeeDAOInterface employeeDAO = new EmployeeDAO();
            employeeDAO.delete(dsEmployee.getEmployeeId());
            this.employeesSet.remove(dsEmployee);
            this.employeeIdWiseEmployeesMap.remove(employeeId.toUpperCase());
            this.panNumberWiseEmployeesMap.remove(employeeId.toUpperCase());
            this.aadharCardNumberWiseEmployeesMap.remove(employeeId.toUpperCase());
            Set<EmployeeInterface> ets;
            ets = this.designationCodeWiseEmployeesMap.get(dsEmployee.getDesignation().getCode());
            ets.remove(dsEmployee);
        } catch (DAOException daoException) {
            BLException blException = new BLException();
            blException.setGenericException(daoException.getMessage());
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
        EmployeeInterface dsEmployee = new Employee();
        dsEmployee = this.employeeIdWiseEmployeesMap.get(employeeId);
        if (dsEmployee == null) {
            blException.addException("employeeId", "Invalid Employee Id " + employeeId);
            throw blException;
        }
        EmployeeInterface ee = new Employee();
        ee.setEmployeeId(employeeId);
        ee.setName(dsEmployee.getName());
        DesignationInterface designation = new Designation();
        designation.setCode(dsEmployee.getDesignation().getCode());
        designation.setTitle(dsEmployee.getDesignation().getTitle());
        ee.setDesignation(designation);
        ee.setDateOfBirth((Date) (dsEmployee.getDateOfBirth()).clone());
        if (dsEmployee.getGender() == 'M')
            ee.setGender(GENDER.MALE);
        else if (dsEmployee.getGender() == 'F')
            ee.setGender(GENDER.FEMALE);
        ee.setIsIndian(dsEmployee.getIsIndian());
        ee.setBasicSalary(dsEmployee.getBasicSalary());
        ee.setPANNumber(dsEmployee.getPANNumber());
        ee.setAadharCardNumber(dsEmployee.getAadharCardNumber());
        return ee;
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
        EmployeeInterface employee = new Employee();
        employee = this.panNumberWiseEmployeesMap.get(panNumber);
        if (employee == null) {
            blException.addException("panNumber", "Invalid PAN Number " + panNumber);
            throw blException;
        }
        EmployeeInterface ee = new Employee();
        ee.setEmployeeId(ee.getEmployeeId());
        ee.setName(employee.getName());
        DesignationInterface designation = new Designation();
        designation.setCode(employee.getDesignation().getCode());
        designation.setTitle(employee.getDesignation().getTitle());
        ee.setDesignation(designation);
        ee.setDateOfBirth((Date) employee.getDateOfBirth().clone());
        if (employee.getGender() == 'M')
            ee.setGender(GENDER.MALE);
        else if (employee.getGender() == 'F')
            ee.setGender(GENDER.FEMALE);
        ee.setIsIndian(employee.getIsIndian());
        ee.setBasicSalary(employee.getBasicSalary());
        ee.setPANNumber(employee.getPANNumber());
        ee.setAadharCardNumber(employee.getAadharCardNumber());
        return ee;
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
        EmployeeInterface employee = new Employee();
        employee = this.aadharCardNumberWiseEmployeesMap.get(aadharCardNumber);
        if (employee == null) {
            blException.addException("aadharCardNumber", "Invalid Aadhar Card Number " + aadharCardNumber);
            throw blException;
        }
        EmployeeInterface ee = new Employee();
        ee.setEmployeeId(ee.getEmployeeId());
        ee.setName(employee.getName());
        DesignationInterface designation = new Designation();
        designation.setCode(employee.getDesignation().getCode());
        designation.setTitle(employee.getDesignation().getTitle());
        ee.setDesignation(designation);
        ee.setDateOfBirth((Date) employee.getDateOfBirth().clone());
        if (employee.getGender() == 'M')
            ee.setGender(GENDER.MALE);
        else if (employee.getGender() == 'F')
            ee.setGender(GENDER.FEMALE);
        ee.setIsIndian(employee.getIsIndian());
        ee.setBasicSalary(employee.getBasicSalary());
        ee.setPANNumber(employee.getPANNumber());
        ee.setAadharCardNumber(employee.getAadharCardNumber());
        return ee;
    }

    public int getEmployeeCount() throws BLException {
        return this.employeesSet.size();
    }

    public boolean employeeIdExists(String employeeId) {
        return this.employeeIdWiseEmployeesMap.containsKey(employeeId.toUpperCase());
    }

    public boolean employeePANNumberExists(String panNumber) {
        return this.panNumberWiseEmployeesMap.containsKey(panNumber.toUpperCase());
    }

    public boolean employeeAadharCardNumberExists(String aadharCardNumber) {
        return this.aadharCardNumberWiseEmployeesMap.containsKey(aadharCardNumber.toUpperCase());
    }

    public Set<EmployeeInterface> getEmployees() throws BLException {
        Set<EmployeeInterface> employees = new TreeSet<>();
        EmployeeInterface employee;
        DesignationInterface dsDesignation;
        DesignationInterface designation;
        for (EmployeeInterface dsEmployee : this.employeesSet) {
            employee = new Employee();
            employee.setEmployeeId(dsEmployee.getEmployeeId());
            employee.setName(dsEmployee.getName());
            employee.setDateOfBirth((Date) dsEmployee.getDateOfBirth().clone());
            dsDesignation = dsEmployee.getDesignation();
            designation = new Designation();
            designation.setCode(dsDesignation.getCode());
            designation.setTitle(dsDesignation.getTitle());
            employee.setDesignation(designation);
            if (dsEmployee.getGender() == 'M')
                employee.setGender(GENDER.MALE);
            else if (dsEmployee.getGender() == 'F')
                employee.setGender(GENDER.FEMALE);
            employee.setIsIndian(dsEmployee.getIsIndian());
            employee.setBasicSalary(dsEmployee.getBasicSalary());
            employee.setPANNumber(dsEmployee.getPANNumber());
            employee.setAadharCardNumber(dsEmployee.getAadharCardNumber());
            employees.add(employee);
        }
        return employees;
    }

    public Set<EmployeeInterface> getEmployeesByDesignationCode(int designationCode) throws BLException {
        DesignationManagerInterface designationManager;
        designationManager = DesignationManager.getDesignationManager();
        if (designationManager.designationCodeExists(designationCode) == false) {
            BLException blException = new BLException();
            blException.setGenericException("Invalid Designation Code " + designationCode);
        }
        Set<EmployeeInterface> employees = new TreeSet<>();
        Set<EmployeeInterface> ets;
        ets = this.designationCodeWiseEmployeesMap.get(designationCode);
        if (ets == null) {
            return employees;
        }
        ets.forEach((dsEmployee) -> {
            DesignationInterface designation;
            DesignationInterface dsDesignation;
            EmployeeInterface employee;
            employee = new Employee();
            employee.setEmployeeId(dsEmployee.getEmployeeId());
            employee.setName(dsEmployee.getName());
            employee.setDateOfBirth((Date) dsEmployee.getDateOfBirth().clone());
            dsDesignation = dsEmployee.getDesignation();
            designation = new Designation();
            designation.setCode(dsDesignation.getCode());
            designation.setTitle(dsDesignation.getTitle());
            employee.setDesignation(designation);
            if (dsEmployee.getGender() == 'M')
                employee.setGender(GENDER.MALE);
            else if (dsEmployee.getGender() == 'F')
                employee.setGender(GENDER.FEMALE);
            employee.setIsIndian(dsEmployee.getIsIndian());
            employee.setBasicSalary(dsEmployee.getBasicSalary());
            employee.setPANNumber(dsEmployee.getPANNumber());
            employee.setAadharCardNumber(dsEmployee.getAadharCardNumber());
            employees.add(employee);
        });
        return employees;
    }

    public int getEmployeeCountByDesignationCode(int designationCode) throws BLException {
        Set<EmployeeInterface> ets;
        ets = this.designationCodeWiseEmployeesMap.get(designationCode);
        if (ets == null)
            return 0;
        else
            return ets.size();
    }

    public boolean designationAlloted(int designationCode) throws BLException {
        return this.designationCodeWiseEmployeesMap.containsKey(designationCode);
    }
}