// src/test/java/testRunner/Runner.java
package testRunner;

import org.junit.platform.suite.api.*;


import static io.cucumber.junit.platform.engine.Constants.*;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features/transaction.feature") 
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "stepDefinition")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME,
        value = "pretty,summary,html:target/cucumber-report.html")
public class Runner {}
