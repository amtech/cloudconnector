github url: https://github.com/i042416/cloudconnector

local path: C:\Users\i042416\scp20180425\connectivity

See Jerry's blog:

[Use SAP Cloud Connector + SAP Cloud Platform + Java to consume function in ABAP On-Premise system](https://blogs.sap.com/2018/05/16/use-sap-cloud-connector-sap-cloud-platform-java-to-consume-function-in-abap-on-premise-system/)

test via url: https://demoi042416trial.hanatrial.ondemand.com/connectivity/api?userinput=a

No CloudFoundry environment is touched!

#########################################################
# Welcome to the SAP Cloud Platform Connectivity Sample #
#########################################################

The connectivity sample covers two basic connectivity scenarios:
 - Outbound Internet destinations usage
 - On-demand to on-premise connectivity scenario.

Prerequisites for the execution of the outbound Internet destination scenario in a local Server from the Eclipse IDE

1. Configure the destination 'outbound-internet-destination'
In order to run the sample from your Eclipse IDE, you need to import a destination into the local server.
You can do that by double-clicking on your local server in the 'Servers' view and then switching to the Connectivity tab.
Import the destination outbound-internet-destination from this sample '/destinations' folder.

This operation is done automatically by the Maven build. It uses the accordingly located destination and copies it (check out pom.xml)
to the local server, which it creates on-the-fly during the build.
In this way, the integration tests will successfully detect the destination.

NOTE: If you need to create yourself another local server in the Eclipse IDE, you have to do it separately 
in the 'Servers' view.

For more information, see https://help.sap.com/viewer/65de2977205c403bbc107264b8eccf4b/Cloud/en-US/22123f544cb64372959b4a1bd8e234c4.html


2. (Optional) Configure an HTTP proxy for the outbound Internet connection
The sample shows how to consume the Internet resource https://help.hana.ondemand.com/terms_of_use.html. In case you run the sample
from behind an outgoing HTTP proxy, make sure you have configured the proxy as part of the SAP Cloud Platform local runtime
in Eclipse. To do this, double-click the local server in the 'Servers' view, then click the 'Open launch
configuration' link on the Overview tab, this opens the 'Edit launch configuration properties' view. Navigate there to
the 'Arguments' tab and add your proxy definition as 'VM arguments' in the following format:
-Dhttps.proxyHost=<your_proxy_host> -Dhttps.proxyPort=<your_proxy_port>


Prerequisites for the execution of the on-demand to on-premise connectivity scenario in the cloud

1. Configure on-premise destinations.
On-premise destinations are used in a more complex scenario when an cloud application wants to access resources from an on-premise backend system.
The following destination 'backend-no-auth-destination' should be uploaded to the cloud server application.
It is located in this sample's '/destinations' folder.

For more information, see https://help.sap.com/viewer/65de2977205c403bbc107264b8eccf4b/Cloud/en-US/474eae1b69c9434b9dce0314b8d2b6b1.html

2. Run backend service application

The application WAR file located in the '/onpremise' folder of the sample has to be deployed as backend application.
You can use an arbitrary local Java Web Container for this, like the local server created in Eclipse.

For more information, see https://help.sap.com/viewer/65de2977205c403bbc107264b8eccf4b/Cloud/en-US/e59dc589bb57101480939e290c55e680.html

In order to use on-premise backend services in a cloud application, the SAP Cloud Connector should be set up and configured.

For more information, see https://help.sap.com/viewer/65de2977205c403bbc107264b8eccf4b/Cloud/en-US/e6c7616abb5710148cfcf3e75d96d596.html

7:01PM - ���ˣ�ΪʲôSDK���ص�maven projectһ���Ӿ�build���ˣ�

# 2018-06-17 

1:04PM default: <Connector connectionTimeout="20000" port="9098"
no https allowed.

# 2018-06-19

11:40AM I have performed maven build and try to deploy .war to SAP internal SCP neo environment.

11:51AM Regions and Hosts Available for the Neo Environment

https://help.sap.com/viewer/65de2977205c403bbc107264b8eccf4b/Cloud/en-US/350356d1dc314d3199dca15bd2ab9b0e.html#loio350356d1dc314d3199dca15bd2ab9b0e

2:11PM - int.sap.hana.ondemand.com

2018-06-19 14:13:39,063 INFO  [main] com.sap.jpaas.infrastructure.console.ConsoleLogger: Arguments:   --account wc4e460ce --user jerry.wang@sap.com --host int.sap.hana.ondemand.com --amount lite:1
2018-06-19 14:13:40,191 INFO  [main] com.sap.jpaas.infrastructure.console.ConsoleLogger: Starting execution of command [set-quota]
2018-06-19 14:13:40,415 INFO  [main] com.sap.jpaas.infrastructure.console.ConsoleLogger: Version validity check is completed
2018-06-19 14:13:40,415 INFO  [main] com.sap.jpaas.infrastructure.console.ConsoleLogger: Command [set-quota] init() finished for [224] ms
2018-06-19 14:13:48,859 INFO  [main] com.sap.jpaas.infrastructure.console.ConsoleLogger: Command [set-quota] cleanup() finished for [0] ms
2018-06-19 14:13:48,859 FATAL [main] com.sap.jpaas.infrastructure.console.ConsoleLogger: (!) ERROR: Missing message for status code:500
com.sap.jpaas.infrastructure.console.exception.BackendException: Missing message for status code:500
	at com.sap.cloud.commercial.account.ops.commands.AccountOperationsWithHeaderProcessingCommand.handleErrorResponse(AccountOperationsWithHeaderProcessingCommand.java:36)
	at com.sap.cloud.commercial.account.ops.commands.SetQuotaCommand.setQuota(SetQuotaCommand.java:93)
	at com.sap.cloud.commercial.account.ops.commands.SetQuotaCommand.executeCommand(SetQuotaCommand.java:59)
	at com.sap.cloud.commercial.account.ops.commands.AccountOperationsAbstractCommand.run(AccountOperationsAbstractCommand.java:153)
	at com.sap.jpaas.infrastructure.console.CommandManager.run(CommandManager.java:185)
	at com.sap.jpaas.infrastructure.console.CommandManager.run(CommandManager.java:146)
	at com.sap.jpaas.infrastructure.console.ConsoleClient.executeCommand(ConsoleClient.java:242)
	at com.sap.jpaas.infrastructure.console.ConsoleClient.run(ConsoleClient.java:184)
	at com.sap.jpaas.infrastructure.console.ConsoleClient.main(ConsoleClient.java:80)

# 2018-07-27

Destination is defined based on org unit level.

my-backend-system-destination: for XCD/111