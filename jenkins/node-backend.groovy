pipeline {
    agent any
    stages {
        stage('git checkout node-backend') {
            steps {
                git branch: 'main', url: 'https://github.com/jaqlig/node-backend'
            }
        }
        stage('Build Docker image') {
            steps {
                sh 'docker build -t node-backend .'
            }
        }
        stage('Run Docker container for local tests') {
            steps {
                sh 'docker run -p 8081:80 --name node-backend -d node-backend:latest'
            }
        }
        stage('curl test') {
            steps {
                 sh '''#!/bin/bash
                        response=$(curl -s -o /dev/null -w "%{http_code}\n" http://localhost:8081/)
                        if [ "$response" != "200" ]
                        then
                        exit 1
                        fi
                 '''
            }
        }
        stage('Remove Docker container') {
            steps {
                sh 'docker container rm -f node-backend'
            }
        }
        stage('Tag Docker image') {
            steps {
                sh 'docker tag node-backend:latest ${image}:${version}'
            }
        }
        stage('Push Docker image') {
            steps {
                 sh '''#!/bin/bash
                        docker login -u ${docker_login} -p ${docker_pasword}
                        docker push ${image}:${version}
                 '''
            }
        }
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