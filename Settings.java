/**
 * Represents one settings object.
 * @author istvan_vig
 *
 */
public class Settings {
	public String profileName;
    public String harvestFiletype;
    public Integer logLevel;
    public Integer displayUi;
    public String rootDir;
    
    public Settings(String profile, String filetypes, Integer log_level, Integer display_ui, String root_dir) {
        profileName = profile;
        harvestFiletype = filetypes;
        logLevel = log_level;
        displayUi = display_ui;
        rootDir = root_dir;
    }
    
    public Settings() {
    	profileName = "";
    	harvestFiletype = "";
    	logLevel = 1;
    	displayUi = 1;
    	rootDir = "";
    }
}
