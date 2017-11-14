#!/bin/sh

./gradlew build

cd Ansible

ansible-playbook smaDataLogger.yml
