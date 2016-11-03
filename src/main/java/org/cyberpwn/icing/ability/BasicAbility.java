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

public abstract class BasicAbility extends ConfigurableController implements Ability
{
	@Comment("The level a player's skill and level must be at to unlock this ability.")
	@Keyed("ability.level")
	public int level = 1;
	
	@Comment("The max level this ability can have (absolute max is 64)")
	@Keyed("ability.max")
	public int maxLevel = 64;
	
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
	
	@Override
	public boolean isUnlocked(Player p)
	{
		return Icing.getInst().getSk().getAbilityDataController().get(p).getUnlockedAbilities().contains(getCodeName());
	}
	
	@Override
	public long getLevel(Player p)
	{
		return Icing.getInst().getSk().getAbilityDataController().get(p).getAbilityLevel(getCodeName());
	}
	
	@Override
	public void addLevel(Player p)
	{
		Icing.getInst().getSk().getAbilityDataController().get(p).addAbilityLevel(getCodeName(), 1);
	}
	
	@Override
	public Skill getSkill()
	{
		return (Skill) parentController;
	}
	
	@Override
	public boolean canUpgradeOrUnlock(Player p)
	{
		if(getSkill().getLevel(p) >= getMinimumUpgradeLevel(p) && XP.getLevelForXp(XP.getXp(p)) >= getMinimumUpgradeLevel(p))
		{
			return true;
		}
		
		return false;
	}
	
	@Override
	public long getMinimumUpgradeLevel(Player p)
	{
		long alevel = getLevel(p);
		
		if(alevel < 1)
		{
			return level;
		}
		
		return level + ((alevel + 1) * levelStep);
	}
	
	@Override
	public int getLevel()
	{
		return level;
	}
	
	@Override
	public int getLevelStep()
	{
		return levelStep;
	}
	
	@Override
	public int getUnlockCost()
	{
		return unlockCost;
	}
	
	@Override
	public int getUpgradeCost()
	{
		return upgradeCost;
	}
	
	@Override
	public String name()
	{
		return getCodeName();
	}
	
	@Override
	public String fancyName()
	{
		return StringUtils.capitalize(getCodeName());
	}
	
	@Override
	public int getMaxLevel()
	{
		return maxLevel;
	}
}
