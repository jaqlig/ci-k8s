pipeline {
    agent any
    stages {
        stage('git checkout ci-k8s') {
            steps {
                git branch: 'main', url: 'https://github.com/jaqlig/ci-k8s'
            }
        }
        stage('Install dependencies') {
            steps {
                 sh '''#!/bin/bash
                        cd ansible/
                        sudo chmod +x ./init.sh && ./init.sh
                 '''
            }
        }
        stage('Setup k3s with Ansible') {
            steps {
                 sh '''#!/bin/bash
                        cd ansible/
                        ansible-playbook -i inventories/production -e "node1_ip=${node1_ip} node2_ip=${node2_ip} ssh_private_key=${ssh_private_key}" k3s_setup.yml
                 '''
            }
        }
    }
}