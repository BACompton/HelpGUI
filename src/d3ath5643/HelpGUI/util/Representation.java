package d3ath5643.HelpGUI.util;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum Representation {
    PLUGIN_OVERVIEW("PLUGIN_OVERVIEW", "Used on Help Results Pages.", Material.STAINED_GLASS_PANE, (byte)10, "representation.plugin.overview", ChatColor.DARK_PURPLE, "", ChatColor.LIGHT_PURPLE, "No Description", 35, true),
    PLUGIN_NAME("PLUGIN_NAME", "Used on Plugin Overviews.", Material.STAINED_GLASS_PANE, (byte)10, "representation.plugin.name", ChatColor.DARK_PURPLE, "", ChatColor.LIGHT_PURPLE, "", 35),
    PLUGIN_DESCRIPTION("PLUGIN_DESCRIPTION", "Used on Plugin Overviews.", Material.STAINED_GLASS_PANE, (byte)2, "representation.plugin.description", ChatColor.DARK_PURPLE, " ", ChatColor.LIGHT_PURPLE, "No Description", 35, false),
    
    COMMAND_OVERVIEW("COMMAND_OVERVIEW", "Used on Help Results Pages and Plugin Overviews.", Material.STAINED_GLASS_PANE, (byte)11, "representation.command.overview", ChatColor.DARK_BLUE, "", ChatColor.BLUE, "No Description", 35, true),
    COMMAND_NAME("COMMAND_NAME", "Used on Command Overviews.", Material.STAINED_GLASS_PANE, (byte)11, "representation.command.name", ChatColor.DARK_BLUE, " ", ChatColor.BLUE, "Unknown Usage", 35),
    COMMAND_DESCRIPTION("COMMAND_DESCRIPTION", "Used on Command Overviews.", Material.STAINED_GLASS_PANE, (byte)3, "representation.command.description", ChatColor.DARK_BLUE, " ", ChatColor.BLUE, "No Description", 35, false),
    COMMAND_ALIAS("COMMAND_ALIAS", "Used on Command Overviews.", Material.STAINED_GLASS_PANE, (byte)9, "representation.command.alias", ChatColor.DARK_AQUA, "", ChatColor.AQUA, "", 35),
    
    PERMISSION_OVERVIEW("PERMISSION_OVERVIEW", "Used on Help Results Pages, Plugin Overviews, and Command Overviews.", Material.STAINED_GLASS_PANE, (byte)13, "representation.permission.overview", ChatColor.DARK_GREEN, "", ChatColor.GREEN, "No Description", 35, true),
    PERMISSION_NAME("PERMISSION_NAME", "Used on Permission Overviews", Material.STAINED_GLASS_PANE, (byte)13, "representation.permission.name", ChatColor.DARK_GREEN, "", ChatColor.GREEN, "", 35),
    PERMISSION_DESCRIPTION("PERMISSION_DESCRIPTION", "Used on Permission Overviews", Material.STAINED_GLASS_PANE, (byte)5, "representation.permission.description", ChatColor.DARK_GREEN, " ", ChatColor.GREEN, "No Description", 35, false),
    PERMISSION_CHILD("PERMISSION_CHILD", "Used on Permission Overviews", Material.STAINED_GLASS_PANE, (byte)4, "representation.permission.child", ChatColor.GOLD, "", ChatColor.YELLOW, "No Description", 35, true),
    
    SHAPELESS("SHAPELESS", "Used on Recipe Results pages.", Material.STAINED_GLASS_PANE, (byte)14, "representation.recipe.shapeless", ChatColor.DARK_RED, "", ChatColor.RED, "", 35, true),
    SHAPED("SHAPED", "Used on Recipe Results pages.", Material.STAINED_GLASS_PANE, (byte)4, "representation.recipe.shaped", ChatColor.GOLD, "", ChatColor.YELLOW, "", 35, true),
    FURNACE("FURNACE", "Used on Recipe Results pages.", Material.STAINED_GLASS_PANE, (byte)8, "representation.recipe.furnace", ChatColor.DARK_GRAY, "", ChatColor.GRAY, "", 35, true),
    
    FILLER("FILLER", "Used on when ever empty slots are left.", Material.STAINED_GLASS_PANE, (byte)7, "representation.button.filler", ChatColor.DARK_GRAY, " "),
    FILLER_MENU("FILLER_MENU", "Used on the Navigation Bar(Bottom Bar)", Material.STAINED_GLASS_PANE, (byte)15, "representation.button.fillerMenu", ChatColor.DARK_GRAY, " "),
    NEXT_PAGE("NEXT_PAGE", "Used on the Navigation Bar(Bottom Bar)", Material.INK_SACK, (byte)6, "representation.button.nextPage", ChatColor.DARK_AQUA, "Next Page", ChatColor.AQUA, "Click to go to the Next page.", 35),
    PREVIOUS_PAGE("PREVIOUS_PAGE", "Used on the Navigation Bar(Bottom Bar)", Material.INK_SACK, (byte)12, "representation.button.previousPage", ChatColor.DARK_AQUA, "Previous Page", ChatColor.AQUA, "Click to go to the previous page.", 35),
    PREVIOUS_MENU("PREVIOUS_MENU", "Used on the Navigation Bar(Bottom Bar)", Material.INK_SACK, (byte)1, "representation.button.previousMenu", ChatColor.DARK_RED, "Previous Menu", ChatColor.RED, "Click to got to the previous menu.", 35);
    
    private Material mat;
    private byte data;
    private String path, name, lore, type, use;
    private int lineLength;
    private ChatColor nameColor, lineColor;
    private boolean overview;
    
    private Representation(String type, String use, Material mat, byte data, String path)
    {
        this(type, use, mat, data, path, ChatColor.WHITE, "", ChatColor.GRAY, "", 35, false);
    }
    
    private Representation(String type, String use, Material mat, byte data, String path, ChatColor nameColor, String name)
    {
        this(type, use, mat, data, path, nameColor, name, ChatColor.GRAY, "", 35, false);
    }
    
    private Representation(String type, String use, Material mat, byte data, String path, ChatColor nameColor, String name, ChatColor lineColor, String lore, int lineLength)
    {
        this(type, use, mat, data, path, nameColor, name, lineColor, lore, lineLength, false);
    }
    
    private Representation(String type, String use, Material mat, byte data, String path, ChatColor nameColor, String name, ChatColor lineColor, String lore, int lineLength, boolean overview)
    {
        this.type = type;
        this.use = use;
        this.mat = mat;
        this.data = data;
        this.path = path;
        this.name = name;
        this.lore = lore;
        this.lineLength = lineLength;
        this.nameColor = nameColor;
        this.lineColor = lineColor;
        this.overview = overview;
    }
    
    public ItemStack getDefaultItemStack()
    {
        return getDefaultItemStack(1, (short)1);
    }
    
    public ItemStack getDefaultItemStack(int amount)
    {
        return getDefaultItemStack(amount, (short)1);
    }
    
    @SuppressWarnings("deprecation")
    public ItemStack getDefaultItemStack(int amount, short damage)
    {
        return new ItemStack(mat, amount, damage, data);
    }
    
    public String getPath()
    {
        return path;
    }
    
    public String getDefaultName()
    {
        return name;
    }
    
    public String getDefaultLore()
    {
        return lore;
    }
    
    public int getDefaultLineLength()
    {
        return lineLength;
    }
    
    public ChatColor getDefaultNameColor()
    {
        return nameColor;
    }
    
    public ChatColor getDefaultLineColor()
    {
        return lineColor;
    }
    
    public boolean isOverview()
    {
        return overview;
    }
    
    public String getType()
    {
        return type;
    }
    
    public String getUse()
    {
        return use;
    }
}
