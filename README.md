# Azure Spring Cloud webinar 


* Install the [Azure Spring Cloud CLI](https://docs.microsoft.com/en-us/cli/azure/install-azure-cli?WT.mc_id=azurespringcloud-github-judubois&view=azure-cli-latest)
* Install the Azure Spring Cloud extension: `az extension add -y --source https://azureclitemp.blob.core.windows.net/spring-cloud/spring_cloud-0.1.0-py2.py3-none-any.whl`
* Login: `az login`
* set the default resource group: `az configure --defaults group=asc-resource-group`
* set the default service: `az configure --defaults spring-cloud=asc-webinar-06-12-2019`
* Here's the configuration that you need in order to configure the Azure Spring Cloud dependency: 
* build a simple spring boot app using reactive mongodb (no changes to the build required)
* create an application in the Azure portal and name it simple-microservice.
* create a CosmosDB MongoDB instance with a collection called `customers` 
* go back to the application and create an Azure Spring Cloud service-binding linking the CosmosDB instance with the application.
* Now you can deploy the application itself using the following incantation. Make sure that the endpoint that returns the entities also logs what's happening. Log _something_! We'll look at the logs later on. 

    ```
    #!/bin/bash
    ./mvnw clean package
    az spring-cloud app deploy -n simple-microservice --jar-path target/simple-microservice-0.0.1-SNAPSHOT.jar
    ```    
* you can look at the logs from the application by going into Azure Log Analytics. Go to http://portal.azure.com and search for "Log Analytics workspaces"
* create a new workspace or link an existing one. I created `asc-webinar-...` as a Log Analytics workspace.
* Now that the Log analytics workspace has been created, we must configure our Azure Spring Cloud cluster instance to send its data to this workspace.
*  Go to the "Overview" page of your Azure Spring Cloud cluster, and select "Diagnostic settings" in the "Monitoring" section of the navigation pane.
*  Click on "Add diagnostic setting" and configure your cluster to send all its logs to the Log analytics workspace that we just created.
* Fill in the values as shown here and click "Save".
* Now you can goto the `Logs` section and run the following query: 

```AppPlatformLogsforSpring
| where AppName == "simple-microservice"
```

* You'll need to add the following to your Maven builds to get this to work
```
  <profiles>
        <profile>
            <id>cloud</id>
            <repositories>
                <repository>
                    <id>nexus-snapshots</id>
                    <url>https://oss.sonatype.org/content/repositories/snapshots/</url>
                    <snapshots>
                        <enabled>true</enabled>
                    </snapshots>
                </repository>
            </repositories>
            <dependencies>
                <dependency>
                    <groupId>com.microsoft.azure</groupId>
                    <artifactId>spring-cloud-starter-azure-spring-cloud-client</artifactId>
                    <version>2.1.0-SNAPSHOT</version>
                </dependency>
            </dependencies>
        </profile>
    </profiles>
    ```
