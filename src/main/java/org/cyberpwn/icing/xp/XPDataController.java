package org.cyberpwn.icing.xp;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.phantomapi.clust.ConfigurableObject;
import org.phantomapi.clust.DataController;
import org.phantomapi.clust.PD;
import org.phantomapi.clust.REM;
import org.phantomapi.clust.SQLREM;
import org.phantomapi.construct.Controllable;
import org.phantomapi.sync.TaskLater;

public class XPDataController extends DataController<ConfigurableObject, Player>
{
	public XPDataController(Controllable parentController)
	{
		super(parentController);
	}
	
	@Override
	public XPPlayer onLoad(Player identifier)
	{
		XPPlayer xpp = new XPPlayer(identifier);
		REM rem = new SQLREM();
		
		try
		{
			if(rem.exists(xpp))
			{
				loadMysql(xpp);
				PD.get(identifier).getConfiguration().set("i.x.x", xpp.xp);
				PD.get(identifier).getConfiguration().set("i.x.b", xpp.boost);
				PD.get(identifier).getConfiguration().set("i.x.bt", xpp.boosterTicks);
				PD.get(identifier).getConfiguration().set("i.x.ba", xpp.boosterAmount);
				PD.get(identifier).getConfiguration().set("i.x.d", xpp.discredit);
				PD.get(identifier).getConfiguration().set("i.x.s", xpp.stfu);
				
				new TaskLater()
				{
					@Override
					public void run()
					{
						try
						{
							rem.drop(xpp);
						}
						
						catch(Exception e)
						{
							
						}
					}
				};
			}
			
			Long xp = PD.get(identifier).getConfiguration().getLong("i.x.x");
			Double boost = PD.get(identifier).getConfiguration().getDouble("i.x.b");
			Integer boosterTicks = PD.get(identifier).getConfiguration().getInt("i.x.bt");
			Double boosterAmount = PD.get(identifier).getConfiguration().getDouble("i.x.ba");
			Double discredit = PD.get(identifier).getConfiguration().getDouble("i.x.d");
			Boolean stfu = PD.get(identifier).getConfiguration().getBoolean("i.x.s");
			
			if(xp == null)
			{
				PD.get(identifier).getConfiguration().set("i.x.x", 0);
			}
			
			if(boost == null)
			{
				PD.get(identifier).getConfiguration().set("i.x.b", 0.0);
			}
			
			if(boosterTicks == null)
			{
				PD.get(identifier).getConfiguration().set("i.x.bt", 0);
			}
			
			if(boosterAmount == null)
			{
				PD.get(identifier).getConfiguration().set("i.x.ba", 0.0);
			}
			
			if(discredit == null)
			{
				PD.get(identifier).getConfiguration().set("i.x.d", 0.0);
			}
			
			if(stfu == null)
			{
				PD.get(identifier).getConfiguration().set("i.x.s", false);
			}
		}
		
		catch(Exception e)
		{
			
		}
		
		return xpp;
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
