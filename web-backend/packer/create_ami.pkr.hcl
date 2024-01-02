packer {
  required_plugins {
    amazon = {
      version = ">= 0.0.2"
      source  = "github.com/hashicorp/amazon"
    }
  }
}

variable "aws_region" {
  type    = string
  default = "us-west-2"
}

variable "aws_access_key" {
  type    = string
}

variable "aws_secret_key" {
  type    = string
}

variable "dev_user" {
  type    = string
}

variable "demo_user" {
  type    = string
}

variable "source_ami" {
  type    = string
  default = "ami-0f1a5f5ada0e7da53" # Amazon Linux 2 AMI (HVM) - Kernel 5.10
}

variable "ssh_username" {
  type    = string
  default = "ec2-user"
}

variable "subnet_id" {
  type    = string
  default = "subnet-0419fbfa47a1b2539"
}

variable "app_file" {
  type = string
  default = "web-backend-0.0.1-SNAPSHOT.jar"
}

source "amazon-ebs" "my-ami" {
  access_key = "${var.aws_access_key}"
  secret_key = "${var.aws_secret_key}"

  region     = "${var.aws_region}"
  ami_name        = "csye6225_${formatdate("YYYY_MM_DD_hh_mm_ss", timestamp())}"
  ami_description = "AMI for CSYE 6225"
  ami_regions = [
    "${var.aws_region}",
  ]
  ami_users=[
    "${var.dev_user}",
    "${var.demo_user}"
  ]

  aws_polling {
    delay_seconds = 120
    max_attempts  = 50
  }


  instance_type = "t2.micro"
  source_ami    = "${var.source_ami}"
  ssh_username  = "${var.ssh_username}"
  subnet_id     = "${var.subnet_id}"

  launch_block_device_mappings {
    delete_on_termination = true
    device_name           = "/dev/xvda"
    volume_size           = 50
    volume_type           = "gp2"
  }

}

build {
  sources = ["source.amazon-ebs.my-ami"]

  provisioner "file" {
    source = "target/${var.app_file}"
    destination = "/tmp/webapp.jar"
  }

  provisioner "file" {
    source = "packer/CloudWatchAgent.json"
    destination = "/tmp/config.json"
  }

  provisioner "file" {
    source = "packer/services/webapp.service"
    destination = "/tmp/webapp.service"
  }

  provisioner "shell" {
    environment_vars = [
      "DEBIAN_FRONTEND=noninteractive",
      "CHECKPOINT_DISABLE=1",
      "APP_FILE=${var.app_file}",
    ]

    scripts = [
      "packer/scripts/install-dependencies.sh",
      "packer/scripts/set-auto-run.sh",
    ]
  }
}