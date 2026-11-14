
package com.gdb;

import com.gdb.logging.FileLogDestination;
import com.gdb.logging.TransactionLogger;
import com.gdb.service.AccountService;
import com.gdb.ui.AccountUI;

public class Main
{
    public static void main(String[] args)
    {
        try
        {
            System.out.println("Booting Global Digital Bank...");

            // STEP 15: Initialize the file logging backend
            FileLogDestination fileDestination =
                new FileLogDestination();

            // Create TransactionLogger
            TransactionLogger logger =
                new TransactionLogger(fileDestination);

            // Create AccountService
            AccountService service =
                new AccountService(logger);

            // Create AccountUI
            AccountUI ui = new AccountUI(service);

            // Launch the interactive application
            ui.start();
        }
        catch(Exception e)
        {
            System.out.println(
                "Application failed: " + e.getMessage()
            );

            e.printStackTrace();
        }
    }
}
