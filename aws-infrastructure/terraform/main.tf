resource "aws_vpc" "main" {
  cidr_block = "10.0.0.0/16"

  tags = {
    Name = "Project VPC"
  }
}

resource "aws_subnet" "public_subnets" {
  count             = length(var.public_subnet_cidrs)
  vpc_id            = aws_vpc.main.id
  cidr_block        = element(var.public_subnet_cidrs, count.index)
  availability_zone = element(var.azs, count.index)

  tags = {
    Name = "Public Subnet ${count.index + 1}"
  }
}

resource "aws_subnet" "private_subnets" {
  count             = length(var.private_subnet_cidrs)
  vpc_id            = aws_vpc.main.id
  cidr_block        = element(var.private_subnet_cidrs, count.index)
  availability_zone = element(var.azs, count.index)

  tags = {
    Name = "Private Subnet ${count.index + 1}"
  }
}

resource "aws_internet_gateway" "gw" {
  vpc_id = aws_vpc.main.id

  tags = {
    Name = "Project VPC IG"
  }
}

resource "aws_route_table" "second_rt" {
  vpc_id = aws_vpc.main.id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.gw.id
  }

  tags = {
    Name = "2nd Route Table"
  }
}

resource "aws_route_table_association" "public_subnet_asso" {
  count          = length(var.public_subnet_cidrs)
  subnet_id      = element(aws_subnet.public_subnets[*].id, count.index)
  route_table_id = aws_route_table.second_rt.id
}

resource "aws_security_group" "load_balancer" {
  name        = "load_balancer"
  description = "Allow TLS traffic for load balancer"
  vpc_id      = aws_vpc.main.id
  ingress {
    from_port   = 443
    to_port     = 443
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
  ingress {
    from_port   = 8081
    to_port     = 8081
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_security_group" "tcp_rule" {
  name        = "application"
  description = "Allow TLS traffic for CSYE Web App"
  vpc_id      = aws_vpc.main.id

  ingress {
    from_port       = 22
    to_port         = 22
    protocol        = "tcp"
    security_groups = [aws_security_group.load_balancer.id]
  }
  egress {
    from_port        = 22
    to_port          = 22
    protocol         = "tcp"
    cidr_blocks      = ["0.0.0.0/0"]
    ipv6_cidr_blocks = ["::/0"]
  }
  ingress {
    from_port       = 80
    to_port         = 80
    protocol        = "tcp"
    security_groups = [aws_security_group.load_balancer.id]
  }
  egress {
    from_port        = 80
    to_port          = 80
    protocol         = "tcp"
    cidr_blocks      = ["0.0.0.0/0"]
    ipv6_cidr_blocks = ["::/0"]
  }
  ingress {
    from_port       = 443
    to_port         = 443
    protocol        = "tcp"
    security_groups = [aws_security_group.load_balancer.id]
  }
  egress {
    from_port        = 443
    to_port          = 443
    protocol         = "tcp"
    cidr_blocks      = ["0.0.0.0/0"]
    ipv6_cidr_blocks = ["::/0"]
  }

  ingress {
    from_port       = 8081
    to_port         = 8081
    protocol        = "tcp"
    security_groups = [aws_security_group.load_balancer.id]
  }
  egress {
    from_port        = 8081
    to_port          = 8081
    protocol         = "tcp"
    cidr_blocks      = ["0.0.0.0/0"]
    ipv6_cidr_blocks = ["::/0"]
  }

  ingress {
    from_port   = 3306
    to_port     = 3306
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
  egress {
    from_port   = 3306
    to_port     = 3306
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
}
resource "aws_security_group" "db_rule" {
  name        = "database"
  description = "Allow TLS traffic for CSYE Web App"
  vpc_id      = aws_vpc.main.id
  ingress {
    from_port = 3306
    to_port   = 3306
    protocol  = "tcp"
    # cidr_blocks = ["0.0.0.0/0"]
    # cidr_blocks     = concat(var.private_subnet_cidrs, var.public_subnet_cidrs)
    security_groups = [aws_security_group.tcp_rule.id]
  }
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_kms_key" "kms_key" {
  description = "KMS key"

}

resource "aws_kms_key_policy" "kms_key_policy" {
  key_id = aws_kms_key.kms_key.id
  policy = jsonencode({
    Id = "example"
    Statement = [
      {
        Action = "kms:*"
        Effect = "Allow"
        Principal = {
          AWS = "*"
        }

        Resource = "*"
        Sid      = "Enable IAM User Permissions"
      },
    ]
    Version = "2012-10-17"
  })
}

resource "random_uuid" "bucket_name" {
}

resource "aws_s3_bucket" "my_bucket" {
  bucket        = random_uuid.bucket_name.result
  force_destroy = true
}

# resource "aws_s3_bucket_acl" "my_bucket_acl" {
#   bucket = aws_s3_bucket.my_bucket.id
#   acl    = "private"
# }

resource "aws_s3_bucket_versioning" "my_bucket_versioning" {
  bucket = aws_s3_bucket.my_bucket.id
  versioning_configuration {
    status = "Disabled"
  }
}

resource "aws_s3_bucket_lifecycle_configuration" "my_bucket_lifecycle" {
  bucket = aws_s3_bucket.my_bucket.id

  rule {
    id = "bucket_lifecycle"

    status = "Enabled"

    transition {
      days          = 30
      storage_class = "STANDARD_IA"
    }
  }
}

resource "aws_s3_bucket_server_side_encryption_configuration" "my_bucket_encryption" {
  bucket = aws_s3_bucket.my_bucket.id

  rule {
    apply_server_side_encryption_by_default {
      sse_algorithm = "AES256"
    }
  }
}

resource "aws_db_subnet_group" "my_subnet_group" {
  name       = "db_subnet_group"
  subnet_ids = concat(aws_subnet.public_subnets[*].id, aws_subnet.private_subnets[*].id)
}

resource "aws_db_instance" "my_db_instance" {
  allocated_storage      = 20
  max_allocated_storage  = 20
  db_name                = "mydb"
  engine                 = "mysql"
  engine_version         = "8.0.26"
  instance_class         = "db.t3.micro"
  identifier             = var.db_identifier
  username               = var.db_username
  password               = var.db_password
  multi_az               = false
  db_subnet_group_name   = aws_db_subnet_group.my_subnet_group.name
  vpc_security_group_ids = [aws_security_group.db_rule.id]
  skip_final_snapshot    = true
  kms_key_id             = aws_kms_key.kms_key.arn
  storage_encrypted      = true
}

resource "aws_iam_policy" "WebAppS3" {
  name = "WebAppS3"
  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect = "Allow"
        Action = [
          "s3:PutObject",
          "s3:GetObject",
          "s3:DeleteObject",
          "s3:PutObjectAcl",
          "s3:GetObjectAcl"
        ]
        Resource = [
          "arn:aws:s3:::${aws_s3_bucket.my_bucket.bucket}",
          "arn:aws:s3:::${aws_s3_bucket.my_bucket.bucket}/*"
        ]
      },
    ]
  })
}

