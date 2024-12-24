package com.thinking.machines.hr.bl.managers;

import com.thinking.machines.hr.bl.interfaces.managers.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.hr.bl.exceptions.*;
import java.util.*;
import com.thinking.machines.network.common.*;
import com.thinking.machines.network.client.*;
import com.thinking.machines.network.common.exceptions.*;


public class DesignationManager implements DesignationManagerInterface {

    private static DesignationManager designationManager = null;

    private DesignationManager() throws BLException {
    }

    public static DesignationManager getDesignationManager() throws BLException {
        if (designationManager == null)
            designationManager = new DesignationManager();
        return designationManager;
    }

    public void addDesignation(DesignationInterface designation) throws BLException {
        // validating designation 
        BLException blException = new BLException();
        if (designation == null) {
            blException.setGenericException("Designation required");
            throw blException;
        }
        int code = designation.getCode();
        String title = designation.getTitle();
        if (code != 0) {
            blException.addException("code", "code should be zero");
        }
        if (title == null) {
            blException.addException("title", "Title required");
            title = "";
        } else {
            title = title.trim();
            if (title.length() == 0) {
                blException.addException("title", "Title required");
            }
        }
        if (blException.hasExceptions()) {
            throw blException;
        }
        // validation done 

        // making request to send 
        Request request = new Request();
        request.setManager(Managers.getManagerType(Managers.DESIGNATION));
        request.setAction(Managers.getActionType(Managers.Designation.ADD));
        request.setArguments(designation);
        NetworkClient client = new NetworkClient();
        try {
            Response response = client.send(request);
            if (response.hasException()) {
                blException = (BLException) response.getException();
                throw blException;
            }

            // vvv.imp
            // these two lines are imp in add because when in actual bl we call add the code
            // is
            // generated at dl which is set in bl but here
            // in network programming we need to get it as result so we can update the code
            // of
            // designation object provided by pl
            DesignationInterface d = (DesignationInterface) response.getResult();
            designation.setCode(d.getCode());

        } catch (NetworkException networkException) {
            blException.setGenericException(networkException.getMessage());
            throw blException;
        }
    }

    public void updateDesignation(DesignationInterface designation) throws BLException {
        BLException blException = new BLException();
        if (designation == null) {
            blException.setGenericException("Designation required");
            throw blException;
        }
        int code = designation.getCode();
        String title = designation.getTitle();
        if (code <= 0) {
            blException.addException("code", "code should be zero");
            throw blException;
        }
        if (title == null) {
            blException.addException("title", "Title required");
            title = "";
        } else {
            title = title.trim();
            if (title.length() == 0) {
                blException.addException("title", "Title required");
            }
        }
        if (blException.hasExceptions()) {
            throw blException;
        }

        Request request = new Request();
        request.setManager(Managers.getManagerType(Managers.DESIGNATION));
        request.setAction(Managers.getActionType(Managers.Designation.UPDATE));
        request.setArguments(designation);

        NetworkClient client = new NetworkClient();
        try {
            Response response = client.send(request);
            if(response.hasException()) {
                blException = (BLException) response.getException();
                throw blException;
            }
        } catch (NetworkException networkException) {
            blException.setGenericException(networkException.getMessage());
            throw blException;
        }
    }

    public void removeDesignation(int code) throws BLException {
        if (code <= 0) {
            BLException blException = new BLException();
            blException.addException("code", "Invalid Code " + code);
            throw blException;
        }  

        Request request = new Request();
        request.setManager(Managers.getManagerType(Managers.DESIGNATION));
        request.setAction(Managers.getActionType(Managers.Designation.REMOVE));
        request.setArguments(code);

        NetworkClient networkClient = new NetworkClient();

        try {   
            Response response = networkClient.send(request);
            if(response.hasException()) {
                BLException blException;
                blException = (BLException) response.getException();
                throw blException;
            }
        } catch(NetworkException networkException) {
            BLException blException = new BLException();
            blException.setGenericException(networkException.getMessage());
            throw blException;
        }
    }

