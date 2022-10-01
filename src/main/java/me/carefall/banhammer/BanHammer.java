package me.carefall.banhammer;

import org.bukkit.plugin.java.JavaPlugin;

import me.carefall.banhammer.commands.HammerCommand;
import me.carefall.banhammer.listeners.BanDismountListener;
import me.carefall.banhammer.listeners.HammerListener;

import static me.carefall.banhammer.utils.Colorizer.colorize;

public class BanHammer extends JavaPlugin {
	
	public void onEnable() {
		new HammerCommand(this);
		new HammerListener(this);
		new BanDismountListener(this);
		send("&aPlugin enabled!");
	}
	
	public void onDisable() {
		send("&cPlugin disabled!");
	}
	
	public void send(String s) {
		getServer().getConsoleSender().sendMessage("[" + getDescription().getName() + "] " + colorize(s));
	}

}
