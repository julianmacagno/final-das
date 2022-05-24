package com.das.entities.core.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public abstract class Dao<T, S> implements AutoCloseable {
    private final String providerName;
    private final String connectionString;
    private Connection connection;
    private PreparedStatement statement;

    protected Dao() {
        this.providerName = System.getenv().get("DATABASE_PROVIDERNAME");
        this.connectionString = System.getenv().get("DATABASE_CONNECTIONSTRING");
    }

    protected abstract T make(ResultSet param) throws SQLException;

    protected abstract T insert(S param) throws SQLException;

    protected abstract T update(S param) throws SQLException;

    protected abstract T delete(S param) throws SQLException;

    protected abstract List<T> select(S param) throws SQLException;

    protected abstract boolean valid(S param) throws SQLException;

    protected void connect() throws SQLException {
        try {
            Class.forName(this.providerName);
            this.connection = DriverManager.getConnection(this.connectionString);
            this.connection.setAutoCommit(true);
        } catch (ClassNotFoundException | SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

    protected int executeUpdate() throws SQLException {
        int updatedRows = 0;
        try {
            this.connection.setAutoCommit(false);
            updatedRows = this.statement.executeUpdate();
            this.connection.commit();
        } catch (SQLException e) {
            this.connection.rollback();
            throw e;
        } finally {
            this.connection.setAutoCommit(true);
        }

        return updatedRows;
    }

    protected List<T> executeUpdateQuery() throws SQLException {
        try {
            List<T> list = new LinkedList<>();
            this.connection.setAutoCommit(false);
            ResultSet result = this.statement.executeQuery();

            while (result.next()) {
                list.add(this.make(result));
            }

            this.connection.commit();
            return list;
        } catch (SQLException e) {
            this.connection.rollback();
            throw new SQLException(e.getMessage());
        } finally {
            this.connection.setAutoCommit(true);
        }
    }

    protected List<T> executeQuery() throws SQLException {
        List<T> list = new LinkedList<>();
        ResultSet result = this.statement.executeQuery();

        while (result.next()) {
            list.add(this.make(result));
        }

        return list;
    }

    protected void setProcedure(String procedure) throws SQLException {
        this.statement = this.connection.prepareCall("{ CALL " + procedure + " }");
    }

    protected void setProcedure(String procedure, int resultSetType, int resultSetConcurrency) throws SQLException {
        this.statement = this.connection.prepareCall("{ CALL " + procedure + " }", resultSetType, resultSetConcurrency);
    }

    protected void setStatement(String statement) throws SQLException {
        this.statement = this.connection.prepareStatement(statement);
    }

    protected void setStatement(String statement, int resultSetType, int resultSetConcurrency) throws SQLException {
        this.statement = this.connection.prepareStatement(statement, resultSetType, resultSetConcurrency);
    }

    protected PreparedStatement getStatement() {
        return this.statement;
    }

    protected void setNull(int paramIndex, int sqlType) throws SQLException {
        this.statement.setNull(paramIndex, sqlType);
    }

    protected void setParameter(int paramIndex, long paramValue) throws SQLException {
        this.statement.setLong(paramIndex, paramValue);
    }

    protected void setParameter(int paramIndex, boolean paramValue) throws SQLException {
        this.statement.setBoolean(paramIndex, paramValue);
    }

    protected void setParameter(int paramIndex, int paramValue) throws SQLException {
        this.statement.setInt(paramIndex, paramValue);
    }

    protected void setParameter(int paramIndex, double paramValue) throws SQLException {
        this.statement.setDouble(paramIndex, paramValue);
    }

    protected void setParameter(int paramIndex, String paramValue) throws SQLException {
        this.statement.setString(paramIndex, paramValue);
    }

    protected void setParameter(int paramIndex, Date paramValue) throws SQLException {
        this.statement.setDate(paramIndex, paramValue);
    }

    protected void setOutParameter(int paramIndex, int sqlType) throws SQLException {
        ((CallableStatement) this.statement).registerOutParameter(paramIndex, sqlType);
    }

    protected boolean getBooleanParam(String paramName) throws SQLException {
        return ((CallableStatement) this.statement).getBoolean(paramName);
    }

    protected long getLongParam(String paramName) throws SQLException {
        return ((CallableStatement) this.statement).getLong(paramName);
    }

    protected int getIntParam(String paramName) throws SQLException {
        return ((CallableStatement) this.statement).getInt(paramName);
    }

    protected double getDoubleParam(String paramName) throws SQLException {
        return ((CallableStatement) this.statement).getDouble(paramName);
    }

    protected String getStringParam(String paramName) throws SQLException {
        return ((CallableStatement) this.statement).getString(paramName);
    }

    protected Date getDateParam(String paramName) throws SQLException {
        return ((CallableStatement) this.statement).getDate(paramName);
    }

    protected long getLongParam(int paramIndex) throws SQLException {
        return ((CallableStatement) this.statement).getLong(paramIndex);
    }

    protected int getIntParam(int paramIndex) throws SQLException {
        return ((CallableStatement) this.statement).getInt(paramIndex);
    }

    protected double getDoubleParam(int paramIndex) throws SQLException {
        return ((CallableStatement) this.statement).getDouble(paramIndex);
    }

    protected String getStringParam(int paramIndex) throws SQLException {
        return ((CallableStatement) this.statement).getString(paramIndex);
    }

    protected Date getDateParam(int paramIndex) throws SQLException {
        return ((CallableStatement) this.statement).getDate(paramIndex);
    }

    protected boolean hasColumn(ResultSet rs, String columnName) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int columns = rsmd.getColumnCount();
        for (int x = 1; x <= columns; x++) {
            if (columnName.equals(rsmd.getColumnName(x))) {
                return true;
            }
        }
        return false;
    }

    public void close() {
        try {
            if (this.statement != null && !this.statement.isClosed()) {
                this.statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (this.connection != null && !this.connection.isClosed()) {
                    this.connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
