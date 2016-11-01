package org.cyberpwn.icing.skill;

import org.bukkit.entity.Player;

public interface Skill
{
	public long getSkill(Player p);
	
	public long getXp(Player p);
	
	public void addXp(Player p, long amt);
}
