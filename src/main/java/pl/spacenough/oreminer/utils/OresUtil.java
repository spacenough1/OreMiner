package pl.spacenough.oreminer.utils;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import pl.spacenough.oreminer.config.ConfigManager;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class OresUtil {
  private static ConfigManager configManager;

  public static boolean isOreBlackListed(@NotNull Block block) {
    Material blockMaterial = block.getType();
    List<Material> blacklisted = getBlackListedOres();

    return blacklisted.contains(blockMaterial);
  }

  public static boolean isOre(@NotNull Block block) {
    Material blockMaterial = block.getType();
    return blockMaterial.toString().contains("_ORE");
  }

  private static @NotNull @Unmodifiable List<Material> getBlackListedOres() {
    List<String> disabledOres = configManager.getDisabledOres();
    return disabledOres.stream()
      .map(Material::getMaterial)
      .filter(Objects::nonNull)
      .toList();
  }

  public static int getExperienceForOre(@NotNull Material ore) {
    return switch (ore) {
      case COAL_ORE, DEEPSLATE_COAL_ORE -> ThreadLocalRandom.current().nextInt(0, 3);
      case DIAMOND_ORE, DEEPSLATE_DIAMOND_ORE, EMERALD_ORE, DEEPSLATE_EMERALD_ORE ->
        ThreadLocalRandom.current().nextInt(3, 8);
      case IRON_ORE, DEEPSLATE_IRON_ORE, GOLD_ORE, DEEPSLATE_GOLD_ORE, COPPER_ORE, DEEPSLATE_COPPER_ORE,
           NETHER_GOLD_ORE -> ThreadLocalRandom.current().nextInt(0, 2);
      case LAPIS_ORE, DEEPSLATE_LAPIS_ORE, NETHER_QUARTZ_ORE -> ThreadLocalRandom.current().nextInt(2, 6);
      case REDSTONE_ORE, DEEPSLATE_REDSTONE_ORE -> ThreadLocalRandom.current().nextInt(1, 6);
      case ANCIENT_DEBRIS -> 2;
      default -> 0;
    };
  }

  public static @NotNull List<ItemStack> calculateOreDrops(Material ore, ItemStack tool) {
    List<ItemStack> drops = new ArrayList<>();

    if (tool != null && tool.containsEnchantment(Enchantment.SILK_TOUCH)) {
      drops.add(new ItemStack(ore));
      return drops;
    }

    int fortuneLevel = (tool != null) ? tool.getEnchantmentLevel(Enchantment.FORTUNE) : 0;

    switch (ore) {
      case DIAMOND_ORE,DEEPSLATE_DIAMOND_ORE:
        drops.add(new ItemStack(Material.DIAMOND, calculateFortuneDrops(fortuneLevel)));
        break;

      case COAL_ORE,DEEPSLATE_COAL_ORE:
        drops.add(new ItemStack(Material.COAL, calculateFortuneDrops(fortuneLevel)));
        break;

      case EMERALD_ORE,DEEPSLATE_EMERALD_ORE:
        drops.add(new ItemStack(Material.EMERALD, calculateFortuneDrops(fortuneLevel)));
        break;

      case NETHER_QUARTZ_ORE:
        drops.add(new ItemStack(Material.QUARTZ, calculateFortuneDrops(fortuneLevel)));
        break;

      case ANCIENT_DEBRIS:
        drops.add(new ItemStack(Material.ANCIENT_DEBRIS, calculateFortuneDrops(fortuneLevel)));
        break;

      case IRON_ORE,DEEPSLATE_IRON_ORE:
        drops.add(new ItemStack(Material.RAW_IRON, calculateFortuneDrops(fortuneLevel)));
        break;

      case GOLD_ORE,DEEPSLATE_GOLD_ORE:
        drops.add(new ItemStack(Material.RAW_GOLD, calculateFortuneDrops(fortuneLevel)));
        break;

      case COPPER_ORE, DEEPSLATE_COPPER_ORE:
        drops.add(new ItemStack(Material.RAW_COPPER, calculateFortuneDrops(fortuneLevel)));
        break;

      case REDSTONE_ORE, DEEPSLATE_REDSTONE_ORE:
        int redstoneBase = ThreadLocalRandom.current().nextInt(4, 6);
        int redstoneBonus = ThreadLocalRandom.current().nextInt(0, fortuneLevel + 1);
        drops.add(new ItemStack(Material.REDSTONE, redstoneBase + redstoneBonus));
        break;

      case LAPIS_ORE, DEEPSLATE_LAPIS_ORE:
        int lapisBase = ThreadLocalRandom.current().nextInt(4, 9);
        int lapisBonus = ThreadLocalRandom.current().nextInt(0, fortuneLevel + 1);
        drops.add(new ItemStack(Material.LAPIS_LAZULI, lapisBase + lapisBonus));
        break;

      case NETHER_GOLD_ORE:
        int goldBase = ThreadLocalRandom.current().nextInt(2, 7);
        int goldBonus = ThreadLocalRandom.current().nextInt(0, fortuneLevel + 1);
        drops.add(new ItemStack(Material.GOLD_NUGGET, goldBase + goldBonus));
        break;

      default:
        drops.add(new ItemStack(ore));
    }

    return drops;
  }

  private static int calculateFortuneDrops(int fortuneLevel) {
    if (fortuneLevel <= 0) return 1;

    int randomValue = ThreadLocalRandom.current().nextInt(0, fortuneLevel + 2);
    return Math.max(1, randomValue);
  }

  public static void setConfigManager(ConfigManager configManager) {
    OresUtil.configManager = configManager;
  }
}

