#!/usr/bin/env bash

json=$(<'./wiki.vg 1.0.0/protocol_claude.json')
len=$(echo "$json" | jq '.protocol.packets | length')
for ((i = 0; i < "$len"; i++))
do
  echo "$json" | jq ".protocol.packets[$i].id"
done
