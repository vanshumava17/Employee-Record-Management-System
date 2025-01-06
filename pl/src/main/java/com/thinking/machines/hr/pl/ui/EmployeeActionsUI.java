package com.thinking.machines.hr.pl.ui;

import com.thinking.machines.hr.pl.model.*;
import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.enums.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import java.util.*;
import java.math.*;

import java.io.*;

class EmployeeActionsUI extends JFrame{

    private EmployeeUI employeeUI;
    private EmployeeModel employeeModel;
    private EmployeeInterface selectedEmployee;
    private MODE mode;

    private Map<String,DesignationInterface> designationMap = new HashMap<>();

    private JLabel nameLabel;
    private JLabel designationLabel;
    private JLabel genderLabel;
    private JLabel dateOfBirthLabel;
    private JLabel isIndianLabel;
    private JLabel basicSalaryLabel;
    private JLabel panNumberLabel;
    private JLabel aadharCardNumberLabel;
    private JTextField nameTextField;
    private JComboBox designationComboBox;
    private JRadioButton maleRadioButton;
    private JRadioButton femaleRadioButton;
    private ButtonGroup genderButtonGroup;
    private JComboBox dateComboBox;
    private JComboBox monthComboBox;
    private JComboBox yearComboBox;
    private JCheckBox isIndianCheckBox;
    private JTextField basicSalaryTextField;
    private JTextField panNumberTextField;
    private JTextField aadharCardNumberTextField;

    private Font captionFont = new Font("Verdana", Font.BOLD, 16);
    private Font dataFont = new Font("Verdana", Font.PLAIN, 16);

    private JButton submitButton;

    private Container container;

    public EmployeeActionsUI(EmployeeUI employeeUI,EmployeeModel employeeModel,EmployeeInterface selectedEmployee,MODE mode){
        this.employeeUI = employeeUI;
        this.employeeModel = employeeModel;
        this.selectedEmployee =selectedEmployee;
        this.mode = mode;

        initComponents();
        setAppearance();
        addListeners();
    }

    public void initComponents(){

        nameLabel = new JLabel("Name");
        designationLabel = new JLabel("Designation");
        genderLabel = new JLabel("Gender");
        dateOfBirthLabel = new JLabel("D.O.B");
        isIndianLabel = new JLabel("Is Indian");
        basicSalaryLabel = new JLabel("Base Salary");
        panNumberLabel = new JLabel("PAN Number");
        aadharCardNumberLabel = new JLabel("Aadhar Card Number");

        initInputFields();

        container = getContentPane();
        container.setLayout(null);
        container.add(nameLabel);
        container.add(designationLabel);
        container.add(genderLabel);
        container.add(dateOfBirthLabel);
        container.add(isIndianLabel);
        container.add(basicSalaryLabel);
        container.add(panNumberLabel);
        container.add(aadharCardNumberLabel);
        container.add(nameTextField);
        container.add(designationComboBox);
        container.add(maleRadioButton);
        container.add(femaleRadioButton);
        container.add(dateComboBox);
        container.add(monthComboBox);
        container.add(yearComboBox);
        container.add(isIndianCheckBox);
        container.add(basicSalaryTextField);
        container.add(panNumberTextField);
        container.add(aadharCardNumberTextField);
        container.add(submitButton);

       
    }

