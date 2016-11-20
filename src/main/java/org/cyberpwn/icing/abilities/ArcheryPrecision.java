package org.cyberpwn.icing.abilities;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.cyberpwn.icing.ability.BasicAbility;
import org.cyberpwn.icing.skill.Skill;
import org.cyberpwn.icing.xp.XP;
import org.phantomapi.clust.Comment;
import org.phantomapi.clust.Keyed;
import org.phantomapi.lang.GList;
import org.phantomapi.nms.NMSX;
import org.phantomapi.sync.Task;
import org.phantomapi.util.C;
import org.phantomapi.util.F;
import org.phantomapi.util.P;
import org.phantomapi.world.MaterialBlock;
import org.phantomapi.world.W;

public class ArcheryPrecision extends BasicAbility
{
	@Comment("The initial range the rangefinder starts at")
	@Keyed("initial-range")
	public int initialRange = 42;
	
	@Comment("The range increase per upgrade")
	@Keyed("range-step")
	public int rangeStep = 12;
	
	private GList<Player> viewing;
	
	public ArcheryPrecision(Skill parent)
	{
		super(parent, "precision");
		
		viewing = new GList<Player>();
		maxLevel = 30;
		level = 5;
		levelStep = 2;
		upgradeCost = 1;
		unlockCost = 3;
	}
	
	@EventHandler
	public void on(ProjectileLaunchEvent e)
	{
		if(e.getEntity().getShooter() instanceof Player)
		{
			if(!XP.isReady((Player) e.getEntity().getShooter()))
			{
				return;
			}
			
			viewing.remove((Player) e.getEntity().getShooter());
		}
	}
	
	@EventHandler
	public void on(PlayerInteractEvent e)
	{
		if(!XP.isReady(e.getPlayer()))
		{
			return;
		}
		
		if(e.getAction().equals(Action.RIGHT_CLICK_AIR) && e.getPlayer().getItemInHand() != null && e.getPlayer().getItemInHand().getType().equals(Material.BOW) && isEnabled(e.getPlayer()) && isUnlocked(e.getPlayer()))
		{
			if(viewing.contains(e.getPlayer()))
			{
				viewing.remove(e.getPlayer());
				return;
			}
			
			viewing.add(e.getPlayer());
			
			new Task(20)
			{
				@Override
				public void run()
				{
					if(!viewing.contains(e.getPlayer()) || e.getPlayer().getItemInHand() == null || !e.getPlayer().getItemInHand().getType().equals(Material.BOW))
					{
						viewing.remove(e.getPlayer());
						cancel();
						return;
					}
					
					int distance = 0;
					String targ = "";
					Entity en = W.getEntityLookingAt(e.getPlayer(), initialRange + (getLevel(e.getPlayer()) * rangeStep), 1);
					
					if(en == null)
					{
						Location l = P.targetBlock(e.getPlayer(), (int) (initialRange + (getLevel(e.getPlayer()) * rangeStep)));
						
						if(!l.getBlock().getType().equals(Material.AIR))
						{
							distance = (int) l.distance(e.getPlayer().getLocation());
							targ = StringUtils.capitalize(l.getBlock().getType().toString().toLowerCase());
						}
					}
					
					else
					{
						distance = (int) en.getLocation().distance(e.getPlayer().getLocation());
						
						if(en instanceof Player)
						{
							targ = StringUtils.capitalize(((Player) en).getName());
						}
						
						else
						{
							targ = StringUtils.capitalize(en.getType().toString().toLowerCase());
						}
					}
					
					String text = "";
					
					if(distance > 0)
					{
						text = C.LIGHT_PURPLE + F.f(distance) + "m ";
						text = text + C.AQUA + "[" + targ + "]";
						
					}
					
					else
					{
						text = C.RED + "> " + F.f((int) (initialRange + (getLevel(e.getPlayer()) * rangeStep))) + "m ";
					}
					
					NMSX.sendActionBar(e.getPlayer(), text);
				}
			};
		}
	}
	
	public int getRange(int level)
	{
		return (int) (initialRange + (level * rangeStep));
	}
	
	@Override
	public MaterialBlock getMaterialBlock()
	{
		return new MaterialBlock(Material.COMPASS);
	}
	
	@Override
	public String getDescription()
	{
		return "Displays distance and type of target";
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
			return C.LIGHT_PURPLE + F.f(getRange(1)) + "m " + getAbilityGraph(20, (double) getLevel(p) / (double) getMaxLevel(), "") + C.LIGHT_PURPLE + " " + F.f(getRange(getMaxLevel())) + "m";
		}
		
		return C.LIGHT_PURPLE + F.f(getRange(1)) + "m " + getAbilityGraph(20, (double) getLevel(p) / (double) getMaxLevel(), F.f(getRange((int) getLevel(p))) + "m") + C.LIGHT_PURPLE + " " + F.f(getRange(getMaxLevel())) + "m";
	}
	
	@Override
	public String getGraphInitial()
	{
		return getRange(1) + "m";
	}
	
	@Override
	public String getGraphMax()
	{
		return getRange(getMaxLevel()) + "m";
	}
	
	@Override
	public String getGraphCurrent(int level)
	{
		return getRange(level) + "m";
	}
}
