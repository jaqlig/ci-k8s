pipeline {
    agent any
    stages {
        stage('git checkout ci-k8s') {
            steps {
                git branch: 'main', url: 'https://github.com/jaqlig/ci-k8s'
            }
        }
        stage('Run deployment with Ansible') {
            steps {
                 sh '''#!/bin/bash
                        cd ansible/
                        export KUBECONFIG=~/kubeconfig
                        ansible-playbook -e "cluster_dns=${cluster_dns} image=${image} version=${version}" k3s_deploy.yml
                 '''
            }
        }
    }
}