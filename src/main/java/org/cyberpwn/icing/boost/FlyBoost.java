package org.cyberpwn.icing.boost;

import org.bukkit.entity.Player;
import org.cyberpwn.icing.Blizzard;
import org.cyberpwn.icing.BlizzardInstance;
import org.cyberpwn.icing.BoostType;
import org.phantomapi.Phantom;
import org.phantomapi.sync.Task;
import org.phantomapi.vfx.ParticleEffect;

public class FlyBoost extends Blizzard
{
	private static final long serialVersionUID = 1L;
	
	public FlyBoost()
	{
		super(BoostType.FLY.toString().toLowerCase());
	}
	
	@Override
	public void onStart()
	{
		new Task(10)
		{
			@Override
			public void run()
			{
				if(isExpired())
				{
					for(Player i : Phantom.instance().onlinePlayers())
					{
						i.setAllowFlight(false);
					}
					
					cancel();
				}
				
				for(Player i : Phantom.instance().onlinePlayers())
				{
					if(i.isFlying())
					{
						ParticleEffect.CLOUD.display(0.1f, 3, i.getLocation(), 24);
					}
					
					i.setAllowFlight(true);
				}
			}
		};
	}
	
	@Override
	public void onExpire()
	{
		
	}
	
	@Override
	public void onTick()
	{
		
	}
	
	@Override
	public BlizzardInstance copy()
	{
		BlizzardInstance i = new FlyBoost();
		i.getConfiguration().setData(getConfiguration().copy().getData());
		return i;
	}
}