    /**
     * 
     */
    private void setAppearance(){
        nameLabel.setFont(captionFont);
        designationLabel.setFont(captionFont);
        genderLabel.setFont(captionFont);
        dateOfBirthLabel.setFont(captionFont);
        isIndianLabel.setFont(captionFont);
        basicSalaryLabel.setFont(captionFont);
        panNumberLabel.setFont(captionFont);
        aadharCardNumberLabel.setFont(captionFont);

        nameTextField.setFont(dataFont);
        designationComboBox.setFont(dataFont);
        maleRadioButton.setFont(dataFont);
        femaleRadioButton.setFont(dataFont);
        dateComboBox.setFont(dataFont);
        monthComboBox.setFont(dataFont);
        yearComboBox.setFont(dataFont);
        basicSalaryTextField.setFont(dataFont);
        panNumberTextField.setFont(dataFont);
        aadharCardNumberTextField.setFont(dataFont);

        int lm = 50;
        int tm = 50;
        nameLabel.setBounds(lm + 10, tm + 10, 200, 30);
        designationLabel.setBounds(lm + 10, tm + 10 + 30 +10, 200, 30);
        genderLabel.setBounds(lm + 10, tm + 10 + 30 +10+30+10, 200, 30);
        dateOfBirthLabel.setBounds(lm + 10, tm + 10 + 30 +10+30+10+30+10, 200, 30);
        isIndianLabel.setBounds(lm+10,tm+ 10 + 30 +10+30+10+30+10+30+10,200,30);
        basicSalaryLabel.setBounds(lm+10,tm+ 10 + 30 +10+30+10+30+10+30+10+10+30,200,30);
        panNumberLabel.setBounds(lm+10,tm+ 10 + 30 +10+30+10+30+10+30+10+10+30+30+10,200,30);
        aadharCardNumberLabel.setBounds(lm+10,tm+ 10 + 30 +10+30+10+30+10+30+10+10+30+30+10+10+30,200,30);

        nameTextField.setBounds(lm + 10 + 200 + 10, tm + 10, 250, 30);
        designationComboBox.setBounds(lm + 10 + 200 + 10, tm + 10 + 30 +10, 250, 30);
        maleRadioButton.setBounds(lm + 10 + 200 + 10, tm + 10 + 30 +10+30+10, 70, 30);
        femaleRadioButton.setBounds(lm + 10 + 200 + 10 + 70 +10, tm + 10 + 30 +10+30+10, 100, 30);
        dateComboBox.setBounds(lm + 10 + 200 + 10, tm + 10 + 30 +10+30+10+30+10, 60, 30);
        monthComboBox.setBounds(lm + 10 + 200 + 10+60+2, tm + 10 + 30 +10+30+10+30+10, 60, 30);
        yearComboBox.setBounds(lm + 10 + 200 + 10+60+2+60+2, tm + 10 + 30 +10+30+10+30+10, 70, 30);
        isIndianCheckBox.setBounds(lm+10 + 200 + 10,tm+ 10 + 30 +10+30+10+30+10+30+10,200,30);
        basicSalaryTextField.setBounds(lm + 10 + 200 + 10,tm+ 10 + 30 +10+30+10+30+10+30+10+10+30,250,30);
        panNumberTextField.setBounds(lm + 10 + 200 + 10,tm+ 10 + 30 +10+30+10+30+10+30+10+10+30+30+10,250,30);
        aadharCardNumberTextField.setBounds(lm + 10 + 200 + 10,tm+ 10 + 30 +10+30+10+30+10+30+10+10+30+30+10+10+30,250,30);
        
        submitButton.setBounds(lm+10, tm+10+30+10+30+10+30+10+30+10+10+30+30+10+10+30+10+30, 458, 50);
        
        
        int w, h;
        w = 600;
        h = 700;
        setSize(w, h);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((d.width / 2) - (w / 2), (d.height / 2) - (h / 2));
        setVisible(true);
    }

