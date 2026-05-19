#!/usr/bin/env bash


doFields() {
  local name="$1"
  local fields="$2"
  local direction="$3"

  if [ "$fields" == "null" ]; then
    echo "-- NO-OP"
    return
  fi
  echo "-- TODO"
}

json=$(<'./wiki.vg 1.0.0/protocol_claude.json')
len=$(echo "$json" | jq '.protocol.packets | length')

cd templates
for ((i = 0; i < "$len"; i++)); do
  packet=$(echo "$json" | jq ".protocol.packets[$i]")
  id=$(echo "$packet" | jq -r ".id")
  name=$(echo "$packet" | jq -r ".name")
  desc=$(echo "$packet" | jq -r ".description")
  direction=$(echo "$packet" | jq -r ".direction")
  fields=$(echo "$packet" | jq ".fields")

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
