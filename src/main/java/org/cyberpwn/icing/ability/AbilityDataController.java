package org.cyberpwn.icing.ability;

import org.bukkit.entity.Player;
import org.phantomapi.clust.DataController;
import org.phantomapi.construct.Controllable;

public class AbilityDataController extends DataController<AblePlayer, Player>
{
	public AbilityDataController(Controllable parentController)
	{
		super(parentController);
	}
	
	@Override
	public AblePlayer onLoad(Player identifier)
	{
		AblePlayer sp = new AblePlayer(identifier);
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
