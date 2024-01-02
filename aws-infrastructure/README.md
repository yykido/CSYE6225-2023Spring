# Commands

Initialize terraform: `terraform init`

Apply IaC: `terraform apply -var-file=FILE` (example `-var-file=USWest1.tfvars or -var-file=USWest2.tfvars`)

    - You will be required to input profile name (such as dev or demo)

Destroy IaC: `terraform destroy -var-file=FILE`

Import SSL certificate: `aws --profile demo acm import-certificate --certificate fileb://demo_glimmerllx_me.crt --certificate-chain fileb://demo_glimmerllx_me.ca-bundle --private-key fileb://yourkey.key`