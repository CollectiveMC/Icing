package org.cyberpwn.icing.xp;

import org.bukkit.entity.Player;
import org.phantomapi.currency.Currency;

public class SkillCurrency implements Currency
{
	@Override
	public double get(Player p)
	{
		return XP.getSkill(p);
	}
	
	@Override
	public void give(Player p, double amt)
	{
		XP.addSkill(p, (long) amt);
	}
	
	@Override
	public void take(Player p, double amt)
	{
		XP.takeSkill(p, (long) amt);
	}
	
	@Override
	public String getSuffix()
	{
		return " Skill";
	}
	
	@Override
	public String getPrefix()
	{
		return "";
	}
}
