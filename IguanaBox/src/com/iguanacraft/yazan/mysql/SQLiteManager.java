package com.iguanacraft.yazan.mysql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;


public class SQLiteManager {
	
	private SQLite db;
	
	private String table_pvp = "`icbox_data_pvp`";
	
	private static SQLiteManager m = new SQLiteManager();
	
	private String fileLoc;
	private String ext;
	
	public static SQLiteManager get()
	         {return m;}
	
	public void setupSQLite(Plugin p) throws SQLException
	{
		this.ext = ".yazan";
		this.fileLoc = "icdata" + ext;
		this.db = new SQLite(p, fileLoc);
		this.db.openConnection();
		
		
		Statement statement = this.db.getConnection().createStatement();
		
		statement.executeUpdate("CREATE TABLE IF NOT EXISTS "+table_pvp+" (`Player` varchar(32)," +
	                                                           			      " `Kills` int," +
		                                                           		 " `Deaths` int(32));");
		
		statement.close();
		closeSQLiteConnection();
	}
	
	public void closeSQLiteConnection()
	{
		this.db.closeConnection();
	}
	
	public SQLite getDatabase()
	{
		return this.db;
	}
	
	public int getKills(Player p) throws SQLException
	{
		this.db.openConnection();
	    String name = p.getName().toLowerCase();
		
		Statement statement = this.db.getConnection().createStatement();
		
		ResultSet result = statement.executeQuery("SELECT * FROM "+table_pvp+" WHERE `Player` = '" + name + "';");
		
		if(!result.next()) return 0;
		
		int res = result.getInt("Kills");
		
		result.close();
		statement.close();
		closeSQLiteConnection();
		return res;
	}
	
	public int getDeaths(Player p) throws SQLException
	{
		this.db.openConnection();
	    String name = p.getName().toLowerCase();
		
		Statement statement = this.db.getConnection().createStatement();

		ResultSet result = statement.executeQuery("SELECT * FROM "+table_pvp+" WHERE `Player` = '" + name + "';");
		
		if(!result.next()) return 0;
		
		int res = result.getInt("Deaths");
		result.close();
		return res;
	}
	
	public void updateKills(Player p) throws SQLException
	{
		this.db.openConnection();
		Statement statement = this.db.getConnection().createStatement();
		
		String name = p.getName();
		
		int kills = this.getKills(p);
		
		if(kills != 0)
		{
			//update joins
			statement.executeUpdate("UPDATE "+table_pvp+" SET `Kills` = '" + (kills + 1) + "' WHERE `Player` = '" + name + "';");
		}
		else
		{
			if(this.getKills(p) == 0 && this.getDeaths(p) == 0)
			{
				statement.executeUpdate("INSERT INTO "+table_pvp+" (`Player`,`Kills`) VALUES ('"+name+"','1');");
			}
		}
		
		statement.close();
		closeSQLiteConnection();
	}
	
	public void updateDeaths(Player p) throws SQLException
	{
		this.db.openConnection();
		
		Statement statement = this.db.getConnection().createStatement();
		
		String name = p.getName();
		
		int deaths = this.getDeaths(p);
		
		if(deaths != 0)
		{
			//update joins
			statement.executeUpdate("UPDATE "+table_pvp+" SET `Deaths` = '" + (deaths + 1) + "' WHERE `Player` = '" + name + "';");
		}
		else
		{
			if(this.getKills(p) == 0 && this.getDeaths(p) == 0)
			{
				statement.executeUpdate("INSERT INTO "+table_pvp+" (`Player`,`Deaths`) VALUES ('"+name+"','1');");
			}
			
		}
		statement.close();
		closeSQLiteConnection();
	}
	
}
