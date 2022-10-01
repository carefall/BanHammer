package me.carefall.banhammer.listeners;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

import java.util.UUID;

import org.bukkit.BanList.Type;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.persistence.PersistentDataType;

import me.carefall.banhammer.BanHammer;

public class QuitListener implements Listener {
	
	private NamespacedKey banKey, standKey;
	private BanHammer plugin;

	public QuitListener(BanHammer plugin) {
		banKey = new NamespacedKey(plugin, "banned");
		standKey = new NamespacedKey(plugin, "stand");
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		var player = event.getPlayer();
		if (player.getPersistentDataContainer().has(banKey, PersistentDataType.LONG)) {
			plugin.getServer().getScheduler().cancelTask(player.getPersistentDataContainer().get(banKey, PersistentDataType.INTEGER));
			player.getPersistentDataContainer().remove(banKey);
			plugin.getServer().getEntity(UUID.fromString(player.getPersistentDataContainer().get(standKey, PersistentDataType.STRING))).remove();
			player.getPersistentDataContainer().remove(standKey);
			plugin.getServer().getBanList(Type.NAME).addBan(player.getName(), "Pen is", null, null);
			Location banLoc = player.getLocation();
			var fw = (Firework) banLoc.getWorld().spawnEntity(banLoc, EntityType.FIREWORK);
			var meta = fw.getFireworkMeta();
			meta.setPower(16);
			meta.addEffect(FireworkEffect.builder().withColor(Color.AQUA).with(FireworkEffect.Type.BALL).build());
			fw.setFireworkMeta(meta);
			fw.detonate();
			// I DON'T DESTROY PEN IS
		}
	}

}
