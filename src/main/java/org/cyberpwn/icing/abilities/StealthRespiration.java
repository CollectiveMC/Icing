package org.cyberpwn.icing.abilities;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.cyberpwn.icing.ability.BasicAbility;
import org.cyberpwn.icing.skill.Skill;
import org.phantomapi.event.PlayerMoveBlockEvent;
import org.phantomapi.util.C;
import org.phantomapi.util.F;
import org.phantomapi.world.MaterialBlock;

public class StealthRespiration extends BasicAbility
{
	public StealthRespiration(Skill parent)
	{
		super(parent, "respiration");
		
		maxLevel = 13;
		level = 12;
		levelStep = 1;
		upgradeCost = 3;
		unlockCost = 6;
	}
	
	@EventHandler
	public void on(PlayerMoveBlockEvent e)
	{
		if(isUnlocked(e.getPlayer()))
		{
			if(isEnabled(e.getPlayer()))
			{
				e.getPlayer().setMaximumAir(30 * (10 + (int) getLevel(e.getPlayer())));
			}
			
			else
			{
				e.getPlayer().setMaximumAir(300);
			}
		}
	}
	
	public String getV(int level)
	{
		return ((10 + level) * 1.5) + "s";
	}
	
	public double getMitigation(int level)
	{
		return ((double) level / (double) getMaxLevel());
	}
	
	@Override
	public MaterialBlock getMaterialBlock()
	{
		return new MaterialBlock(Material.GLASS_BOTTLE);
	}
	
	@Override
	public String getDescription()
	{
		return "Increases time underwater.";
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
		return getV(1);
	}
	
	@Override
	public String getGraphMax()
	{
		return getV(getMaxLevel());
	}
	
	@Override
	public String getGraphCurrent(int level)
	{
		return getV(level);
	}
}
