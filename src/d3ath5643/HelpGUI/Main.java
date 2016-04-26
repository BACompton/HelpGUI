package d3ath5643.HelpGUI;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.plugin.java.JavaPlugin;

import d3ath5643.HelpGUI.commands.HelpGui;
import d3ath5643.HelpGUI.commands.Reload;
import d3ath5643.HelpGUI.commands.RepresentationCommand;
import d3ath5643.HelpGUI.listener.HelpMenuTransition;
import d3ath5643.HelpGUI.listener.RepresentionMenuListener;
import d3ath5643.HelpGUI.util.ConfigPath;
import d3ath5643.HelpGUI.util.HelpMenu;
import d3ath5643.HelpGUI.util.Util;

public class Main extends JavaPlugin
{
    private HashMap<UUID, HelpMenu> helpMenus;
    
    @Override
    public void onEnable()
    {
        createConfigs();
        registerCommands();
        registerListener();
        initlizeVariables();
    }
    
    @Override
    public void onDisable()
    {
        for(UUID uuid: helpMenus.keySet())
        {
            if(getServer().getPlayer(uuid) != null)
                getServer().getPlayer(uuid).closeInventory();
            helpMenus.remove(uuid);
        }
    }
    
    public boolean containsKey(UUID uuid)
    {
        return helpMenus.containsKey(uuid);
    }
    
    public void remove(UUID uuid)
    {
        helpMenus.remove(uuid);
    }
    
    public void put(UUID uuid, HelpMenu helpMenu)
    {
        helpMenus.put(uuid, helpMenu);
    }
    
    public HelpMenu get(UUID uuid)
    {
        if(helpMenus.containsKey(uuid))
            return helpMenus.get(uuid);
        return null;
    }

    private void initlizeVariables() {
        helpMenus = new HashMap<UUID, HelpMenu>();
    }

    public void createConfigs() {
        File config = new File("plugins/HelpGUI/config.yml");
        if(!config.exists())
        {
            getConfig().options().copyDefaults(true);
            saveConfig();
        }
        
        if(getConfig().getInt(ConfigPath.HELP_MENU_ROWS.getPath()) < Util.MIN_ROWS)
        {
            getConfig().set(ConfigPath.HELP_MENU_ROWS.getPath(), Util.MIN_ROWS);
            reloadConfig();
        }
    }

    private void registerCommands() {
        //TODO: add recipehelp, representations, sort each page
        getCommand("helpgui").setExecutor(new HelpGui(this));
        getCommand("helpguireload").setExecutor(new Reload(this));
        getCommand("helpguirepresentation").setExecutor(new RepresentationCommand(this));
    }
    
    private void registerListener()
    {
        new HelpMenuTransition(this);
        new RepresentionMenuListener(this);
    }
    
}
