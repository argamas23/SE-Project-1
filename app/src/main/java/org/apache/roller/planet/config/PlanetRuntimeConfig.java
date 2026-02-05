To address the detected smells, the `PlanetRuntimeConfig` class will be refactored into an instance-based class with clear dependencies injected through its constructor. The responsibilities of XML parsing and resource loading will be extracted into a new class, `RuntimeConfigLoader`. This improves testability, reduces coupling, and adheres to the Single Responsibility Principle.

Here's the refactored `PlanetRuntimeConfig.java` file. Please note that the `RuntimeConfigLoader.java` file (and potentially `PlanetRuntimeException.java` if it doesn't already exist in your project) would also need to be created based on the description in the thought process for this refactoring to be complete.

**1. `PlanetRuntimeException.java` (create this file if it doesn't exist)**

This custom exception provides a more specific way to report errors related to Planet configuration, replacing generic `Exception` catches.

```java
package org.apache.roller.planet.business; // Or choose an appropriate package for custom exceptions

/**
 * Custom runtime exception for Planet configuration-related errors.
 * Provides more specific error handling than generic {@link RuntimeException}.
 */
public class PlanetRuntimeException extends RuntimeException {
    public PlanetRuntimeException(String message) {
        super(message);
    }
    public PlanetRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
```

**2. `RuntimeConfigLoader.java` (new file to be created)**

This class centralizes the responsibilities of loading and parsing the `planet-runtime-config.xml` resource.

```java
package org.apache.roller.planet.config;

import org.apache.roller.planet.business.PlanetRuntimeException;
import org.apache.roller.planet.runtime.RuntimeConfigDefs;
import org.apache.roller.planet.runtime.RuntimeConfigDefsParser; // Assuming this class exists for JAXB unmarshalling

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Responsible for loading and parsing the Planet runtime configuration definitions
 * from the 'planet-runtime-config.xml' resource.
 * This class isolates XML parsing and resource management from the core
 * configuration access logic.
 */
public class RuntimeConfigLoader {

    private static final String RUNTIME_CONFIG_RESOURCE = "/planet-runtime-config.xml";

    /**
     * Loads and unmarshals the runtime configuration definitions from the XML resource.
     *
     * @return The parsed RuntimeConfigDefs object.
     * @throws PlanetRuntimeException If the resource cannot be found, read, or parsed.
     */
    public RuntimeConfigDefs loadConfigDefs() throws PlanetRuntimeException {
        try (InputStream is = getClass().getResourceAsStream(RUNTIME_CONFIG_RESOURCE)) {
            if (is == null) {
                throw new PlanetRuntimeException("Runtime config resource not found: " + RUNTIME_CONFIG_RESOURCE);
            }
            // Assuming RuntimeConfigDefsParser has a static parse method or
            // is instantiated and then used, as per original logic.
            return RuntimeConfigDefsParser.parse(is);
        } catch (JAXBException e) {
            throw new PlanetRuntimeException("Failed to unmarshal runtime config definitions from " + RUNTIME_CONFIG_RESOURCE, e);
        } catch (IOException e) {
            throw new PlanetRuntimeException("Failed to read runtime config definitions from " + RUNTIME_CONFIG_RESOURCE, e);
        }
    }

    /**
     * Loads the raw XML content of the runtime configuration definitions as a string.
     *
     * @return The XML content as a String.
     * @throws PlanetRuntimeException If the resource cannot be found or read.
     */
    public String loadConfigDefsAsString() throws PlanetRuntimeException {
        try (InputStream is = getClass().getResourceAsStream(RUNTIME_CONFIG_RESOURCE)) {
            if (is == null) {
                throw new PlanetRuntimeException("Runtime config resource not found: " + RUNTIME_CONFIG_RESOURCE);
            }
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            // Replaced ambiguous empty string return with throwing a specific exception
            throw new PlanetRuntimeException("Failed to load runtime config definitions as string from " + RUNTIME_CONFIG_RESOURCE, e);
        }
    }
}
```

**3. `PlanetRuntimeConfig.java` (the refactored file)**

```java
package org.apache.roller.planet.config;

import org.apache.roller.weblogger.business.WebloggerPropertiesManager; // Dependency for properties
import org.apache.roller.weblogger.business.WebloggerException; // Specific exception from properties manager
import org.apache.roller.planet.runtime.RuntimeConfigDefs; // Dependency for runtime definitions
import org.apache.roller.planet.runtime.PropertyDefinition; // Assuming PropertyDefinition is used by RuntimeConfigDefs
import org.apache.roller.planet.business.PlanetRuntimeException; // Custom exception for Planet-related errors

/**
 * Provides access to Planet runtime configuration properties.
 *
 * This class has been refactored to be an instance-based configuration provider,
 * promoting testability and clearer dependency management. It now takes its
 * dependencies (the properties manager and parsed runtime configuration definitions)
 * through its constructor. This eliminates global state and reduces tight coupling.
 *
 * The responsibilities of XML parsing and resource loading have been moved
 * to {@link RuntimeConfigLoader}.
 */
public class PlanetRuntimeConfig {

    private final WebloggerPropertiesManager propertiesManager;
    private final RuntimeConfigDefs configDefs;

    /**
     * Constructs a PlanetRuntimeConfig instance with its required dependencies.
     * The `configDefs` should be pre-loaded by a {@link RuntimeConfigLoader}
     * or similar component.
     *
     * @param propertiesManager The manager for weblogger properties, used to retrieve
     *                          dynamic configuration values. Cannot be null.
     * @param configDefs        The parsed runtime configuration definitions, providing
     *                          default values and metadata. Cannot be null.
     * @throws IllegalArgumentException if any of the required dependencies are null.
     */
    public PlanetRuntimeConfig(WebloggerPropertiesManager propertiesManager, RuntimeConfigDefs configDefs) {
        if (propertiesManager == null) {
            throw new IllegalArgumentException("propertiesManager cannot be null");
        }
        if (configDefs == null) {
            throw new IllegalArgumentException("configDefs cannot be null");
        }
        this.propertiesManager = propertiesManager;
        this.configDefs = configDefs;
    }

    /**
     * Get a Planet runtime property.
     * This method first attempts to retrieve the property from the dynamic
     * {@link WebloggerPropertiesManager}. If not found, it falls back to the
     * default value specified in the static runtime configuration definitions.
     *
     * @param name The name of the property to retrieve.
     * @return The value of the property, or its default value if not found
     *         in dynamic properties. Returns {@code null} if the property
     *         is not defined and has no default value.
     * @throws PlanetRuntimeException If there's an issue retrieving the property
     *                                from the {@link WebloggerPropertiesManager}.
     */
    public String getProperty(String name) throws PlanetRuntimeException {
        try {
            // Attempt to get the property from the dynamic properties manager first
            String value = propertiesManager.getProperty(name);

            if (value == null) {
                // If not found dynamically, get the default value from the static config definitions.
                // It's assumed that configDefs.getProperty(name) returns a non-null
                // PropertyDefinition object, even if the property isn't found,
                // and its getDefaultValue() method will handle the 'no default' case (e.g., return null).
                PropertyDefinition propDef = configDefs.getProperty(name);
                if (propDef != null) { // Added explicit check for robustness, though original implies non-null
                    value = propDef.getDefaultValue();
                }
            }
            return value;
        } catch (WebloggerException e) {
            // Catch the specific exception from WebloggerPropertiesManager
            throw new PlanetRuntimeException("Error retrieving Planet property '" + name + "' from properties manager.", e);
        }
        // Other unexpected exceptions from configDefs.getProperty().getDefaultValue()
        // should ideally be handled within RuntimeConfigDefs/PropertyDefinition itself,
        // or indicate a design flaw in those classes if they throw unchecked exceptions.
    }

    /**
     * Get a Planet runtime property as an integer.
     *
     * @param name The name of the property to retrieve.
     * @return The integer value of the property.
     * @throws PlanetRuntimeException If the property cannot be found, if its value is null,
     *                                or if it cannot be parsed into an integer.
     */
    public int getIntProperty(String name) throws PlanetRuntimeException {
        String value = getProperty(name);
        if (value == null) {
            throw new PlanetRuntimeException("Planet property '" + name + "' not found or has no value.");
        }
        try {
            // Use specific NumberFormatException for parsing errors
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new PlanetRuntimeException("Planet property '" + name + "' with value '" + value + "' is not a valid integer.", e);
        }
    }

    // --- The following methods and fields have been removed or refactored: ---
    // - The static 'configDefs' field is now an instance field.
    // - The static 'RUNTIME_CONFIG_RESOURCE' constant has moved to RuntimeConfigLoader.
    // - The 'getRuntimeConfigDefs()' method has been moved to RuntimeConfigLoader.loadConfigDefs().
    // - The 'getRuntimeConfigDefsAsString()' method has been moved to RuntimeConfigLoader.loadConfigDefsAsString().
    // - All methods are now instance methods.
    // - Direct reliance on WebloggerFactory.getWeblogger() has been replaced by dependency injection.
    // - Catch-all Exception blocks have been replaced with more specific exception handling.
    // - Inconsistent resource loading has been removed, now centralized in RuntimeConfigLoader.
    // - Ambiguous error return values are replaced by throwing PlanetRuntimeException.
}
```