    private void addListeners(){
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                employeeUI.setEnabled(true);  // Re-enable EmployeeUI
                employeeUI.setVisible(true);
                dispose();  // Close the current window
            }
        });

        basicSalaryTextField.addKeyListener(new KeyAdapter() { 
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar(); 
                if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE) {
                    e.consume(); // Ignore non-digit characters (except backspace)
                }
            }
        });

        this.submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                
                if(mode == MODE.ADD){
                    // ADD DATA 
                    addEmployee();
                }
                else if(mode == MODE.EDIT){
                    editEmployee();
                }
                else {
                    // VIEW DATA
                    employeeUI.setEnabled(true);  // Re-enable EmployeeUI
                    employeeUI.setVisible(true);
                    dispose();  // Close the current window
                }
            }
        });

    }

    private void addEmployee(){
        
        //validations
        String name = nameTextField.getText().trim();
        if (name.length() == 0) {
            JOptionPane.showMessageDialog(this, "Name required");
            nameTextField.requestFocus();
            return ;
        }
        DesignationInterface designation = designationMap.get(this.designationComboBox.getSelectedItem());
        if (designation == null) {
            JOptionPane.showMessageDialog(this, "Designation required");
            designationComboBox.requestFocus();
            return ;
        }
        if(maleRadioButton.isSelected() == false && femaleRadioButton.isSelected() == false){
            // gender not selected
            JOptionPane.showMessageDialog(this,"Gender Required");
            return;
        }
        GENDER gender;
        if(maleRadioButton.isSelected() == true){
            gender = GENDER.MALE;
        } else {
            gender = GENDER.FEMALE;
        }

        // need to validate date month year
        int date = Integer.parseInt((String)dateComboBox.getSelectedItem());
        int month = Integer.parseInt((String)monthComboBox.getSelectedItem());
        int year = Integer.parseInt((String)yearComboBox.getSelectedItem());
        Date dateOfBirth = new Date();
        dateOfBirth.setDate(date);
        dateOfBirth.setMonth(month-1);
        dateOfBirth.setYear(year-1900);

        boolean isIndian = isIndianCheckBox.isSelected();

        String basicSalaryText = basicSalaryTextField.getText();
        if (basicSalaryText.length() == 0) {
            JOptionPane.showMessageDialog(this, "Basic Salary Required");
            nameTextField.requestFocus();
            return ;
        }
        java.math.BigDecimal basicSalary = new BigDecimal(basicSalaryText);
        
        String panNumber = panNumberTextField.getText();
        if (panNumber.length() == 0) {
            JOptionPane.showMessageDialog(this, "PAN Number Required");
            nameTextField.requestFocus();
            return ;
        }

        String aadharCardNumber = aadharCardNumberTextField.getText();
        if (aadharCardNumber.length() == 0) {
            JOptionPane.showMessageDialog(this, "Aadhar Card Number Required");
            nameTextField.requestFocus();
            return ;
        }
        // validation complete

        EmployeeInterface employee = new Employee();
        employee.setName(name);
        employee.setDesignation(designation);
        employee.setGender(gender);
        employee.setDateOfBirth(dateOfBirth);
        employee.setIsIndian(isIndian);
        employee.setBasicSalary(basicSalary);
        employee.setPANNumber(panNumber);
        employee.setAadharCardNumber(aadharCardNumber);

        try {
            employeeModel.add(employee);
            int rowIndex = 0;
            try {
                rowIndex = employeeModel.indexOfEmployee(employee);
            } catch (BLException blException) {
                // do nothing
            }
            employeeUI.employeeTable.setRowSelectionInterval(rowIndex, rowIndex);
            Rectangle rectangle = employeeUI.employeeTable.getCellRect(rowIndex, 0, true);
            employeeUI.employeeTable.scrollRectToVisible(rectangle);

            JOptionPane.showMessageDialog(this,"Employee Added successfully");

            employeeUI.setEnabled(true);  // Re-enable EmployeeUI
            employeeUI.setVisible(true);
            dispose();  // Close the current window
            return;
        } catch (BLException blException) {
            if (blException.hasGenericException()) {
                JOptionPane.showMessageDialog(this, blException.getGenericException());
            } else if (blException.hasException("name")) {
                JOptionPane.showMessageDialog(this, blException.getException("name"));
            } else if (blException.hasException("designation")) {
                JOptionPane.showMessageDialog(this, blException.getException("designation"));
            } else if (blException.hasException("gender")) {
                JOptionPane.showMessageDialog(this, blException.getException("gender"));
            } else if (blException.hasException("dateOfBirth")) {
                JOptionPane.showMessageDialog(this, blException.getException("dateOfBirth"));
            } else if (blException.hasException("isIndian")) {
                JOptionPane.showMessageDialog(this, blException.getException("isIndian"));
            } else if (blException.hasException("basicSalary")) {
                JOptionPane.showMessageDialog(this, blException.getException("basicSalary"));
            } else if (blException.hasException("panNumber")) {
                JOptionPane.showMessageDialog(this, blException.getException("panNumber"));
            } else if (blException.hasException("aadharCardNumber")) {
                JOptionPane.showMessageDialog(this, blException.getException("aadharCardNumber"));
            }
            return;
        }
    }
    

    private void editEmployee(){
        //validations
        String name = nameTextField.getText().trim();
        if (name.length() == 0) {
            JOptionPane.showMessageDialog(this, "Name required");
            nameTextField.requestFocus();
            return ;
        }
        DesignationInterface designation = designationMap.get(this.designationComboBox.getSelectedItem());
        if (designation == null) {
            JOptionPane.showMessageDialog(this, "Designation required");
            designationComboBox.requestFocus();
            return ;
        }
        if(maleRadioButton.isSelected() == false && femaleRadioButton.isSelected() == false){
            // gender not selected
            JOptionPane.showMessageDialog(this,"Gender Required");
            return;
        }
        GENDER gender;
        if(maleRadioButton.isSelected() == true){
            gender = GENDER.MALE;
        } else {
            gender = GENDER.FEMALE;
        }

        // need to validate date month year
        int date = Integer.parseInt((String)dateComboBox.getSelectedItem());
        int month = Integer.parseInt((String)monthComboBox.getSelectedItem());
        int year = Integer.parseInt((String)yearComboBox.getSelectedItem());
        Date dateOfBirth = new Date();
        dateOfBirth.setDate(date);
        dateOfBirth.setMonth(month-1);
        dateOfBirth.setYear(year-1900);

        boolean isIndian = isIndianCheckBox.isSelected();

        String basicSalaryText = basicSalaryTextField.getText();
        if (basicSalaryText.length() == 0) {
            JOptionPane.showMessageDialog(this, "Basic Salary Required");
            nameTextField.requestFocus();
            return ;
        }
        java.math.BigDecimal basicSalary = new BigDecimal(basicSalaryText);
        
        String panNumber = panNumberTextField.getText();
        if (panNumber.length() == 0) {
            JOptionPane.showMessageDialog(this, "PAN Number Required");
            nameTextField.requestFocus();
            return ;
        }

        String aadharCardNumber = aadharCardNumberTextField.getText();
        if (aadharCardNumber.length() == 0) {
            JOptionPane.showMessageDialog(this, "Aadhar Card Number Required");
            nameTextField.requestFocus();
            return ;
        }
        // validation complete

        EmployeeInterface employee = new Employee();
        employee.setEmployeeId(selectedEmployee.getEmployeeId());
        employee.setName(name);
        employee.setDesignation(designation);
        employee.setGender(gender);
        employee.setDateOfBirth(dateOfBirth);
        employee.setIsIndian(isIndian);
        employee.setBasicSalary(basicSalary);
        employee.setPANNumber(panNumber);
        employee.setAadharCardNumber(aadharCardNumber);

        try {
            employeeModel.update(employee);
            int rowIndex = 0;
            try {
                rowIndex = employeeModel.indexOfEmployee(employee);
            } catch (BLException blException) {
                // do nothing
            }
            employeeUI.employeeTable.setRowSelectionInterval(rowIndex, rowIndex);
            Rectangle rectangle = employeeUI.employeeTable.getCellRect(rowIndex, 0, true);
            employeeUI.employeeTable.scrollRectToVisible(rectangle);

            JOptionPane.showMessageDialog(this,"Employee Updated successfully");

            employeeUI.setEnabled(true);  // Re-enable EmployeeUI
            employeeUI.setVisible(true);
            dispose();  // Close the current window
            return;
        } catch (BLException blException) {
            if (blException.hasGenericException()) {
                JOptionPane.showMessageDialog(this, blException.getGenericException());
            } else if (blException.hasException("name")) {
                JOptionPane.showMessageDialog(this, blException.getException("name"));
            } else if (blException.hasException("designation")) {
                JOptionPane.showMessageDialog(this, blException.getException("designation"));
            } else if (blException.hasException("gender")) {
                JOptionPane.showMessageDialog(this, blException.getException("gender"));
            } else if (blException.hasException("dateOfBirth")) {
                JOptionPane.showMessageDialog(this, blException.getException("dateOfBirth"));
            } else if (blException.hasException("isIndian")) {
                JOptionPane.showMessageDialog(this, blException.getException("isIndian"));
            } else if (blException.hasException("basicSalary")) {
                JOptionPane.showMessageDialog(this, blException.getException("basicSalary"));
            } else if (blException.hasException("panNumber")) {
                JOptionPane.showMessageDialog(this, blException.getException("panNumber"));
            } else if (blException.hasException("aadharCardNumber")) {
                JOptionPane.showMessageDialog(this, blException.getException("aadharCardNumber"));
            }
            return;
        }
    }

    private void initInputFields(){
        nameTextField = new JTextField();
        designationComboBox = new JComboBox();
        initDesignationComboBox();
        maleRadioButton = new JRadioButton("Male");
        femaleRadioButton = new JRadioButton("Female");
        genderButtonGroup = new ButtonGroup();
        genderButtonGroup.add(maleRadioButton);
        genderButtonGroup.add(femaleRadioButton);
        dateComboBox = new JComboBox();
        monthComboBox = new JComboBox();
        yearComboBox = new JComboBox();
        initDOBComboBox();
        isIndianCheckBox = new JCheckBox();
        basicSalaryTextField = new JTextField();
        panNumberTextField = new JTextField();
        aadharCardNumberTextField = new JTextField();

        submitButton = new JButton();
        if(mode == MODE.ADD){
            // ADD MODE
            submitButton.setText("Add");
        } 
        else if(mode == MODE.EDIT){
            // EDIT MODE
            submitButton.setText("EDIT");
            nameTextField.setText(selectedEmployee.getName());
            designationComboBox.setSelectedItem(selectedEmployee.getDesignation().getTitle());
            if(selectedEmployee.getGender() == 'M')
                maleRadioButton.setSelected(true);
            else 
                femaleRadioButton.setSelected(true);
            Date dob = selectedEmployee.getDateOfBirth();
            String date = String.valueOf(dob.getDate());
            String month = String.valueOf(dob.getMonth()+1);
            String year = String.valueOf(dob.getYear()+1900);
            dateComboBox.setSelectedItem(date);
            monthComboBox.setSelectedItem(month);
            yearComboBox.setSelectedItem(year);
            isIndianCheckBox.setSelected(selectedEmployee.getIsIndian());
            basicSalaryTextField.setText(String.valueOf(selectedEmployee.getBasicSalary()));
            panNumberTextField.setText(selectedEmployee.getPANNumber());
            aadharCardNumberTextField.setText(selectedEmployee.getAadharCardNumber());
            
        }
        else {
            // VIEW MODE
            submitButton.setText("OK");
            nameTextField.setText(selectedEmployee.getName());
            designationComboBox.setSelectedItem(selectedEmployee.getDesignation().getTitle());
            if(selectedEmployee.getGender() == 'M')
                maleRadioButton.setSelected(true);
            else 
                femaleRadioButton.setSelected(true);
            Date dob = selectedEmployee.getDateOfBirth();
            String date = String.valueOf(dob.getDate());
            String month = String.valueOf(dob.getMonth()+1);
            String year = String.valueOf(dob.getYear()+1900);
            dateComboBox.setSelectedItem(date);
            monthComboBox.setSelectedItem(month);
            yearComboBox.setSelectedItem(year);
            isIndianCheckBox.setSelected(selectedEmployee.getIsIndian());
            basicSalaryTextField.setText(String.valueOf(selectedEmployee.getBasicSalary()));
            panNumberTextField.setText(selectedEmployee.getPANNumber());
            aadharCardNumberTextField.setText(selectedEmployee.getAadharCardNumber());
            
            // setEnabled = false
            nameTextField.setEnabled(false);
            designationComboBox.setEnabled(false);
            maleRadioButton.setEnabled(false);
            femaleRadioButton.setEnabled(false);
            dateComboBox.setEnabled(false);
            monthComboBox.setEnabled(false);
            yearComboBox.setEnabled(false);
            isIndianCheckBox.setEnabled(false);
            basicSalaryTextField.setEnabled(false);
            panNumberTextField.setEnabled(false);
            aadharCardNumberTextField.setEnabled(false);
        }
    }

    private void initDOBComboBox(){
        dateComboBox.addItem("1");
        dateComboBox.addItem("2");
        dateComboBox.addItem("3");
        dateComboBox.addItem("4");
        dateComboBox.addItem("5");
        dateComboBox.addItem("6");
        dateComboBox.addItem("7");
        dateComboBox.addItem("8");
        dateComboBox.addItem("9");
        dateComboBox.addItem("10");
        dateComboBox.addItem("11");
        dateComboBox.addItem("12");
        dateComboBox.addItem("13");
        dateComboBox.addItem("14");
        dateComboBox.addItem("15");
        dateComboBox.addItem("16");
        dateComboBox.addItem("17");
        dateComboBox.addItem("18");
        dateComboBox.addItem("19");
        dateComboBox.addItem("20");
        dateComboBox.addItem("21");
        dateComboBox.addItem("22");
        dateComboBox.addItem("23");
        dateComboBox.addItem("24");
        dateComboBox.addItem("25");
        dateComboBox.addItem("26");
        dateComboBox.addItem("27");
        dateComboBox.addItem("28");
        dateComboBox.addItem("29");
        dateComboBox.addItem("30");
        dateComboBox.addItem("31");

        monthComboBox.addItem("1");
        monthComboBox.addItem("2");
        monthComboBox.addItem("3");
        monthComboBox.addItem("4");
        monthComboBox.addItem("5");
        monthComboBox.addItem("6");
        monthComboBox.addItem("7");
        monthComboBox.addItem("8");
        monthComboBox.addItem("9");
        monthComboBox.addItem("10");
        monthComboBox.addItem("11");
        monthComboBox.addItem("12");

        Date mydate = new Date();
        int crrYear = mydate.getYear()+1900;
        for(int i=crrYear-1;i>1900;i--){
            yearComboBox.addItem(""+i+"");
        }

    }
    private void initDesignationComboBox(){
        try{
            DesignationModel designationModel = new DesignationModel();
        DesignationInterface designation;
        for(int i=0;i<designationModel.getRowCount();i++){
            designation = designationModel.getDesignationAt(i);
            designationMap.put(designation.getTitle(),designation);
            designationComboBox.addItem(designation.getTitle());
        }
        designationComboBox.addItem(null);
        designationComboBox.setSelectedItem(null);
        }catch(BLException blException){
            System.out.println("Error in EmployeeActionsUI.java in initDesignationComboBOx");
        }
    }
}