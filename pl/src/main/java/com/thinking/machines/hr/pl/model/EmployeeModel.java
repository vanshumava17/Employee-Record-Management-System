package com.thinking.machines.hr.pl.model;

import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.interfaces.managers.*;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.hr.bl.managers.*;

import java.util.*;

import javax.swing.*;
import javax.swing.table.*;

import java.io.*;

import com.itextpdf.kernel.pdf.*;
import com.itextpdf.io.image.*;
import com.itextpdf.kernel.font.*;
import com.itextpdf.io.font.constants.*;
import com.itextpdf.layout.*;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.*;
import com.itextpdf.layout.borders.*;

public class EmployeeModel extends AbstractTableModel {
    private java.util.List<EmployeeInterface> employees;
    private String[] columnTitle;
    private EmployeeManagerInterface employeeManager;

    public EmployeeModel() {
        populateDatastructures();
    }

    public int getRowCount() {
        return employees.size();
    }

    public int getColumnCount() {
        return this.columnTitle.length;
    }

    public String getColumnName(int columnIndex) {
        return columnTitle[columnIndex];
    }

    public Class getClass(int columnIndex) {
        if (columnIndex == 0)
            return Integer.class;
        return String.class;
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0)
            return rowIndex + 1;
        if (columnIndex == 1)
            return employees.get(rowIndex).getEmployeeId();
        return employees.get(rowIndex).getName();
    }

    private void populateDatastructures() {
        columnTitle = new String[3];
        columnTitle[0] = "S.No.";
        columnTitle[1] = "Employee Id";
        columnTitle[2] = "Name";
        try {
            employeeManager = EmployeeManager.getEmployeeManager();
            Set<EmployeeInterface> blEmployees = employeeManager.getEmployees();
            employees = new LinkedList<>();
            for (EmployeeInterface d : blEmployees) {
                this.employees.add(d);
            }
            Collections.sort(this.employees, new Comparator<EmployeeInterface>() {
                public int compare(EmployeeInterface left, EmployeeInterface right) {
                    return left.getEmployeeId().toUpperCase().compareTo(right.getEmployeeId().toUpperCase());
                }
            });
        } catch (BLException blException) {
            // yet to decide what to do here
        }
    }

    // Application Specific methods

    public void add(EmployeeInterface employee) throws BLException {
        employeeManager.addEmployee(employee);
        employees.add(employee);
        Collections.sort(this.employees, new Comparator<EmployeeInterface>() {
            public int compare(EmployeeInterface left, EmployeeInterface right) {
                return left.getEmployeeId().toUpperCase().compareTo(right.getEmployeeId().toUpperCase());
            }
        });
        fireTableDataChanged(); // to update the table on window
    }

    // main purpose of following method is to highlight that row of Employee that
    // may be added/updated etc
    public int indexOfEmployee(EmployeeInterface employee) throws BLException {
        Iterator<EmployeeInterface> iterator = this.employees.iterator();
        EmployeeInterface d;
        int index = 0;
        while (iterator.hasNext()) {
            d = iterator.next();
            if (d.equals(employee))
                return index;
            index++;
        }
        BLException blException = new BLException();
        blException.setGenericException("Invalid Employee : " + employee.getName());
        throw blException;
    }

    public int indexOfName(String name, boolean partialLeftSearch) throws BLException {
        Iterator<EmployeeInterface> iterator = this.employees.iterator();
        EmployeeInterface d;
        int index = 0;
        while (iterator.hasNext()) {
            d = iterator.next();
            if (partialLeftSearch) {
                if (d.getName().toUpperCase().startsWith(name.toUpperCase()))
                    return index;
            } else {
                if (d.getName().equalsIgnoreCase(name))
                    return index;
            }
            index++;
        }
        BLException blException = new BLException();
        blException.setGenericException("Invalid title: " + name);
        throw blException;
    }

    public void update(EmployeeInterface employee) throws BLException {
        employeeManager.updateEmployee(employee);
        this.employees.remove(indexOfEmployee(employee));
        employees.add(employee);
        Collections.sort(this.employees, new Comparator<EmployeeInterface>() {
            public int compare(EmployeeInterface left, EmployeeInterface right) {
                return left.getEmployeeId().toUpperCase().compareTo(right.getEmployeeId().toUpperCase());
            }
        });
        fireTableDataChanged(); // to update the table on window
    }

    public void remove(String employeeId) throws BLException {
        employeeManager.removeEmployee(employeeId);
        Iterator<EmployeeInterface> iterator = this.employees.iterator();
        int index = 0;
        while (iterator.hasNext()) {
            if (iterator.next().getEmployeeId().toUpperCase().equalsIgnoreCase(employeeId.toUpperCase()))
                break;
            index++;
        }
        if (index == this.employees.size()) {
            BLException blException = new BLException();
            blException.setGenericException("Invalid employee employee Id : " + employeeId);
            throw blException;
        }
        this.employees.remove(index);
        fireTableDataChanged();
    }

    public EmployeeInterface getEmployeeAt(int index) throws BLException {
        if (index < 0 || index >= this.employees.size()) {
            BLException blException = new BLException();
            blException.setGenericException("Invalid index : " + index);
            throw blException;
        }
        return this.employees.get(index);
    }

    public void exportToPDF(File file) throws BLException { 
        try {
            if (file.exists())
                file.delete();
            PdfWriter pdfWriter = new PdfWriter(file);
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            Document document = new Document(pdfDocument);
            Image logo = new Image(ImageDataFactory.create(this.getClass().getResource("/icons/logoIcon.png")));
            Paragraph logoPara = new Paragraph();
            logoPara.add(logo);
            Paragraph companyNamePara = new Paragraph();
            companyNamePara.add("Vku IT solutions");
            PdfFont companyNameFont = PdfFontFactory.createFont(StandardFonts.TIMES_BOLD);
            companyNamePara.setFont(companyNameFont);
            companyNamePara.setFontSize(18);
            Paragraph reportTitlePara = new Paragraph("List of Employees");
            PdfFont reportTitleFont = PdfFontFactory.createFont(StandardFonts.TIMES_BOLD);
            reportTitlePara.setFont(reportTitleFont);
            reportTitlePara.setFontSize(16);
            PdfFont columnTitleFont = PdfFontFactory.createFont(StandardFonts.TIMES_BOLD);
            PdfFont dataFont = PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN);
            PdfFont pageNumberFont = PdfFontFactory.createFont(StandardFonts.TIMES_BOLD);
            Paragraph columnTitle1 = new Paragraph("S.No");
            columnTitle1.setFont(columnTitleFont);
            columnTitle1.setFontSize(14);
            Paragraph columnTitle2 = new Paragraph("Employee");
            columnTitle2.setFont(columnTitleFont);
            columnTitle2.setFontSize(14);
            Paragraph pageNumberPara;
            Paragraph dataPara;
            float topTableColumnWidths[] = { 1, 5 };
            float dataTableColumnWidths[] = { 1, 5 };
            int sno, x, pageSize;
            pageSize = 5;
            boolean newPage = true;
            Table topTable = null;
            Table dataTable = null;
            Table pageNumberTable = null;
            Cell cell;
            int pageNumber = 0;
            int numberOfPages = this.employees.size() / pageSize;
            if (this.employees.size() % pageSize != 0)
                numberOfPages++;
            EmployeeInterface employee;
            sno = 0;
            x = 0;
            while (x < this.employees.size()) {
                if (newPage == true) {
                    // create Page header
                    pageNumber++;
                    topTable = new Table(UnitValue.createPercentArray(topTableColumnWidths));
                    cell = new Cell();
                    cell.setBorder(Border.NO_BORDER);
                    cell.add(logoPara);
                    topTable.addCell(cell);
                    cell = new Cell();
                    cell.setBorder(Border.NO_BORDER);
                    cell.add(companyNamePara);
                    cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
                    topTable.addCell(cell);
                    document.add(topTable);
                    pageNumberPara = new Paragraph("Page :" + pageNumber + "/" + numberOfPages);
                    pageNumberPara.setFont(pageNumberFont);
                    pageNumberPara.setFontSize(14);
                    pageNumberTable = new Table(1);
                    pageNumberTable.setWidth(UnitValue.createPercentValue(100));
                    cell = new Cell();
                    cell.setBorder(Border.NO_BORDER);
                    cell.add(pageNumberPara);
                    cell.setTextAlignment(TextAlignment.RIGHT);
                    pageNumberTable.addCell(cell);
                    document.add(pageNumberTable);
                    dataTable = new Table(UnitValue.createPercentArray(dataTableColumnWidths));
                    dataTable.setWidth(UnitValue.createPercentValue(100));
                    cell = new Cell(1, 2); // row,column
                    cell.add(reportTitlePara);
                    cell.setTextAlignment(TextAlignment.CENTER);
                    dataTable.addHeaderCell(cell);
                    dataTable.addHeaderCell(columnTitle1);
                    dataTable.addHeaderCell(columnTitle2);
                    newPage = false;
                }
                employee = this.employees.get(x);
                // add row to table
                sno++;
                // for sno
                cell = new Cell();
                dataPara = new Paragraph(String.valueOf(sno));
                dataPara.setFont(dataFont);
                dataPara.setFontSize(14);
                cell.add(dataPara);
                cell.setTextAlignment(TextAlignment.RIGHT);
                dataTable.addCell(cell);
                // for Employee
                cell = new Cell();
                dataPara = new Paragraph(employee.getEmployeeId());
                dataPara.setFont(dataFont);
                dataPara.setFontSize(14);
                cell.add(dataPara);
                dataTable.addCell(cell);
                x++;
                if (sno % pageSize == 0 || x == this.employees.size()) {
                    // create footer
                    document.add(dataTable);
                    document.add(new Paragraph("Software By : Vansh Umava"));
                    if (x < this.employees.size()) {
                        // add new page to document
                        document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
                        newPage = true;
                    }
                }
            }

            document.close();
        } catch (Exception e) {
            // BLException blException = new BLException();
            // blException.setGenericException(e.getMessage());
            // throw blException;
            System.out.println(e);
        }
    }
}