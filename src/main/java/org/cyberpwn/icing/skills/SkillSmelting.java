package org.cyberpwn.icing.skills;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.cyberpwn.icing.skill.BasicSkill;
import org.cyberpwn.icing.xp.XPReason;
import org.phantomapi.clust.Keyed;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Ticked;
import org.phantomapi.world.MaterialBlock;

@Ticked(20)
public class SkillSmelting extends BasicSkill
{
	@Keyed("base")
	public int base = 12;
	
	public SkillSmelting(Controllable parentController)
	{
		super(parentController, "smelter", XPReason.SMELTING);
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
	public void on(InventoryClickEvent e)
	{
		if(e.getInventory().getType().equals(InventoryType.FURNACE))
		{
			if(e.getSlotType().equals(SlotType.RESULT))
			{
				int amt = e.getCurrentItem().getAmount();
				Player p = (Player) e.getWhoClicked();
				
				addReward(p, base * amt);
			}
		}
	}
	
	@Override
	public MaterialBlock getSkillMaterial()
	{
		return new MaterialBlock(Material.COAL);
	}
	
	@Override
	public void createControllers()
	{
		
	}
}
