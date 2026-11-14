
package com.gdb.logging;

import com.gdb.command.TransactionCommand;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TransactionLog
{
    private static final Path LOG_PATH =
        Path.of("data", "transactions.ser");

    public void log(TransactionCommand cmd) throws IOException
    {
        if(cmd == null)
        {
            throw new IllegalArgumentException(
                "Command cannot be null"
            );
        }

        Path parent = LOG_PATH.getParent();
        Files.createDirectories(parent);

        boolean append = Files.exists(LOG_PATH)
            && Files.size(LOG_PATH) > 0;

        try(FileOutputStream fileOut =
                new FileOutputStream(LOG_PATH.toFile(), append))
        {
            ObjectOutputStream out;

            if(append)
            {
                out = new AppendableObjectOutputStream(fileOut);
            }
            else
            {
                out = new ObjectOutputStream(fileOut);
            }

            try(ObjectOutputStream stream = out)
            {
                stream.writeObject(cmd);
                stream.flush();
            }
        }
    }

    public List<TransactionCommand> readAll() throws IOException
    {
        List<TransactionCommand> commands = new ArrayList<>();

        if(!Files.exists(LOG_PATH) || Files.size(LOG_PATH) == 0)
        {
            return commands;
        }

        try(ObjectInputStream in =
                new ObjectInputStream(
                    new FileInputStream(LOG_PATH.toFile())))
        {
            while(true)
            {
                try
                {
                    Object obj = in.readObject();

                    if(obj instanceof TransactionCommand)
                    {
                        commands.add((TransactionCommand) obj);
                    }
                    else
                    {
                        throw new IOException(
                            "Unexpected object in transaction log"
                        );
                    }
                }
                catch(EOFException e)
                {
                    break;
                }
                catch(ClassNotFoundException e)
                {
                    throw new IOException(
                        "Unable to load a logged command", e
                    );
                }
            }
        }

        return commands;
    }

    public void clear() throws IOException
    {
        Files.deleteIfExists(LOG_PATH);
    }

    private static class AppendableObjectOutputStream
        extends ObjectOutputStream
    {
        public AppendableObjectOutputStream(OutputStream out)
            throws IOException
        {
            super(out);
        }

        @Override
        protected void writeStreamHeader() throws IOException
        {
            reset();
        }
    }
}
