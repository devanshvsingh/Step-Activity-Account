
package com.gdb.logging;

import com.gdb.command.TransactionCommand;
import java.util.*;

public class MemoryLogDestination implements LogDestination
{
    private final List<TransactionCommand> transactions =
        new ArrayList<>();

    @Override
    public void write(TransactionCommand cmd)
    {
        transactions.add(cmd);
    }

    @Override
    public List<TransactionCommand> readAll()
    {
        return new ArrayList<>(transactions);
    }

    @Override
    public void clear()
    {
        transactions.clear();
    }

    @Override
    public String getDestinationName()
    {
        return "MEMORY";
    }
}
