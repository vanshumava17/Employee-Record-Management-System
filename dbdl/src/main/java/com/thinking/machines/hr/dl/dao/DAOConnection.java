package com.thinking.machines.hr.dl.dao;

import com.thinking.machines.hr.dl.exceptions.*;
import java.sql.*;

public class DAOConnection 
{
private DAOConnection(){}

public static Connection getConnection() throws DAOException
{
Connection connection = null;
try
{
Class.forName("com.mysql.cj.jdbc.Driver");
connection = DriverManager.getConnection("jdbc:mysql://hr@localhost:3306/hrdb","hr","hr");
}catch(Exception e)
{
throw new DAOException(e.getMessage());
}
return connection;
}
}