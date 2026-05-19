mc = Proto('mc', 'Minecraft 1.0.0')

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
