# Azure Spring Cloud webinar 

* Install the [Azure Spring Cloud CLI](https://docs.microsoft.com/en-us/cli/azure/install-azure-cli?WT.mc_id=azurespringcloud-github-judubois&view=azure-cli-latest)
* Install the Azure Spring Cloud extension: `az extension add -y --source https://azureclitemp.blob.core.windows.net/spring-cloud/spring_cloud-0.1.0-py2.py3-none-any.whl`
* Login: `az login`
* set the default resource group: `service az configure --defaults group=asc-resource-group`
* set the default service: `az configure --defaults spring-cloud=asc-webinar-06-12-2019`
* Here's the configuration that you need in order to configure the Azure Spring Cloud dependency: 
* build a simple spring boot app using reactive mongodb (no changes to the build required)
* create an application in the Azure portal and name it simple-microservice.
* create a CosmosDB MongoDB instance with a collection called `customers` 
* go back to the application and create an Azure Spring Cloud service-binding linking the CosmosDB instance with the application.
* Now you can deploy the application itself using the following incantation. 

  ```
    #!/bin/bash
    ./mvnw clean package
    az spring-cloud app deploy -n simple-microservice --jar-path target/service-0.0.1-SNAPSHOT.jar
  ```
* 
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
