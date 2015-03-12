Amazon AWS S3 - Monitoring Extension
====================================


Use case:
---------

The Amazon AWS S3 custom monitor captures statistics for Amazon S3 and displays them in the AppDynamics Metric Browser.
 
This extension works only with the standalone machine agent.

 
Installation:
-------------

Download and Unzip the file S3Monitor.zip into <machineagent install dir>/monitors/
Open S3Configurations.yml and monitor.xml and configure the arguments
Restart the machineagent
In the AppDynamics Metric Browser, look for: Application Infrastructure Performance | Tier-Name | Custom Metrics | Amazon S3.
 
 
Configurations ( * -> Required ):
---------------------------------

1. *accesskey:	Access-Key for S3 account
2. *secretkey: Secret-Key for S3 account
3. sizeunit: B, KB (Default), MB
4. timeunit: Seconds (Default), Minutes, Hours, Days
5. buckets: Bucket names that you want to monitor. If you want to monitor all available buckets remove this field or Add a bucket named "All".
 
 
Amazon S3 Metrics:
------------------

1. Size:	Size of all the objects present in bucket(s) configured in S3Configurations.yml. Unit of this metric can also be configured in S3Configurations.yml file.
2. Objects Count:	Number of objects present in bucket(s) configured in S3Configurations.yml.
3. Since Last Modified:	Time since latest modification of any object in bucket(s) configured in S3Configurations.yml. Unit of this metric can also be configured in S3Configurations.yml file.

 