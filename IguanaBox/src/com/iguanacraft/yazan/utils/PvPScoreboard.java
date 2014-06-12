package com.iguanacraft.yazan.utils;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import com.iguanacraft.yazan.mysql.MySQLManager;
import com.iguanacraft.yazan.mysql.SQLiteManager;


public class PvPScoreboard {
	
	private ScoreboardManager    manager;
	private Scoreboard                sb;
	private Objective                  o;
	        Integer              cd_task;

    private static PvPScoreboard instance = new PvPScoreboard();
	public static PvPScoreboard get()
	       {return instance;}
	
	public void setupScoreboard()
	{
		manager = Bukkit.getScoreboardManager();
		sb      =    manager.getNewScoreboard();
		o       = sb.registerNewObjective("pvpkills", "dummy");
		
		o.setDisplayName(ChatColor.GREEN + ""+ ChatColor.BOLD + "IguanaCraft");
		o.setDisplaySlot(DisplaySlot.SIDEBAR);
		
	}
	
	public void setTempScoreboard(final Player p, int seconds)
	{
		
		@SuppressWarnings("deprecation")
		Score kills = o.getScore(Bukkit.getOfflinePlayer(ChatColor.AQUA + "Kills:"));
		@SuppressWarnings("deprecation")
		Score deaths  = o.getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + "Deaths:"));
		
		try {
			
			if(MySQLManager.get().msql_enabled == true)
			{
				kills. setScore(MySQLManager.get().getKills (p));
				deaths.setScore(MySQLManager.get().getDeaths(p));
			}
			else
			{
				kills. setScore(SQLiteManager.get().getKills (p));
				deaths.setScore(SQLiteManager.get().getDeaths(p));
			}
			
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		p.setScoreboard(sb);
		
		cd_task = 
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Settings.get().getPlugin(), new Runnable()
		{

			@Override
			public void run() 
			{
				p.setScoreboard(Bukkit.getServer().getScoreboardManager().getNewScoreboard());
			}
			
		}, seconds*20L);
	}
	
	

}
