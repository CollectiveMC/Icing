package org.cyberpwn.icing;

import org.bukkit.entity.Player;
import org.cyberpwn.icing.xp.XP;
import org.phantomapi.clust.PD;
import org.phantomapi.lang.GTime;
import org.phantomapi.placeholder.PlaceholderHook;
import org.phantomapi.util.F;

public class PlaceholderController extends PlaceholderHook
{
	@Override
	public String onPlaceholderRequest(Player p, String q)
	{
		if(q.equalsIgnoreCase("x_xp"))
		{
			if(!XP.isReady(p))
			{
				return "???";
			}
			
			return F.f(XP.getXp(p));
		}
		
		if(q.equalsIgnoreCase("x_level"))
		{
			if(!XP.isReady(p))
			{
				return "???";
			}
			
			return F.f(XP.getLevelForXp(XP.getXp(p)));
		}
		
		if(q.equalsIgnoreCase("x_stealth_multiplier"))
		{
			if(!XP.isReady(p))
			{
				return "???";
			}
			
			return F.pc(Icing.inst().getSk().getStealthBm(p));
		}
		
		if(q.equalsIgnoreCase("x_boost"))
		{
			if(!XP.isReady(p))
			{
				return "???";
			}
			
			return F.pc(XP.getBoost(p));
		}
		
		if(q.equalsIgnoreCase("x_boost_amt"))
		{
			if(!XP.isReady(p))
			{
				return "???";
			}
			
			return F.pc(PD.get(p).getConfiguration().getDouble("i.x.ba"));
		}
		
		if(q.equalsIgnoreCase("x_boost_time"))
		{
			if(!XP.isReady(p))
			{
				return "???";
			}
			
			return new GTime(Math.abs(PD.get(p).getConfiguration().getInt("i.x.bt") * 50)).to();
		}
		
		if(q.equalsIgnoreCase("x_discred"))
		{
			if(!XP.isReady(p))
			{
				return "???";
			}
			
			return F.pc(PD.get(p).getConfiguration().getDouble("i.x.d"));
		}
		
		return null;
	}
}
