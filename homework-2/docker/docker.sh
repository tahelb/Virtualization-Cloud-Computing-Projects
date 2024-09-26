#!/bin/bash
VM_2="10.0.2.15"
PORT=10072
DURATION=10
INTERVAL=1

echo Configuration variables:
echo --------------------------------------
echo VM_2 address: $VM_2
echo Port: $PORT
echo Duration: $DURATION
echo Interval: $INTERVAL

echo --------------------------------------
echo Starting ssh session and running container on $VM_2:
ssh $VM_2 docker pull tahelbu/alpine_iperf:latest 
sleep 1
echo Running iperf server of $VM_2:
ssh $VM_2 docker run --name 315259168_207599499 -d -p $PORT:$PORT tahelbu/alpine_iperf iperf -s -p $PORT
sleep 2
echo Running iperf client:
iperf -c $VM_2 -p $PORT -t $DURATION -i $INTERVAL
echo --------------------------------------
echo iperf session completed, stopping container and clearing docker history
ssh $VM_2 docker stop 315259168_207599499 > /dev/null
ssh $VM_2 docker rm 315259168_207599499  > /dev/null
echo Done

