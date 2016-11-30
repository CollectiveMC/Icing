package org.cyberpwn.icing.abilities;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.cyberpwn.icing.ability.BasicAbility;
import org.cyberpwn.icing.skill.Skill;
import org.cyberpwn.icing.xp.XP;
import org.phantomapi.lang.GList;
import org.phantomapi.sync.Task;
import org.phantomapi.util.C;
import org.phantomapi.util.F;
import org.phantomapi.world.Blocks;
import org.phantomapi.world.Cuboid;
import org.phantomapi.world.MaterialBlock;

public class MiningShredding extends BasicAbility
{
	public MiningShredding(Skill parent)
	{
		super(parent, "shredding");
		
		maxLevel = 6;
		level = 12;
		levelStep = 3;
		upgradeCost = 2;
		unlockCost = 5;
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
		
		Player p = e.getPlayer();
		Block b = e.getBlock();
		
		if(!XP.isReady(p))
		{
			return;
		}
		
		int level = (int) getLevel(p);
		GList<Block> blocks = new GList<Block>();
		
		if(isOre(b) && isUnlocked(p) && isEnabled(p))
		{
			ItemStack is = p.getItemInHand();
			
			if(is != null && is.getType().toString().contains("PICKAXE"))
			{
				int kvl = level > 2 ? 2 : level;
				ItemStack iv = is.clone();
				Cuboid cc = new Cuboid(b.getLocation().clone().add(kvl, kvl, kvl), b.getLocation().clone().add(-kvl, -kvl, -kvl));
				blocks.add(new GList<Block>(cc.iterator()));
				
				for(Block i : blocks.copy())
				{
					if(!i.getType().equals(b.getType()) || i.equals(b) || !Blocks.canModify(p, i) || p.getLocation().distance(i.getLocation()) > 5)
					{
						blocks.remove(i);
					}
				}
				
				blocks.shuffle();
				
				new Task(0)
				{
					@Override
					public void run()
					{
						if(blocks.isEmpty())
						{
							cancel();
							return;
						}
						
						for(int i = 0; i < level; i++)
						{
							if(blocks.isEmpty())
							{
								cancel();
								return;
							}
							
							blocks.pop().breakNaturally(iv);
						}
					}
				};
			}
		}
	}
	
	public boolean isOre(Block block)
	{
		return block.getType().toString().endsWith("_ORE");
	}
	
	public double getResistanceBonus(int level)
	{
		return level * 4;
	}
	
	@Override
	public MaterialBlock getMaterialBlock()
	{
		return new MaterialBlock(Material.GOLD_PICKAXE);
	}
	
	@Override
	public String getDescription()
	{
		return "Breaks multiple ores at a time.";
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
			return C.LIGHT_PURPLE + F.f(getResistanceBonus(1), 1) + " " + getAbilityGraph(20, (double) getLevel(p) / (double) getMaxLevel(), "") + C.LIGHT_PURPLE + " " + F.f(getResistanceBonus(getMaxLevel()), 1) + " Damage";
		}
		
		return C.LIGHT_PURPLE + F.f(getResistanceBonus(1), 1) + " " + getAbilityGraph(20, (double) getLevel(p) / (double) getMaxLevel(), F.f(getResistanceBonus((int) getLevel(p)), 1)) + C.LIGHT_PURPLE + " " + F.f(getResistanceBonus(getMaxLevel()), 1) + " Damage";
	}
	
	@Override
	public String getGraphInitial()
	{
		return "" + getResistanceBonus(1);
	}
	
	@Override
	public String getGraphMax()
	{
		return "" + getResistanceBonus(getMaxLevel());
	}
	
	@Override
	public String getGraphCurrent(int level)
	{
		return "" + getResistanceBonus(level);
	}
}
