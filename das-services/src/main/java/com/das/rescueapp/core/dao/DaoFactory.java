package com.das.rescueapp.core.dao;

import java.sql.SQLException;

public class DaoFactory {
    public static Object getDao(Class<?> myClass) throws SQLException {
        try {
            return Class.forName(myClass.getName()).getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException(e.getMessage());
        }
    }
}
