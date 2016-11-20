package org.cyberpwn.icing.abilities;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.cyberpwn.icing.ability.BasicAbility;
import org.cyberpwn.icing.skill.Skill;
import org.cyberpwn.icing.xp.XP;
import org.phantomapi.clust.Keyed;
import org.phantomapi.sync.TaskLater;
import org.phantomapi.util.C;
import org.phantomapi.util.F;
import org.phantomapi.util.M;
import org.phantomapi.world.MaterialBlock;

public class ExcavationScavenger extends BasicAbility
{
	@Keyed("max-chance")
	public double maxPercent = 0.48;
	
	public ExcavationScavenger(Skill parent)
	{
		super(parent, "scavenger");
		
		maxLevel = 12;
		level = 4;
		levelStep = 2;
		upgradeCost = 1;
		unlockCost = 1;
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void on(BlockBreakEvent e)
	{
		if(!XP.isReady(e.getPlayer()))
		{
			return;
		}
		
		if(isUnlocked(e.getPlayer()) && isEnabled(e.getPlayer()))
		{
			if(e.isCancelled())
			{
				return;
			}
			
			MaterialBlock m = new MaterialBlock(e.getBlock().getLocation());
			Player p = e.getPlayer();
			double pc = getPercent((int) getLevel(p));
			
			ItemStack is = p.getItemInHand();
			
			if(is != null && (is.getType().equals(Material.SHEARS) || is.getEnchantments().containsKey(Enchantment.SILK_TOUCH)))
			{
				return;
			}
			
			if(m.getMaterial().equals(Material.LONG_GRASS) && m.getData() == 1)
			{
				if(M.r(pc / 1.5))
				{
					new TaskLater()
					{
						@Override
						public void run()
						{
							ItemStack is = new ItemStack(Material.SEEDS);
							e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation().add(0.5, 0.5, 0.5), is);
						}
					};
				}
			}
			
			if(m.getMaterial().equals(Material.LEAVES) || m.getMaterial().equals(Material.LEAVES_2))
			{
				if(M.r(pc / 2))
				{
					new TaskLater()
					{
						@Override
						public void run()
						{
							ItemStack is = new ItemStack(Material.APPLE);
							e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation().add(0.5, 0.5, 0.5), is);
						}
					};
				}
			}
		}
	}
	
	@Override
	public MaterialBlock getMaterialBlock()
	{
		return new MaterialBlock(Material.SEEDS);
	}
	
	@Override
	public String getDescription()
	{
		return "Increases drop chances for items.";
	}
	
	@Override
	public void onStart()
	{
		
	}
	
	@Override
	public void onStop()
	{
		
	}
	
	public double getPercent(int level)
	{
		return maxPercent * ((double) ((double) level / (double) maxLevel));
	}
	
	public String vs(int level)
	{
		return "+ " + F.pc(getPercent(level));
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
