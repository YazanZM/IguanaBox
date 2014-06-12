package com.iguanacraft.yazan.listeners;

import java.sql.SQLException;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.iguanacraft.yazan.mysql.MySQLManager;
import com.iguanacraft.yazan.mysql.SQLiteManager;
import com.iguanacraft.yazan.utils.MessageManager;
import com.iguanacraft.yazan.utils.PvPScoreboard;

public class KillListener implements Listener{
	
	@EventHandler
	public void onK(PlayerDeathEvent e)
	{
		Player dead = e.getEntity();
		
		
		if(dead.getKiller() instanceof Player)
		{
			Player killer = dead.getKiller();
			try
			{
				if(MySQLManager.get().msql_enabled == true)
				{
					MySQLManager.get().updateKills(killer);
				}
				else
				{
					SQLiteManager.get().updateKills(killer);
				}
				PvPScoreboard.get().setTempScoreboard(killer, 5);
				
			} catch(SQLException sqlE)
			{
				sqlE.printStackTrace();
			}
		}
		
		if(dead instanceof Player)
		{
			try
			{
				if(MySQLManager.get().msql_enabled == true)
				{
					MySQLManager.get().updateDeaths(dead);
				} else 
				{
					SQLiteManager.get().updateDeaths(dead);
				}
				
				PvPScoreboard.get().setTempScoreboard(dead, 5);
				
			} catch(SQLException sqlE)
			{
				sqlE.printStackTrace();
			}
			
		}
		
		return;
	}
	
	@EventHandler
	public void onEdE(EntityDamageByEntityEvent e)
	{
		if(e.getEntity() instanceof Player && e.getDamager() instanceof Player)
		{
			Player d = (Player) e.getDamager();
			Player p = (Player) e.getEntity();
			
			if(d.hasPermission("icbox.event.nopvp") || p.hasPermission("icbox.event.nopvp"))
			{
				d.sendMessage(MessageManager.get().fail("Staff could not participate in PvP."));
				e.setCancelled(true);
			} else e.setCancelled(false);
			
		}
	}
	
	

}
