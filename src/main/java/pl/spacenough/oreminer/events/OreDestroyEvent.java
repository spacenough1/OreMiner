package pl.spacenough.oreminer.events;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import pl.spacenough.oreminer.OreMiner;
import pl.spacenough.oreminer.utils.*;

import java.util.*;

public class OreDestroyEvent implements Listener {
  private static final int DEFAULT_CLUSTER_RADIUS = 5;
  private static final BlockFace[] NEIGHBOR_FACES = new BlockFace[]{
    BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST, BlockFace.UP, BlockFace.DOWN
  };

  private record ExpAndCount(int totalExp, int blocksBroken) {
  }

  @EventHandler
  public void onOreBreak(@NotNull BlockBreakEvent event) {
    final Player player = event.getPlayer();
    final Block block = event.getBlock();
    final ItemStack tool = player.getInventory().getItemInMainHand();

    if (!isEligibleOreBreak(block, tool)) {
      return;
    }

    event.setCancelled(true);
    final Material oreType = block.getType();
    final List<Location> oreLocations = findConnectedOres(block, DEFAULT_CLUSTER_RADIUS);

    ExpAndCount stats = calculateClusterExpAndCount(oreLocations, oreType);
    int remainingExp = handleToolDurabilityAndMending(player, tool, stats.blocksBroken, stats.totalExp);

    processOreDrops(player, oreLocations, oreType, tool);

    if (remainingExp > 0) {
      InventoryUtil.dropExperience(player.getLocation(), remainingExp);
    }
  }

  private boolean isEligibleOreBreak(@NotNull Block block, @NotNull ItemStack tool) {
    return OresUtil.isOre(block)
      && !OresUtil.isOreBlackListed(block)
      && block.isPreferredTool(tool);
  }

  @Contract("_, _ -> new")
  private @NotNull ExpAndCount calculateClusterExpAndCount(@NotNull List<Location> oreLocations, @NotNull Material oreType) {
    int totalExp = 0;
    int blocksBroken = 0;
    for (Location loc : oreLocations) {
      Block currentBlock = loc.getBlock();
      if (currentBlock.getType() != oreType) continue;
      totalExp += OresUtil.getExperienceForOre(oreType);
      blocksBroken++;
    }
    return new ExpAndCount(totalExp, blocksBroken);
  }

  private int handleToolDurabilityAndMending(@NotNull Player player, @NotNull ItemStack tool, int blocksBroken, int totalExp) {
    return ToolUtil.handleToolDurabilityAndMending(player, tool, blocksBroken, totalExp);
  }

  private void processOreDrops(@NotNull Player player,
                               @NotNull List<Location> oreLocations,
                               @NotNull Material oreType,
                               @NotNull ItemStack tool) {
    for (Location loc : oreLocations) {
      Block currentBlock = loc.getBlock();
      if (currentBlock.getType() != oreType) {
        continue;
      }
      List<ItemStack> drops = OresUtil.calculateOreDrops(oreType, tool);
      for (ItemStack item : drops) {
        if (OreMiner.getConfigManager().isItemMagnet()) {
          ItemStack leftover = InventoryUtil.addToInventoryOrReturnLeftover(player, item);
          if (leftover != null) {
            dropItem(loc, leftover);
          }
        } else {
          dropItem(loc, item);
        }
      }
      currentBlock.setType(Material.AIR);
    }
  }

  private void dropItem(@NotNull Location location, @NotNull ItemStack itemStack) {
    location.getWorld().dropItem(location, itemStack);
  }

  private @NotNull List<Location> findConnectedOres(Block startBlock, int maxRadius) {
    if (maxRadius <= 0) maxRadius = 1;
    List<Location> matchingBlocks = new ArrayList<>();
    Material oreType = startBlock.getType();
    Queue<Location> toCheck = new LinkedList<>();
    Set<Location> checked = new HashSet<>();
    toCheck.add(startBlock.getLocation());
    checked.add(startBlock.getLocation());
    while (!toCheck.isEmpty()) {
      Location current = toCheck.poll();
      matchingBlocks.add(current);
      for (BlockFace face : NEIGHBOR_FACES) {
        Location adjacent = current.clone().add(face.getModX(), face.getModY(), face.getModZ());
        if (checked.contains(adjacent)) continue;
        checked.add(adjacent);
        if (!isWithinRadius(adjacent, startBlock, maxRadius)) continue;
        Block adjacentBlock = adjacent.getBlock();
        if (adjacentBlock.getType() == oreType) {
          toCheck.add(adjacent);
        }
      }
    }
    return matchingBlocks;
  }

  private boolean isWithinRadius(@NotNull Location candidate, @NotNull Block origin, int maxRadius) {
    int dx = Math.abs(candidate.getBlockX() - origin.getX());
    int dy = Math.abs(candidate.getBlockY() - origin.getY());
    int dz = Math.abs(candidate.getBlockZ() - origin.getZ());
    return Math.max(Math.max(dx, dy), dz) <= maxRadius;
  }
}