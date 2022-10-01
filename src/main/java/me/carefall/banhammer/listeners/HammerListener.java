package me.carefall.banhammer.listeners;

import org.bukkit.BanList.Type;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import me.carefall.banhammer.BanHammer;
import static me.carefall.banhammer.utils.Colorizer.colorize;

public class HammerListener implements Listener {
	
	private NamespacedKey key, banKey, standKey;
	private BanHammer plugin;

	public HammerListener(BanHammer plugin) {
		key = new NamespacedKey(plugin, "hammer");
		banKey = new NamespacedKey(plugin, "banned");
		standKey = new NamespacedKey(plugin, "stand");
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onHammer(PlayerInteractAtEntityEvent event) {
		var player = event.getPlayer();
		var item = player.getInventory().getItemInMainHand();
		if (item == null || item.getItemMeta() == null || !item.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.BYTE)) return;
		if (!(event.getRightClicked() instanceof Player banned)) return;
		player.sendMessage(colorize("&aYou banned &d" + banned.getName()));
		banned.sendMessage(colorize("You got pen is-banned lol"));
		var loc = banned.getLocation().clone();
		var locs = new Location[5];
		locs[0] = loc;
		locs[1] = loc.clone().add(1, 0, 0);
		locs[2] = loc.clone().subtract(1, 0, 0);
		locs[3] = loc.clone().add(0, 1, 0);
		locs[4] = loc.clone().add(0, 2, 0);
		for (var l : locs) {
			l.getBlock().setType(Material.ORANGE_WOOL);
		}
		locs[4].clone().add(0, 1, 0).getBlock().setType(Material.END_ROD);
		var stand = (ArmorStand) loc.getWorld().spawnEntity(locs[4].clone().add(0, 1, 0), EntityType.ARMOR_STAND);
		stand.setInvisible(true);
		stand.setGravity(false);
		stand.setInvulnerable(true);
		banned.getPersistentDataContainer().set(standKey, PersistentDataType.STRING, stand.getUniqueId().toString());
		banned.setInvulnerable(true);
		stand.addPassenger(banned);
		var height = loc.getWorld().getMaxHeight() - 2;
		int id = new BukkitRunnable() {
			@Override
			public void run() {
				if (locs[4].getBlockY() >= height) {
					plugin.getServer().getBanList(Type.NAME).addBan(banned.getName(), "Pen is", null, null);
					Location banLoc = banned.getLocation();
					var fw = (Firework) banLoc.getWorld().spawnEntity(banLoc, EntityType.FIREWORK);
					var meta = fw.getFireworkMeta();
					meta.setPower(16);
					meta.addEffect(FireworkEffect.builder().withColor(Color.AQUA).with(FireworkEffect.Type.BALL).build());
					fw.setFireworkMeta(meta);
					fw.detonate();
					for (var l : locs) {
						l.getBlock().setType(Material.AIR);
						locs[4].clone().add(0, 1, 0).getBlock().setType(Material.AIR);
					}
					stand.removePassenger(banned);
					stand.remove();
					banned.getPersistentDataContainer().remove(banKey);
					banned.getPersistentDataContainer().remove(standKey);
					banned.kickPlayer("You got pen is-banned lol");
					this.cancel();
				} else {
					for (var l : locs) {
						l.getBlock().setType(Material.AIR);
					}
					for (int i = 0; i < 5; i++) {
						locs[i] = locs[i].clone().add(0, 1, 0);
					}
					for (var l : locs) {
						l.getBlock().setType(Material.ORANGE_WOOL);
					}
					locs[4].clone().add(0, 1, 0).getBlock().setType(Material.END_ROD);
					stand.removePassenger(banned);
					banned.teleport(stand.getLocation().clone().add(0, 1, 0));
					stand.teleport(stand.getLocation().clone().add(0, 1, 0));
					stand.addPassenger(banned);
				}
			}
		}.runTaskTimer(plugin, 1L, 2L).getTaskId();
		banned.getPersistentDataContainer().set(banKey, PersistentDataType.INTEGER, id);
	}

}
