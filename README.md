# GWorld
GWorld offers a comprehensive system to manage your worlds.


## Description​


The plugin manages your worlds. You can import, create, recreate, clone, set attributes (for example MobSpawning) and delete worlds. Furthermore, you can customize all messages according to your wishes. We provide a German and an English language file. In addition, all commands can be tabbed in the console as well as in user chat, as long as you have the permission of the specific command.


## Commands:​


/gcreate <world name> <world type> {seed} – create new worlds. A world can also be created based on a seed

/gimport <world name> <world type> – import unloaded worlds

/gclone <world name> <target world name> <world type> – clone worlds

/gset <world name> <attribute> <value> – set attributes for different worlds
  
Verfügbare Attribute von /gset:

    timeCycle – toggle day and night cycle
    time – default time when the day and night cycle is disabled
    weatherCycle – toggle weather cycle (storm, sun, rain)
    weather – default weather when the weather cycle is disabled
    pvp – toggle pvp for each world
    mobs – toggle spawning of monsters
    animals – toggle spawning of animals
    forcedGamemode – toggle setting a default gamemode for all players of a world
    defaultGamemode – default gamemode when forcedGamemode is activated

/gdelete <world name> – delete worlds with their folders

/gworlds – list all loaded worlds; by clicking on a world name, you will be teleported there.

/gwreload – reload all configurations

/gtp – Teleport yourself to a world

/ginfo – List all world settings on

/grecreate – Create a world based on an old world new

/gload - Load worlds that are already imported

/gunload - Unload imported worlds.


## Permissions​


#### gworld.* – get all permissions

/gcreate – Gworld.create

/gimport – Gworld.import

/ginfo – Gworld.info

/gtp – Gworld.teleport

/gset – Gworld.set

/gclone – Gworld.clone

/gdelete – Gworld.delete

/gworlds – Gworld.worlds

/gwrelaod – Gworld.reload

/grecreate – Gworld.recreate

/gload - Gworld.load

/gunload - Gworld.unload




###### Copyright © 2020-2021 Gilljan. All rights reserved.
