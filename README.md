AppDynamics AWS CloudWatch Monitoring Extension
===============================================

Use Case
-------- 

The AWS CloudWatch custom monitor captures statistics from Amazon CloudWatch and displays them in the AppDynamics Metric Browser.

Metrics are categorized under the AWS Product Namespaces:

 <table>
  <tr>
    <th align="left">AWS Product</th>
    <th align="left">Namespace</th>
  </tr>
  <tr>
    <td> Auto Scaling </td>
    <td> AWS/AutoScaling</td>
  </tr>
  <tr>
    <td> Billing </td>
    <td> AWS/Billng</td>
  </tr>
  <tr>
    <td> Amazon DynamoDB </td>
    <td> AWS/DynamoDB</td>
  </tr>
  <tr>
    <td> Amazon ElastiCache </td>
    <td> AWS/ElastiCache</td>
  </tr>
  <tr>
    <td> Amazon Elastic Block Store </td>
    <td> AWS/EBS</td>
  </tr>
  <tr>
    <td> Amazon Elastic Compute Cloud </td>
    <td> AWS/AutoScaling</td>
  </tr>
  <tr>
    <td> Elastic Load Balancing </td>
    <td> AWS/ELB</td>
  </tr>
  <tr>
    <td> Amazon Elastic MapReduce </td>
    <td> AWS/ElasticMapReduce</td>
  </tr>
  <tr>
    <td> AWS OpsWorks </td>
    <td> AWS/OpsWorks</td>
  </tr>
  <tr>
    <td> Amazon Redshift </td>
    <td> AWS/Redshift</td>
  </tr>
  <tr>
    <td> Amazon Relational Database Service </td>
    <td> AWS/RDS</td>
  </tr>
  <tr>
    <td> Amazon Route 53 </td>
    <td> AWS/Route53</td>
  </tr>
  <tr>
    <td> Amazon Simple Notification Service </td>
    <td> AWS/SNS</td>
  </tr>
  <tr>
    <td> Amazon Simple Queue Service </td>
    <td> AWS/SQS</td>
  </tr>
  <tr>
    <td> AWS Storage Gateway </td>
    <td> AWS/StorageGateway</td>
  </tr>
</table>

Specific metrics under each of these namespaces can be found at this link http://docs.aws.amazon.com/AmazonCloudWatch/latest/DeveloperGuide/CW_Support_For_AWS.html

Installation
------------

 1. Run 'ant-package' from the aws-cloudwatch-monitoring-extension directory
 2. Download the AmazonMonitor.zip found in the 'dist' directory into <machineagent install dir>/monitors/
 3. Unzip the downloaded zip file
 4. In the newly created "AmazonMonitor" directory, edit the "AWSConfigurations.xml" file configuring the parameters specified below 
 5. Restart the machine agent
 6. In the AppDynamics Metric Browser, look for: Application Infrastructure Performance | Amazon Cloud Watch


Rebuilding the Project
----------------------

 1. At the command line, go to the root directory of this extension
 2. Run 'ant'. This will update the dist directory

Directory Structure
-------------------

<table>
  <tr>
    <th align="left">File/Folder</th>
    <th align="left">Description</th>
  </tr>
  <tr>
    <td> conf </td>
    <td> Contains monitor.xml and AWSConfigurations.xml</td>
  </tr>
  <tr>
    <td> lib </td>
    <td> Contains third-party project references</td>
  </tr>
  <tr>
    <td> src</td>
    <td> Contains the source code for aws-cloudwatch-extension</td>
  </tr>
  <tr>
    <td> dist </td>
    <td> The directory created when 'ant' is run. Run 'ant build' to generate the binaries. Run 'ant package' to generate distributable .zip file.</td>
  </tr>
  <tr>
    <td> build.xml </td>
    <td> Ant build script to package the project (required only if changing the Java code)</td>
  </tr>
</table>  

Main Java File: 
