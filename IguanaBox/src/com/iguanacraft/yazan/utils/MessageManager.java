package com.iguanacraft.yazan.utils;

import org.bukkit.ChatColor;

public class MessageManager {
	
	private static MessageManager mm = new MessageManager();
	private String prefix = ChatColor.GRAY + "[" + ChatColor.AQUA + "IguanaBox" + ChatColor.GRAY + "]";
	
	public static MessageManager get()
	{
		return mm;
	}
	
	public String neutral(String msg)
	{
		return prefix + ChatColor.GRAY +" " + msg;
	}
	
	public String success(String msg)
	{
		return prefix + ChatColor.GREEN +" "+ msg;
	}
	
	public String fail(String msg)
	{
		return prefix + ChatColor.RED +" "+ msg;
	}
	
	public String game(String msg)
	{
		return prefix + ChatColor.GOLD + " " + msg;
	}
	
	public String getRawPrefix()
	{
		if (prefix.length() > 0 && prefix.charAt(prefix.length()-1)== ' ')
		{
			prefix = prefix.substring(0, prefix.length()-1);
		}
		    return prefix;
	}

}