    public DesignationInterface getDesignationByCode(int code) throws BLException {
        BLException blException = new BLException();
        if (code <= 0) {
            blException.addException("code", "Invalid Code " + code);
        }
        if (blException.hasExceptions()) {
            throw blException;
        }

        
        Request request = new Request();
        request.setManager(Managers.getManagerType(Managers.DESIGNATION));
        request.setAction(Managers.getActionType(Managers.Designation.GET_BY_CODE));
        request.setArguments(code);
        
        NetworkClient networkClient = new NetworkClient();
        try{
            Response response = networkClient.send(request);
            if(response.hasException()){
                blException = (BLException) response.getException();
                throw blException;
            }
            DesignationInterface designation;
            designation = (DesignationInterface) response.getResult();
            return designation;
        }catch(NetworkException networkException) {
            blException.setGenericException(networkException.getMessage());
            throw blException;
        }
    }

    public DesignationInterface getDesignationByTitle(String title) throws BLException {
        BLException blException = new BLException();
        if (title == null) {
            blException.addException("title", "Title required");
            title = "";
        } else {
            title = title.trim();
            if (title.length() == 0) {
                blException.addException("title", "Title required");
            }
        }
        if (blException.hasExceptions()) {
            throw blException;
        }

        
        Request request = new Request();
        request.setManager(Managers.getManagerType(Managers.DESIGNATION));
        request.setAction(Managers.getActionType(Managers.Designation.GET_BY_TITLE));
        request.setArguments(title);
        
        NetworkClient networkClient = new NetworkClient();
        try{
            Response response = networkClient.send(request);
            if(response.hasException()){
                blException = (BLException) response.getException();
                throw blException;
            }
            DesignationInterface designation;
            designation = (DesignationInterface) response.getResult();
            return designation;
        }catch(NetworkException networkException) {
            blException.setGenericException(networkException.getMessage());
            throw blException;
        }
    }

    public boolean designationCodeExists(int code) {
        if (code <= 0) {
            return false;
        }
        Request request = new Request();
        request.setManager(Managers.getManagerType(Managers.DESIGNATION));
        request.setAction(Managers.getActionType(Managers.Designation.CODE_EXISTS));
        request.setArguments(code);

        NetworkClient networkClient = new NetworkClient();
        try {
            Response response = networkClient.send(request);
            return (boolean) response.getResult();
        }catch(NetworkException networkException) {
            return false;
        }
    }

    public boolean designationTitleExists(String title) {
        BLException blException = new BLException();
        if (title == null) {
            return false; // need to recheck
        } else {
            title = title.trim();
            if (title.length() == 0) {
                return false; // need to recheck
            }
        }
        Request request = new Request();
        request.setManager(Managers.getManagerType(Managers.DESIGNATION));
        request.setAction(Managers.getActionType(Managers.Designation.TITLE_EXISTS));
        request.setArguments(title);

        NetworkClient networkClient = new NetworkClient();
        try {
            Response response = networkClient.send(request);
            return (boolean) response.getResult();
        }catch(NetworkException networkException) {
            // throw new BLException().setGenericException(networkException.getMessage());
            return false;
        }
    }

    public Set<DesignationInterface> getDesignations() {
        Set<DesignationInterface> designations = null;
        Request request = new Request();
        request.setManager(Managers.getManagerType(Managers.DESIGNATION));
        request.setAction(Managers.getActionType(Managers.Designation.GET_ALL));

        NetworkClient networkClient = new NetworkClient();
        try {
            Response response = networkClient.send(request);
            DesignationInterface designation;
            designations = (Set<DesignationInterface>) response.getResult();
            return designations;
        }catch(NetworkException networkException) {
            // throw new BLException().setGenericException(networkException.getMessage());
            System.out.println("In proxybl getDesignations : Network Exception : "+networkException.getMessage());
        }
        return designations;
    }
    
    public int getDesignationCount() {
        Request request = new Request();
        request.setManager(Managers.getManagerType(Managers.DESIGNATION));
        request.setAction(Managers.getActionType(Managers.Designation.GET_DESIGNATION_COUNT));

        NetworkClient networkClient = new NetworkClient();
        try {
            Response response = networkClient.send(request);
            Integer count;
            count = (Integer) response.getResult();
            return count.intValue();
        }catch(NetworkException networkException) {
            throw new RuntimeException(networkException.getMessage());
        }
    }
}