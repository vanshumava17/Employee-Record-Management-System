package com.thinking.machines.hr.dl.dao;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.exceptions.*;
import java.util.*;
import java.sql.*;
import java.io.*;
public class DesignationDAO implements DesignationDAOInterface
{
public void add(DesignationDTOInterface designationDTO) throws DAOException
{
if(designationDTO == null)throw new DAOException("Designation is null");
String title = designationDTO.getTitle();
if(title == null)throw new DAOException("Designation is null");
title = title.trim();
if(title.length() == 0)throw new DAOException("Length of Designation is zero");
try
{
Connection connection = DAOConnection.getConnection();
// to check if title already exists
PreparedStatement preparedStatement;
ResultSet resultSet;
preparedStatement = connection.prepareStatement("select code from designation where title=?");
preparedStatement.setString(1, title);
resultSet = preparedStatement.executeQuery();
if(resultSet.next())
{
resultSet.close();
preparedStatement.close();
connection.close();
throw new DAOException("Designation " +title + " exists");
}
preparedStatement = connection.prepareStatement("insert into designation (title) values (?)",Statement.RETURN_GENERATED_KEYS);
preparedStatement.setString(1, title);
preparedStatement.executeUpdate();
resultSet = preparedStatement.getGeneratedKeys();
resultSet.next();
int code = resultSet.getInt(1);
designationDTO.setCode(code);
resultSet.close();
preparedStatement.close();
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
}


public void update(DesignationDTOInterface designationDTO) throws DAOException
{
if(designationDTO == null)throw new DAOException("Designation is null");
int code = designationDTO.getCode();
if(code<=0)throw new DAOException("Designation is null");
String title = designationDTO.getTitle();
if(title == null)throw new DAOException("Designation is null");
title = title.trim();
if(title.length() == 0)throw new DAOException("Length of Designation is zero");
try
{
Connection connection = DAOConnection.getConnection();
PreparedStatement preparedStatement;
// check for code existence 
preparedStatement = connection.prepareStatement("select code from designation where code=?");
preparedStatement.setInt(1, code);
ResultSet resultSet = preparedStatement.executeQuery();
if(resultSet.next() == false)
{
resultSet.close();
preparedStatement.close();
connection.close();
throw new DAOException("Invalid Code : "+code);
}
resultSet.close();
preparedStatement.close();
// check if title exists against another code 
preparedStatement = connection.prepareStatement("select code from designation where title=? and code<>?"); // here <> means != 
preparedStatement.setString(1, title);
preparedStatement.setInt(2, code);
resultSet = preparedStatement.executeQuery();
if(resultSet.next())
{
resultSet.close();
preparedStatement.close();
connection.close();
throw new DAOException("Designation "+title+" exists");
}
resultSet.close();
preparedStatement.close();
// update designation in designation table
preparedStatement = connection.prepareStatement("update designation set title=? where code=?");
preparedStatement.setString(1,title);
preparedStatement.setInt(2, code);
preparedStatement.executeUpdate();
preparedStatement.close();
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
    
}
public void delete(int code) throws DAOException
{
if(code<=0) throw new DAOException("Invalid code : "+code);
try
{
// need to check this code 
Connection connection = DAOConnection.getConnection();
PreparedStatement preparedStatement ;
// checking if code not exists
preparedStatement = connection.prepareStatement("select * from designation where code=?");
preparedStatement.setInt(1,code);
ResultSet resultSet = preparedStatement.executeQuery();
if(resultSet.next()==false)
{
resultSet.close();
preparedStatement.close();
connection.close();
throw new DAOException("Invalid Code : "+code);
}
String designation = resultSet.getString("title");
resultSet.close();
preparedStatement.close();
// check if designation alloted to any employee
preparedStatement = connection.prepareStatement("select gender from employee where designation_code=?");
preparedStatement.setInt(1, code);
resultSet = preparedStatement.executeQuery();
if(resultSet.next())
{
resultSet.close();
preparedStatement.close();
connection.close();
throw new DAOException("Cannot delete designation "+designation+" as it has been alloted to employee(s)");
}
resultSet.close();
preparedStatement.close();
// delete designation from table
preparedStatement = connection.prepareStatement("delete from designation where code=?");
preparedStatement.setInt(1, code);
preparedStatement.executeUpdate();
preparedStatement.close();
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
}


public Set<DesignationDTOInterface> getAll() throws DAOException    // we'll return a treeSet
{
Set<DesignationDTOInterface> designations = new TreeSet<>();
try
{
DesignationDTOInterface designationDTO;
Connection connection = DAOConnection.getConnection();
PreparedStatement preparedStatement = connection.prepareStatement("select * from designation");
ResultSet resultSet = preparedStatement.executeQuery();
while(resultSet.next())
{
designationDTO = new DesignationDTO();
designationDTO.setCode(resultSet.getInt("code"));
designationDTO.setTitle(resultSet.getString("title").trim());
designations.add(designationDTO);
}
resultSet.close();
preparedStatement.close();
connection.close();
return designations;
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
}


public DesignationDTOInterface getByCode(int code) throws DAOException
{
if(code<=0)throw new DAOException("Invalid Code " + code);
try
{
Connection connection = DAOConnection.getConnection();
PreparedStatement preparedStatement = connection.prepareStatement("select * from designation where code=?");
preparedStatement.setInt(1, code);
ResultSet resultSet = preparedStatement.executeQuery();
if(resultSet.next() == false)
{
resultSet.close();
preparedStatement.close();
connection.close();
throw new DAOException("Invalid code "+code);
}
DesignationDTO designationDTO = new DesignationDTO();
designationDTO.setCode(resultSet.getInt("code"));
designationDTO.setTitle(resultSet.getString("title").trim());
resultSet.close();
preparedStatement.close();
connection.close();
return designationDTO;
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
}


public DesignationDTOInterface getByTitle(String title) throws DAOException
{
if(title == null || title.trim().length() == 0)throw new DAOException("Title is missing, please provide a valid title");
try
{
Connection connection = DAOConnection.getConnection();
PreparedStatement preparedStatement = connection.prepareStatement("select * from designation where title=?");
preparedStatement.setString(1, title);
ResultSet resultSet = preparedStatement.executeQuery();
if(resultSet.next() == false)
{
resultSet.close();
preparedStatement.close();
connection.close();
throw new DAOException("Invalid title "+title);
}
DesignationDTO designationDTO = new DesignationDTO();
designationDTO.setCode(resultSet.getInt("code"));
designationDTO.setTitle(resultSet.getString("title").trim());
resultSet.close();
preparedStatement.close();
connection.close();
return designationDTO;
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
}


public boolean codeExists(int code) throws DAOException
{
if(code<=0)return false;
try
{
Connection connection = DAOConnection.getConnection();
PreparedStatement preparedStatement;
preparedStatement = connection.prepareStatement("select code from designation where code=?");
preparedStatement.setInt(1, code);
ResultSet resultSet = preparedStatement.executeQuery();
boolean exists = resultSet.next();
resultSet.close();
preparedStatement.close();
connection.close();
return exists;
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
}


public boolean titleExists(String title) throws DAOException
{
if(title == null || title.trim().length() == 0)throw new DAOException("Title is missing, please provide a valid title");
try
{
Connection connection = DAOConnection.getConnection();
PreparedStatement preparedStatement;
preparedStatement = connection.prepareStatement("select code from designation where title=?");
preparedStatement.setString(1, title);
ResultSet resultSet = preparedStatement.executeQuery();
boolean exists = resultSet.next();
resultSet.close();
preparedStatement.close();
connection.close();
return exists;
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
}



public int getCount() throws DAOException
{
try
{
Connection connection = DAOConnection.getConnection();
PreparedStatement preparedStatement = connection.prepareStatement("select count(*) as cnt from designation");
ResultSet resultSet = preparedStatement.executeQuery();
resultSet.next();
int count = resultSet.getInt("cnt");
resultSet.close();
preparedStatement.close();
connection.close();
return count;
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
}
}