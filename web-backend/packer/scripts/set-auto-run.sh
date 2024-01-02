#!/bin/bash
sudo systemctl daemon-reload
sudo systemctl enable webapp.service
sudo systemctl start --no-block webapp.service
sudo /opt/aws/amazon-cloudwatch-agent/bin/amazon-cloudwatch-agent-ctl -a fetch-config -m ec2 -c file:/opt/webapp/custom-amazon-cloudwatch-agent.json -s