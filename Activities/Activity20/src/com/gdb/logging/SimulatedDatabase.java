
package com.gdb.logging;

import java.util.*;

public class SimulatedDatabase
{
    private final Map<String, List<Object>> tables =
        new HashMap<>();

    public void insert(String tableName, Object data)
    {
        tables.computeIfAbsent(
            tableName, k -> new ArrayList<>()
        ).add(data);
    }

    public List<Object> selectAll(String tableName)
    {
        return new ArrayList<>(
            tables.getOrDefault(tableName, new ArrayList<>())
        );
    }

    public void deleteAll(String tableName)
    {
        tables.remove(tableName);
    }
}
