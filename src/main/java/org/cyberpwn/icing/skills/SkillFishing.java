package org.cyberpwn.icing.skills;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerFishEvent;
import org.cyberpwn.icing.skill.BasicSkill;
import org.cyberpwn.icing.xp.XPReason;
import org.phantomapi.clust.Keyed;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Ticked;
import org.phantomapi.world.MaterialBlock;

@Ticked(20)
public class SkillFishing extends BasicSkill
{
	@Keyed("initial")
	public int initial = 105;
	
	public SkillFishing(Controllable parentController)
	{
		super(parentController, "fishing", XPReason.FISHING);
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
	public void on(PlayerFishEvent e)
	{
		Player p = e.getPlayer();
		
		if(e.getCaught() != null)
		{
			addReward(p, initial);
		}
		
		else
		{
			return;
		}
	}
	
	@Override
	public MaterialBlock getSkillMaterial()
	{
		return new MaterialBlock(Material.DIAMOND_AXE);
	}
	
	@Override
	public void createControllers()
	{
		
	}
}
