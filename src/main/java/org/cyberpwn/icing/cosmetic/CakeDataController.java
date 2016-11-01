package org.cyberpwn.icing.cosmetic;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.phantomapi.clust.DataController;
import org.phantomapi.construct.Controllable;

public class CakeDataController extends DataController<CakePlayerData, Player>
{
	public CakeDataController(Controllable parentController)
	{
		super(parentController);
	}
	
	@Override
	public CakePlayerData onLoad(Player p)
	{
		CakePlayerData cpd = new CakePlayerData(p);
		loadMysql(cpd);
		return cpd;
	}
	
	@Override
	public void onSave(Player p)
	{
		CakePlayerData cpd = cache.get(p);
		saveMysql(cpd);
	}
	
	@Override
	public void onStart()
	{
		for(Player i : onlinePlayers())
		{
			onLoad(i);
		}
	}
	
	@Override
	public void onStop()
	{
		saveAll();
	}
	
	@EventHandler
	public void on(PlayerJoinEvent e)
	{
		load(e.getPlayer());
	}
	
	@EventHandler
	public void on(PlayerQuitEvent e)
	{
		save(e.getPlayer());
	}
}
