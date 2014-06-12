package com.iguanacraft.yazan.utils;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class Settings {
	
	private static Settings instance = new Settings();
	
	private Plugin                p;
	private File               data;
	private FileConfiguration dataC;
	
	public  boolean         created;
	
	public static Settings get()
	{
		return instance;
	}
	public void tryDataSetup(Plugin p)
	{
		this.p = p;
		this.data = new File("plugins" + File.separator + p.getDataFolder().getName() + File.separator + "data.yml");
		
			if(!data.exists())
			{
				try { data.createNewFile(); } catch(IOException ioe) { p.getLogger().warning("Could not create data file."); }
			}
			
			dataC = YamlConfiguration.loadConfiguration(data);
			
			p.getLogger().info("Loaded data file.");	
	}
	
	public FileConfiguration getDataFile()
	{
		return dataC;
	}
	
	public void saveDataFile()
	{
		try { dataC.save(data); } catch(IOException ioe) { ioe.printStackTrace(); }

		p.getLogger().info("Saved data file.");
	}
	
	public Plugin getPlugin()
	{
		return p;
	}
	
	public void loadData()
	{

		this.data = new File("plugins" + File.separator + p.getDataFolder().getName() + File.separator + "data.yml");
		dataC = YamlConfiguration.loadConfiguration(data);
	}
	
    public String serializeLoc(Location l)
    {
        return 
        	   l.getWorld().getName()+","
              +l.getBlockX()+","
              +l.getBlockY()+","
              +l.getBlockZ();
    }
    
    public Location deserializeLoc(String l)
    {
        String[] l_a = l.split(",");
        
        return new Location(Bukkit.getWorld(l_a[0]),
        		           Integer.parseInt(l_a[1]),
        		           Integer.parseInt(l_a[2]),
        		           Integer.parseInt(l_a[3]));
    }
    
}
