package com.zippy.api.constants;

public enum Roles {
    ADMIN, CLIENT, EMPLOYEE;

    public static Roles[] getRoles() {
        return Roles.values();
    }
}
