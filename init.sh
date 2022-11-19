#!/bin/bash
sudo apt install -y pip
python3 -m pip install ansible==6.3.0
python3 -m pip install openshift
ansible-galaxy collection install ansible.posix
ansible-galaxy collection install community.general
ansible-galaxy collection install kubernetes.core