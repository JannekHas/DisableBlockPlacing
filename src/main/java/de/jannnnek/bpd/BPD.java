package de.jannnnek.bpd;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class BPD extends JavaPlugin implements Listener {

    List<String> blocks;

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(this, this);
        blocks = this.getConfig().getStringList("blocks");
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        if (forbiddenPlace(e.getBlockReplacedState().getLocation(), e.getBlockReplacedState().getType())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onHangingPlace(HangingPlaceEvent e) {
        if (forbiddenPlace(e.getBlock().getLocation(), e.getBlock().getType())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onUpdate(BlockPhysicsEvent e) {
        if (e.getBlock().getType().equals(Material.CARPET)) {
            e.setCancelled(true);
        }
    }

    private boolean forbiddenPlace(Location blockLocation, Material material) {
        for (String s : blocks) {
            if (material.equals(Material.WATER) || material.equals(Material.LAVA) || material.equals(Material.STATIONARY_LAVA) || material.equals(Material.STATIONARY_WATER)) {
                return true;
            } else if (blockLocation.clone().subtract(0.0, 1.0, 0.0).getBlock().getType().toString().contains(s)) {
                return true;
            } else {
                Block[] adjacentBlocks = new Block[4];
                adjacentBlocks[0] = blockLocation.getBlock().getRelative(BlockFace.NORTH);
                adjacentBlocks[1] = blockLocation.getBlock().getRelative(BlockFace.SOUTH);
                adjacentBlocks[2] = blockLocation.getBlock().getRelative(BlockFace.EAST);
                adjacentBlocks[3] = blockLocation.getBlock().getRelative(BlockFace.WEST);
                for (Block adjacentBlock : adjacentBlocks) {
                    if (adjacentBlock.getType() == Material.CARPET) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
