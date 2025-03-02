mc = Proto("mc", "Minecraft 1.0.0")

local packet_id = ProtoField.uint8("mc.packet_id", "Packet ID", base.HEX)

local packet_kick_reason = ProtoField.string("mc.packet.kick.reason", "Kick Reason", base.ASCII)
local packet_handshake_body = ProtoField.string("mc.packet.handshake.body", "Handshake Body", base.ASCII)
local packet_login_server_eid = ProtoField.int32("mc.packet.login.server.eid", "Entity ID", base.DEC)
local packet_login_server_seed = ProtoField.int64("mc.packet.login.server.seed", "World Seed", base.DEC)
local packet_login_server_gamemode = ProtoField.int32("mc.packet.login.server.gamemode", "Gamemode", base.DEC)
local packet_login_server_dimension = ProtoField.int32("mc.packet.login.server.dimension", "Dimension", base.DEC)
local packet_login_server_difficulty = ProtoField.int32("mc.packet.login.server.difficulty", "Difficulty", base.DEC)
local packet_login_server_height = ProtoField.int32("mc.packet.login.server.height", "World Height", base.DEC)
local packet_login_server_max_players = ProtoField.int32("mc.packet.login.server.max_players", "Max Players", base.DEC)
local packet_login_client_protocol = ProtoField.int32("mc.packet.login.client.proto", "Protocol Version", base.DEC)
local packet_login_client_username = ProtoField.string("mc.packet.login.client.username", "Username", base.ASCII)

mc.fields = {
   packet_id,
   packet_login_server_eid, -- 0x01 (Login)
   packet_login_server_seed,
   packet_login_server_gamemode,
   packet_login_server_dimension,
   packet_login_server_difficulty,
   packet_login_server_height,
   packet_login_server_max_players,
   packet_login_client_protocol,
   packet_login_client_username, -- </0x01>
   packet_handshake_body, -- 0x02 (Handshake (C <-> S both handled by one impl))
   packet_kick_reason, -- 0xFF (Kick/Disconnect)
}

function mc.dissector(buffer, pinfo, tree)
  length = buffer:len()
  if length == 0 then return end

  pinfo.cols.protocol = mc.name
  local subtree = tree:add(mc, buffer(), "Minecraft")

  local id = buffer(0, 1)
  subtree:add(packet_id, id):append_text(" (".. packetName(id:uint()) ..")")

  -- Packet data, stripped of the packet ID
  local data = buffer:range(1):tvb()
  packetDecode(id:uint(), subtree, data, length - 1)
end

local port = DissectorTable.get("tcp.port")
port:add(25065, mc)
port:add(25565, mc)

function decode_0x00(tree, buffer, length)

end

function decode_0x01(tree, buffer, length)
  local field1 = buffer(0, 4):int()
  local string2 = readMcStr(buffer:range(4):tvb(), length - 5)
  if string2 == "" then
    -- Server -> Client
    local seed = buffer(6, 8):int64()
    local gamemode = buffer(14, 4):int()
    local dim = buffer(18, 1):int()
    local difficulty = buffer(19, 1):int()
    local height = buffer(20, 1):uint()
    local max_players = buffer(21, 1):uint()
    tree:add(packet_login_server_eid, field1)
    tree:add(packet_login_server_seed, seed)
    tree:add(packet_login_server_gamemode, gamemode)
    tree:add(packet_login_server_dimension, dim)
    tree:add(packet_login_server_difficulty, difficulty)
    tree:add(packet_login_server_height, height)
    tree:add(packet_login_server_max_players, max_players)
  else
    -- Client -> Server
    tree:add(packet_login_client_protocol, field1)
    tree:add(packet_login_client_username, string2)
  end
end

function decode_0x02(tree, buffer, length)
  tree:add(packet_handshake_body, readMcStr(buffer, length))
end

function decode_0x03(tree, buffer, length)

end

function decode_0x04(tree, buffer, length)

end

function decode_0x05(tree, buffer, length)

end

function decode_0x06(tree, buffer, length)

end

function decode_0x07(tree, buffer, length)

end

function decode_0x08(tree, buffer, length)

end

function decode_0x09(tree, buffer, length)

end

function decode_0x0A(tree, buffer, length)

end

function decode_0x0B(tree, buffer, length)

end

function decode_0x0C(tree, buffer, length)

end

function decode_0x0D(tree, buffer, length)

end

function decode_0x0E(tree, buffer, length)

end

function decode_0x0F(tree, buffer, length)

end

function decode_0x10(tree, buffer, length)

end

