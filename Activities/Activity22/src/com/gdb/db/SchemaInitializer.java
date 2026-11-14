package com.gdb.db;

import java.io.*;
import java.sql.Connection;
import java.sql.Statement;

/**
 * Initializes the database schema by executing schema.sql.
 */
public class SchemaInitializer {

    public static void initialize(ConnectionProvider provider) {

        String schema = readSchemaSql();

        String[] statements = schema.split(";");

        try (
            Connection connection = provider.getConnection();
            Statement statement = connection.createStatement()
        ) {

            for (String sql : statements) {

                if (!sql.trim().isEmpty()) {

                    statement.execute(sql.trim());
                }
            }

        } catch (Exception e) {

            throw new RuntimeException(
                    "Database schema initialization failed", e);
        }
    }

    public static String readSchemaSql() {

        StringBuilder content = new StringBuilder();

        InputStream input = SchemaInitializer.class
                .getClassLoader()
                .getResourceAsStream("schema.sql");

        try {

            if (input == null) {

                input = new FileInputStream(
                        "src/main/resources/schema.sql");
            }

            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(input));

            String line;

            while ((line = reader.readLine()) != null) {

                content.append(line).append("\n");
            }

            reader.close();

        } catch (IOException e) {

            throw new RuntimeException(
                    "Unable to read schema.sql", e);
        }

        return content.toString();
    }
}