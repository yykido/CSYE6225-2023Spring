#!/bin/bash
sudo yum update -y
sudo yum upgrade -y
sudo yum install -y wegt expect

sudo amazon-linux-extras enable corretto8
sudo yum install java-1.8.0-amazon-corretto -y
sudo yum install java-1.8.0-amazon-corretto-devel -y

sudo yum install amazon-cloudwatch-agent -y

sudo mkdir /opt/webapp
sudo chmod -R 777 /opt/webapp

touch /opt/webapp/csye6225.log

sudo mv /tmp/config.json /opt/webapp/custom-amazon-cloudwatch-agent.json
sudo mv /tmp/webapp.jar /opt/webapp/webapp.jar
sudo mv /tmp/webapp.service /etc/systemd/system/webapp.service
sudo touch /opt/setenv.sh

sudo yum clean all