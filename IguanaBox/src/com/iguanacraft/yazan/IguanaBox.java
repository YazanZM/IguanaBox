package com.iguanacraft.yazan;

import java.io.File;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.iguanacraft.yazan.commands.MainCommand;
import com.iguanacraft.yazan.listeners.KillListener;
import com.iguanacraft.yazan.mysql.MySQLManager;
import com.iguanacraft.yazan.mysql.SQLiteManager;
import com.iguanacraft.yazan.utils.PvPScoreboard;
import com.iguanacraft.yazan.utils.Settings;

public class IguanaBox extends JavaPlugin{
	
	/****************************************************
	 *               \\\\\IguanaBox/////                *
	 *              @category JavaPlugin                *
	 *              @author Yazan/yazan123              *
	 *              @version 1.0.0                      *
	 ****************************************************/
	public Logger              logs;
	public FileConfiguration config;
	
	boolean  useMySQL;
	boolean  temp_use;
	
	@Override
	public void onEnable()
	{
		logs = this.getLogger();
		logs.info("Enabled.");
		config = getConfig();
		
		
		File configFile = new File(this.getDataFolder() + "/config.yml");
		
		if(!configFile.exists())
		{
			//MySQL
			config.addDefault("MySQL.enable",         false);
			config.addDefault("MySQL.host",     "localhost");
			config.addDefault("MySQL.port",          "3306");
			config.addDefault("MySQL.database", "minecraft");
			config.addDefault("MySQL.user",         "yazan");
			config.addDefault("MySQL.pass",     "localpass");
			//^                                'minecrafter'
			
			//Warps
			config.addDefault("Warps.price",       20000.0D);
			
			config.options().copyDefaults(true);
			saveConfig();
			
			
			logs.info("Created config file.");
			
		}

		Settings.get().tryDataSetup(this);
		useMySQL = config.getBoolean("MySQL.enable");
		
		
		switch(Boolean.toString(useMySQL))
		{
		case "true":
			
			try
			{
				MySQLManager.get().setupSQLDatabase(this);
				logs.info("MySQL: Connected to " + config.getString("MySQL.host") +
					 	                       ":"+config.getString("MySQL.port") +
						      ", database: " + config.getString("MySQL.database"));
				
				temp_use = true;
			} catch(SQLException e)
			{
				logs.severe("\u001B[36m" + "Could not connect to MySQL database," +
		     	          " make sure you have entered the right information"     +
					               " in the 'config.yml' file." + "\u001B[0m")    ;
			    logs.severe("\u001B[36m" + "Using db to store data. " +
			    		"Disabling MySQL." +  "\u001B[0m");
			
			    config.set("MySQL.enable", false);
			    saveConfig();
			
				temp_use = false;
			}
			
			               break;
			
		case "false":
			
			try {
				SQLiteManager.get().setupSQLite(this);
			} catch (SQLException e1) {
				
				e1.printStackTrace();
			}
			logs.info("Loaded database.");
			temp_use   =         false;
			
			                     break;
			                     
	    default: logs.severe("'com.iguanacraft.yazan.IguanaBox:90-110l' exception; default case"); break;
	    
		}
		    getCommand("buywarp" ).setExecutor(    new MainCommand());
		//  getCommand("allwarps").setExecutor(    new MainCommand());
		    getCommand("removemywarp").setExecutor(new MainCommand());
		    getCommand("pvpstats").setExecutor(    new MainCommand());
		    getCommand( "iguanabox").setExecutor(  new MainCommand());
		
		Bukkit.getPluginManager().registerEvents(new KillListener()  , this);
		
		PvPScoreboard.get().setupScoreboard();
		logs.info("Enabled.");
	}
	
	@Override
	public void onLoad()
	{
		//init
		
	}
	
	@Override
	public void onDisable()
	{
		if(temp_use)
		{
			if(MySQLManager.get().getDatabase().getConnection() == null)
			{
				logs.info("Disabled.");
				return;
			}
			
			MySQLManager.get( ).closeSQLDatabase();
			logs.info("MySQL: Connection closed.");
		}
		
		SQLiteManager.get().closeSQLiteConnection();
		
		logs.info("Disabled.");
	}
}