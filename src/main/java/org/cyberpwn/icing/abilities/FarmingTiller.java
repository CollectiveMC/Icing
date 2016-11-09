package org.cyberpwn.icing.abilities;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.cyberpwn.icing.ability.BasicAbility;
import org.cyberpwn.icing.skill.Skill;
import org.phantomapi.lang.GList;
import org.phantomapi.lang.GSound;
import org.phantomapi.nms.NMSX;
import org.phantomapi.sync.Task;
import org.phantomapi.util.C;
import org.phantomapi.world.Blocks;
import org.phantomapi.world.MaterialBlock;
import org.phantomapi.world.PhantomWorldQueue;
import org.phantomapi.world.W;

public class FarmingTiller extends BasicAbility
{
	public FarmingTiller(Skill parent)
	{
		super(parent, "tiller");
		
		maxLevel = 3;
		level = 9;
		levelStep = 6;
		upgradeCost = 4;
		unlockCost = 2;
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void on(PlayerInteractEvent e)
	{
		if(e.isCancelled())
		{
			return;
		}
		
		if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getClickedBlock() != null && (e.getClickedBlock().getType().equals(Material.GRASS) || e.getClickedBlock().getType().equals(Material.DIRT) || e.getClickedBlock().getType().equals(Material.SOIL)) && e.getPlayer().getItemInHand() != null && e.getPlayer().getItemInHand().getType().toString().endsWith("_HOE"))
		{
			Player p = e.getPlayer();
			
			if(isUnlocked(p))
			{
				till(p, e.getClickedBlock(), (int) getLevel(p));
			}
		}
	}
	
	public void till(Player p, Block center, int level)
	{
		GList<Block> br = W.blockRadius(center, (getWidth(level) + 1) / 2);
		br.shuffle();
		PhantomWorldQueue q = new PhantomWorldQueue();
		
		new Task(0)
		{
			@Override
			public void run()
			{
				if(br.isEmpty())
				{
					cancel();
					return;
				}
				
				Block b = br.pop();
				
				if(b.getRelative(BlockFace.UP).getType().equals(Material.AIR) && (b.getType().equals(Material.DIRT) || b.getType().equals(Material.GRASS)) && Blocks.canModify(p, b))
				{
					q.set(b.getLocation(), Material.SOIL);
					q.flush();
					new GSound(Sound.DIG_GRAVEL, 1f, 0.5f).play(b.getLocation());
					NMSX.breakParticles(b.getLocation().add(0.5, 1, 0.5), Material.SOIL, 12);
				}
			}
		};
	}
	
	public int getWidth(int level)
	{
		return 1 + (level * 2);
	}
	
	@Override
	public MaterialBlock getMaterialBlock()
	{
		return new MaterialBlock(Material.GOLD_HOE);
	}
	
	@Override
	public String getDescription()
	{
		return "Increases The effectivness of the hoe.";
	}
	
	@Override
	public void onStart()
	{
		
	}
	
	@Override
	public void onStop()
	{
		
	}
	
	public String vs(int level)
	{
		return getWidth(level) + "x" + getWidth(level);
	}
	
	@Override
	public String getStatGraph(Player p)
	{
		if(getLevel() == getMaxLevel())
		{
			return C.LIGHT_PURPLE + vs(1) + " " + getAbilityGraph(20, (double) getLevel(p) / (double) getMaxLevel(), "") + C.LIGHT_PURPLE + " " + vs(getMaxLevel());
		}
		
		return C.LIGHT_PURPLE + vs(1) + " " + getAbilityGraph(20, (double) getLevel(p) / (double) getMaxLevel(), vs((int) getLevel(p)) + C.LIGHT_PURPLE + " " + vs(getMaxLevel()));
	}
}
