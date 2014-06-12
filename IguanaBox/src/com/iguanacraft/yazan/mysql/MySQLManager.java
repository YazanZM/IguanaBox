package com.iguanacraft.yazan.mysql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.iguanacraft.yazan.utils.Settings;

public class MySQLManager {

	private static MySQLManager instance = new MySQLManager();
	
	private     MySQL     database;
	
	private String table_pvp = "`icbox_data_pvp`";
	
	public boolean msql_enabled = Settings.get().getPlugin().getConfig().getBoolean("MySQL.enable");
	
	public static MySQLManager get()
	{
		return instance;
	}
	
	public void setupSQLDatabase(Plugin p) throws SQLException
	{
		/**
		 * - Opens connection
		 * - Creates table and loads it.
		 * Player (String) | Kills (Int)| Deaths (Int) | KDR (Float)
		 */
		
		String host     = p.getConfig().getString     ("MySQL.host");
		String port     = p.getConfig().getString     ("MySQL.port");
		String database = p.getConfig().getString ("MySQL.database");
		String user     = p.getConfig().getString     ("MySQL.user");
		String pass     = p.getConfig().getString     ("MySQL.pass");
		
		this.database = new MySQL(p, host, port, database, user, pass);
		this.database.openConnection();
		
		Statement statement = this.database.getConnection().createStatement();
		
		statement.executeUpdate("CREATE TABLE IF NOT EXISTS "+table_pvp+" (`Player` varchar(32)," +
	                                                           			      " `Kills` int," +
		                                                           		 " `Deaths` int(32));");
		p.getLogger().info("Connected to MySQL database successfully.");
	}
	
	public void closeSQLDatabase() 
	{
		/**
		 * - Closes connection
		 */
		this.database.closeConnection();
	}
	
	
	public MySQL getDatabase()
	{
		/**
		 * @returns database - MySQL object
		 */
		return this.database;
	}
	
	public int getKills(Player p) throws SQLException
	{
	    String name = p.getName().toLowerCase();
		
		Statement statement = this.database.getConnection().createStatement();

		ResultSet result = statement.executeQuery("SELECT * FROM "+table_pvp+" WHERE `Player` = '" + name + "';");
		
		if(!result.next()) return 0;
		return result.getInt("Kills");
	}
	
	public int getDeaths(Player p) throws SQLException
	{
	    String name = p.getName().toLowerCase();
		
		Statement statement = this.database.getConnection().createStatement();

		ResultSet result = statement.executeQuery("SELECT * FROM "+table_pvp+" WHERE `Player` = '" + name + "';");
		
		if(!result.next()) return 0;
		return result.getInt("Deaths");
	}
	
	public void updateKills(Player p) throws SQLException
	{
		Statement statement = this.database.getConnection().createStatement();
		
		String name = p.getName();
		
		int kills = this.getKills(p);
		
		if(kills != 0)
		{
			//update joins
			statement.executeUpdate("UPDATE "+table_pvp+" SET `Kills` = '" + (kills + 1) + "' WHERE `Player` = '" + name + "';");
		}
		else
		{
			if(this.getDeaths(p) == 0)
			{
				statement.executeUpdate("INSERT INTO "+table_pvp+" (`Player`,`Kills`,`Deaths`) VALUES ('"+name+"','1', 0);");
			}else
			{
				statement.executeUpdate("INSERT INTO "+table_pvp+" (`Player`,`Kills`,`Deaths`) VALUES ('"+name+"','1','"+this.getDeaths(p)+"');");
			}
		}
	}
	
	public void updateDeaths(Player p) throws SQLException
	{
		Statement statement = this.database.getConnection().createStatement();
		
		String name = p.getName();
		
		int deaths = this.getDeaths(p);
		
		if(deaths != 0)
		{
			//update joins
			statement.executeUpdate("UPDATE "+table_pvp+" SET `Deaths` = '" + (deaths + 1) + "' WHERE `Player` = '" + name + "';");
		}
		else
		{
			if(this.getKills(p) == 0)
			{
				statement.executeUpdate("INSERT INTO "+table_pvp+" (`Player`,`Kills`,`Deaths`) VALUES ('"+name+"','0','1');");
			}else
			{
				statement.executeUpdate("INSERT INTO "+table_pvp+" (`Player`,`Kills`,`Deaths`) VALUES ('"+name+"','"+this.getKills(p)+"',1);");
			}
			
		}
	}
}
