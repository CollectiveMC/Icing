package org.cyberpwn.icing;

import org.bukkit.entity.Player;
import org.cyberpwn.icing.xp.XP;
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
			return F.f(XP.getXp(p));
		}
		
		if(q.equalsIgnoreCase("x_level"))
		{
			return F.f(XP.getLevelForXp(XP.getXp(p)));
		}
		
		if(q.equalsIgnoreCase("x_stealth_multiplier"))
		{
			return F.pc(Icing.inst().getSk().getStealthBm(p));
		}
		
		if(q.equalsIgnoreCase("x_boost"))
		{
			return F.pc(XP.getBoost(p));
		}
		
		if(q.equalsIgnoreCase("x_boost_amt"))
		{
			return F.pc(Icing.inst().getSk().getXpp(p).getBoosterAmount());
		}
		
		if(q.equalsIgnoreCase("x_boost_time"))
		{
			return new GTime(Math.abs(Icing.inst().getSk().getXpp(p).getBoosterTicks() * 50)).to();
		}
		
		if(q.equalsIgnoreCase("x_discred"))
		{
			return F.pc(Icing.inst().getSk().getXpp(p).getDiscredit());
		}
		
		return null;
	}
}
