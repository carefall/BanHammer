package me.carefall.banhammer.listeners;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.persistence.PersistentDataType;
import org.spigotmc.event.entity.EntityDismountEvent;

import me.carefall.banhammer.BanHammer;

public class BanDismountListener implements Listener {
	
	private NamespacedKey banKey;

	public BanDismountListener(BanHammer plugin) {
		banKey = new NamespacedKey(plugin, "banned");
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onDismount(EntityDismountEvent event) {
		if (!(event.getDismounted() instanceof Player player)) return;
		if (player.getPersistentDataContainer().has(banKey, PersistentDataType.INTEGER)) event.setCancelled(true);
	}

}
