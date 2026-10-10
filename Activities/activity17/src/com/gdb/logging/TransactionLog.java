package com.gdb.logging;

import com.gdb.command.TransactionCommand;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionLog {
    private static final String FILE_PATH = "data/transactions.ser";

    private static class AppendableObjectOutputStream extends ObjectOutputStream {
        public AppendableObjectOutputStream(OutputStream out) throws IOException {
            super(out);
        }

        @Override
        protected void writeStreamHeader() throws IOException {
            reset();
        }
    }

    public synchronized void log(TransactionCommand cmd) throws IOException {
        File file = new File(FILE_PATH);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists() && !parent.mkdirs() && !parent.isDirectory()) {
            throw new IOException("Could not create log directory: " + parent);
        }

        boolean append = file.exists() && file.length() > 0;
        try (ObjectOutputStream out = append
                ? new AppendableObjectOutputStream(new FileOutputStream(file, true))
                : new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(cmd);
            out.flush();
        }
    }

    public synchronized List<TransactionCommand> readAll()
            throws IOException, ClassNotFoundException {
        List<TransactionCommand> commands = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists() || file.length() == 0) {
            return commands;
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            while (true) {
                try {
                    Object object = in.readObject();
                    if (!(object instanceof TransactionCommand)) {
                        throw new IOException("Unexpected object in transaction log: "
                            + object.getClass().getName());
                    }
                    commands.add((TransactionCommand) object);
                } catch (EOFException eof) {
                    break;
                }
            }
        }
        return commands;
    }

    public synchronized void clear() {
        File file = new File(FILE_PATH);
        if (file.exists() && !file.delete()) {
            throw new IllegalStateException("Could not delete transaction log: " + file);
        }
    }
}
