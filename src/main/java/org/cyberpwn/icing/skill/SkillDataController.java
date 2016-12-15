package org.cyberpwn.icing.skill;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.phantomapi.clust.DataController;
import org.phantomapi.clust.PD;
import org.phantomapi.clust.REDISREM;
import org.phantomapi.clust.REM;
import org.phantomapi.construct.Controllable;
import org.phantomapi.sync.TaskLater;

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
		REM rem = new REDISREM();
		
		try
		{
			if(rem.exists(sp))
			{
				readRedis(sp);
				PD.get(identifier).getConfiguration().add(sp.getConfiguration().copy(), "i.s.");
				
				new TaskLater()
				{
					@Override
					public void run()
					{
						try
						{
							rem.drop(sp);
						}
						
						catch(Exception e)
						{
							e.printStackTrace();
						}
					}
				};
			}
		}
		
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
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
