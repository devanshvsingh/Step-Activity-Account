
package com.gdb.db;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionProvider {

    Connection getConnection() throws SQLException;

    void shutdown();

    String getProviderName();
}
