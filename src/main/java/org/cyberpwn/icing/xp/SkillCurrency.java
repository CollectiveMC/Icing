package org.cyberpwn.icing.xp;

import org.bukkit.entity.Player;
import org.cyberpwn.icing.Icing;
import org.phantomapi.currency.Currency;

public class SkillCurrency implements Currency
{
	private String sk;
	
	public SkillCurrency(String codeName)
	{
		sk = codeName;
	}
	
	@Override
	public double get(Player p)
	{
		return Icing.getInst().getSk().getSkillDataController().get(p).getSkillPoints(sk);
	}
	
	@Override
	public void give(Player p, double amt)
	{
		Icing.getInst().getSk().getSkillDataController().get(p).addSkillPoints(sk, (long) amt);
	}
	
	@Override
	public void take(Player p, double amt)
	{
		Icing.getInst().getSk().getSkillDataController().get(p).takeSkillPoints(sk, (long) amt);
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
