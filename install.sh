#!/bin/sh

./gradlew build

cd Ansible

ansible-playbook --ask-sudo-pass smaDataLogger.yml
