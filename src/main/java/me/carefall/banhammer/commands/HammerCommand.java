package me.carefall.banhammer.commands;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import me.carefall.banhammer.BanHammer;
import static me.carefall.banhammer.utils.Colorizer.colorize;

public class HammerCommand implements CommandExecutor {
	
	private ItemStack hammer = new ItemStack(Material.WOODEN_PICKAXE);
	
	public HammerCommand(BanHammer plugin) {
		plugin.getCommand("banhammer").setExecutor(this);
		var meta = hammer.getItemMeta();
		meta.setDisplayName(colorize("&dBan hammer"));
		meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "hammer"), PersistentDataType.BYTE, (byte) 1);
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length != 0) {
			sender.sendMessage("Oh nibba you gay");
			return false;
		}
		if (!(sender instanceof Player player)) {
			sender.sendMessage("Oh nibba you console");
			return false;
		}
		player.getInventory().setItemInMainHand(hammer);
		player.sendMessage("Here it is my friend");
		return false;
	}

}
