/** 
 * Copyright 2015 AppDynamics 
 * 
 * Licensed under the Apache License, Version 2.0 (the License);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an AS IS BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.appdynamics.monitors.s3.config;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.yaml.snakeyaml.Yaml;

import com.amazonaws.auth.BasicAWSCredentials;
import com.appdynamics.extensions.PathResolver;
import com.appdynamics.monitors.s3.contstants.AWSS3Constants;
import com.google.common.base.Strings;
import com.singularity.ee.agent.systemagent.api.AManagedMonitor;

/**
 * ConfigurationUtil that reads the configuration
 * 
 * @author deepak.batra
 */
public class ConfigurationUtil {

	private static Logger logger = Logger
			.getLogger("com.singularity.extensions.ConfigurationUtil");

	/**
	 * Reads the config file in the S3 monitor home directory and retrieves AWS
	 * credentials and other configuration
	 * 
	 * @param filePath
	 *            Path to the configuration file
	 * @return Configuration Configuration object containing AWS credentials and
	 *         other details
	 */
	public static Configuration getConfigurations(String filePath) {

		// Creating config object
		Configuration awsConfiguration = new Configuration();
		BufferedInputStream configFile = null;

		try {
			logger.info("Reading config file::" + filePath);

			// Getting file name and reading yaml config file
			String fileName = getConfigFilename(filePath);
			configFile = new BufferedInputStream(new FileInputStream(fileName));
			Yaml yaml = new Yaml();

			// Reading mandatory configurations (access and secret key)
			@SuppressWarnings("unchecked")
			Map<String, Object> config = (Map<String, Object>) ((Map<String, Object>) yaml
					.load(configFile)).get(AWSS3Constants.KEY_CONFIG);

			@SuppressWarnings("unchecked")
			Map<String, Object> awsCredentials = (Map<String, Object>) config
					.get(AWSS3Constants.KEY_AWSCREDENTIALS);

			awsConfiguration
					.setAwsCredentials(new BasicAWSCredentials(awsCredentials
							.get(AWSS3Constants.KEY_ACCESSKEY).toString(),
							awsCredentials.get(AWSS3Constants.KEY_SECRETKEY)
									.toString()));

			// Reading optional configurations (buckets, sizeunit, timeunit)

			if (config.containsKey(AWSS3Constants.KEY_BUCKETS)
					&& config.get(AWSS3Constants.KEY_BUCKETS) != null) {
				@SuppressWarnings("unchecked")
				Set<String> bucketNames = (Set<String>) config
						.get(AWSS3Constants.KEY_BUCKETS);
				awsConfiguration.setBucketNames(bucketNames);
			}

			if (config.containsKey(AWSS3Constants.KEY_SIZEUNIT)
					&& config.get(AWSS3Constants.KEY_SIZEUNIT) != null) {
				String sizeUnit = (String) config
						.get(AWSS3Constants.KEY_SIZEUNIT);
				awsConfiguration.setSizeUnit(sizeUnit);
			} else {
				// Setting default value (KB) for sizeunit
				awsConfiguration.setSizeUnit(AWSS3Constants.SIZE_KB);
			}

			if (config.containsKey(AWSS3Constants.KEY_TIMEUNIT)
					&& config.get(AWSS3Constants.KEY_TIMEUNIT) != null) {
				String timeUnit = (String) config
						.get(AWSS3Constants.KEY_TIMEUNIT);
				awsConfiguration.setTimeUnit(timeUnit);
			} else {
				// Setting default value (Seconds) for sizeunit
				awsConfiguration.setTimeUnit(AWSS3Constants.TIMEUNIT_SECONDS);
			}

			return awsConfiguration;
		} catch (Exception exception) {
			logger.error(
					"Error reading config file: " + exception.getMessage(),
					exception);
		}

		finally {
			try {
				if (configFile != null)
					configFile.close();
			} catch (IOException ioException) {
				logger.error("Error occurred while closing configuration file!!");
			}
		}

		return awsConfiguration;
	}

	/**
	 * 
	 * @param filename
	 *            String
	 * @return String
	 */
	private static String getConfigFilename(String filename) {
		if (filename == null) {
			return "";
		}
		// for absolute paths
		if (new File(filename).exists()) {
			return filename;
		}
		// for relative paths
		File jarPath = PathResolver.resolveDirectory(AManagedMonitor.class);
		String configFileName = "";
		if (!Strings.isNullOrEmpty(filename)) {
			configFileName = jarPath + File.separator + filename;
		}
		return configFileName;
	}
}