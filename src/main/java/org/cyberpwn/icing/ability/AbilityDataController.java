package org.cyberpwn.icing.ability;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.phantomapi.clust.DataController;
import org.phantomapi.clust.PD;
import org.phantomapi.clust.REDISREM;
import org.phantomapi.clust.REM;
import org.phantomapi.construct.Controllable;
import org.phantomapi.sync.TaskLater;

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
		
		REM rem = new REDISREM();
		
		try
		{
			if(rem.exists(sp))
			{
				readRedis(sp);
				PD.get(identifier).getConfiguration().add(sp.getConfiguration().copy(), "i.a.");
				
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
