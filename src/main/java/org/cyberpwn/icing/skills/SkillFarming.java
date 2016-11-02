package org.cyberpwn.icing.skills;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.cyberpwn.icing.skill.BasicSkill;
import org.cyberpwn.icing.xp.XPReason;
import org.phantomapi.clust.Keyed;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Ticked;
import org.phantomapi.world.MaterialBlock;

@Ticked(40)
public class SkillFarming extends BasicSkill
{
	@Keyed("crop-multiplier")
	public int mul = 3;
	
	public SkillFarming(Controllable parentController)
	{
		super(parentController, "farming", XPReason.FARMING);
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
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void on(PlayerInteractEvent e)
	{
		if(e.getItem() != null && e.getItem().getType().equals(Material.INK_SACK) && e.getItem().getData().getData() == 15)
		{
			if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
			{
				if(e.getClickedBlock().getType().equals(Material.GRASS) || e.getClickedBlock().getType().equals(Material.CROPS) || e.getClickedBlock().getType().equals(Material.SAPLING))
				{
					addReward(e.getPlayer(), 1);
				}
			}
		}
	}
	
	@EventHandler
	public void on(BlockBreakEvent e)
	{
		if(e.getBlock().getType().equals(Material.CROPS))
		{
			@SuppressWarnings("deprecation")
			int l = e.getBlock().getData();
			addReward(e.getPlayer(), 2 + (l * mul));
		}
		
		if(e.getBlock().getType().equals(Material.MELON_BLOCK))
		{
			addReward(e.getPlayer(), 1);
		}
		
		if(e.getBlock().getType().equals(Material.PUMPKIN))
		{
			addReward(e.getPlayer(), 1);
		}
	}
	
	@Override
	public MaterialBlock getSkillMaterial()
	{
		return new MaterialBlock(Material.WHEAT);
	}
}
