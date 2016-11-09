package org.cyberpwn.icing.skills;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.cyberpwn.icing.skill.BasicSkill;
import org.cyberpwn.icing.xp.XP;
import org.cyberpwn.icing.xp.XPReason;
import org.phantomapi.clust.Keyed;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.ControllerMessage;
import org.phantomapi.construct.Ticked;
import org.phantomapi.util.Players;
import org.phantomapi.world.MaterialBlock;

@Ticked(100)
public class SkillVoter extends BasicSkill
{
	@Keyed("base")
	public int base = 250;
	
	public SkillVoter(Controllable parentController)
	{
		super(parentController, "voter", XPReason.VOTING);
	}
	
	@Override
	public void onStart()
	{
		
	}
	
	@Override
	public void onTick()
	{
		popRewardMap();
	}
	
	@Override
	public void onStop()
	{
		
	}
	
	@Override
	public ControllerMessage onControllerMessageRecieved(ControllerMessage message)
	{
		if(message.contains("e.player"))
		{
			Player p = Players.getPlayer(message.getString("e.player"));
			addReward(p, base);
			XP.dropRandom(p.getLocation());
		}
		
		return message;
	}
	
	@Override
	public MaterialBlock getSkillMaterial()
	{
		return new MaterialBlock(Material.PAPER);
	}
	
	@Override
	public void createControllers()
	{
		
	}
}
