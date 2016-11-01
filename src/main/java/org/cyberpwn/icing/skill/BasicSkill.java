package org.cyberpwn.icing.skill;

import org.bukkit.entity.Player;
import org.phantomapi.clust.ConfigurableController;
import org.phantomapi.construct.Controllable;

public class BasicSkill extends ConfigurableController implements Skill
{
	public BasicSkill(Controllable parentController, String codeName)
	{
		super(parentController, codeName);
	}
	
	@Override
	public long getSkill(Player p)
	{
		return;
	}
	
	@Override
	public long getXp(Player p)
	{
		return 0;
	}
	
	@Override
	public void addXp(Player p, long amt)
	{
		
	}
	
	@Override
	public void onStart()
	{
		
	}
	
	@Override
	public void onStop()
	{
		
	}
}
