package org.cyberpwn.icing.skills;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
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
import org.phantomapi.world.MaterialBlock;

@Ticked(50)
public class SkillMining extends BasicSkill
{
	@Keyed("base")
	public int base = 0;
	
	public SkillMining(Controllable parentController)
	{
		super(parentController, "miner", XPReason.MINING);
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
		if(e.getPlayer().getGameMode().equals(GameMode.CREATIVE))
		{
			return;
		}
		
		if(e.isCancelled())
		{
			return;
		}
		
		if(e.getBlock().getType().equals(Material.GLOWSTONE))
		{
			addReward(e.getPlayer(), 4);
			return;
		}
		
		if(e.getBlock().getType().toString().endsWith("_ORE") && e.getPlayer().getItemInHand() != null && e.getPlayer().getItemInHand().getType().toString().endsWith("_PICKAXE"))
		{
			if(e.getPlayer().getItemInHand().containsEnchantment(Enchantment.SILK_TOUCH))
			{
				return;
			}
			
			int xp = base;
			
			switch(e.getBlock().getType())
			{
				case DIAMOND_ORE:
					xp = 48;
					break;
				case QUARTZ_ORE:
					xp = 8;
					break;
				case REDSTONE_ORE:
					xp = 12;
					break;
				case LAPIS_ORE:
					xp = 36;
					break;
				case COAL_ORE:
					xp = 4;
					break;
				case EMERALD_ORE:
					xp = 72;
					break;
				default:
					break;
			}
			
			if(xp > 0)
			{
				addReward(e.getPlayer(), xp);
				XP.dropRandom(e.getBlock().getLocation());
			}
		}
	}
	
	@Override
	public MaterialBlock getSkillMaterial()
	{
		return new MaterialBlock(Material.IRON_PICKAXE);
	}
	
	@Override
	public void createControllers()
	{
		
	}
}
