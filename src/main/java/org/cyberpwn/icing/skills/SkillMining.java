package org.cyberpwn.icing.skills;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.cyberpwn.icing.skill.BasicSkill;
import org.cyberpwn.icing.xp.XPReason;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Ticked;
import org.phantomapi.event.FalseBlockBreakEvent;

@Ticked(50)
public class SkillMining extends BasicSkill
{
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
	
	@EventHandler
	public void on(FalseBlockBreakEvent e)
	{
		on(new BlockBreakEvent(e.getBlock(), e.getPlayer()));
	}
	
	@EventHandler
	public void on(BlockBreakEvent e)
	{
		if(e.getBlock().getType().toString().endsWith("_ORE") && e.getPlayer().getItemInHand() != null && e.getPlayer().getItemInHand().getType().toString().endsWith("_PICKAXE"))
		{
			if(e.getPlayer().getItemInHand().containsEnchantment(Enchantment.SILK_TOUCH))
			{
				return;
			}
			
			int xp = 0;
			
			switch(e.getBlock().getType())
			{
				case DIAMOND_ORE:
					xp = 48;
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
			}
		}
	}
}
