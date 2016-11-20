package org.cyberpwn.icing.skills;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.cyberpwn.icing.abilities.FarmingTiller;
import org.cyberpwn.icing.skill.BasicSkill;
import org.cyberpwn.icing.xp.XP;
import org.cyberpwn.icing.xp.XPReason;
import org.phantomapi.clust.Keyed;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Ticked;
import org.phantomapi.world.MaterialBlock;
import org.phantomapi.world.W;

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
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void on(PlayerInteractEvent e)
	{
		if(e.isCancelled())
		{
			return;
		}
		
		if(!XP.isReady(e.getPlayer()))
		{
			return;
		}
		
		if(e.getItem() != null && e.getItem().getType().equals(Material.INK_SACK) && e.getItem().getData().getData() == 15)
		{
			if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
			{
				if(e.getClickedBlock().getType().equals(Material.GRASS) || e.getClickedBlock().getType().equals(Material.CROPS) || e.getClickedBlock().getType().equals(Material.SAPLING))
				{
					XP.dropRandom(e.getPlayer().getLocation());
					
					if(e.getPlayer().getGameMode().equals(GameMode.CREATIVE))
					{
						return;
					}
					
					addReward(e.getPlayer(), 1);
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void on(BlockBreakEvent e)
	{
		if(e.isCancelled())
		{
			return;
		}
		
		if(!XP.isReady(e.getPlayer()))
		{
			return;
		}
		
		if(e.getPlayer().getGameMode().equals(GameMode.CREATIVE))
		{
			return;
		}
		
		if(e.getBlock().getType().equals(Material.CROPS))
		{
			@SuppressWarnings("deprecation")
			int l = e.getBlock().getData();
			addReward(e.getPlayer(), 2 + (l * mul));
			XP.dropRandom(e.getBlock().getLocation());
		}
		
		if(e.getBlock().getType().equals(Material.MELON_BLOCK))
		{
			addReward(e.getPlayer(), 1);
			XP.dropRandom(e.getBlock().getLocation());
		}
		
		if(e.getBlock().getType().equals(Material.PUMPKIN))
		{
			addReward(e.getPlayer(), 1);
			XP.dropRandom(e.getBlock().getLocation());
		}
		
		if(e.getBlock().getType().equals(Material.CACTUS))
		{
			int f = 1;
			Block c = W.toAsync(e.getBlock());
			
			while(f < 256)
			{
				if(c.getRelative(BlockFace.UP).getType().equals(Material.CACTUS))
				{
					f++;
					c = c.getRelative(BlockFace.UP);
				}
				
				else
				{
					break;
				}
			}
			
			addReward(e.getPlayer(), 2 * f);
			XP.dropRandom(e.getBlock().getLocation());
		}
		
		if(e.getBlock().getType().equals(Material.SUGAR_CANE_BLOCK))
		{
			int f = 1;
			Block c = W.toAsync(e.getBlock());
			
			while(f < 256)
			{
				if(c.getRelative(BlockFace.UP).getType().equals(Material.SUGAR_CANE_BLOCK))
				{
					f++;
					c = c.getRelative(BlockFace.UP);
				}
				
				else
				{
					break;
				}
			}
			
			addReward(e.getPlayer(), 2 * f);
			XP.dropRandom(e.getBlock().getLocation());
		}
	}
	
	@Override
	public MaterialBlock getSkillMaterial()
	{
		return new MaterialBlock(Material.WHEAT);
	}
	
	@Override
	public void createControllers()
	{
		register(new FarmingTiller(this));
	}
}
