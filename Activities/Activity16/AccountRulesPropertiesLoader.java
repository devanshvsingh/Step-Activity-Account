package Activity16;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class AccountRulesPropertiesLoader {

    private Properties properties = new Properties();

    public AccountRulesPropertiesLoader(String fileName) {

        try {
            InputStream input =
                new FileInputStream(fileName);

            properties.load(input);
            input.close();

        } catch (Exception e) {
            System.out.println(
                "Could not load " + fileName
            );
        }
    }

    public double getDouble(
            String key,
            double defaultValue) {

        String value =
            properties.getProperty(key);

        if (value == null) {
            return defaultValue;
        }

        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }
}