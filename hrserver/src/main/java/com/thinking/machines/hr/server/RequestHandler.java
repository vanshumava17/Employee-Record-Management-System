package com.thinking.machines.hr.server;

import com.thinking.machines.hr.bl.managers.*;

import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.network.common.*;
import com.thinking.machines.hr.bl.interfaces.managers.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.network.server.*;

        
public class RequestHandler implements RequestHandlerInterface {
    private DesignationManagerInterface designationManager;
    private EmployeeManagerInterface employeeManager;

    public RequestHandler() {
        try {
            designationManager = DesignationManager.getDesignationManager();
            employeeManager = EmployeeManager.getEmployeeManager();
        } catch (BLException blException) {
            // do nothing
        }
    }

    public Response process(Request request) {
        Response response = new Response();
        String manager = request.getManager();
        String action = request.getAction();
        Object []arguments = request.getArguments();

        if(manager.equals("DesignationManager")) {
            if(designationManager == null) {
                // need to be implemented
            }

            if(action.equals("getDesignations")){
                try {
                    Object result = designationManager.getDesignations();
                    response.setSuccess(true);
                    response.setResult(result);
                    response.setException(null);
                    return response;
                } catch (BLException blException ) {
                    response.setSuccess(false);
                    response.setResult(null);
                    response.setException(blException);
                    return response;
                }
            }

            if(action.equals("addDesignation")) {
                try {
                    DesignationInterface designation = (DesignationInterface) arguments[0];
                    designationManager.addDesignation(designation);
                    Object result = designation;
                    response.setSuccess(true);
                    response.setResult(result);
                    response.setException(null);
                    return response;
                } catch (BLException blException ) {
                    response.setSuccess(false);
                    response.setResult(null);
                    response.setException(blException);
                    return response;
                }
            }

            if(action.equals("updateDesignation")) {
                try {
                    DesignationInterface designation = (DesignationInterface) arguments[0];
                    designationManager.updateDesignation(designation);
                    response.setSuccess(true);
                    response.setResult(null);
                    response.setException(null);
                    return response;
                } catch (BLException blException ) {
                    response.setSuccess(false);
                    response.setResult(null);
                    response.setException(blException);
                    return response;
                }
            }

            if(action.equals("removeDesignation")) {
                try {
                    Integer code = (Integer) arguments[0];
                    designationManager.removeDesignation(code);
                    response.setSuccess(true);
                    response.setResult(null);
                    response.setException(null);
                    return response;
                } catch (BLException blException ) {
                    response.setSuccess(false);
                    response.setResult(null);
                    response.setException(blException);
                    return response;
                }
            }

            if(action.equals("getDesignationByCode")) {
                try {
                    Integer code = (Integer) arguments[0];
                    Object result = designationManager.getDesignationByCode(code);
                    response.setSuccess(true);
                    response.setResult(result);
                    response.setException(null);
                    return response;
                } catch (BLException blException ) {
                    response.setSuccess(false);
                    response.setResult(null);
                    response.setException(blException);
                    return response;
                }
            }

            if(action.equals("getDesignationByTitle")) {
                try {
                    String title = (String) arguments[0];
                    Object result = designationManager.getDesignationByTitle(title);
                    response.setSuccess(true);
                    response.setResult(result);
                    response.setException(null);
                    return response;
                } catch (BLException blException ) {
                    response.setSuccess(false);
                    response.setResult(null);
                    response.setException(blException);
                    return response;
                }
            }

            if(action.equals("designationCodeExists")) {
                try {
                    Integer code = (Integer) arguments[0];
                    Object result = designationManager.designationCodeExists(code);
                    response.setSuccess(true);
                    response.setResult(result);
                    response.setException(null);
                    return response;
                } catch (BLException blException ) {
                    response.setSuccess(false);
                    response.setResult(null);
                    response.setException(blException);
                    return response;
                }
            }

            if(action.equals("designationTitleExists")) {
                try {
                    String title = (String) arguments[0];
                    Object result = designationManager.designationTitleExists(title);
                    response.setSuccess(true);
                    response.setResult(result);
                    response.setException(null);
                    return response;
                } catch (BLException blException ) {
                    response.setSuccess(false);
                    response.setResult(null);
                    response.setException(blException);
                    return response;
                }
            }

            if(action.equals("getDesignationCount")) {
                Object result = designationManager.getDesignationCount();
                response.setSuccess(true);
                response.setResult(result);
                response.setException(null);
                return response;
            }
        } // DesignationManager part ends here


        // EmployeeManager part starts here
        if(manager.equals("EmployeeManager")){
            if(employeeManager == null){
                // need to do something
                response.setResult(null);
                response.setSuccess(false);
                BLException blException = new BLException();
                blException.setGenericException("Unable to process request ("+request.getManager()+","+request.getAction()+","+request.getArguments()+")");
                response.setException(blException);
                return response;
            }

            if(action.equals("getEmployees")){
                try {
                    Object result = employeeManager.getEmployees();
                    response.setSuccess(true);
                    response.setResult(result);
                    response.setException(null);
                    return response;
                } catch (BLException blException ) {
                    response.setSuccess(false);
                    response.setResult(null);
                    response.setException(blException);
                    return response;
                }
            }

            if(action.equals("addEmployee")){
                try{
                    EmployeeInterface employee = (EmployeeInterface) arguments[0];
                    employeeManager.addEmployee(employee);
                    response.setSuccess(true);
                    Object result = employee;
                    response.setResult(result);
                    response.setException(null);
                    return response;
                }catch(BLException blException){
                    response.setSuccess(false);
                    response.setResult(null);
                    response.setException(blException);
                    return response;
                }
            }

            if(action.equals("updateEmployee")){
                try{
                    EmployeeInterface employee = (EmployeeInterface) arguments[0];
                    employeeManager.updateEmployee(employee);
                    response.setSuccess(true);
                    Object result = employee;
                    response.setResult(result);
                    response.setException(null);
                    return response;
                }catch(BLException blException){
                    response.setSuccess(false);
                    response.setResult(null);
                    response.setException(blException);
                    return response;
                }
            }

            if(action.equals("removeEmployee")){
                try{
                    String employeeId = (String) arguments[0];
                    employeeManager.removeEmployee(employeeId);
                    response.setSuccess(true);
                    response.setResult(null);
                    response.setException(null);
                    return response;
                }catch(BLException blException){
                    response.setSuccess(false);
                    response.setResult(null);
                    response.setException(blException);
                    return response;
                }
            }

            if(action.equals("getEmployeeByEmployeeId")){
                try{
                    String employeeId = (String) arguments[0];
                    Object result =  employeeManager.getEmployeeByEmployeeId(employeeId);
                    response.setSuccess(true);
                    response.setResult(result);
                    response.setException(null);
                    return response;
                }catch(BLException blException){
                    response.setSuccess(false);
                    response.setResult(null);
                    response.setException(blException);
                    return response;
                }
            }

            if(action.equals("getEmployeeByPANNumber")){
                try{
                    String panCardNumber = (String) arguments[0];
                    Object result =  employeeManager.getEmployeeByPANNumber(panCardNumber);
                    response.setSuccess(true);
                    response.setResult(result);
                    response.setException(null);
                    return response;
                }catch(BLException blException){
                    response.setSuccess(false);
                    response.setResult(null);
                    response.setException(blException);
                    return response;
                }
            }

            if(action.equals("getEmployeeByAadharCardNumber")){
                try{
                    String aadharCardNumber = (String) arguments[0];
                    Object result =  employeeManager.getEmployeeByAadharCardNumber(aadharCardNumber);
                    response.setSuccess(true);
                    response.setResult(result);
                    response.setException(null);
                    return response;
                }catch(BLException blException){
                    response.setSuccess(false);
                    response.setResult(null);
                    response.setException(blException);
                    return response;
                }
            }   

            if(action.equals("getEmployeeCount")){
                try{
                    Object result = employeeManager.getEmployeeCount();
                    response.setSuccess(true);
                    response.setResult(result);
                    response.setException(null);
                    return response;
                }catch(BLException blException){
                    response.setSuccess(false);
                    response.setResult(null);
                    response.setException(blException);
                    return response;
                }
            }

            if(action.equals("getEmployeesByDesignationCode")){
                try{ 
                    int designationCode = (int) arguments[0];
                    Object result = employeeManager.getEmployeesByDesignationCode(designationCode);
                    response.setSuccess(true);
                    response.setResult(result);
                    response.setException(null);
                    return response;
                }catch(BLException blException){
                    response.setSuccess(false);
                    response.setResult(null);
                    response.setException(blException);
                    return response;
                }
            }

            if(action.equals("getEmployeeCountByDesignationCode")){
                try{ 
                    int designationCode = (int) arguments[0];
                    Object result = employeeManager.getEmployeeCountByDesignationCode(designationCode);
                    response.setSuccess(true);
                    response.setResult(result);
                    response.setException(null);
                    return response;
                }catch(BLException blException){
                    response.setSuccess(false);
                    response.setResult(null);
                    response.setException(blException);
                    return response;
                }
            }
        }
        // EmployeeManager part ends here
        return response;
    }
}
