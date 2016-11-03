package org.cyberpwn.icing.ability;

import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import org.cyberpwn.icing.Icing;
import org.cyberpwn.icing.skill.Skill;
import org.cyberpwn.icing.xp.XP;
import org.phantomapi.clust.Comment;
import org.phantomapi.clust.ConfigurableController;
import org.phantomapi.clust.Keyed;
import org.phantomapi.construct.Controller;

public class BasicAbility extends ConfigurableController implements Ability
{
	@Comment("The level a player's skill and level must be at to unlock this ability.")
	@Keyed("ability.level")
	public int level = 1;
	
	@Comment("The level increase from the initial or last upgrade to upgrade again.")
	@Keyed("ability.step")
	public int levelStep = 2;
	
	@Comment("The Cost in skill specific shards to unlock this ability")
	@Keyed("ability.cost")
	public int unlockCost = 1;
	
	@Comment("The Cost in skill specific shards to upgrade this ability")
	@Keyed("ability.cost")
	public int upgradeCost = 2;
	
	public BasicAbility(Skill parent, String codeName)
	{
		super((Controller) parent, codeName);
	}
	
	public boolean isUnlocked(Player p)
	{
		return Icing.getInst().getSk().getAbilityDataController().get(p).getUnlockedAbilities().contains(getCodeName());
	}
	
	public long getLevel(Player p)
	{
		return Icing.getInst().getSk().getAbilityDataController().get(p).getAbilityLevel(getCodeName());
	}
	
	public void addLevel(Player p)
	{
		Icing.getInst().getSk().getAbilityDataController().get(p).addAbilityLevel(getCodeName(), 1);
	}
	
	public Skill getSkill()
	{
		return (Skill) parentController;
	}
	
	public boolean canUpgradeOrUnlock(Player p)
	{
		if(getSkill().getLevel(p) >= getMinimumUpgradeLevel(p) && XP.getLevelForXp(XP.getXp(p)) >= getMinimumUpgradeLevel(p))
		{
			return true;
		}
		
		return false;
	}
	
	public long getMinimumUpgradeLevel(Player p)
	{
		long alevel = getLevel(p);
		
		if(alevel < 1)
		{
			return level;
		}
		
		return level + ((alevel + 1) * levelStep);
	}
	
	public int getLevel()
	{
		return level;
	}
	
	public int getLevelStep()
	{
		return levelStep;
	}
	
	public int getUnlockCost()
	{
		return unlockCost;
	}
	
	public int getUpgradeCost()
	{
		return upgradeCost;
	}
	
	public String name()
	{
		return getCodeName();
	}
	
	public String fancyName()
	{
		return StringUtils.capitalize(getCodeName());
	}
	
	@Override
	public void onStart()
	{
		
	}
	
	@Override
	public void onStop()
	{
		
	}
}
