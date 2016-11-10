package org.cyberpwn.icing;

import org.bukkit.entity.Player;
import org.cyberpwn.icing.xp.XP;
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
		
		if(q.equalsIgnoreCase("x_boost"))
		{
			return F.pc(XP.getBoost(p));
		}
		
		return null;
	}
}
