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
package com.appdynamics.monitors.s3.contstants;

/**
 * Constant keys for AWS S3 configuration file
 * 
 * @author deepak.batra
 */
public class AWSS3Constants {
	public static final String METRIC_SEPARATOR = "|";

	public static final String KEY_CONFIG = "configuration";
	public static final String KEY_METRICPREFIX = "metric-prefix";
	public static final String KEY_AWSCREDENTIALS = "awscredentials";
	public static final String KEY_BUCKETS = "buckets";
	public static final String KEY_ACCESSKEY = "accesskey";
	public static final String KEY_SECRETKEY = "secretkey";
	public static final String KEY_SIZEUNIT = "sizeunit";
	public static final String KEY_TIMEUNIT = "timeunit";

	public static final String SIZE_MB = "MB";
	public static final String SIZE_KB = "KB";
	public static final String SIZE_B = "B";

	public static final String BUCKETS_ALL = "All";

	public static final String TIMEUNIT_SECONDS = "Seconds";
	public static final String TIMEUNIT_MIN = "Minutes";
	public static final String TIMEUNIT_HOUR = "Hours";
	public static final String TIMEUNIT_DAYS = "Days";

	public static final String RESULT_KEY_SIZE = "Size";
	public static final String RESULT_KEY_SINCELASTMODIFIED = "SinceLastModified";
	public static final String RESULT_KEY_COUNT = "Count";
}
