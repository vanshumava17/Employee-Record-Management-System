package com.thinking.machines.hr.bl.managers;

public enum Managers {
    DESIGNATION("DesignationManager"),
    EMPLOYEE("EmployeeManager");

    private String className;

    private Managers(String className) {
        this.className = className;
    }

    public static String getManagerType(Managers manager) {
        return manager.className;
    }

    // Nested enum for DESIGNATION actions
    public enum Designation {
        ADD("addDesignation"),
        REMOVE("removeDesignation"),
        UPDATE("updateDesignation"),
        GET_BY_CODE("getDesignationByCode"),
        GET_BY_TITLE("getDesignationByTitle"),
        CODE_EXISTS("designationCodeExists"),
        TITLE_EXISTS("designationTitleExists"),
        GET_ALL("getDesignations"),
        GET_DESIGNATION_COUNT("getDesignationCount");

        private String action;

        private Designation(String action) {
            this.action = action;
        }

        public String getAction() {
            return this.action;
        }
    }

    // Nested enum for EMPLOYEE actions
    public enum Employee {
        ADD("addEmployee"),
        REMOVE("removeEmployee"),
        UPDATE("updateEmployee"),
        GET_BY_ID("getEmployeeByEmployeeId"),
        GET_ALL("getEmployees"),
        GET_BY_PAN_NUMBER("getEmployeeByPANNumber"),
        GET_BY_AADHAR_CARD_NUMBER("getEmployeeByAadharCardNumber"),
        GET_EMPLOYEE_COUNT("getEmployeeCount"),
        GET_BY_DESIGNATION_CODE("g  etEmployeesByDesignationCode"),
        GET_EMPLOYEE_COUNT_BY_DESIGNATION_CODE("getEmployeeCountByDesignationCode"),
        EMPLOYEE_AADHAR_CARD_NUMBER_EXISTS("employeeAadharCardNumberExists"),
        EMPLOYEE__PAN_NUMBER_EXISTS("employeePANNumberExists"),
        EMPLOYEE_ID_EXISTS("employeeIdExists"),
        IS_DESIGNATION_ALLOTED("designationAlloted");
        private String action;

        private Employee(String action) {
            this.action = action;
        }

        public String getAction() {
            return this.action;
        }
    }

    // Static method to get the action type
    public static String getActionType(Object action) {
        if (action instanceof Designation) {
            return ((Designation) action).getAction();
        } else if (action instanceof Employee) {
            return ((Employee) action).getAction();
        }
        return null;
    }
}
