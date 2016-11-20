package org.cyberpwn.icing.skill;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
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
}
