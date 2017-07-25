package org.cyberpwn.icing.abilities;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.cyberpwn.icing.ability.BasicAbility;
import org.cyberpwn.icing.skill.Skill;
import org.cyberpwn.icing.xp.XP;
import org.phantomapi.construct.Ticked;
import org.phantomapi.lang.GList;
import org.phantomapi.lang.GSound;
import org.phantomapi.nms.NMSX;
import org.phantomapi.sync.TaskLater;
import org.phantomapi.util.C;
import org.phantomapi.util.F;
import org.phantomapi.util.Inventories;
import org.phantomapi.world.Area;
import org.phantomapi.world.Blocks;
import org.phantomapi.world.MaterialBlock;

@Ticked(0)
public class StealthSnatching extends BasicAbility
{
	public double maxRange = 2.0;
	private GList<Integer> holds;
	
	public StealthSnatching(Skill parent)
	{
		super(parent, "snatching");
		
		maxLevel = 6;
		level = 4;
		levelStep = 4;
		upgradeCost = 3;
		unlockCost = 1;
		holds = new GList<Integer>();
	}
	
	@Override
	public void onTick()
	{
		for(Player i : new GList<Player>(onlinePlayers()).shuffleCopy())
		{
			if(!XP.isReady(i))
			{
				continue;
			}
			
			if(isEnabled(i) && isUnlocked(i) && i.isSneaking() && !i.isDead())
			{
				double range = getRange((int) getLevel(i));
				Area a = new Area(i.getLocation(), range);
				Area ap = new Area(i.getLocation(), 25);
				
				for(Entity j : a.getNearbyEntities())
				{
					if(j instanceof Item && !holds.contains(j.getEntityId()))
					{
						double dist = j.getLocation().distanceSquared(i.getLocation());
						
						if(dist < range * range && i.isSneaking() && j.getTicksLived() > 1)
						{
							ItemStack is = ((Item) j).getItemStack().clone();
							
							if(Inventories.hasSpace(i.getInventory(), is) && Blocks.canModify(i, j.getLocation().getBlock()))
							{
								holds.add(j.getEntityId());
								
								if(i.isSneaking())
								{
									new GSound(Sound.ENTITY_HORSE_ARMOR, 1f, (float) (1.0 + (Math.random() / 3))).play(i.getLocation());
								}
								
								i.getInventory().addItem(is);
								
								for(Player k : ap.getNearbyPlayers())
								{
									NMSX.showPickup(k, i, j);
								}
								
								j.remove();
								
								int id = j.getEntityId();
								
								new TaskLater()
								{
									@Override
									public void run()
									{
										holds.remove(new Integer(id));
									}
								};
							}
						}
					}
				}
			}
		}
	}
	
	@Override
	public MaterialBlock getMaterialBlock()
	{
		return new MaterialBlock(Material.GHAST_TEAR);
	}
	
	@Override
	public String getDescription()
	{
		return "Increases the range at which you can pick up items";
	}
	
	@Override
	public void onStart()
	{
		
	}
	
	@Override
	public void onStop()
	{
		
	}
	
	public double getRange(int level)
	{
		return 1.5 + ((double) ((double) level / (double) maxLevel) * (double) maxRange);
	}
	
	public String vs(int level)
	{
		return F.f(getRange(level), 1) + "m";
	}
	
	@Override
	public String getStatGraph(Player p)
	{
		if(getLevel() == getMaxLevel())
		{
			return C.LIGHT_PURPLE + vs(1) + " " + getAbilityGraph(20, (double) getLevel(p) / (double) getMaxLevel(), "") + C.LIGHT_PURPLE + " " + vs(getMaxLevel());
		}
		
		return C.LIGHT_PURPLE + vs(1) + " " + getAbilityGraph(20, (double) getLevel(p) / (double) getMaxLevel(), vs((int) getLevel(p))) + C.LIGHT_PURPLE + " " + vs(getMaxLevel());
	}
	
	@Override
	public String getGraphInitial()
	{
		return vs(1);
	}
	
	@Override
	public String getGraphMax()
	{
		return vs(getMaxLevel());
	}
	
	@Override
	public String getGraphCurrent(int level)
	{
		return vs(level);
	}
}
