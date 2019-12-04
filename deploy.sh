#!/bin/bash

# curl -L https://aka.ms/InstallAzureCli | bash
echo "the current UNIX is"
uname -a

curl -sL https://aka.ms/InstallAzureCLIDeb | sudo bash

echo "Installed the Azure CLI..."

# 
# Sign in with a Service Principal 
# https://docs.microsoft.com/en-us/cli/azure/create-an-azure-service-principal-azure-cli?view=azure-cli-latest 
# TLDR: I had to run:
# 		az ad sp create-for-rbac --name azure-ci-username    
az login --service-principal -u http://${AZURE_SERVICE_PRINCIPAL_USERNAME} -p $AZURE_SERVICE_PRINCIPAL_PASSWORD  --tenant $AZURE_SERVICE_PRINCIPAL_TENANT_ID
echo "Logged in to Azure account using servie principal ${AZURE_SERVICE_PRINCIPAL_USERNAME}..."

az extension add -y --source https://azureclitemp.blob.core.windows.net/spring-cloud/spring_cloud-0.1.0-py2.py3-none-any.whl
echo "Added the Azure CLI Azure Spring Cloud extension..."

az configure --defaults group=asc-resource-group
az configure --defaults spring-cloud=asc-webinar-06-12-2019
echo "Configured default resource grup and default Azure Spring Cloud instance..."

az spring-cloud app deploy -n simple-microservice --jar-path simple-microservice/target/service-0.0.1-SNAPSHOT.jar
echo "Deployed the Simple Microservice .jar... "
