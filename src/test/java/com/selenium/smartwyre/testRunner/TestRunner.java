package com.selenium.smartwyre.testRunner;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


import io.cucumber.testng.*;


@CucumberOptions(
        features = "src/test/java/com/selenium/smartwyre/features",
        glue = {"com.selenium.smartwyre.stepDefinitions", "com.selenium.smartwyre.hooks"},
        plugin = {
                "pretty",
                "html:target/cucumber-reports/cucumber-pretty",
                "json:target/cucumber-reports/CucumberTestReport.json",
                "rerun:target/cucumber-reports/rerun.txt",
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:",
				"timeline:test-output-thread/"
        },
        monochrome = true,
        dryRun = false
        )
public class TestRunner {
	
	   private TestNGCucumberRunner testNGCucumberRunner;
	   

	    @BeforeClass(alwaysRun = true)
	    public void setUpClass() throws Exception {
	        testNGCucumberRunner = new TestNGCucumberRunner(this.getClass());
	       
	        
	    }

	    @Test(groups = "cucumber", description = "Runs Cucumber Scenarios", dataProvider = "scenarios")
	    public void runScenario(PickleWrapper pickleWrapper, FeatureWrapper featureWrapper) throws Throwable {
	        // the 'featureWrapper' parameter solely exists to display the feature file in a test report
	        testNGCucumberRunner.runScenario(pickleWrapper.getPickle());
	    }

	    /**
	     * Returns two dimensional array of PickleEventWrapper scenarios
	     * with their associated CucumberFeatureWrapper feature.
	     *
	     * @return a two dimensional array of scenarios features.
	     */
	    @DataProvider
	    public Object[][] scenarios() {
	        if (testNGCucumberRunner == null) {
	            return new Object[0][0];
	        }
	        return testNGCucumberRunner.provideScenarios();
	    }

	    @AfterClass(alwaysRun = true)
	    public void tearDownClass() {
	        if (testNGCucumberRunner == null) {
	            return;
	        }
	        testNGCucumberRunner.finish();
	    }

}
