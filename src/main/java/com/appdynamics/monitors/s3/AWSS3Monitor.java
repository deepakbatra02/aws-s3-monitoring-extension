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
package com.appdynamics.monitors.s3;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.appdynamics.monitors.s3.config.Configuration;
import com.appdynamics.monitors.s3.config.ConfigurationUtil;
import com.appdynamics.monitors.s3.contstants.AWSS3Constants;
import com.singularity.ee.agent.systemagent.api.AManagedMonitor;
import com.singularity.ee.agent.systemagent.api.MetricWriter;
import com.singularity.ee.agent.systemagent.api.TaskExecutionContext;
import com.singularity.ee.agent.systemagent.api.TaskOutput;
import com.singularity.ee.agent.systemagent.api.exception.TaskExecutionException;

/**
 * AWSS3 monitor class that get required details from Amazon AWS and send the
 * metrics to AppDynamics controller
 * 
 * @author deepak.batra
 *
 */
public class AWSS3Monitor extends AManagedMonitor {

	private static final Logger logger = Logger.getLogger(AWSS3Monitor.class);

	// Configuration object
	private Configuration configuration;

	/**
	 * Default Constructor
	 */
	public AWSS3Monitor() {
		String version = getClass().getPackage().getImplementationTitle();
		String msg = String.format("Using Monitor Version [%s]", version);
		logger.info(msg);
	}

	/**
	 * Main execution method that gets data from Amazon S3 and send metrics to
	 * AppDynamics Controller
	 */
	@Override
	public TaskOutput execute(final Map<String, String> argsMap,
			TaskExecutionContext executionContext)
			throws TaskExecutionException {

		logger.debug("The args map is:  " + argsMap);

		// Reading configurations
		configuration = ConfigurationUtil.getConfigurations(argsMap
				.get(AWSS3Constants.KEY_CONFIG));

		if (configuration == null
				|| configuration.getAwsCredentials() == null
				|| configuration.getAwsCredentials().getAWSAccessKeyId() == null
				|| configuration.getAwsCredentials().getAWSSecretKey() == null) {
			logger.error("Sending S3 metric failed. Unable to find mandatory configuration.");
			throw new TaskExecutionException(
					"Sending S3 metric failed. Unable to find mandatory configuration.");
		}

		logger.debug("Using access key: "
				+ configuration.getAwsCredentials().getAWSAccessKeyId()
				+ ", Secret key:"
				+ configuration.getAwsCredentials().getAWSSecretKey());

		// Creating AmazonS3Client
		AmazonS3Client amazonS3Client = new AmazonS3Client(
				configuration.getAwsCredentials());

		// Getting list of buckets from configuration, for which result needs to
		// be collected
		List<Bucket> buckets = getBuckets();

		// Calling getS3Result which calls Amazon WS to get the Result
		Map<String, String> resultMap = getS3Result(buckets, amazonS3Client);

		// Sending all metrics to controller
		printAllMetrics(argsMap.get(AWSS3Constants.KEY_METRICPREFIX), resultMap);

		logger.info("Sending S3 metric complete!");
		return new TaskOutput("Sending S3 metric complete!");
	}

	/**
	 * Purpose: This method reads bucket names for configuration. It returns
	 * null if no bucket is configured or "All" is configured in bucket list
	 * 
	 * @return List<Bucket> or Null
	 */
	private List<Bucket> getBuckets() {
		List<Bucket> buckets = new ArrayList<Bucket>();

		if (configuration.getBucketNames() != null
				&& configuration.getBucketNames().size() != 0) {
			// Adding bucket names mentioned in configuration
			for (String bucketName : configuration.getBucketNames()) {
				if (bucketName.equalsIgnoreCase(AWSS3Constants.BUCKETS_ALL)) {
					// Setting buckets to null as data needs to be fetched for
					// all buckets
					buckets = null;
					break;
				} else {
					buckets.add(new Bucket(bucketName));
				}
			}
		} else {
			// Setting buckets to null as data needs to be fetched for
			// all buckets
			buckets = null;
		}
		return buckets;
	}

