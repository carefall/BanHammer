package me.carefall.banhammer.listeners;

import org.bukkit.NamespacedKey;
import org.bukkit.BanList.Type;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.persistence.PersistentDataType;

import me.carefall.banhammer.BanHammer;

public class QuitListener implements Listener {
	
	private NamespacedKey banKey;
	private BanHammer plugin;

	public QuitListener(BanHammer plugin) {
		banKey = new NamespacedKey(plugin, "banned");
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		var player = event.getPlayer();
		if (player.getPersistentDataContainer().has(banKey, PersistentDataType.LONG)) {
			plugin.getServer().getScheduler().cancelTask(player.getPersistentDataContainer().get(banKey, PersistentDataType.INTEGER));
			player.getPersistentDataContainer().remove(banKey);
			plugin.getServer().getBanList(Type.NAME).addBan(player.getName(), "Pen is", null, null);
		}
	}

}
