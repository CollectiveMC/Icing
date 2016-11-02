package org.cyberpwn.icing.skills;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.cyberpwn.icing.skill.BasicSkill;
import org.cyberpwn.icing.xp.XPReason;
import org.phantomapi.clust.Keyed;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Ticked;
import org.phantomapi.world.MaterialBlock;

@Ticked(100)
public class SkillArchery extends BasicSkill
{
	@Keyed("blocks-per-xp")
	public int blocksPerXp = 2;
	
	@Keyed("initial")
	public int initial = 4;
	
	@Keyed("initial-non-critical")
	public int initialnc = 1;
	
	public SkillArchery(Controllable parentController)
	{
		super(parentController, "archer", XPReason.ARCHERY);
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
	
	@EventHandler
	public void on(EntityDamageByEntityEvent e)
	{
		if(e.getDamager() instanceof Arrow)
		{
			Arrow a = (Arrow) e.getDamager();
			
			if(a.getShooter() instanceof Player)
			{
				Player p = (Player) a.getShooter();
				int dis = (int) a.getLocation().distance(p.getLocation());
				dis /= blocksPerXp;
				
				if(a.isCritical())
				{
					addReward(p, initial + dis);
				}
				
				else
				{
					addReward(p, initialnc);
				}
			}
		}
	}
	
	@Override
	public MaterialBlock getSkillMaterial()
	{
		return new MaterialBlock(Material.BOW);
	}
}
