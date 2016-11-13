package org.cyberpwn.icing.abilities;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.cyberpwn.icing.ability.BasicAbility;
import org.cyberpwn.icing.skill.Skill;
import org.phantomapi.util.C;
import org.phantomapi.util.F;
import org.phantomapi.util.M;
import org.phantomapi.world.MaterialBlock;
import org.phantomapi.world.PE;

public class StealthSwiftness extends BasicAbility
{
	public StealthSwiftness(Skill parent)
	{
		super(parent, "swiftness");
		
		maxLevel = 5;
		level = 6;
		levelStep = 1;
		upgradeCost = 1;
		unlockCost = 2;
	}
	
	@EventHandler
	public void on(PlayerToggleSneakEvent e)
	{
		if(isUnlocked(e.getPlayer()) && isEnabled(e.getPlayer()) && !e.getPlayer().isFlying() && !e.getPlayer().getGameMode().equals(GameMode.CREATIVE))
		{
			if(e.isSneaking())
			{
				if(!e.getPlayer().hasPotionEffect(PotionEffectType.SPEED))
				{
					PE.SPEED.a((int) getLevel(e.getPlayer()) - 1).d(2000).c(e.getPlayer());
				}
			}
			
			else
			{
				for(PotionEffect i : e.getPlayer().getActivePotionEffects())
				{
					if(i.getType().equals(PotionEffectType.SPEED))
					{
						e.getPlayer().removePotionEffect(PotionEffectType.SPEED);
					}
				}
			}
		}
	}
	
	public String getV(int level)
	{
		return "Speed " + M.toRoman(level);
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
		return "Increases speed while sneaking & higher field of view.";
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
