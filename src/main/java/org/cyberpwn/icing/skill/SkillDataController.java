package org.cyberpwn.icing.skill;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
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
		
		return sp;
	}
	
	@Override
	public void onSave(Player identifier)
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
	
	@EventHandler
	public void on(PlayerJoinEvent e)
	{
		load(e.getPlayer());
	}
}
