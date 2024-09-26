#!/bin/bash
echo "- = Starting = -"
echo "- = Creating resources = -"
kubectl apply -f hw2_resources_ver1.yaml
IP=$(kubectl get services | grep nginx-service | awk '{print $3}')
echo "- = The IP address of my service is: $IP = -"

end=$((SECONDS+240))
while [ ${SECONDS} -lt ${end} ]; do
 wget $IP -q -O-
 done

echo "- = Updating application = -"
kubectl apply -f hw2_resources_ver2.yaml
kubectl rollout status deployment nginx-deploy
wget $IP -q -O-

echo "- = Rolling back = -"
kubectl rollout undo deployment/nginx-deploy
kubectl rollout status deployment nginx-deploy
wget $IP -q -O-

echo "- = Cleaning up = -"
kubectl delete hpa nginx-autoscale
kubectl delete deployment nginx-deploy
kubectl delete service nginx-service 

echo "- = Great Success! = -"
