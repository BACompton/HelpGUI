package d3ath5643.HelpGUI.util;

public class MenuView {
    public enum MenuState
    {
        RESULTS, PLUGIN_OVERVIEW, COMMAND_OVERVIEW, PERMISSION_OVERVIEW;
    }
    
    private MenuState state;
    private String details;
    private int pageNumber;
    
    public MenuView(MenuState state, String details)
    {
        this(state, details, 0);
    }
    
    public MenuView(MenuState state, String details, int pageNumber)
    {
        this.state = state;
        this.details = details;
        this.pageNumber = pageNumber;
    }
    
    public MenuState getState()
    {
        return state;
    }
    
    public String getDetails()
    {
        return details;
    }
    
    public int getPageNumber()
    {
        return pageNumber;
    }
}
