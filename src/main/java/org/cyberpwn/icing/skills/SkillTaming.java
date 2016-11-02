package org.cyberpwn.icing.skills;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityTameEvent;
import org.cyberpwn.icing.skill.BasicSkill;
import org.cyberpwn.icing.xp.XPReason;
import org.phantomapi.clust.Keyed;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Ticked;
import org.phantomapi.world.MaterialBlock;

@Ticked(20)
public class SkillTaming extends BasicSkill
{
	@Keyed("base")
	public int base = 68;
	
	public SkillTaming(Controllable parentController)
	{
		super(parentController, "taming", XPReason.TAMING);
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
	public void on(EntityTameEvent e)
	{
		try
		{
			addReward((Player) e.getOwner(), base);
		}
		
		catch(Exception ex)
		{
			
		}
	}
	
	@Override
	public MaterialBlock getSkillMaterial()
	{
		return new MaterialBlock(Material.BONE);
	}
}