resource "aws_iam_policy" "WebAppCloudWatchAgentServerPolicy" {
  name = "WebAppCloudWatchAgentServerPolicy"
  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        "Effect" : "Allow",
        "Action" : [
          "cloudwatch:PutMetricData",
          "ec2:DescribeVolumes",
          "ec2:DescribeTags",
          "logs:PutLogEvents",
          "logs:DescribeLogStreams",
          "logs:DescribeLogGroups",
          "logs:CreateLogStream",
          "logs:CreateLogGroup"
        ],
        "Resource" : "*"
      },
      {
        "Effect" : "Allow",
        "Action" : [
          "ssm:GetParameter"
        ],
        "Resource" : "arn:aws:ssm:*:*:parameter/AmazonCloudWatch-*"
      }
    ]
  })
}

resource "aws_iam_role" "db_iam_role" {
  name = "db_iam_role"
  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRole"
        Effect = "Allow"
        Sid    = ""
        Principal = {
          Service = "ec2.amazonaws.com"
        }
      },
    ]
  })

  managed_policy_arns = [aws_iam_policy.WebAppS3.arn, aws_iam_policy.WebAppCloudWatchAgentServerPolicy.arn]
}


resource "aws_iam_instance_profile" "db_iam_profile" {
  name = "db_iam_profile"
  role = aws_iam_role.db_iam_role.name
}

data "aws_ami" "webapp_ami" {
  filter {
    name   = "name"
    values = ["csye6225_*"]
  }
  most_recent = true
}

data "template_file" "user_data" {

  template = <<-EOF
        #!/bin/bash
        cd /opt
        echo "#!/bin/bash" >> setenv.sh
        echo "export BUCKET_NAME=${aws_s3_bucket.my_bucket.bucket}" >> setenv.sh
        echo "export DB_HOST=${aws_db_instance.my_db_instance.address}" >> setenv.sh
        echo "export DB_NAME=${var.db_name}" >> setenv.sh
        echo "export DB_PASSWORD=${var.db_password}" >> setenv.sh
        echo "export DB_PORT=${var.db_port}" >> setenv.sh
        echo "export DB_USER=${var.db_username}" >> setenv.sh
        echo "export AWS_REGION=${var.region}" >> setenv.sh
        chmod +x setenv.sh
        source setenv.sh
        EOF
}

