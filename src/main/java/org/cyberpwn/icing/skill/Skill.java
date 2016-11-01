package org.cyberpwn.icing.skill;

import org.bukkit.entity.Player;

public interface Skill
{
	public long getXp(Player p);
	
	public void addXp(Player p, long amt);
	
	public long getLevel(Player p);
	
	public double getProgress(Player p);
	
	public long getXpLeft(Player p);
	
	public String fancyName();
}
