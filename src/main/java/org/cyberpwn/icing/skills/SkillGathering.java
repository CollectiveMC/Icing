package org.cyberpwn.icing.skills;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.cyberpwn.icing.abilities.GatheringScavenger;
import org.cyberpwn.icing.abilities.GatheringSnatching;
import org.cyberpwn.icing.skill.BasicSkill;
import org.cyberpwn.icing.xp.XP;
import org.cyberpwn.icing.xp.XPReason;
import org.phantomapi.clust.Keyed;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Ticked;
import org.phantomapi.lang.GMap;
import org.phantomapi.world.MaterialBlock;

@Ticked(40)
public class SkillGathering extends BasicSkill
{
	private GMap<Player, Integer> exc;
	
	@Keyed("pickups-per-xp")
	public int bpxp = 3;
	
	public SkillGathering(Controllable parentController)
	{
		super(parentController, "gathering", XPReason.GATHERING);
		
		exc = new GMap<Player, Integer>();
	}
	
	@Override
	public void onStart()
	{
		
	}
	
	@Override
	public void onTick()
	{
		popRewardMap();
	}
	
	@Override
	public void onStop()
	{
		
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void on(BlockBreakEvent e)
	{
		if(e.isCancelled())
		{
			return;
		}
		
		int rw = 0;
		
		if(e.getBlock().getType().equals(Material.CLAY))
		{
			rw = 4;
		}
		
		if(e.getBlock().getType().equals(Material.LONG_GRASS))
		{
			rw = 1;
		}
		
		if(e.getBlock().getType().equals(Material.COCOA))
		{
			rw = 1;
		}
		
		if(e.getBlock().getType().equals(Material.YELLOW_FLOWER))
		{
			rw = 1;
		}
		
		if(e.getBlock().getType().equals(Material.WEB))
		{
			rw = 1;
		}
		
		if(e.getBlock().getType().equals(Material.WATER_LILY))
		{
			rw = 1;
		}
		
		if(e.getBlock().getType().equals(Material.LEAVES))
		{
			rw = 1;
		}
		
		if(e.getBlock().getType().equals(Material.LEAVES_2))
		{
			rw = 1;
		}
		
		if(rw > 0)
		{
			if(!exc.containsKey(e.getPlayer()))
			{
				exc.put(e.getPlayer(), 0);
			}
			
			exc.put(e.getPlayer(), exc.get(e.getPlayer()) + rw);
			XP.dropRandom(e.getBlock().getLocation());
			
			if(exc.get(e.getPlayer()) > 150 + (Math.random() * 50))
			{
				addReward(e.getPlayer(), exc.get(e.getPlayer()) / bpxp);
				exc.remove(e.getPlayer());
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void on(PlayerPickupItemEvent e)
	{
		if(e.isCancelled())
		{
			return;
		}
		
		if(!exc.containsKey(e.getPlayer()))
		{
			exc.put(e.getPlayer(), 0);
		}
		
		exc.put(e.getPlayer(), exc.get(e.getPlayer()) + e.getItem().getItemStack().getAmount());
		XP.dropRandom(e.getItem().getLocation());
		
		if(exc.get(e.getPlayer()) > 30 + (Math.random() * 10))
		{
			addReward(e.getPlayer(), exc.get(e.getPlayer()) / bpxp);
			exc.remove(e.getPlayer());
		}
	}
	
	@Override
	public MaterialBlock getSkillMaterial()
	{
		return new MaterialBlock(Material.LEATHER);
	}
	
	@Override
	public void createControllers()
	{
		register(new GatheringSnatching(this));
		register(new GatheringScavenger(this));
	}
}
