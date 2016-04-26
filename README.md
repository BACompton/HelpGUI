# HelpGUI
A spigot plugin that creates a graphical interface for the help command.
This plugin also displays the player's current permissions as well as detailed information about each item. 

__Warning:__ If other plugins do not properly register commands or plugins, you will need to add include them in HelpGUI's config file.

Once finished, a link to the pre-complied version will be linked _here_.

HelpGUI allows a player to do the following:
* Search plugins, commands, and permissions with regex
* Displays detailed infromation about accessible commands and permissions
* Categorize accessible commands and permissions by the plugin

### Permissions
* HelpGUI.*:
  * HelpGUI.command.*:
    * HelpGUI.command.reload: Grants access to HelpGUI's reload command.
    * HelpGUI.command.helpgui: Grants access to help's graphical interface.
    * HelpGUI.command.representation: Grants access to the HelpGUI's representation command.

### Commands
* helpgui: Help's graphical interface
  * usage: /helpgui [search]
  * permission: HelpGUI.command.helpgui
* helpguireload:
  * usage: /helpguireload
  * permission: HelpGUI.command.reload
  * aliases: hguireload
* helpguirepresentation:
  * usage: /helpguirepresentation
  * permission: HelpGUI.command.representation
  * aliases: hguirepresentation, helprepresentation
