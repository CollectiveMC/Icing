package org.cyberpwn.icing.skill;

import org.bukkit.entity.Player;
import org.phantomapi.world.MaterialBlock;

public interface Skill
{
	public long getXp(Player p);
	
	public void addXp(Player p, long amt);
	
	public long getLevel(Player p);
	
	public double getProgress(Player p);
	
	public long getXpLeft(Player p);
	
	public String fancyName();
	
	public void popRewardMap();
	
	public void addReward(Player p, Integer r);
	
	public MaterialBlock getSkillMaterial();
}