function decode_0x11(tree, buffer, length)

end

function decode_0x12(tree, buffer, length)

end

function decode_0x13(tree, buffer, length)

end

function decode_0x14(tree, buffer, length)

end

function decode_0x15(tree, buffer, length)

end

function decode_0x16(tree, buffer, length)

end

function decode_0x17(tree, buffer, length)

end

function decode_0x18(tree, buffer, length)

end

function decode_0x19(tree, buffer, length)

end

function decode_0x1A(tree, buffer, length)

end

function decode_0x1B(tree, buffer, length)

end

function decode_0x1C(tree, buffer, length)

end

function decode_0x1D(tree, buffer, length)

end

function decode_0x1E(tree, buffer, length)

end

function decode_0x1F(tree, buffer, length)

end

function decode_0x20(tree, buffer, length)

end

function decode_0x21(tree, buffer, length)

end

function decode_0x22(tree, buffer, length)

end

function decode_0x26(tree, buffer, length)

end

function decode_0x27(tree, buffer, length)

end

function decode_0x28(tree, buffer, length)

end

function decode_0x29(tree, buffer, length)

end

function decode_0x2A(tree, buffer, length)

end

function decode_0x2B(tree, buffer, length)

end

function decode_0x32(tree, buffer, length)

end

function decode_0x33(tree, buffer, length)

end

function decode_0x34(tree, buffer, length)

end

function decode_0x35(tree, buffer, length)

end

function decode_0x36(tree, buffer, length)

end

function decode_0x3C(tree, buffer, length)

end

function decode_0x3D(tree, buffer, length)

end

function decode_0x46(tree, buffer, length)

end

function decode_0x47(tree, buffer, length)

end

function decode_0x64(tree, buffer, length)

end

function decode_0x65(tree, buffer, length)

end

function decode_0x66(tree, buffer, length)

end

function decode_0x67(tree, buffer, length)

end

function decode_0x68(tree, buffer, length)

end

function decode_0x69(tree, buffer, length)

end

function decode_0x6A(tree, buffer, length)

end

function decode_0x6B(tree, buffer, length)

end

function decode_0x6C(tree, buffer, length)

end

function decode_0x82(tree, buffer, length)

end

function decode_0x83(tree, buffer, length)

end

function decode_0xC8(tree, buffer, length)

end

function decode_0xC9(tree, buffer, length)

end

function decode_0xFE(tree, buffer, length)
  -- NO OP
end

function decode_0xFF(tree, buffer, length)
  tree:add(packet_kick_reason, readMcStr(buffer, length))
end

function mcStrLen(buffer)
  -- every char is 0 padded, so the true byte length is the length of the string * 2
  return buffer(0, 2):int() * 2
end

function readMcStr(buffer, length)
  local len = mcStrLen(buffer)

  local str = ""
  for i = 0, len - 1, 2 do
    -- Special handling for \u00A7 (§) since it's a multibyte char
    if i < len - 3 then
      if buffer(2 + i, 2):int() == 0xA7 then
        str = str .. "§"
        goto continue
      end
    end
    str = str .. string.char(buffer(2 + i, 2):int())
    ::continue::
  end
  return str
end

