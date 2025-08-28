package pl.spacenough.oreminer;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import pl.spacenough.oreminer.config.ConfigManager;
import pl.spacenough.oreminer.events.OreDestroyEvent;
import pl.spacenough.oreminer.utils.OresUtil;

public class OreMiner extends JavaPlugin {

  private final String VERSION = "1.0.0 - Stable";
  private OreMiner plugin;
  private static ConfigManager configManager;

  @Override
  public void onEnable() {
    plugin = this;
    configManager = new ConfigManager(this);
    configManager.loadConfig();

    OresUtil.setConfigManager(configManager);

    registerEvents();

    getComponentLogger().info("--- Current Settings ---");
    getComponentLogger().info("--- Version: {} ---", VERSION);
    getComponentLogger().info("--- Disabled ores: {} ---", configManager.getDisabledOres());
    getComponentLogger().info("--- Item Magnet: {} ---", configManager.isItemMagnet() ? "Enabled" : "Disabled");

  }

  public void registerEvents() {
    PluginManager pm = Bukkit.getPluginManager();
    pm.registerEvents(new OreDestroyEvent(), this);
  }

  public OreMiner getPlugin() {
    return plugin;
  }

  public static ConfigManager getConfigManager() {
    return configManager;
  }
}
