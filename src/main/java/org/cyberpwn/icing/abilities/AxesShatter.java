package org.cyberpwn.icing.abilities;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.cyberpwn.icing.ability.BasicAbility;
import org.cyberpwn.icing.skill.Skill;
import org.cyberpwn.icing.xp.XP;
import org.phantomapi.lang.GSound;
import org.phantomapi.util.C;
import org.phantomapi.util.F;
import org.phantomapi.util.Items;
import org.phantomapi.util.M;
import org.phantomapi.world.MaterialBlock;

public class AxesShatter extends BasicAbility
{
	public AxesShatter(Skill parent)
	{
		super(parent, "shatter");
		
		maxLevel = 4;
		level = 12;
		levelStep = 3;
		upgradeCost = 1;
		unlockCost = 4;
	}
	
	@EventHandler
	public void on(EntityDamageByEntityEvent e)
	{
		if(e.getEntity() instanceof Player)
		{
			Player p = (Player) e.getEntity();
			
			if(!XP.isReady(p))
			{
				return;
			}
			
			if(e.getDamager() instanceof Player)
			{
				Player d = (Player) e.getDamager();
				
				if(!XP.isReady(d))
				{
					return;
				}
				
				if(isUnlocked(d) && isEnabled(d))
				{
					int level = (int) getLevel(d);
					
					for(ItemStack is : p.getInventory().getArmorContents())
					{
						if(M.r(0.4))
						{
							Items.damage(is, (int) (Math.random() * (level * 4)));
						}
					}
					
					if(Items.isBroken(p.getInventory().getHelmet()))
					{
						p.getInventory().setHelmet(new ItemStack(Material.AIR));
						new GSound(Sound.ITEM_BREAK).play(p.getLocation());
					}
					
					if(Items.isBroken(p.getInventory().getChestplate()))
					{
						p.getInventory().setChestplate(new ItemStack(Material.AIR));
						new GSound(Sound.ITEM_BREAK).play(p.getLocation());
					}
					
					if(Items.isBroken(p.getInventory().getLeggings()))
					{
						p.getInventory().setLeggings(new ItemStack(Material.AIR));
						new GSound(Sound.ITEM_BREAK).play(p.getLocation());
					}
					
					if(Items.isBroken(p.getInventory().getBoots()))
					{
						p.getInventory().setBoots(new ItemStack(Material.AIR));
						new GSound(Sound.ITEM_BREAK).play(p.getLocation());
					}
				}
			}
		}
	}
	
	public double getResistanceBonus(int level)
	{
		return level * 4;
	}
	
	@Override
	public MaterialBlock getMaterialBlock()
	{
		return new MaterialBlock(Material.CHAINMAIL_CHESTPLATE);
	}
	
	@Override
	public String getDescription()
	{
		return "Damages armor faster.";
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
	
	@Override
	public String getGraphInitial()
	{
		return "" + getResistanceBonus(1);
	}
	
	@Override
	public String getGraphMax()
	{
		return "" + getResistanceBonus(getMaxLevel());
	}
	
	@Override
	public String getGraphCurrent(int level)
	{
		return "" + getResistanceBonus(level);
	}
}
