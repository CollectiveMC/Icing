package org.cyberpwn.icing.abilities;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.cyberpwn.icing.ability.BasicAbility;
import org.cyberpwn.icing.skill.Skill;
import org.phantomapi.clust.Comment;
import org.phantomapi.clust.Keyed;
import org.phantomapi.util.C;
import org.phantomapi.util.F;
import org.phantomapi.world.MaterialBlock;

public class SwordsBlocking extends BasicAbility
{
	@Comment("The Step")
	@Keyed("block-step")
	public double damageStep = 0.08;
	
	public SwordsBlocking(Skill parent)
	{
		super(parent, "block");
		
		maxLevel = 4;
		level = 5;
		levelStep = 3;
		upgradeCost = 5;
		unlockCost = 1;
	}
	
	@EventHandler
	public void on(EntityDamageByEntityEvent e)
	{
		if(e.getEntity() instanceof Player)
		{
			Player p = (Player) e.getEntity();
			
			if(p.isBlocking())
			{
				if(isUnlocked(p) && isEnabled(p))
				{
					int level = (int) getLevel(p);
					e.setDamage(e.getDamage() - (e.getDamage() * (1.0 - getResistanceBonus(level))));
				}
			}
		}
	}
	
	public double getResistanceBonus(int level)
	{
		return level * damageStep;
	}
	
	@Override
	public MaterialBlock getMaterialBlock()
	{
		return new MaterialBlock(Material.IRON_INGOT);
	}
	
	@Override
	public String getDescription()
	{
		return "Increases Sword Blocking Resistance.";
	}
	
	@Override
	public void onStart()
	{
		
	}
	
	@Override
	public void onStop()
	{
		
	}
	
	@Override
	public String getStatGraph(Player p)
	{
		if(getLevel() == getMaxLevel())
		{
			return C.LIGHT_PURPLE + F.f(getResistanceBonus(1), 1) + " " + getAbilityGraph(20, (double) getLevel(p) / (double) getMaxLevel(), "") + C.LIGHT_PURPLE + " " + F.f(getResistanceBonus(getMaxLevel()), 1) + " Damage";
		}
		
		return C.LIGHT_PURPLE + F.f(getResistanceBonus(1), 1) + " " + getAbilityGraph(20, (double) getLevel(p) / (double) getMaxLevel(), F.f(getResistanceBonus((int) getLevel(p)), 1)) + C.LIGHT_PURPLE + " " + F.f(getResistanceBonus(getMaxLevel()), 1) + " Damage";
	}
}
