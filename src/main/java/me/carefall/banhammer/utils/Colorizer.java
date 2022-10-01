package me.carefall.banhammer.utils;

import net.md_5.bungee.api.ChatColor;

public class Colorizer {
	
	public static String colorize(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}

}
