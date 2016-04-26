package d3ath5643.HelpGUI.util;

public enum ConfigPath {
    HELP_MENU_NAME("helpMenu.name"),
    HELP_MENU_ROWS("helpMenu.rows"),
    HELP_MENU_NON_PLAYER_MSG("helpMenu.nonPlayerMessage"),
    
    REPRESENTATION_MENU_NAME("representationMenu.name"),
    REPRESENTATION_MENU_NON_PLAYER_MSG("representationMenu.nonPlayerMessage"),
    
    BASE("base"),
    
    PLUGIN_ALIAS(".aliases"),
    PLUGIN_COMMANDS(".commands"),
    PLUGIN_PERMISSIONS(".permissions"),
    
    //Special Cases
    PERMISSION_DESCRIPTION(".description"),
    PERMISSION_CHILDREN(".children"),
    
    COMMAND_DESCRIPTION(".description"),
    COMMAND_PERMISSION(".permission"),
    COMMAND_USAGE(".usage"),
    COMMAND_ALIASES(".aliases"),
    
    BUKKIT_NAME("base.bukkit.name"),
    BUKKIT_DESCRIPTION("base.bukkit.description"),
    BUKKIT_ALIAS("base.bukkit.aliases"),
    
    MINECRAFT_NAME("base.minecraft.name"),
    MINECRAFT_DESCRIPTION("base.minecraft.description"),
    MINECRAFT_ALIAS("base.minecraft.aliases"),
    
    UNKNOWN_NAME("base.unknown.name"),
    UNKNOWN_DESCRIPTION("base.unknown.description"),
    UNKNOWN_ALIAS("base.unknown.aliases"),
    
    REPRESENTATION_MATERIAL(".material"),
    REPRESENTATION_DATA(".data"),
    REPRESENTATION_NAME_COLOR_CODE(".nameColorCode"),
    REPRESENTATION_LORE_COLOR_CODE(".loreColorCode"),
    REPRESENTATION_LORE_LINE_LENGTH(".loreLineLength"),
    REPRESENTATION_NAME(".name"),
    REPRESENTATION_LORE(".lore"),
    REPRESENTATION_OVERVIEW(".overview");
    
    private String name;
    
    private ConfigPath(String name)
    {
        this.name = name;
    }
    
    public String getPath()
    {
        return name;
    }
    
    @Override
    public String toString()
    {
        return name;
    }
}
