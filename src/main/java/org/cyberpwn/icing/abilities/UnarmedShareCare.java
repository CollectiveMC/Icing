package org.cyberpwn.icing.abilities;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.cyberpwn.icing.ability.BasicAbility;
import org.cyberpwn.icing.skill.Skill;
import org.phantomapi.clust.Comment;
import org.phantomapi.clust.Keyed;
import org.phantomapi.util.C;
import org.phantomapi.util.F;
import org.phantomapi.util.M;
import org.phantomapi.world.MaterialBlock;

public class UnarmedShareCare extends BasicAbility
{
	@Comment("Flame Ticks")
	@Keyed("flame-ticks")
	public int ft = 30;
	
	public UnarmedShareCare(Skill parent)
	{
		super(parent, "sharing is caring");
		
		maxLevel = 10;
		level = 3;
		levelStep = 2;
		upgradeCost = 1;
		unlockCost = 1;
	}
	
	@EventHandler
	public void on(EntityDamageByEntityEvent e)
	{
		if(e.getDamager() instanceof Player)
		{
			Player p = (Player) e.getDamager();
			
			if(p.getItemInHand() == null || p.getItemInHand().getType().equals(Material.AIR))
			{
				if(M.r((double) (getLevel(p) / 10.0)))
				{
					if(p.getFireTicks() > 0)
					{
						e.getEntity().setFireTicks(ft);
						
						for(PotionEffect i : p.getActivePotionEffects())
						{
							if(e.getEntity() instanceof LivingEntity)
							{
								LivingEntity l = (LivingEntity) e.getEntity();
								
								l.addPotionEffect(new PotionEffect(i.getType(), 20, 0));
							}
						}
					}
				}
			}
		}
	}
	
	public double getDamage(int level)
	{
		return (double) level / 10.0;
	}
	
	@Override
	public MaterialBlock getMaterialBlock()
	{
		return new MaterialBlock(Material.FLINT_AND_STEEL);
	}
	
	@Override
	public String getDescription()
	{
		return "Passes potion effects and fire onto the enemy";
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
			return C.LIGHT_PURPLE + F.pc(getDamage(1)) + " " + getAbilityGraph(20, (double) getLevel(p) / (double) getMaxLevel(), "") + C.LIGHT_PURPLE + " " + F.pc(getDamage(getMaxLevel())) + " Damage";
		}
		
		return C.LIGHT_PURPLE + F.pc(getDamage(1)) + " " + getAbilityGraph(20, (double) getLevel(p) / (double) getMaxLevel(), F.pc(getDamage((int) getLevel(p)), 1)) + C.LIGHT_PURPLE + " " + F.pc(getDamage(getMaxLevel())) + " Damage";
	}
}
