package com.gdb.domain;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.Properties;
 
public class AccountRulesPropertiesLoader
{
    private final Properties properties;

    public AccountRulesPropertiesLoader(String filePath)
    {
        properties = new Properties();

        try
        {
            InputStream inputStream;

            inputStream = new FileInputStream(filePath);

            properties.load(inputStream);
            inputStream.close();
        }
        catch(IOException e)
        {
            throw new RuntimeException(
                "Unable to load properties file: " + filePath,
                e
            );
        }
    }

    public AccountRulesPropertiesLoader()
    {
        properties = new Properties();
    }

    public void loadFromClasspath(String resourcePath)
    {
        try
        {
            InputStream inputStream =
                getClass()
                    .getClassLoader()
                    .getResourceAsStream(resourcePath);

            if(inputStream == null)
            {
                throw new RuntimeException(
                    "Properties resource not found: " + resourcePath
                );
            }

            properties.load(inputStream);
            inputStream.close();
        }
        catch(IOException e)
        {
            throw new RuntimeException(
                "Unable to load properties resource: "
                + resourcePath,
                e
            );
        }
    }

    public String getProperty(
        String key,
        String defaultValue)
    {
        return properties.getProperty(key, defaultValue);
    }

    public double getDouble(
        String key,
        double defaultValue)
    {
        String value = properties.getProperty(key);

        if(value == null)
        {
            return defaultValue;
        }

        try
        {
            return Double.parseDouble(value.trim());
        }
        catch(NumberFormatException e)
        {
            return defaultValue;
        }
    }
}