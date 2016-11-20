package org.cyberpwn.icing.ability;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
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
		readRedis(sp);
		return sp;
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
