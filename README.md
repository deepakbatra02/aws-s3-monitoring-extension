AppDynamics S3 Monitoring Extension
===============================================

This extension works only with the standalone machine agent.

Use Case
-------- 

The AWS S3 custom monitor captures statistics from Amazon S3 and displays them in the AppDynamics Metric Browser.
Metrics include:
* Count
* SinceLastModified
* Size

Installation
------------

 1. Clone aws-s3-monitoring-extension from GitHub https://github.com/Appdynamics
 2. Run 'mvn clean install' from the cloned aws-s3-monitoring-extension directory.
 3. Download the S3Monitor.zip found in the 'target' directory into <machineagent install dir>/monitors/
 4. Unzip the downloaded zip file.
 5. In the newly created "S3Monitor" directory, edit the "S3Configurations.yml" file and configure required parameters
 6. Restart the machine agent.
 7. In the AppDynamics Metric Browser, look for: Application Infrastructure Performance | Amazon S3
 
 
 
 Configuration
-------------

In the S3Configurations.yml, there are a few things that can be configured:

*accesskey: Access key of Amazon S3 account
*secretkey: Secret key of Amazon S3 account
sizeunit (Default- KB): Unit to use for S3 objects size
timeunit (Default- Seconds): Unit to use for SinceLastModified
buckets (Default- All): Buckets to consider for getting all statistics
