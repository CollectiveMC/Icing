package org.cyberpwn.icing.skills;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.cyberpwn.icing.skill.BasicSkill;
import org.cyberpwn.icing.xp.XP;
import org.cyberpwn.icing.xp.XPReason;
import org.phantomapi.clust.Keyed;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Ticked;
import org.phantomapi.event.FalseBlockBreakEvent;
import org.phantomapi.lang.GMap;
import org.phantomapi.world.MaterialBlock;

@Ticked(40)
public class SkillExcavation extends BasicSkill
{
	private GMap<Player, Integer> exc;
	
	@Keyed("blocks-per-xp")
	public int bpxp = 6;
	
	public SkillExcavation(Controllable parentController)
	{
		super(parentController, "excavation", XPReason.EXCAVATION);
		
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
	public void on(FalseBlockBreakEvent e)
	{
		if(e.isCancelled())
		{
			return;
		}
		
		on(new BlockBreakEvent(e.getBlock(), e.getPlayer()));
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void on(BlockBreakEvent e)
	{
		if(e.isCancelled())
		{
			return;
		}
		
		if(!exc.containsKey(e.getPlayer()))
		{
			exc.put(e.getPlayer(), 0);
		}
		
		exc.put(e.getPlayer(), exc.get(e.getPlayer()) + 1);
		XP.dropRandom(e.getBlock().getLocation());
		
		if(exc.get(e.getPlayer()) > 150 + (Math.random() * 50))
		{
			addReward(e.getPlayer(), exc.get(e.getPlayer()) / bpxp);
			exc.remove(e.getPlayer());
		}
	}
	
	@Override
	public MaterialBlock getSkillMaterial()
	{
		return new MaterialBlock(Material.IRON_SPADE);
	}
	
	@Override
	public void createControllers()
	{
		
	}
}
