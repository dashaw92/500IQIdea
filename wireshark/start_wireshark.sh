#!/bin/bash
wireshark -Y"mc" -X lua_script:mcproto.lua "$@"
