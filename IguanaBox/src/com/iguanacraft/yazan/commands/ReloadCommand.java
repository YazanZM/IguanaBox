package com.iguanacraft.yazan.commands;

import org.bukkit.entity.Player;

import com.iguanacraft.yazan.utils.MessageManager;
import com.iguanacraft.yazan.utils.Settings;

public class ReloadCommand extends SubCommand
{

	@Override
	public void execute(Player player, String[] args, String permission) {
		
		if(!player.hasPermission(permission) || !player.isOp())
		{
			player.sendMessage(MessageManager.get().fail("No permission."));
			return;
		}
		
		Settings.get().getPlugin().reloadConfig();
		Settings.get().loadData();
		
		player.sendMessage(MessageManager.get().success("Reloaded data."));
		
	}
	
}
