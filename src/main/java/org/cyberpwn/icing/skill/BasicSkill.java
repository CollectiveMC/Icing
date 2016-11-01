package org.cyberpwn.icing.skill;

import org.bukkit.entity.Player;
import org.cyberpwn.icing.Icing;
import org.cyberpwn.icing.xp.XP;
import org.phantomapi.clust.ConfigurableController;
import org.phantomapi.construct.Controllable;

public class BasicSkill extends ConfigurableController implements Skill
{
	public BasicSkill(Controllable parentController, String codeName)
	{
		super(parentController, codeName);
	}
	
	@Override
	public void onStart()
	{
		
	}
	
	@Override
	public void onStop()
	{
		
	}
	
	@Override
	public long getXp(Player p)
	{
		return Icing.inst().getSk().getSkillDataController().get(p).getSkill(getCodeName());
	}
	
	@Override
	public void addXp(Player p, long amt)
	{
		Icing.inst().getSk().getSkillDataController().get(p).addSkill(getCodeName(), amt);
	}
	
	@Override
	public long getLevel(Player p)
	{
		return XP.getLevelForXp(getXp(p));
	}
	
	@Override
	public double getProgress(Player p)
	{
		return XP.percentToNextLevel(getXp(p));
	}
	
	@Override
	public long getXpLeft(Player p)
	{
		return (long) XP.xpToNextLevel(getXp(p));
	}
}