-- Packet names
local packets = {
	[0x00] = {["name"] = "Keep Alive", ["decode"] = decode_0x00},
	[0x01] = {["name"] = "Login Request", ["decode"] = decode_0x01},
	[0x02] = {["name"] = "Handshake", ["decode"] = decode_0x02},
	[0x03] = {["name"] = "Chat Message", ["decode"] = decode_0x03},
	[0x04] = {["name"] = "Time Update", ["decode"] = decode_0x04},
	[0x05] = {["name"] = "Entity Equipment", ["decode"] = decode_0x05},
	[0x06] = {["name"] = "Spawn Position", ["decode"] = decode_0x06},
	[0x07] = {["name"] = "Use Entity", ["decode"] = decode_0x07},
	[0x08] = {["name"] = "Update Health", ["decode"] = decode_0x08},
	[0x09] = {["name"] = "Respawn", ["decode"] = decode_0x09},
	[0x0A] = {["name"] = "Player", ["decode"] = decode_0x0A},
	[0x0B] = {["name"] = "Player Position", ["decode"] = decode_0x0B},
	[0x0C] = {["name"] = "Player Look", ["decode"] = decode_0x0C},
	[0x0D] = {["name"] = "Player Position & Look", ["decode"] = decode_0x0D},
	[0x0E] = {["name"] = "Player Digging", ["decode"] = decode_0x0E},
	[0x0F] = {["name"] = "Player Block Placement", ["decode"] = decode_0x0F},
	[0x10] = {["name"] = "Holding Change", ["decode"] = decode_0x10},
	[0x11] = {["name"] = "Use Bed", ["decode"] = decode_0x11},
	[0x12] = {["name"] = "Animation", ["decode"] = decode_0x12},
	[0x13] = {["name"] = "Entity Action", ["decode"] = decode_0x13},
	[0x14] = {["name"] = "Named Entity Spawn", ["decode"] = decode_0x14},
	[0x15] = {["name"] = "Pickup Spawn", ["decode"] = decode_0x15},
	[0x16] = {["name"] = "Collect Item", ["decode"] = decode_0x16},
	[0x17] = {["name"] = "Add Object/Vehicle", ["decode"] = decode_0x17},
	[0x18] = {["name"] = "Mob Spawn", ["decode"] = decode_0x18},
	[0x19] = {["name"] = "Painting", ["decode"] = decode_0x19},
	[0x1A] = {["name"] = "Experience Orb", ["decode"] = decode_0x1A},
	[0x1B] = {["name"] = "Stance update", ["decode"] = decode_0x1B},
	[0x1C] = {["name"] = "Entity Velocity", ["decode"] = decode_0x1C},
	[0x1D] = {["name"] = "Destroy Entity", ["decode"] = decode_0x1D},
	[0x1E] = {["name"] = "Entity", ["decode"] = decode_0x1E},
	[0x1F] = {["name"] = "Entity Relative Move", ["decode"] = decode_0x1F},
	[0x20] = {["name"] = "Entity Look", ["decode"] = decode_0x20},
	[0x21] = {["name"] = "Entity Look and Relative Move", ["decode"] = decode_0x21},
	[0x22] = {["name"] = "Entity Teleport", ["decode"] = decode_0x22},
	[0x26] = {["name"] = "Entity Status", ["decode"] = decode_0x26},
	[0x27] = {["name"] = "Attach Entity", ["decode"] = decode_0x27},
	[0x28] = {["name"] = "Entity Metadata", ["decode"] = decode_0x28},
	[0x29] = {["name"] = "Entity Effect", ["decode"] = decode_0x29},
	[0x2A] = {["name"] = "Remove Entity Effect", ["decode"] = decode_0x2A},
	[0x2B] = {["name"] = "Experience", ["decode"] = decode_0x2B},
	[0x32] = {["name"] = "Pre-Chunk", ["decode"] = decode_0x32},
	[0x33] = {["name"] = "Map Chunk", ["decode"] = decode_0x33},
	[0x34] = {["name"] = "Multi Block Change", ["decode"] = decode_0x34},
	[0x35] = {["name"] = "Block Change", ["decode"] = decode_0x35},
	[0x36] = {["name"] = "Block Action", ["decode"] = decode_0x36},
	[0x3C] = {["name"] = "Explosion", ["decode"] = decode_0x3C},
	[0x3D] = {["name"] = "Sound/particle effect", ["decode"] = decode_0x3D},
	[0x46] = {["name"] = "New/Invalid State", ["decode"] = decode_0x46},
	[0x47] = {["name"] = "Thunderbolt", ["decode"] = decode_0x47},
	[0x64] = {["name"] = "Open window", ["decode"] = decode_0x64},
	[0x65] = {["name"] = "Close window", ["decode"] = decode_0x65},
	[0x66] = {["name"] = "Window click", ["decode"] = decode_0x66},
	[0x67] = {["name"] = "Set slot", ["decode"] = decode_0x67},
	[0x68] = {["name"] = "Window items", ["decode"] = decode_0x68},
	[0x69] = {["name"] = "Update window property", ["decode"] = decode_0x69},
	[0x6A] = {["name"] = "Transaction", ["decode"] = decode_0x6A},
	[0x6B] = {["name"] = "Creative inventory action", ["decode"] = decode_0x6B},
	[0x6C] = {["name"] = "Enchant Item", ["decode"] = decode_0x6C},
	[0x82] = {["name"] = "Update Sign", ["decode"] = decode_0x82},
	[0x83] = {["name"] = "Item Data", ["decode"] = decode_0x83},
	[0xC8] = {["name"] = "Increment Statistic", ["decode"] = decode_0xC8},
	[0xC9] = {["name"] = "Player List Item", ["decode"] = decode_0xC9},
	[0xFE] = {["name"] = "Server List Ping", ["decode"] = decode_0xFE},
	[0xFF] = {["name"] = "Disconnect/Kick", ["decode"] = decode_0xFF},
}

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

