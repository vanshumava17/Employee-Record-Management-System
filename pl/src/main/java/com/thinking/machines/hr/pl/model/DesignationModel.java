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

public class DesignationModel extends AbstractTableModel
{
private java.util.List<DesignationInterface> designations;
private String[] columnTitle;
private DesignationManagerInterface designationManager;
public DesignationModel()
{
populateDatastructures();
}

public int getRowCount()
{
return designations.size();
}

public int getColumnCount()
{
return this.columnTitle.length;
}

public String getColumnName(int columnIndex)
{
return columnTitle[columnIndex];
}

public Class getClass(int columnIndex)
{
if(columnIndex == 0) return Integer.class;
return String.class;
}

public boolean isCellEditable(int rowIndex,int columnIndex)
{
return false;
}

public Object getValueAt(int rowIndex, int columnIndex)
{
if(columnIndex == 0) return rowIndex+1;
return designations.get(rowIndex).getTitle();
}

private void populateDatastructures()
{
columnTitle = new String[2];
columnTitle[0] = "S.No.";
columnTitle[1] = "Designation";
try
{
designationManager = DesignationManager.getDesignationManager();
Set<DesignationInterface> blDesignations = designationManager.getDesignations();
designations = new LinkedList<>();
for(DesignationInterface d : blDesignations)
{
this.designations.add(d);
}
Collections.sort(this.designations,new Comparator<DesignationInterface>(){
public int compare(DesignationInterface left,DesignationInterface right)
{
return left.getTitle().toUpperCase().compareTo(right.getTitle().toUpperCase());
}
});
}
catch(BLException blException)
{
// yet to decide what to do here
}
}

// Application Specific methods

public void add(DesignationInterface designation) throws BLException
{
designationManager.addDesignation(designation);
designations.add(designation);
Collections.sort(this.designations,new Comparator<DesignationInterface>(){
public int compare(DesignationInterface left,DesignationInterface right)
{
return left.getTitle().toUpperCase().compareTo(right.getTitle().toUpperCase());
}
});
fireTableDataChanged();  // to update the table on window
}

// main purpose of following method is to highlight that row of Designation that may be added/updated etc
public int  indexOfDesignation(DesignationInterface designation) throws BLException
{
Iterator<DesignationInterface> iterator = this.designations.iterator();
DesignationInterface d;
int index = 0;
while(iterator.hasNext())
{
d = iterator.next();
if(d.equals(designation))return index;
index++; 
}
BLException blException = new BLException();
blException.setGenericException("Invalid Designation : "+designation.getTitle());
throw blException;
}


public int indexOfTitle(String title,boolean partialLeftSearch) throws BLException
{
Iterator<DesignationInterface> iterator = this.designations.iterator();
DesignationInterface d;
int index = 0;
while(iterator.hasNext())
{
d = iterator.next();
if(partialLeftSearch)
{
if(d.getTitle().toUpperCase().startsWith(title.toUpperCase()))return index;
}
else
{
if(d.getTitle().equalsIgnoreCase(title)) return index;
}
index++; 
}
BLException blException = new BLException();
blException.setGenericException("Invalid title: "+title);
throw blException;
}

public void update(DesignationInterface designation) throws BLException
{
designationManager.updateDesignation(designation);
this.designations.remove(indexOfDesignation(designation));	 
designations.add(designation);
Collections.sort(this.designations,new Comparator<DesignationInterface>(){
public int compare(DesignationInterface left,DesignationInterface right)
{
return left.getTitle().toUpperCase().compareTo(right.getTitle().toUpperCase());
}
});
fireTableDataChanged();  // to update the table on window
}

public void remove(int code) throws BLException
{
designationManager.removeDesignation(code);
Iterator<DesignationInterface> iterator = this.designations.iterator();
int index = 0;
while(iterator.hasNext())
{
if(iterator.next().getCode() == code)break;
index++;
}
if(index == this.designations.size())
{
BLException blException = new BLException();
blException.setGenericException("Invalid designation code : "+code);
throw blException;
}
this.designations.remove(index);
fireTableDataChanged();
}

public DesignationInterface getDesignationAt(int index) throws BLException
{
if(index<0 || index>=this.designations.size())
{
BLException blException = new BLException();
blException.setGenericException("Invalid index : "+index);
throw blException;
}
return this.designations.get(index);
}

public void exportToPDF(File file) throws BLException
{
try
{
if(file.exists()) file.delete();
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
Paragraph reportTitlePara = new Paragraph("List of Designations");
PdfFont reportTitleFont = PdfFontFactory.createFont(StandardFonts.TIMES_BOLD);
reportTitlePara.setFont(reportTitleFont);
reportTitlePara.setFontSize(16);
PdfFont columnTitleFont = PdfFontFactory.createFont(StandardFonts.TIMES_BOLD);
PdfFont dataFont = PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN);
PdfFont pageNumberFont = PdfFontFactory.createFont(StandardFonts.TIMES_BOLD);
Paragraph columnTitle1 = new Paragraph("S.No"); 
columnTitle1.setFont(columnTitleFont);
columnTitle1.setFontSize(14);
Paragraph columnTitle2 = new Paragraph("Designation"); 
columnTitle2.setFont(columnTitleFont);
columnTitle2.setFontSize(14);
Paragraph pageNumberPara;
Paragraph dataPara;
float topTableColumnWidths[] = {1,5};
float dataTableColumnWidths[] = {1,5};
int sno,x,pageSize;
pageSize = 5;
boolean newPage = true;
Table topTable = null;
Table dataTable = null;
Table pageNumberTable = null; 
Cell cell;  
int pageNumber = 0;
int numberOfPages = this.designations.size()/pageSize;
if(this.designations.size()%pageSize != 0) numberOfPages++;
DesignationInterface designation;
sno = 0;
x = 0;
while(x<this.designations.size())
{
if(newPage == true)
{
// create Page header
pageNumber++;
topTable = new Table(UnitValue.createPercentArray(topTableColumnWidths)); 
cell = new Cell();
cell.setBorder(Border.NO_BORDER);
cell.add(logoPara);
topTable.addCell(cell);
cell =new Cell();
cell.setBorder(Border.NO_BORDER);
cell.add(companyNamePara);
cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
topTable.addCell(cell);
document.add(topTable);
pageNumberPara = new Paragraph("Page :"+pageNumber+"/"+numberOfPages);
pageNumberPara.setFont(pageNumberFont);
pageNumberPara.setFontSize(14);
pageNumberTable = new Table(1);
pageNumberTable.setWidth(UnitValue.createPercentValue(100));
cell =new Cell();   
cell.setBorder(Border.NO_BORDER);
cell.add(pageNumberPara);
cell.setTextAlignment(TextAlignment.RIGHT);
pageNumberTable.addCell(cell);
document.add(pageNumberTable);
dataTable =new Table(UnitValue.createPercentArray(dataTableColumnWidths));
dataTable.setWidth(UnitValue.createPercentValue(100));
cell = new Cell(1,2); // row,column
cell.add(reportTitlePara);
cell.setTextAlignment(TextAlignment.CENTER);
dataTable.addHeaderCell(cell);
dataTable.addHeaderCell(columnTitle1);
dataTable.addHeaderCell(columnTitle2);
newPage = false;
}
designation = this.designations.get(x);
//add row to table
sno++;
// for sno
cell = new Cell();
dataPara = new Paragraph(String.valueOf(sno));
dataPara.setFont(dataFont);
dataPara.setFontSize(14); 
cell.add(dataPara);
cell.setTextAlignment(TextAlignment.RIGHT);
dataTable.addCell(cell);
// for Designation
cell = new Cell();
dataPara = new Paragraph(designation.getTitle());
dataPara.setFont(dataFont);
dataPara.setFontSize(14); 
cell.add(dataPara);
dataTable.addCell(cell);
x++;
if(sno%pageSize == 0 || x == this.designations.size())
{
// create footer
document.add(dataTable);
document.add(new Paragraph("Software By : Vansh Umava"));
if(x<this.designations.size())
{
// add new page to document  
document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
newPage = true;
}
}
}

document.close();
}catch(Exception e)
{
// BLException blException = new BLException();
// blException.setGenericException(e.getMessage());
// throw blException;
System.out.println(e);
}
}
}