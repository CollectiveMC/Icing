package org.cyberpwn.icing.abilities;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.cyberpwn.icing.ability.BasicAbility;
import org.cyberpwn.icing.skill.Skill;
import org.phantomapi.util.C;
import org.phantomapi.util.F;
import org.phantomapi.world.MaterialBlock;
import org.phantomapi.world.PE;

public class ExcavationNuker extends BasicAbility
{
	public ExcavationNuker(Skill parent)
	{
		super(parent, "nuker");
		
		maxLevel = 4;
		level = 12;
		levelStep = 2;
		upgradeCost = 16;
		unlockCost = 8;
	}
	
	@EventHandler
	public void on(PlayerInteractEvent e)
	{
		if(isUnlocked(e.getPlayer()))
		{
			if(isEnabled(e.getPlayer()))
			{
				PE.FAST_DIGGING.a((int) getLevel(e.getPlayer()) - 1).d(200).apply(e.getPlayer());
			}
		}
	}
	
	public String getV(int level)
	{
		return F.pc(getMitigation(level));
	}
	
	public double getMitigation(int level)
	{
		return ((double) level / (double) getMaxLevel());
	}
	
	@Override
	public MaterialBlock getMaterialBlock()
	{
		return new MaterialBlock(Material.BLAZE_POWDER);
	}
	
	@Override
	public String getDescription()
	{
		return "TEAR THE WORLD APART";
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
