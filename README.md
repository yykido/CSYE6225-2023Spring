# CSYE 6225 Network Structures and Cloud Computing
---------------------------------------------------------------

### Summary

This is a  web application Library Management system built with spring
boot and deployed on AWS

-   EC2 instances are built on a custom
    [AMI](https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/AMIs.html)
    using [packer](https://packer.io/)
-   Setting up the network and creation of resources is automated with
    Cloud formation, aws cli and shell scripts
-   Instances are autoscaled with
    [ELB](https://aws.amazon.com/elasticloadbalancing/) to handle the
    web traffic
-   The application is deployed with Terraform and Packer

### Architecture Diagram

 ![aws_full](https://user-images.githubusercontent.com/42703011/92800898-211c7580-f383-11ea-9b4e-76c171fca750.png)


Tools and Technologies
----------------------
                          
| Infrastructure       | VPC, ELB, EC2, Route53, Cloud formation, Shell, Packer |
|----------------------|--------------------------------------------------------|
| Webapp               | Java, Spring Boot, MySQL, Maven                        |
| CI/CD                | Terraform, Packer                            |
| Alerting and logging | Cloud Watch                  |


Infrastructure-setup
--------------------

-   Create the networking setup using cloud formation and aws cli
-   Create the required IAM policies and users
-   Setup Load Balancers, Route53 and RDS

Webapp
------

-   The Library Management System Web application is developed using
    Java Spring Boot framework that uses the REST architecture
-   Secured the application with [Spring
    Security](https://spring.io/projects/spring-security) Basic
    [authentication](https://developer.mozilla.org/en-US/docs/Web/HTTP/Authentication)
    to retrieve user information
-   Created Maven profiles to run the app locally and when deployed on
    AWS
-   Storing the images of Book covers in S3


Auto scaling groups
-------------------

-   Created auto scaling groups to scale to the application to handle
    the webtraffic and keep the costs low when traffic is low
-   Created cloud watch alarms to scale up and scale down the EC2
    instances


[Packer](https://packer.io/)
----------------------------

-   Implemented CI to build out an AMI and share it between organization
    on AWS
-   Created provisioners and bootstrapped the EC2 instance with required
    tools like Tomcat, JAVA, Python

