package org.cyberpwn.icing.skill;

import org.bukkit.entity.Player;
import org.phantomapi.clust.DataController;
import org.phantomapi.construct.Controllable;

public class SkillDataController extends DataController<SkilledPlayer, Player>
{
	public SkillDataController(Controllable parentController)
	{
		super(parentController);
	}
	
	@Override
	public SkilledPlayer onLoad(Player identifier)
	{
		SkilledPlayer sp = new SkilledPlayer(identifier);
		loadMysql(sp);
		return sp;
	}
	
	@Override
	public void onSave(Player identifier)
	{
		saveMysql(cache.get(identifier));
	}
	
	@Override
	public void onStart()
	{
		for(Player i : onlinePlayers())
		{
			load(i);
		}
	}
	
	@Override
	public void onStop()
	{
		saveAll();
	}
	
}
