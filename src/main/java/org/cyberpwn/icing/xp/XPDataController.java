package org.cyberpwn.icing.xp;

import org.bukkit.entity.Player;
import org.phantomapi.clust.DataController;
import org.phantomapi.construct.Controllable;

public class XPDataController extends DataController<XPPlayer, Player>
{
	public XPDataController(Controllable parentController)
	{
		super(parentController);
	}
	
	@Override
	public XPPlayer onLoad(Player identifier)
	{
		XPPlayer xpp = new XPPlayer(identifier);
		loadMysql(xpp);
		return xpp;
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
