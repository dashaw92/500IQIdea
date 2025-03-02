mc = Proto("mc", "Minecraft 1.0.0")

local packet_id = ProtoField.uint8("mc.packet_id", "packetId", base.HEX)

mc.fields = {
   packet_id
}

function mc.dissector(buffer, pinfo, tree)
  length = buffer:len()
  if length == 0 then return end

  pinfo.cols.protocol = mc.name
  local subtree = tree:add(mc, buffer(), "Minecraft 1.0.0 Packet")
  subtree:add(packet_id, buffer(0, 1)):append_text(" (".. packetName(buffer(0, 1):uint()) ..")")
end

local port = DissectorTable.get("tcp.port")
port:add(25065, mc)
port:add(25565, mc)

-- Packet names
local packets = {
	[0x00] = "Keep Alive",
	[0x01] = "Login Request",
	[0x02] = "Handshake",
	[0x03] = "Chat Message",
	[0x04] = "Time Update",
	[0x05] = "Entity Equipment",
	[0x06] = "Spawn Position",
	[0x07] = "Use Entity",
	[0x08] = "Update Health",
	[0x09] = "Respawn",
	[0x0A] = "Player",
	[0x0B] = "Player Position",
	[0x0C] = "Player Look",
	[0x0D] = "Player Position & Look",
	[0x0E] = "Player Digging",
	[0x0F] = "Player Block Placement",
	[0x10] = "Holding Change",
	[0x11] = "Use Bed",
	[0x12] = "Animation",
	[0x13] = "Entity Action",
	[0x14] = "Named Entity Spawn",
	[0x15] = "Pickup Spawn",
	[0x16] = "Collect Item",
	[0x17] = "Add Object/Vehicle",
	[0x18] = "Mob Spawn",
	[0x19] = "Painting",
	[0x1A] = "Experience Orb",
	[0x1B] = "Stance update",
	[0x1C] = "Entity Velocity",
	[0x1D] = "Destroy Entity",
	[0x1E] = "Entity",
	[0x1F] = "Entity Relative Move",
	[0x20] = "Entity Look",
	[0x21] = "Entity Look and Relative Move",
	[0x22] = "Entity Teleport",
	[0x26] = "Entity Status",
	[0x27] = "Attach Entity",
	[0x28] = "Entity Metadata",
	[0x29] = "Entity Effect",
	[0x2A] = "Remove Entity Effect",
	[0x2B] = "Experience",
	[0x32] = "Pre-Chunk",
	[0x33] = "Map Chunk",
	[0x34] = "Multi Block Change",
	[0x35] = "Block Change",
	[0x36] = "Block Action",
	[0x3C] = "Explosion",
	[0x3D] = "Sound/particle effect",
	[0x46] = "New/Invalid State",
	[0x47] = "Thunderbolt",
	[0x64] = "Open window",
	[0x65] = "Close window",
	[0x66] = "Window click",
	[0x67] = "Set slot",
	[0x68] = "Window items",
	[0x69] = "Update window property",
	[0x6A] = "Transaction",
	[0x6B] = "Creative inventory action",
	[0x6C] = "Enchant Item",
	[0x82] = "Update Sign",
	[0x83] = "Item Data",
	[0xC8] = "Increment Statistic",
	[0xC9] = "Player List Item",
	[0xFE] = "Server List Ping",
	[0xFF] = "Disconnect/Kick",
}

function packetName(id)
  local name = "<Unknown>"
  if packets[id] ~= nil then
    return packets[id]
  else
    return name
  end
end
