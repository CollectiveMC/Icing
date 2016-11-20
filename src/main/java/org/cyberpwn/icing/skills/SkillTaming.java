package org.cyberpwn.icing.skills;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityTameEvent;
import org.cyberpwn.icing.abilities.TamingTrainingStrength;
import org.cyberpwn.icing.skill.BasicSkill;
import org.cyberpwn.icing.xp.XP;
import org.cyberpwn.icing.xp.XPReason;
import org.phantomapi.clust.Keyed;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Ticked;
import org.phantomapi.world.MaterialBlock;

@Ticked(20)
public class SkillTaming extends BasicSkill
{
	@Keyed("base")
	public int base = 68;
	
	public SkillTaming(Controllable parentController)
	{
		super(parentController, "taming", XPReason.TAMING);
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
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void on(EntityTameEvent e)
	{
		if(e.isCancelled())
		{
			return;
		}
		
		try
		{
			if(!XP.isReady((Player) e.getOwner()))
			{
				return;
			}
			
			if(((Player) e.getOwner()).getGameMode().equals(GameMode.CREATIVE))
			{
				return;
			}
			
			addReward((Player) e.getOwner(), base);
			XP.dropRandom(e.getEntity().getLocation());
		}
		
		catch(Exception ex)
		{
			
		}
	}
	
	@Override
	public MaterialBlock getSkillMaterial()
	{
		return new MaterialBlock(Material.BONE);
	}
	
	@Override
	public void createControllers()
	{
		register(new TamingTrainingStrength(this));
	}
}
