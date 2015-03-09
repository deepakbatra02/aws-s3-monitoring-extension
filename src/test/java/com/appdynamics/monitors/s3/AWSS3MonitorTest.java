package com.appdynamics.monitors.s3;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.appdynamics.monitors.s3.contstants.AWSS3Constants;
import com.singularity.ee.agent.systemagent.api.exception.TaskExecutionException;

public class AWSS3MonitorTest {

	private Map<String, String> taskArguments = new HashMap<String, String>();

	@Before
	public void setup() {
		taskArguments.put("configuration",
				"src/test/resources/conf/S3Configurations.yml");
		taskArguments.put("metric-prefix", "Custom Metrics"
				+ AWSS3Constants.METRIC_SEPARATOR + "Amazon S3");
	}

	@Test
	public void testDefaultConfigPositive() throws TaskExecutionException {
		AWSS3Monitor monitor = new AWSS3Monitor();
		String result = monitor.execute(taskArguments, null).getStatusMessage();
		Assert.assertTrue(result
				.equalsIgnoreCase("Sending S3 metric complete!"));
	}

	@Test(expected = TaskExecutionException.class)
	public void testWrongFilePath() throws TaskExecutionException {
		taskArguments.put("configuration",
				"src/test/resources/conf/WrongS3Configurations.yml");

		AWSS3Monitor monitor = new AWSS3Monitor();
		monitor.execute(taskArguments, null).getStatusMessage();
	}

	@Test
	public void testSpecificConfigPositive() throws TaskExecutionException {
		taskArguments.put("configuration",
				"src/test/resources/conf/SpecificS3Configurations.yml");
		AWSS3Monitor monitor = new AWSS3Monitor();
		monitor.execute(taskArguments, null).getStatusMessage();
	}

	@Test(expected = TaskExecutionException.class)
	public void testWrongCredentialConfig() throws TaskExecutionException {
		taskArguments.put("configuration",
				"src/test/resources/conf/WrongCredentialsS3Configurations.yml");

		AWSS3Monitor monitor = new AWSS3Monitor();
		monitor.execute(taskArguments, null).getStatusMessage();
	}
}
