package org.cyberpwn.icing.abilities;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.cyberpwn.icing.ability.BasicAbility;
import org.cyberpwn.icing.skill.Skill;
import org.cyberpwn.icing.xp.XP;
import org.phantomapi.clust.Comment;
import org.phantomapi.clust.Keyed;
import org.phantomapi.lang.GSound;
import org.phantomapi.util.C;
import org.phantomapi.util.F;
import org.phantomapi.util.Items;
import org.phantomapi.util.M;
import org.phantomapi.world.MaterialBlock;

public class AxesShatter extends BasicAbility
{
	@Comment("Damage to armor is as follows\nLEVEL * MULT. If the entropy matches up to at least one piece of armor that is.\nSince the max level is 4 and the default mult is 4, 16 max damage per piece per hit. (ignoring entropy)")
	@Keyed("level-mult")
	public int levelMult = 4;
	
	@Comment("Damage entropy is the amount of damage spread\nIf it is higher than 1.0, than 100% of the damage will be spread across all armor pieces\nIf it is at or below 0.0, no damage will ever be taken to armor.")
	@Keyed("damage-entropy")
	public double ent = 0.4;
	
	public AxesShatter(Skill parent)
	{
		super(parent, "shatter");
		
		maxLevel = 4;
		level = 12;
		levelStep = 3;
		upgradeCost = 1;
		unlockCost = 4;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void on(EntityDamageByEntityEvent e)
	{
		if(e.isCancelled())
		{
			return;
		}
		
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
						if(M.r(ent))
						{
							Items.damage(is, (int) (Math.random() * (level * levelMult)));
						}
					}
					
					if(Items.isBroken(p.getInventory().getHelmet()))
					{
						p.getInventory().setHelmet(new ItemStack(Material.AIR));
						new GSound(Sound.ENTITY_ITEM_BREAK).play(p.getLocation());
					}
					
					if(Items.isBroken(p.getInventory().getChestplate()))
					{
						p.getInventory().setChestplate(new ItemStack(Material.AIR));
						new GSound(Sound.ENTITY_ITEM_BREAK).play(p.getLocation());
					}
					
					if(Items.isBroken(p.getInventory().getLeggings()))
					{
						p.getInventory().setLeggings(new ItemStack(Material.AIR));
						new GSound(Sound.ENTITY_ITEM_BREAK).play(p.getLocation());
					}
					
					if(Items.isBroken(p.getInventory().getBoots()))
					{
						p.getInventory().setBoots(new ItemStack(Material.AIR));
						new GSound(Sound.ENTITY_ITEM_BREAK).play(p.getLocation());
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