	/**
	 * This method calls Amazon WS to get required S3 statistics, set values
	 * based on configured unit, and returns the result back
	 * 
	 * @param buckets
	 * @param amazonS3Client
	 * @return Map<String, String>
	 * @throws TaskExecutionException
	 */
	private Map<String, String> getS3Result(List<Bucket> buckets,
			AmazonS3Client amazonS3Client) throws TaskExecutionException {
		// Declaring result variables with default values
		long size = 0;
		long count = 0;
		Date lastModified = new Date(0);

		try {
			// Fetching all bucket names if passed buckets is null
			if (buckets == null) {
				logger.debug("Calling Webservice to list all buckets");
				buckets = amazonS3Client.listBuckets();
			}

			// Looping over all buckets
			for (Bucket bucket : buckets) {

				logger.debug("Getting data for bucket: " + bucket.getName());

				ObjectListing objectListing = null;

				do {
					// Getting objectListing while calling it for the first time
					if (objectListing == null) {
						logger.debug("Calling Webservice to get objectlisting for first time");
						objectListing = amazonS3Client.listObjects(bucket
								.getName());
					} else {
						// Calling listNextBatchOfObjects if previous response
						// is truncated
						logger.debug("Calling Webservice to get objectlisting subsequent time");
						objectListing = amazonS3Client
								.listNextBatchOfObjects(objectListing);
					}

					// Incrementing the count
					count += objectListing.getObjectSummaries().size();

					// Looping over all objects
					for (S3ObjectSummary s3ObjectSummary : objectListing
							.getObjectSummaries()) {
						// Incrementing size
						size += s3ObjectSummary.getSize();

						// Setting last modified if lastModifiedDate is latest
						if (lastModified.before(s3ObjectSummary
								.getLastModified())) {
							lastModified = s3ObjectSummary.getLastModified();
						}
					}
				}

				// Continuing till objectListing is complete
				while (objectListing.isTruncated());
			}

		} catch (AmazonS3Exception exception) {
			logger.error("AmazonS3Exception occurred", exception);
			throw new TaskExecutionException(
					"Sending S3 metric failed due to AmazonS3Exception");
		}

		return getResultWithRequiredUnit(size, count, lastModified);
	}

	/**
	 * Purpose: This method calculates the result values based on required unit
	 * defined in configuration
	 * 
	 * @param size
	 * @param count
	 * @param lastModified
	 * @return Map<String, String>
	 */
	private Map<String, String> getResultWithRequiredUnit(long size,
			long count, Date lastModified) {
		logger.debug("Creating result map that will be sent to controller.");

		Map<String, String> resultMap = new HashMap<String, String>();

		// Setting object count in result
		resultMap.put(AWSS3Constants.RESULT_KEY_COUNT, String.valueOf(count));

		// Setting size in metrics based on the sizeUnit configured in
		// configurations
		if (configuration.getSizeUnit()
				.equalsIgnoreCase(AWSS3Constants.SIZE_MB)) {
			size = size / (1024 * 1024);
		} else if (!configuration.getSizeUnit().equalsIgnoreCase(
				AWSS3Constants.SIZE_B)) {
			size = size / 1024;
		}

		resultMap.put(AWSS3Constants.RESULT_KEY_SIZE, String.valueOf(size));

		// Setting sinceLastModified in metrics based on the timeUnit configured
		// in configurations

		// Calculating SinceLastModified in seconds (Default)
		long sinceLastModified = (new Date().getTime() - lastModified.getTime()) / 1000;

		// Calculating SinceLastModified based on timeunit defined in
		// configuration
		if (configuration.getTimeUnit().equalsIgnoreCase(
				AWSS3Constants.TIMEUNIT_MIN)) {
			sinceLastModified = sinceLastModified / 60;
		} else if (configuration.getTimeUnit().equalsIgnoreCase(
				AWSS3Constants.TIMEUNIT_HOUR)) {
			sinceLastModified = sinceLastModified / (60 * 60);
		} else if (configuration.getTimeUnit().equalsIgnoreCase(
				AWSS3Constants.TIMEUNIT_DAYS)) {
			sinceLastModified = sinceLastModified / (60 * 60 * 24);
		}

		resultMap.put(AWSS3Constants.RESULT_KEY_SINCELASTMODIFIED,
				String.valueOf(sinceLastModified));
		return resultMap;
	}

	/**
	 * This method publishes all values to controller
	 * 
	 * @param metricPrefix
	 * @param resultMap
	 */
	private void printAllMetrics(String metricPrefix,
			Map<String, String> resultMap) {
		for (Map.Entry<String, String> metricEntry : resultMap.entrySet()) {
			printMetric(metricPrefix, metricEntry.getKey(),
					metricEntry.getValue());
		}
	}

	/**
	 * Returns the metric to the AppDynamics Controller.
	 *
	 * @param metricPrefix
	 *            Metric prefix
	 * @param metricName
	 *            Name of the Metric
	 * @param metricValue
	 *            Value of the Metric
	 */
	private void printMetric(String metricPrefix, String metricName,
			Object metricValue) {
		MetricWriter metricWriter = getMetricWriter(metricPrefix
				+ AWSS3Constants.METRIC_SEPARATOR + metricName,
				MetricWriter.METRIC_AGGREGATION_TYPE_OBSERVATION,
				MetricWriter.METRIC_TIME_ROLLUP_TYPE_AVERAGE,
				MetricWriter.METRIC_CLUSTER_ROLLUP_TYPE_COLLECTIVE);
		metricWriter.printMetric(String.valueOf(metricValue));
	}
}
