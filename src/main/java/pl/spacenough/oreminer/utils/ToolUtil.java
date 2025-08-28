package pl.spacenough.oreminer.utils;

import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

public class ToolUtil {
  public static int calculateDurabilityCost(@NotNull ItemStack tool, int blocksBroken) {
    int unbreakingLevel = tool.getEnchantmentLevel(Enchantment.UNBREAKING);
    int durabilityCost = 0;
    for (int i = 0; i < blocksBroken; i++) {
      if (ThreadLocalRandom.current().nextDouble() < (1.0 / (unbreakingLevel + 1))) {
        durabilityCost++;
      }
    }
    return durabilityCost;
  }

  public static int handleToolDurabilityAndMending(@NotNull Player player, @NotNull ItemStack tool, int blocksBroken, int totalExp) {
    if (tool.getType().getMaxDurability() <= 0) {
      return totalExp;
    }

    int durabilityCost = calculateDurabilityCost(tool, blocksBroken);

    if (tool.containsEnchantment(Enchantment.MENDING) && tool.getDurability() > 0) {
      int maxRepairableDurability = tool.getType().getMaxDurability() - tool.getDurability();
      int xpNeededForRepair = (int) Math.ceil(maxRepairableDurability / 2.0);
      int xpForRepair = Math.min(totalExp, xpNeededForRepair);
      if (xpForRepair > 0) {
        short newDurability = (short) (tool.getDurability() - (xpForRepair * 2));
        tool.setDurability(newDurability);
        totalExp -= xpForRepair;
        if (tool.getDurability() + durabilityCost < tool.getType().getMaxDurability()) {
          durabilityCost = Math.max(0, durabilityCost - (xpForRepair * 2));
        }
      }
    }

    if (durabilityCost > 0) {
      short newDurability = (short) (tool.getDurability() + durabilityCost);
      if (newDurability >= tool.getType().getMaxDurability()) {
        player.getInventory().setItemInMainHand(null);
        player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
      } else {
        tool.setDurability(newDurability);
      }
    }

    return totalExp;
  }
}
