function packetName(id)
  if packets[id] ~= nil then
    return packets[id]["name"]
  end
  return "<Unknown>"
end

function packetDecode(id, tree, buffer, length)
  if packets[id] ~= nil then
    packets[id]["decode"](tree, buffer, length)
  end
end
