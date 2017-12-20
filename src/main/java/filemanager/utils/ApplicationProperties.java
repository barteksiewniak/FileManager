package filemanager.utils;

import org.apache.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApplicationProperties {
    private String property;
    private static final String PROPERTIES_FILE_NAME = "application.properties";
    private static final Logger LOGGER = Logger.getLogger(ApplicationProperties.class);

    public String getStringValueFromPropertiesForKey(String propertiesValue) throws IOException {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE_NAME)) {
            Properties prop = new Properties();

            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("Property file '" + PROPERTIES_FILE_NAME + "' not found in the classpath");
            }

            // get the property value and print it out
            property = prop.getProperty(propertiesValue);
        } catch (Exception e) {
            LOGGER.error("Exception: " + e);
        }
        return property;
    }
}
