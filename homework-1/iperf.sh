#!/bin/bash
function parse_yaml() {
   local prefix=$2
   local s='[[:space:]]*' w='[a-zA-Z0-9_]*' fs=$(echo @|tr @ '\034')
   sed -ne "s|^\($s\):|\1|" \
        -e "s|^\($s\)\($w\)$s:$s[\"']\(.*\)[\"']$s\$|\1$fs\2$fs\3|p" \
        -e "s|^\($s\)\($w\)$s:$s\(.*\)$s\$|\1$fs\2$fs\3|p"  $1 |
   awk -F$fs '{
      indent = length($1)/2;
      vname[indent] = $2;
      for (i in vname) {if (i > indent) {delete vname[i]}}
      if (length($3) > 0) {
         vn=""; for (i=0; i<indent; i++) {vn=(vn)(vname[i])("_")}
         printf("%s%s%s=\"%s\"\n", "'$prefix'",vn, $2, $3);
      }
   }'
}
eval $(parse_yaml configuration.yaml)

echo Configuration variables:
echo --------------------------------------
echo VM_2 address: $VM_2
echo Port: $PORT
echo Duration: $DURATION
echo Interval: $INTERVAL
echo PROTOCOL: $PROTOCOL
echo Log File: $MY_FILE

PROT=""
if [[ "$PROTOCOL" = "UDP" ]]
then
  PROT=-u
fi

echo --------------------------------------
echo Starting ssh session and iperf server on $VM_2:
# -P 1 should close iperf after 1 client
ssh $VM_2 iperf -s $PROT -p $PORT -P 1 &
sleep 2
echo Starting iperf client and saving log:
iperf -c $VM_2 $PROT -p $PORT -t $DURATION -i $INTERVAL | tee "$MY_FILE"
# To close iperf process if it is left open 
ssh $VM_2 pkill iperf
echo 
echo Simple Traffic Generator Completed

