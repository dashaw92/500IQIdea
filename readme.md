### yo dawg I heard you liked Minecraft servers.

... so I wrote a Minecraft server in a Bukkit plugin for a Minecraft server :D

This plugin is for Minecraft release 1.0.

This plugin was originally just a proof of concept, but now I plan on writing an actual server with it. The goal is to write enough to support an API which can in turn be used to write a server in a plugin:

![Server topology diagram](./diagram.png)  
*Generated with [http://www.webgraphviz.com/](http://www.webgraphviz.com/)*

### Installing this plugin

Drop the plugin jar into your server's plugins folder, reload the server, and connect to the same IP as your server port `25065`.

Early testing version:

![im smart](./loool.png)

Working server list ping (`0xFE` Client -> Server, `0xFF` Server -> Client)

![Working server list ping to show status](./serverlist.png)

Logging into the server:

![logging in](./login.gif)

### For other devs

I built this against `craftbukkit-1.0.0-SNAPSHOT.jar`

### License

MIT, have fun.
