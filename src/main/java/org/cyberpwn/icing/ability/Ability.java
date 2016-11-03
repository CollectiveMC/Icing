package org.cyberpwn.icing.ability;

import org.bukkit.entity.Player;
import org.cyberpwn.icing.skill.Skill;
import org.phantomapi.world.MaterialBlock;

public interface Ability
{
	public boolean isUnlocked(Player p);
	
	public long getLevel(Player p);
	
	public void addLevel(Player p);
	
	public Skill getSkill();
	
	public boolean canUpgradeOrUnlock(Player p);
	
	public long getMinimumUpgradeLevel(Player p);
	
	public int getLevel();
	
	public int getLevelStep();
	
	public int getUnlockCost();
	
	public int getUpgradeCost();
	
	public String name();
	
	public String fancyName();
	
	public MaterialBlock getMaterialBlock();
	
	public String getDescription();
	
	public int getMaxLevel();
}