resource "aws_launch_template" "lt" {
  name          = "webapp-load-balancer-launch-template"
  image_id      = data.aws_ami.webapp_ami.id
  instance_type = "t2.micro"
  key_name      = var.ssh_key_name
  # vpc_security_group_ids = [aws_security_group.tcp_rule.id, aws_security_group.db_rule.id]
  iam_instance_profile {
    name = aws_iam_instance_profile.db_iam_profile.name
  }

  user_data = base64encode(data.template_file.user_data.rendered)

  block_device_mappings {

    device_name = "/dev/xvda"
    ebs {
      volume_size           = 50
      volume_type           = "gp2"
      delete_on_termination = true
      encrypted             = true
      kms_key_id            = aws_kms_key.kms_key.arn
    }
  }

  network_interfaces {
    associate_public_ip_address = true
    security_groups             = [aws_security_group.tcp_rule.id, aws_security_group.db_rule.id]
  }
}

resource "aws_autoscaling_group" "asg" {

  name             = "csye6225-asg-spring2023"
  max_size         = 3
  min_size         = 1
  desired_capacity = 1

  force_delete = true
  tag {
    key                 = "Name"
    value               = "ec2 instance"
    propagate_at_launch = true
  }

  health_check_grace_period = 10
  health_check_type         = "EC2"

  vpc_zone_identifier = aws_subnet.public_subnets[*].id

  launch_template {

    id = aws_launch_template.lt.id

    version = "$Latest"

  }

  target_group_arns = [aws_lb_target_group.alb_tg.arn]
}

resource "aws_autoscaling_policy" "asg_cpu_policy_scale_out" {

  name = "csye6225-asg-cpu-up"

  autoscaling_group_name = aws_autoscaling_group.asg.name

  adjustment_type = "ChangeInCapacity"

  policy_type = "SimpleScaling"

  cooldown = 60

  scaling_adjustment = 1
}

resource "aws_autoscaling_policy" "asg_cpu_policy_scale_in" {

  name = "csye6225-asg-cpu-down"

  autoscaling_group_name = aws_autoscaling_group.asg.name

  adjustment_type = "ChangeInCapacity"

  policy_type = "SimpleScaling"

  cooldown = 60

  scaling_adjustment = -1
}

resource "aws_cloudwatch_metric_alarm" "web_cpu_alarm_up" {
  alarm_name          = "cpu-alarm-up"
  alarm_description   = "cpu-alarm-up"
  comparison_operator = "GreaterThanOrEqualToThreshold"
  evaluation_periods  = 1
  metric_name         = "CPUUtilization"
  namespace           = "AWS/EC2"
  period              = 60
  statistic           = "Average"
  threshold           = 5
  dimensions = {
    AutoScalingGroupName = aws_autoscaling_group.asg.name
  }
  actions_enabled = true
  alarm_actions   = [aws_autoscaling_policy.asg_cpu_policy_scale_out.arn]
}

resource "aws_cloudwatch_metric_alarm" "web_cpu_alarm_down" {
  alarm_name          = "cpu-alarm-down"
  alarm_description   = "cpu-alarm-down"
  comparison_operator = "LessThanOrEqualToThreshold"
  evaluation_periods  = 1
  metric_name         = "CPUUtilization"
  namespace           = "AWS/EC2"
  period              = 60
  statistic           = "Average"
  threshold           = 3
  dimensions = {
    AutoScalingGroupName = aws_autoscaling_group.asg.name
  }
  actions_enabled = true
  alarm_actions   = [aws_autoscaling_policy.asg_cpu_policy_scale_in.arn]
}


resource "aws_lb_target_group" "alb_tg" {
  name        = "webapp-lb-tg"
  port        = 8081
  protocol    = "HTTP"
  vpc_id      = aws_vpc.main.id
  target_type = "instance"

  health_check {
    path     = "/healthz"
    port     = 8081
    protocol = "HTTP"
    timeout  = 5
    interval = 30
  }
}

resource "aws_lb" "lb" {

  name = "csye6225-lb"

  internal = false

  load_balancer_type = "application"
  security_groups    = [aws_security_group.load_balancer.id]
  subnets            = aws_subnet.public_subnets[*].id
  tags = {

    Application = "WebApp"

  }

}

resource "aws_lb_listener" "front_end" {
  load_balancer_arn = aws_lb.lb.arn
  port              = 443
  protocol          = "HTTPS"
  ssl_policy        = "ELBSecurityPolicy-2016-08"
  certificate_arn   = var.certificate_arn

  default_action {

    type = "forward"

    target_group_arn = aws_lb_target_group.alb_tg.arn

  }

}

data "aws_route53_zone" "domain" {
  name = var.domain
}

resource "aws_route53_record" "www" {
  zone_id = data.aws_route53_zone.domain.zone_id
  name    = var.domain
  type    = "A"
  # ttl     = 300
  # records = [aws_instance.ec2instance.public_ip]
  alias {
    name                   = aws_lb.lb.dns_name
    zone_id                = aws_lb.lb.zone_id
    evaluate_target_health = true
  }
}
