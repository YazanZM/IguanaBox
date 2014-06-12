package com.iguanacraft.yazan.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.melonbrew.fe.API;
import org.melonbrew.fe.Fe;
import org.melonbrew.fe.database.Account;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.Warps;
import com.iguanacraft.yazan.utils.MessageManager;
import com.iguanacraft.yazan.utils.PvPScoreboard;
import com.iguanacraft.yazan.utils.Settings;

public class MainCommand implements CommandExecutor{
	
	Fe fe = (Fe) Bukkit.getPluginManager().getPlugin("Fe");
	Essentials e = (Essentials) Bukkit.getPluginManager().getPlugin("Essentials");
	
	API api = new API(fe);
	
	Warps warps = new Warps(Settings.get().getPlugin().getServer(), e.getDataFolder());
	
	@Override
	public boolean onCommand(CommandSender sender,
			                 Command          cmd,
			                 String         label,
			                 String[]        args) 
	{
		
		if(sender instanceof Player)
		{

			Player player = (Player) sender;
			Account p     = api.getAccount ( player.getName() );
			Double  m     = p.getMoney();
			Double  price = Settings.get().getPlugin().getConfig().getDouble("Warps.price");
			
			List<String> existing_warps;
			//
			
			if(cmd.getName().equalsIgnoreCase("buywarp"))
			{
				if(player.hasPermission("icbox.cmd.buywarp") || player.isOp())
				{
					if(args.length == 0 || args[0].isEmpty())
					{
						player.sendMessage(MessageManager.get().neutral("Missing arguments: /buywarp <name>"));
						return true;
					}
					if(p.getMoney() < price)
					{
						player.sendMessage(MessageManager.get().fail("The price of a warp is " + price + " Fe. You have: " + m + " Fe."));
						return true;
					}
					
					if(Settings.get().getPlugin().getConfig().getStringList("Warps.existing-warps") == null)
					{
						
						List<String> temp = new ArrayList<String>();
						temp.add(args[0].toLowerCase() + ":" + player.getName());
						
						Settings.get().getPlugin().getConfig ().set("Warps.existing-warps", temp);  //UUID
						Settings.get().getPlugin().saveConfig();
						
						existing_warps = Settings.get().getPlugin().getConfig().getStringList("Warps.existing-warps");
						Settings.get().getPlugin().saveConfig();
						
						try {
							warps.setWarp(args[0].toLowerCase(), player.getLocation());
							e.reload();
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						p.setMoney(m - price);
						player.sendMessage(MessageManager.get().success("Bought warp " + args[0].toLowerCase() + "!"));
						return true;
					}
					else
					{
						
						existing_warps = Settings.get().getPlugin().getConfig().getStringList("Warps.existing-warps");

						for(String all : existing_warps)
						{
							String[] all_s = all.split(":");
							
							if(all_s[0].equals(args[0].toLowerCase()))
							{
								player.sendMessage(MessageManager.get().fail("Error: warp already exists."));
								return true;
							}
						}
						existing_warps.add(args[0].toLowerCase() + ":" + player.getName());
						
						Settings.get().getPlugin().getConfig().set("Warps.existing-warps", existing_warps);
						Settings.get().getPlugin().saveConfig();
						
						try {
							warps.setWarp(args[0].toLowerCase(), player.getLocation());
							e.reload();
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						p.setMoney(m - price);
						player.sendMessage(MessageManager.get().success("Bought warp " + args[0].toLowerCase() + " for " +price +" Fe!"));
						return true;
					}
				}
			}
			
			/*if(cmd.getName().equalsIgnoreCase("allwarps"))
			{
				
				if(Settings.get().getPlugin().getConfig().getStringList("Warps.existing-warps") == null)
				{
					player.sendMessage(MessageManager.get().fail("No warps found."));
					return true;
				}

				List<String> list = Settings.get().getPlugin().getConfig().getStringList("Warps.existing-warps");
				
				player.sendMessage(MessageManager.get().success(" Available warps:"));
				if(list.isEmpty())
				{
					player.sendMessage(MessageManager.get().neutral("No warps to show."));
					return true;
				}
				int id = 0;
				for(String l : list)
				{
					String[] l_s = l.split(":");
					id++;
					player.sendMessage(MessageManager.get().neutral(" #" + id + ": " + l_s[0] + " by " + l_s[1]));
				}
				
				return true;
			}*/
			
			if(cmd.getName().equalsIgnoreCase("removemywarp"))
			{
				if(player.hasPermission("icbox.cmd.removemywarp") || player.isOp())
				{
					if(args.length == 0 || args[0].isEmpty())
					{
						player.sendMessage(MessageManager.get().neutral("Missing arguments: /removewarp <name> (must be yours)"));
						return true;
					}
					
					if(Settings.get().getPlugin().getConfig().getStringList("Warps.existing-warps") == null)
					{
						player.sendMessage("No warps to delete.");
						return true;
					}

					List<String> list = Settings.get().getPlugin().getConfig().getStringList("Warps.existing-warps");
					for(String l : list)
					{
						String[] s_t = l.split(":");
						if(l.startsWith(args[0].toLowerCase()))
						{
							if(!s_t[1].equalsIgnoreCase(player.getName()))
							{
								player.sendMessage(MessageManager.get().fail("This is not your warp."));
								return true;
							}
							
						} 
						else
						{
							player.sendMessage(MessageManager.get().fail("Warp does not exist."));
							return true;
						}
						
						if(l.equals(args[0].toLowerCase() + ":" + player.getName()))
						{
							try {
								warps.removeWarp(args[0].split(":")[0].toLowerCase());
							} catch (Exception e) {
								e.printStackTrace();
							}
							list.remove(args[0].toLowerCase() + ":" + player.getName());
							
							Settings.get().getPlugin().getConfig().set("Warps.existing-warps", list);
							Settings.get().getPlugin().saveConfig();
							e.reload();
							
							player.sendMessage(MessageManager.get().success("Warp " + ChatColor.DARK_AQUA + args[0] + ChatColor.GREEN +" successfully removed."));
							return true;
						}
					}
				}
				
			}
			
			if(cmd.getName().equalsIgnoreCase("pvpstats"))
			{
				if(Settings.get().getPlugin().getConfig().getBoolean("MySQL.enable") == false)
				{
					player.sendMessage(MessageManager.get().fail("Error: MySQL is not enabled. (FlatFile support coming soon)"));
					return true;
				}
				PvPScoreboard.get().setTempScoreboard(player, 10);
				player.sendMessage(MessageManager.get().success("Stats scoreboard opened."));
				return true;
			}
			
			if(cmd.getName().equalsIgnoreCase("iguanabox"))
			{
				if(args.length == 0)
				{
					player.sendMessage(ChatColor.GOLD + "                     -=[" + MessageManager.get().getRawPrefix() +ChatColor.GOLD+"]=-");
					player.sendMessage(ChatColor.DARK_GRAY + "  IguanaBox is a plugin made for the server IguanaCraft.");
					player.sendMessage(ChatColor.GRAY + " - Developer: " + ChatColor.GREEN + " Yazan | yazan123");
					player.sendMessage(ChatColor.GRAY + " - Contents:- ");
					player.sendMessage(ChatColor.DARK_AQUA + "     Buy warps| IguanaBox implements both Fe and Essentials to use the functions in it to make it easier for server admins.");
					player.sendMessage(ChatColor.DARK_AQUA + "      PvPStats| Both your kills and deaths are stored. (more additions later)");
					player.sendMessage(ChatColor.DARK_AQUA + "         MySQL| Uses MySQL to store some of the data in this plugin.");
					player.sendMessage(ChatColor.GRAY + " - Commands:- ");
					player.sendMessage(ChatColor.DARK_AQUA + "       buywarp| Deducts " + Settings.get().getPlugin().getConfig().getDouble("Warps.price") + " Fe from your account and sets a warp at your position.");
					player.sendMessage(ChatColor.GRAY + "                     /buywarp <name>");
					player.sendMessage(ChatColor.DARK_AQUA + " /removemywarp| Removes a warp YOU bought. (no refunds)");
					player.sendMessage(ChatColor.GRAY + "                     /removemywarp <name>");
					player.sendMessage(ChatColor.DARK_AQUA + "     /pvpstats| View your kills and deaths.");
					player.sendMessage(ChatColor.GRAY + "                     /pvpstats");
					
					return true;
				}
				
				switch(args[0].toUpperCase())
				{
				case "RELOAD":
					ReloadCommand rc = new ReloadCommand();
					rc.execute(player, args, "icbox.cmd.reload");
					break;
				
				default:
					player.sendMessage(MessageManager.get().fail("Unknown sub-command."));
					break;
				
				}
			}
		}
		else return true;
		
		return false;
		
	}

}
//