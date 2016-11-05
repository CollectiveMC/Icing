package org.cyberpwn.icing.skills;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.cyberpwn.icing.skill.BasicSkill;
import org.cyberpwn.icing.xp.XPReason;
import org.phantomapi.clust.Keyed;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Ticked;
import org.phantomapi.world.MaterialBlock;

@Ticked(200)
public class SkillAxes extends BasicSkill
{
	@Keyed("xp-per-hp")
	public int xpPerHp = 1;
	
	public SkillAxes(Controllable parentController)
	{
		super(parentController, "axes", XPReason.AXES);
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
	public void on(EntityDamageByEntityEvent e)
	{
		if(e.isCancelled())
		{
			return;
		}
		
		if(e.getDamager() instanceof Player)
		{
			Player p = (Player) e.getDamager();
			
			if(p.getItemInHand() != null && p.getItemInHand().getType().toString().contains("_AXE"))
			{
				addReward(p, (int) (e.getDamage() * xpPerHp));
			}
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
