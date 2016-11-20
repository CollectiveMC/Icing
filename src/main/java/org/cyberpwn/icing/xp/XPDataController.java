package org.cyberpwn.icing.xp;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
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
		readRedis(xpp);
		return xpp;
	}
	
	@Override
	public void onSave(Player identifier)
	{
		saveRedis(cache.get(identifier));
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
	
	@EventHandler
	public void on(PlayerQuitEvent e)
	{
		save(e.getPlayer());
	}
	
	@EventHandler
	public void on(PlayerJoinEvent e)
	{
		load(e.getPlayer());
	}
}
