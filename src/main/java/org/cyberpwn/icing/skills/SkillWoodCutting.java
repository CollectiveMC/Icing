package org.cyberpwn.icing.skills;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.cyberpwn.icing.skill.BasicSkill;
import org.cyberpwn.icing.xp.XPReason;
import org.phantomapi.clust.Keyed;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Ticked;
import org.phantomapi.world.MaterialBlock;

@Ticked(55)
public class SkillWoodCutting extends BasicSkill
{
	@Keyed("base")
	public int base = 0;
	
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
	
	@EventHandler
	public void on(BlockBreakEvent e)
	{
		if(e.getBlock().getType().equals(Material.LOG))
		{
			addReward(e.getPlayer(), 4);
		}
		
		if(e.getBlock().getType().equals(Material.LOG_2))
		{
			addReward(e.getPlayer(), 4);
		}
		
		if(e.getBlock().getType().equals(Material.LEAVES))
		{
			addReward(e.getPlayer(), 1);
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
