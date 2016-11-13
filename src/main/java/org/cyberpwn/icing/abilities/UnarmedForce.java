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

public class UnarmedForce extends BasicAbility
{
	@Comment("The Step")
	@Keyed("damage-step")
	public double damageStep = 0.96;
	
	public UnarmedForce(Skill parent)
	{
		super(parent, "force");
		
		maxLevel = 8;
		level = 5;
		levelStep = 3;
		upgradeCost = 4;
		unlockCost = 1;
	}
	
	@EventHandler
	public void on(EntityDamageByEntityEvent e)
	{
		if(e.getDamager() instanceof Player)
		{
			Player p = (Player) e.getDamager();
			
			if(isUnlocked(p) && isEnabled(p))
			{
				if(p.getItemInHand() == null || p.getItemInHand().getType().equals(Material.AIR))
				{
					e.setDamage(e.getDamage() + getDamage((int) getLevel(p)));
				}
			}
		}
	}
	
	public double getDamage(int level)
	{
		return level * damageStep;
	}
	
	@Override
	public MaterialBlock getMaterialBlock()
	{
		return new MaterialBlock(Material.FIREBALL);
	}
	
	@Override
	public String getDescription()
	{
		return "Increases Unarmed Damage.";
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
			return C.LIGHT_PURPLE + F.f(getDamage(1), 1) + " " + getAbilityGraph(20, (double) getLevel(p) / (double) getMaxLevel(), "") + C.LIGHT_PURPLE + " " + F.f(getDamage(getMaxLevel()), 1) + " Damage";
		}
		
		return C.LIGHT_PURPLE + F.f(getDamage(1), 1) + " " + getAbilityGraph(20, (double) getLevel(p) / (double) getMaxLevel(), F.f(getDamage((int) getLevel(p)), 1)) + C.LIGHT_PURPLE + " " + F.f(getDamage(getMaxLevel()), 1) + " Damage";
	}
}
