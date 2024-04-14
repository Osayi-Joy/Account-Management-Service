package com.interswitchgroup.accountmanagementsystem.common.constants;


/**
 * @author Joy Osayi
 * @createdOn Apr-13(Sat)-2024
 */
public class Constants {
    public static final String ROLE_SUPER_ADMIN = "ROLE_SUPER_ADMIN";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_USER = "ROLE_USER";

    public static final String SUPER_ADMIN_EMAIL = "superadmin@interswitch.com";
    public static final String SUPER_ADMIN_PASSWORD = "SuperAdminPassword123!";
    public static final String SUPER_ADMIN_FIRSTNAME = "super";
    public static final String SUPER_ADMIN_LASTNAME = "Admin";
    public static final String SUPER_ADMIN_USERNAME = "superadmin";
    public static final String CREATED_DATE = "createdDate";

    public static final String PAGE_SIZE_DEFAULT_VALUE = "10";
    public static final String PAGE_NUMBER_DEFAULT_VALUE = "0";
    public static final String PAGE_SIZE = "pageSize";
    public static final String PAGE_NUMBER = "pageNumber";

    public static final String API_V1 = "/api/v1/account-service/";
    public static final String AUTHENTICATION_API_VI = API_V1 + "authentication/process/";
    public static final String ROLES_API_V1 = API_V1 + "role/process/";
    public static final String ADMINISTRATOR_API_V1 = API_V1 + "administrator/process/";
    private Constants(){
    }
}
