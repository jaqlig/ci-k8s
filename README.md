`git clone https://github.com/jaqlig/ci-k8s.git`

`cd ci-k8s`

`sudo chmod +x ./init.sh && ./init.sh`

Enter public IPs and DNS in ansible/inventories/production/group_vars/all.yml

`cd ansible`

`ansible-playbook -i inventories/production k3s_setup.yml`

Wait couple of minutes for cluster to set up properly

`ansible-playbook -i inventories/production k3s_deploy.yml`