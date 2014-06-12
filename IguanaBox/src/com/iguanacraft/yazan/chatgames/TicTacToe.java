package com.iguanacraft.yazan.chatgames;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.iguanacraft.yazan.utils.MessageManager;

public class TicTacToe {
	
	private String line_1,
	               line_2,
	               line_3;
	
	private String[] l1_s;
	private String[] l2_s;
	private String[] l3_s;
	
	private boolean p_1_p,
	                p_2_p;
	
	private Player    one,
	                  two;
	
	public TicTacToe(Player one, Player two)
	{
		/*
		 * Sets up game
		 */
		this.one = one;
		this.two = two;
		                                        //0 1 2
		this.line_1 = ChatColor.GREEN + "        |-|-|-|";
		this.line_2 = ChatColor.GREEN + "        |-|-|-|";
		this.line_3 = ChatColor.GREEN + "        |-|-|-|";
		
		this.l1_s = line_1.split("|");
		this.l2_s = line_2.split("|");
		this.l3_s = line_3.split("|");
	}
	
	public void startTurn(int i)
	{
		/*
		 * Gives turn to 1 or 2
		 */
		switch(i)
		{
		case 1:
			one.sendMessage(MessageManager.get().game(one.getName() + "'s turn:"));
			two.sendMessage(MessageManager.get().game(one.getName() + "'s turn:"));
			
			sendGameTable();
			
			one.sendMessage(MessageManager.get().game("- /iguanabox ttt <row> <(1 or 2 or 3)>"));
			
			p_1_p = true;
			break;
		case 2:
			
			one.sendMessage(MessageManager.get().game(two.getName() + "'s turn:"));
			two.sendMessage(MessageManager.get().game(two.getName() + "'s turn:"));
			
			sendGameTable();
			
			two.sendMessage(MessageManager.get().game("- /iguanabox ttt <row> <box>"));
			
			p_2_p = true;
			break;
			
		default: System.out.println("Error with TICTACTOE"); break;
		
		}
	}
	
	public Player getTurn()
	{
		if(p_1_p) { return one;}
		if(p_2_p) { return two;}
		     return null;
	}
	
	public void switchTurns()
	{
		/*
		 * Changes turns once player is complete
		 */
		if(p_1_p)
		{
			p_1_p = false;
			
			startTurn(2);
		}
		
		if(p_2_p)
		{
			p_2_p = false;
			
			startTurn(1);
		}
	}
	
	public void sendGameTable()
	{
		/*
		 * Sends game table
		 */
		one.sendMessage(line_1);
		one.sendMessage(line_2);
		one.sendMessage(line_3);
		
		two.sendMessage(line_1);
		two.sendMessage(line_2);
		two.sendMessage(line_3);
	}
	
	public void updateBoard(int row, int box, boolean ans)
	{
		/*
		 * true  =  o
		 * false =  x
		 */
		String ans_cov;
		
		if(ans = true){ans_cov = "o";} 
		      else ans_cov = "x";
		
		switch(row)
		{
		case 1:
			l1_s[box + 1] = ans_cov;
			break;
		case 2:
			l2_s[box + 1] = ans_cov;
			break;
		case 3:
			l3_s[box + 1] = ans_cov;
			break;
		}
		
		if(l1_s[0] == "x" &&
		   l1_s[1] == "x" &&
		   l1_s[2] == "x"  )
		{
			//stop game and check for "o" and crosses and disable overwriting
		}
		switchTurns();
		
	}
	

}
