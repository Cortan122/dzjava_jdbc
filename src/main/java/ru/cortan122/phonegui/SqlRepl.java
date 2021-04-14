package ru.cortan122.phonegui;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class SqlRepl {
    static String url = "jdbc:derby:derbyDB;create=true";

    static void printSqlException(SQLException e) {
        System.err.println("----- SQLException -----");
        System.err.println("  SQL State:  " + e.getSQLState());
        System.err.println("  Error Code: " + e.getErrorCode());
        System.err.println("  Message:    " + e.getMessage());
        System.err.println();
    }

    static void printResultSet(ResultSet rs) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();

        StringBuilder header = new StringBuilder();
        for (int i = 1; i <= columnCount; i++) {
            header.append(rsmd.getColumnName(i)).append("\t");
        }
        System.out.println(header);

        while(rs.next()) {
            StringBuilder line = new StringBuilder();
            for (int i = 1; i <= columnCount; i++) {
                line.append(rs.getString(i)).append("\t");
            }
            System.out.println(line);
        }
        System.out.println();
    }

    static void runCommand(String command) {
        try (var connection = DriverManager.getConnection(url);
             var statement = connection.createStatement()) {
            if (statement.execute(command)) {
                printResultSet(statement.getResultSet());
            } else {
                System.out.println("всё норм (:\n");
            }
        } catch (SQLException e) {
            printSqlException(e);
        }
    }

    public static void main(String[] args) {
        var scanner = new Scanner(System.in);
        while (true) {
            String cmd;
            try {
                System.out.print("Введите запрос:\n");
                cmd = scanner.nextLine().trim();
            } catch (NoSuchElementException ignored) {
                System.out.println("stdin закончился — комманд больше нет");
                break;
            }

            runCommand(cmd);
        }
    }
}
