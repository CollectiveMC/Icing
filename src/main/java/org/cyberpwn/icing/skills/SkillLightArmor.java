package org.cyberpwn.icing.skills;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.cyberpwn.icing.skill.BasicSkill;
import org.cyberpwn.icing.xp.XPReason;
import org.phantomapi.clust.Keyed;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Ticked;
import org.phantomapi.world.MaterialBlock;

@Ticked(200)
public class SkillLightArmor extends BasicSkill
{
	@Keyed("xp-per-hp")
	public int xpPerHp = 1;
	
	public SkillLightArmor(Controllable parentController)
	{
		super(parentController, "light armor", XPReason.LIGHT_ARMOR);
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
	public void on(EntityDamageEvent e)
	{
		if(e.getEntity() instanceof Player)
		{
			Player p = (Player) e.getEntity();
			
			int v = 0;
			
			for(ItemStack i : p.getInventory().getArmorContents())
			{
				if(i.getType().toString().contains("CHAINMAIL") || i.getType().toString().contains("LEATHER"))
				{
					v++;
				}
			}
			
			double k = e.getDamage() * v;
			int l = (int) (k * xpPerHp);
			
			if(l > 0)
			{
				addReward(p, l);
			}
		}
	}
	
	@Override
	public MaterialBlock getSkillMaterial()
	{
		return new MaterialBlock(Material.LEATHER_CHESTPLATE);
	}
	
	@Override
	public void createControllers()
	{
		
	}
}
