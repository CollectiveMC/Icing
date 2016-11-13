package org.cyberpwn.icing.skills;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.cyberpwn.icing.skill.BasicSkill;
import org.cyberpwn.icing.xp.XP;
import org.cyberpwn.icing.xp.XPReason;
import org.phantomapi.clust.Keyed;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Ticked;
import org.phantomapi.world.MaterialBlock;

@Ticked(70)
public class SkillWoodCutting extends BasicSkill
{
	@Keyed("base")
	public int base = 0;
	
	@Keyed("leaves")
	public int le = 1;
	
	@Keyed("logs")
	public int lo = 4;
	
	public SkillWoodCutting(Controllable parentController)
	{
		super(parentController, "woodcutting", XPReason.WOOD_CUTTING);
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
		
		if(e.getPlayer().getGameMode().equals(GameMode.CREATIVE))
		{
			return;
		}
		
		if(e.getBlock().getType().equals(Material.LOG))
		{
			addReward(e.getPlayer(), lo);
			addReward(e.getPlayer(), base);
			XP.dropRandom(e.getBlock().getLocation());
		}
		
		if(e.getBlock().getType().equals(Material.LOG_2))
		{
			addReward(e.getPlayer(), lo);
			addReward(e.getPlayer(), base);
			XP.dropRandom(e.getBlock().getLocation());
		}
		
		if(e.getBlock().getType().equals(Material.LEAVES))
		{
			addReward(e.getPlayer(), le);
			addReward(e.getPlayer(), base);
			XP.dropRandom(e.getBlock().getLocation());
		}
		
		if(e.getBlock().getType().equals(Material.LEAVES_2))
		{
			addReward(e.getPlayer(), le);
			addReward(e.getPlayer(), base);
			XP.dropRandom(e.getBlock().getLocation());
		}
	}
	
	@Override
	public MaterialBlock getSkillMaterial()
	{
		return new MaterialBlock(Material.IRON_AXE);
	}
	
	@Override
	public void createControllers()
	{
		
	}
}
