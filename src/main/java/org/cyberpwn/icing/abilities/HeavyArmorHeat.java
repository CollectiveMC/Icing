package org.cyberpwn.icing.abilities;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.cyberpwn.icing.ability.BasicAbility;
import org.cyberpwn.icing.skill.Skill;
import org.phantomapi.clust.Comment;
import org.phantomapi.clust.Keyed;
import org.phantomapi.lang.GSound;
import org.phantomapi.util.C;
import org.phantomapi.util.F;
import org.phantomapi.world.MaterialBlock;

public class HeavyArmorHeat extends BasicAbility
{
	@Comment("The Max")
	@Keyed("max-taper")
	public double maxMitigation = 0.68;
	
	public HeavyArmorHeat(Skill parent)
	{
		super(parent, "heat resistance");
		
		maxLevel = 14;
		level = 6;
		levelStep = 1;
		upgradeCost = 1;
		unlockCost = 1;
	}
	
	@EventHandler
	public void on(EntityDamageEvent e)
	{
		if(e.getEntity() instanceof Player)
		{
			Player p = (Player) e.getEntity();
			
			if(isUnlocked(p) && isEnabled(p))
			{
				int heat = p.getFireTicks();
				
				heat = (int) (heat * getMitigation((int) getLevel(p)));
				
				if(heat > 0)
				{
					p.setFireTicks(heat);
				}
				
				new GSound(Sound.HORSE_ARMOR, 1f, (float) (0.4 + Math.random() * 0.4)).play(p.getLocation());
			}
		}
	}
	
	public double getMitigation(int level)
	{
		return ((double) level / (double) getMaxLevel()) * maxMitigation;
	}
	
	@Override
	public MaterialBlock getMaterialBlock()
	{
		return new MaterialBlock(Material.FIREBALL);
	}
	
	@Override
	public String getDescription()
	{
		return "Cool off from fire quicker.";
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
			return C.LIGHT_PURPLE + F.pc(getMitigation(1)) + " " + getAbilityGraph(20, (double) getLevel(p) / (double) getMaxLevel(), "") + C.LIGHT_PURPLE + " " + F.pc(getMitigation(getMaxLevel())) + " Reduction";
		}
		
		return C.LIGHT_PURPLE + F.pc(getMitigation(1)) + " " + getAbilityGraph(20, (double) getLevel(p) / (double) getMaxLevel(), F.pc(getMitigation((int) getLevel(p)))) + C.LIGHT_PURPLE + " " + F.pc(getMitigation(getMaxLevel())) + " Reduction";
	}
	
	@Override
	public String getGraphInitial()
	{
		return F.pc(getMitigation(1));
	}
	
	@Override
	public String getGraphMax()
	{
		return F.pc(getMitigation(getMaxLevel()));
	}
	
	@Override
	public String getGraphCurrent(int level)
	{
		return F.pc(getMitigation(level));
	}
}
