#!/usr/bin/env bash

fieldTypeToWireshark() {
  case "$1" in
    bool)
      echo "bool"
      ;;
    *byte*)
      echo "int8"
      ;;
    short)
      echo "int16"
      ;;
    int)
      echo "int32"
      ;;
    long)
      echo "int64"
      ;;
    float)
      echo "float"
      ;;
    double)
      echo "double"
      ;;
    string16)
      echo "string"
      ;;
    *)
      echo "string"
      ;;
  esac
}

doFields() {
  local name="$1"
  local fields="$2"
  local direction="$3"

  if [ "$fields" == "null" ]; then
    echo "    -- NO-OP"
    return
  fi
  echo "    -- TODO"
}

declare -A packets
i=0
while read -r data; do
  IFS="#" read -ra packet <<< "$data"
  packets["${i}_ID"]="${packet[0]}"
  packets["${i}_NAME"]="${packet[1]}"
  packets["${i}_DESC"]="${packet[2]}"
  packets["${i}_DIR"]="${packet[3]}"
  packets["${i}_FIELDS"]="${packet[4]}"
  ((i++))
done <<< $(jq -r ".protocol.packets.[] | \"\\(.id)#\\(.name)#\\(.description)#\\(.direction)#\\(.fields)\"" < "./wiki.vg 1.0.0/protocol_claude.json")

cd templates
rm table/table.lua
rm decode/*.lua

for ((i = 0; i < "$((${#packets[@]} / 5))"; i++)); do
  id="${packets[${i}_ID]}"
  name="${packets[${i}_NAME]}"
  desc="${packets[${i}_DESC]}"
  direction="${packets[${i}_DIR]}"
  fields="${packets[${i}_FIELDS]}"

  cat << EOF > "decode/decode_$id.lua"
-- $name: $desc
function decode_$id(tree, buffer, length)  
EOF

  echo "    [$id] = {['name'] = '$name', ['decode'] = decode_$id}," >> "table/table.lua"

  doFields "$name" "$fields" "$direction" >> "decode/decode_$id.lua"
  echo -e "end\n" >> "decode/decode_$id.lua"
done

cat header.lua
echo
cat decode/decode_*.lua

echo
echo "local packets = {"
cat table/table.lua
echo "}"
echo

cat after_table.lua
