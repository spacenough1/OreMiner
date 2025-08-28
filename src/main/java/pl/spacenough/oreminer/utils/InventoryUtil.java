package pl.spacenough.oreminer.utils;

import org.bukkit.Location;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class InventoryUtil {
  public static final int INVENTORY_HOTBAR_AND_MAIN_SIZE = 36;

  public static @Nullable ItemStack addToInventoryOrReturnLeftover(@NotNull Player player, ItemStack item) {
    PlayerInventory inventory = player.getInventory();
    for (int i = 0; i < INVENTORY_HOTBAR_AND_MAIN_SIZE; i++) {
      ItemStack invItem = inventory.getItem(i);
      if (invItem != null && invItem.isSimilar(item) && invItem.getAmount() < invItem.getMaxStackSize()) {
        int space = invItem.getMaxStackSize() - invItem.getAmount();
        int amountToAdd = Math.min(space, item.getAmount());
        invItem.setAmount(invItem.getAmount() + amountToAdd);
        item.setAmount(item.getAmount() - amountToAdd);
        if (item.getAmount() <= 0) {
          return null;
        }
      }
    }
    for (int i = 0; i < INVENTORY_HOTBAR_AND_MAIN_SIZE; i++) {
      if (inventory.getItem(i) == null) {
        inventory.setItem(i, item);
        return null;
      }
    }
    return item;
  }

  public static void dropExperience(@NotNull Location location, int amount) {
    while (amount > 0) {
      int orbSize = Math.min(amount, 10);
      location.getWorld().spawn(location, ExperienceOrb.class).setExperience(orbSize);
      amount -= orbSize;
    }
  }
}
