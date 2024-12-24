package com.thinking.machines.hr.dl.dto;
import com.thinking.machines.hr.dl.interfaces.dto.*;
public class DesignationDTO implements DesignationDTOInterface
{
private int code;
private String Title;
public DesignationDTO()
{
this.code = 0;
this.Title = "";
}
public void setCode(int code)
{
this.code = code;
}
public int getCode()
{
return this.code;
}
public void setTitle(java.lang.String Title)
{
this.Title = Title;
}
public java.lang.String getTitle()
{
return this.Title;
}
public boolean equals(Object other)
{
if(!(other instanceof DesignationDTO))return false;
DesignationDTOInterface designationDTO;
designationDTO = (DesignationDTOInterface)other;
return this.code == designationDTO.getCode();
}
public int compareTo(DesignationDTOInterface other)
{
return this.getCode() - other.getCode();
}
}