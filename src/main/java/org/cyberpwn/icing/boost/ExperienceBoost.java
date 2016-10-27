package org.cyberpwn.icing.boost;

import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.cyberpwn.icing.BoostController;
import org.cyberpwn.icing.BoostType;
import org.phantomapi.Phantom;

public class ExperienceBoost extends BaseBoost
{
	public ExperienceBoost()
	{
		super(BoostType.XP);
		
		name = "&bExperience";
	}
	
	@Override
	public void onTick(double multiplier)
	{
		
	}
	
	@EventHandler
	public void on(BlockBreakEvent e)
	{
		e.setExpToDrop(e.getExpToDrop() + (int) (e.getExpToDrop() * BoostController.getMultiple(BoostType.XP)));
	}
	
	@EventHandler
	public void on(EntityDeathEvent e)
	{
		e.setDroppedExp(e.getDroppedExp() + (int) (e.getDroppedExp() * BoostController.getMultiple(BoostType.XP)));
	}
	
	@Override
	public void onAdded()
	{
		Phantom.instance().registerListener(this);
	}
	
	@Override
	public void onExpire()
	{
		Phantom.instance().unRegisterListener(this);
	}
}
