package pl.spacenough.oreminer.config;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import pl.spacenough.oreminer.OreMiner;

import java.util.List;

public class ConfigManager {
  private final OreMiner plugin;
  private FileConfiguration config;

  public ConfigManager(@NotNull OreMiner plugin) {
    this.plugin = plugin;
    plugin.saveDefaultConfig();
    this.config = plugin.getConfig();
  }

  private List<String> disabledOres;
  private boolean  itemMagnet;

  public void loadConfig() {
    config = plugin.getConfig();
    disabledOres = config.getStringList("settings.disabled-ores");
    itemMagnet = config.getBoolean("settings.item-magnet");
  }

  public List<String> getDisabledOres() {
    return disabledOres;
  }

  public boolean isItemMagnet() {
    return itemMagnet;
  }
}
