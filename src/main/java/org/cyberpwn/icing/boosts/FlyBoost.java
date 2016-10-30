package org.cyberpwn.icing.boosts;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.cyberpwn.icing.boost.BaseBoost;
import org.cyberpwn.icing.boost.BoostType;
import org.phantomapi.Phantom;
import org.phantomapi.vfx.ParticleEffect;

public class FlyBoost extends BaseBoost
{
	public FlyBoost()
	{
		super(BoostType.FLY);
		
		name = "&bFly";
	}
	
	@Override
	public void onTick(double multiplier)
	{
		for(Player i : Phantom.instance().onlinePlayers())
		{
			i.setAllowFlight(true);
			
			if(i.isFlying())
			{
				ParticleEffect.CLOUD.display(new Vector(0, -0.4, 0).add(i.getVelocity().clone().multiply(0.4)), 0.1f, i.getLocation(), 24);
			}
		}
	}
	
	@Override
	public void onAdded()
	{
		for(Player i : Phantom.instance().onlinePlayers())
		{
			i.setAllowFlight(true);
		}
	}
	
	@Override
	public void onExpire()
	{
		for(Player i : Phantom.instance().onlinePlayers())
		{
			i.setAllowFlight(false);
		}
	}
}
