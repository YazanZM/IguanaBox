package com.iguanacraft.yazan.commands;

import org.bukkit.entity.Player;

public abstract class SubCommand {
	/**
	 * Class object for sub commands of the command 'iguanabox'
	 * @param player executor
	 * @param args command string
	 * @param permission perm value needed
	 */
	public abstract void execute(Player player,
			                     String[] args,
			                String permission);
}
