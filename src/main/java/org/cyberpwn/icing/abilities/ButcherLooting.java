package org.cyberpwn.icing.abilities;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.cyberpwn.icing.ability.BasicAbility;
import org.cyberpwn.icing.skill.Skill;
import org.phantomapi.clust.Keyed;
import org.phantomapi.lang.GList;
import org.phantomapi.util.C;
import org.phantomapi.util.F;
import org.phantomapi.util.M;
import org.phantomapi.world.MaterialBlock;

public class ButcherLooting extends BasicAbility
{
	@Keyed("mult")
	public double mult = 3.67;
	
	public ButcherLooting(Skill parent)
	{
		super(parent, "looting");
		
		maxLevel = 18;
		level = 6;
		levelStep = 2;
		upgradeCost = 1;
		unlockCost = 1;
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void on(EntityDeathEvent e)
	{
		if(e.getEntity().getKiller() != null)
		{
			Player p = e.getEntity().getKiller();
			
			if(isUnlocked(p) && isEnabled(p) && !p.getGameMode().equals(GameMode.CREATIVE))
			{
				int level = (int) getLevel(p);
				double mit = getMitigation(level);
				
				if(!e.getEntityType().equals(EntityType.HORSE) && !e.getEntityType().equals(EntityType.PLAYER))
				{
					if(e.getEntity() instanceof LivingEntity)
					{
						for(ItemStack i : new GList<ItemStack>(e.getDrops()))
						{
							double m = mit;
							
							while(m > 1.0)
							{
								e.getDrops().add(i.clone());
								m -= 1.0;
							}
							
							if(M.r(m))
							{
								e.getDrops().add(i.clone());
							}
						}
					}
				}
			}
		}
		
	}
	
	public String getV(int level)
	{
		return F.pc(getMitigation(level));
	}
	
	public double getMitigation(int level)
	{
		return ((double) (mult * level) / ((double) getMaxLevel()));
	}
	
	@Override
	public MaterialBlock getMaterialBlock()
	{
		return new MaterialBlock(Material.GOLD_INGOT);
	}
	
	@Override
	public String getDescription()
	{
		return "Increases the drops of killed mobs";
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
