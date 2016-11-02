package org.cyberpwn.icing.skills;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.cyberpwn.icing.skill.BasicSkill;
import org.cyberpwn.icing.xp.XPReason;
import org.phantomapi.clust.Keyed;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.ControllerMessage;
import org.phantomapi.construct.Ticked;
import org.phantomapi.util.Players;
import org.phantomapi.world.MaterialBlock;

@Ticked(20)
public class SkillEnchanting extends BasicSkill
{
	@Keyed("base")
	public int base = 12;
	
	@Keyed("base-2")
	public int base2 = 3;
	
	public SkillEnchanting(Controllable parentController)
	{
		super(parentController, "enchanting", XPReason.ENCHANTING);
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
	
	@Override
	public MaterialBlock getSkillMaterial()
	{
		return new MaterialBlock(Material.ENCHANTED_BOOK);
	}
	
	@EventHandler
	public void on(EnchantItemEvent e)
	{
		addReward(e.getEnchanter(), e.getExpLevelCost() * base2);
	}
	
	@Override
	public ControllerMessage onControllerMessageRecieved(ControllerMessage message)
	{
		if(message.contains("e.player"))
		{
			Player p = Players.getPlayer(message.getString("e.player"));
			int level = message.getInt("e.level");
			addReward(p, level * base);
		}
		
		return message;
	}
}
