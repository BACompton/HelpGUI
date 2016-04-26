package d3ath5643.HelpGUI.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.inventory.ItemStack;

public class HelpMenu {
    private Map<String, List<ItemStack>> pluginCommands,
                                         pluginPermissions;
    private List<MenuView> views;
    private List<ItemStack> results;
    
    public HelpMenu()
    {
        pluginCommands = new HashMap<String, List<ItemStack>>();
        pluginPermissions = new HashMap<String, List<ItemStack>>();
        results = new ArrayList<ItemStack>();
        views = new ArrayList<MenuView>();
    }
    
    public void setPluginCommands(Map<String, List<ItemStack>> pluginCommands)
    {
        this.pluginCommands = pluginCommands;
    }
    
    public void setPluginPermissions(Map<String, List<ItemStack>> pluginPermissions)
    {
        this.pluginPermissions = pluginPermissions;
    }
    
    public void setResults(List<ItemStack> results)
    {
        this.results = results;
    }
    
    public List<ItemStack> getPluginCommands(String pluginName)
    {
        if(pluginCommands.containsKey(pluginName))
            return pluginCommands.get(pluginName);
        return null;
    }
    
    public List<ItemStack> getPluinPermissions(String pluginName)
    {
        if(pluginPermissions.containsKey(pluginName))
            return pluginPermissions.get(pluginName);
        return null;
    }
    
    public List<ItemStack> getResults()
    {
        return results;
    }
    
    public List<String> getPlugins()
    {
        ArrayList<String> plugins = new ArrayList<String>();
        
        for(String str: pluginCommands.keySet())
            if(!plugins.contains(str))
                plugins.add(str);
        
        for(String str: pluginPermissions.keySet())
            if(!plugins.contains(str))
                plugins.add(str);
        
        return plugins;
    }
    
    public void addView(MenuView view)
    {
        views.add(view);
    }
    
    public MenuView removeCurrView()
    {
        return views.remove(views.size()-1);
    }
    
    public MenuView getLastView()
    {
        if(hasPreviousMenu())
            return views.get(views.size()-2);
        return null;
    }
    
    public MenuView getCurrView()
    {
        if(!views.isEmpty())
            return views.get(views.size()-1);
        return null;
    }
    
    public boolean hasPreviousMenu()
    {
        return views.size() > 1;
    }
    
    public int getViewSize()
    {
        return views.size();
    }
    
}
