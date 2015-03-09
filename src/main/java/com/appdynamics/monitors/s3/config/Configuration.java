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

import java.util.Set;

import com.amazonaws.auth.AWSCredentials;

/**
 * Configuration class- contains all the configurations
 * 
 * @author deepak.batra
 *
 */
public class Configuration {

	private AWSCredentials awsCredentials;

	private Set<String> bucketNames;

	private String sizeUnit;

	private String timeUnit;

	public Set<String> getBucketNames() {
		return bucketNames;
	}

	public void setBucketNames(Set<String> bucketNames) {
		this.bucketNames = bucketNames;
	}

	public AWSCredentials getAwsCredentials() {
		return awsCredentials;
	}

	public void setAwsCredentials(AWSCredentials awsCredentials) {
		this.awsCredentials = awsCredentials;
	}

	public String getSizeUnit() {
		return sizeUnit;
	}

	public void setSizeUnit(String sizeUnit) {
		this.sizeUnit = sizeUnit;
	}

	public String getTimeUnit() {
		return timeUnit;
	}

	public void setTimeUnit(String timeUnit) {
		this.timeUnit = timeUnit;
	}
}
