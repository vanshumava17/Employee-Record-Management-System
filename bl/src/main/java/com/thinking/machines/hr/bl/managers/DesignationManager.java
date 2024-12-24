package com.thinking.machines.hr.bl.managers;

import com.thinking.machines.hr.bl.interfaces.managers.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.dao.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.exceptions.*;
import java.util.*;

import javax.swing.text.BadLocationException;

public class DesignationManager implements DesignationManagerInterface {
    private Map<Integer, DesignationInterface> codeWiseDesignationsMap;
    private Map<String, DesignationInterface> titleWiseDesignationsMap;
    private Set<DesignationInterface> designationsSet;
    private static DesignationManager designationManager = null;

    private DesignationManager() throws BLException {
        populateDataStructures();
    }

    public static DesignationManager getDesignationManager() throws BLException {
        if (designationManager == null)
            designationManager = new DesignationManager();
        return designationManager;
    }

    private void populateDataStructures() throws BLException {
        this.codeWiseDesignationsMap = new HashMap<>();
        this.titleWiseDesignationsMap = new HashMap<>();
        this.designationsSet = new TreeSet<>();
        try {
            Set<DesignationDTOInterface> dlDesignations = new DesignationDAO().getAll();
            DesignationInterface designation;
            for (DesignationDTOInterface dlDesignation : dlDesignations) {
                designation = new Designation();
                designation.setCode(dlDesignation.getCode());
                designation.setTitle(dlDesignation.getTitle());
                this.codeWiseDesignationsMap.put(designation.getCode(), designation);
                this.titleWiseDesignationsMap.put(designation.getTitle().toUpperCase(), designation);
                this.designationsSet.add(designation);
            }
        } catch (DAOException daoException) {
            BLException blException = new BLException();
            blException.setGenericException(daoException.getMessage());
            throw blException;
        }
    }

    public void addDesignation(DesignationInterface designation) throws BLException {
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
        if (title.length() > 0) {
            if (this.titleWiseDesignationsMap.containsKey(title.toUpperCase())) {
                blException.addException("title", "Designation : " + title + " exists");
            }
        }
        if (blException.hasExceptions()) {
            throw blException;
        }
        try {
            DesignationDTOInterface designationDTO = new DesignationDTO();
            designationDTO.setTitle(title);
            DesignationDAOInterface designationDAO = new DesignationDAO();
            designationDAO.add(designationDTO);
            code = designationDTO.getCode();
            designation.setCode(code);

            Designation dsDesignation = new Designation();
            dsDesignation.setCode(code);
            dsDesignation.setTitle(title);

            this.codeWiseDesignationsMap.put(code, dsDesignation);
            this.titleWiseDesignationsMap.put(title.toUpperCase(), dsDesignation);
            this.designationsSet.add(dsDesignation);
        } catch (DAOException daoException) {
            blException.setGenericException(daoException.getMessage());
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
        if (code > 0) {
            if (!this.codeWiseDesignationsMap.containsKey(code)) {
                blException.addException("cod", "Invalid Code : " + code);
                throw blException;
            }
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
        if (title.length() > 0) {
            DesignationInterface d;
            d = this.titleWiseDesignationsMap.get(title.toUpperCase());
            if (d != null && d.getCode() != code) {
                blException.addException("title", "Designation : " + title + " exists");
            }
        }
        if (blException.hasExceptions()) {
            throw blException;
        }
        try {
            DesignationInterface dsDesignation = this.codeWiseDesignationsMap.get(code);
            DesignationDTOInterface dlDesignation = new DesignationDTO();
            dlDesignation.setCode(code);
            dlDesignation.setTitle(title);

            // updating in data layer below:
            new DesignationDAO().update(dlDesignation);

            // removing the old one from All Data Structures
            this.codeWiseDesignationsMap.remove(code);
            this.titleWiseDesignationsMap.remove(dsDesignation.getTitle().toUpperCase());
            this.designationsSet.remove(dsDesignation);

            // update the DS object
            dsDesignation.setTitle(title);

            // update the Data Structures
            this.codeWiseDesignationsMap.put(code, dsDesignation);
            this.titleWiseDesignationsMap.put(title, dsDesignation);
            this.designationsSet.add(dsDesignation);
        } catch (DAOException daoException) {
            blException.setGenericException(daoException.getMessage());
        }
    }

    public void removeDesignation(int code) throws BLException {
        BLException blException = new BLException();
        if (code <= 0) {
            blException.addException("code", "Invalid Code " + code);
        }
        if (code > 0) {
            if (this.codeWiseDesignationsMap.containsKey(code) == false) {
                blException.addException("code", "Invalid Code : " + code);
            }
        }
        if (blException.hasExceptions()) {
            throw blException;
        }
        try {
            DesignationDAOInterface designationDAO = new DesignationDAO();
            designationDAO.delete(code);
            DesignationInterface dsDesignation = this.codeWiseDesignationsMap.get(code);
            this.codeWiseDesignationsMap.remove(code);
            this.titleWiseDesignationsMap.remove(dsDesignation.getTitle().toUpperCase());
            this.designationsSet.remove(dsDesignation);
        } catch (DAOException daoException) {
            blException.setGenericException(daoException.getMessage());
            throw blException;
        }
    }

    DesignationInterface getDSDesignationByCode(int code) throws BLException {
        // it will return original object of designation which is stored in DS of
        // DesignationManager
        DesignationInterface designation;
        designation = this.codeWiseDesignationsMap.get(code);
        if (designation == null) {
            BLException blException = new BLException();
            blException.addException("code", "Invalid code " + code);
            throw blException;
        }
        return designation;
    }

    public DesignationInterface getDesignationByCode(int code) throws BLException {
        // returns copy of designation's object stored in DS
        // will not return the reference to original object of designation stored in DS
        // similarly in other methods returning designation
        DesignationInterface designation;
        designation = this.codeWiseDesignationsMap.get(code);
        if (designation == null) {
            BLException blException = new BLException();
            blException.addException("code", "Invalid code " + code);
            throw blException;
        }
        DesignationInterface d = new Designation();
        d.setCode(designation.getCode());
        d.setTitle(designation.getTitle());
        return d;
    }

    public DesignationInterface getDesignationByTitle(String title) throws BLException {
        DesignationInterface designation;
        designation = this.titleWiseDesignationsMap.get(title.toUpperCase());
        if (designation == null) {
            BLException blException = new BLException();
            blException.addException("title", "Invalid Title " + title);
            throw blException;
        }
        DesignationInterface d = new Designation();
        d.setCode(designation.getCode());
        d.setTitle(designation.getTitle());
        return d;
    }

    public boolean designationCodeExists(int code) throws BLException {
        return this.codeWiseDesignationsMap.containsKey(code);
    }

    public boolean designationTitleExists(String title) throws BLException {
        return this.titleWiseDesignationsMap.containsKey(title.toUpperCase());
    }

    public Set<DesignationInterface> getDesignations() throws BLException {
        Set<DesignationInterface> designations = new TreeSet<>();
        this.designationsSet.forEach((designation) -> {
            DesignationInterface d = new Designation();
            d.setCode(designation.getCode());
            d.setTitle(designation.getTitle());
            designations.add(d);
        });
        return designations;
    }

    public int getDesignationCount() {
        return this.designationsSet.size();
    }
}