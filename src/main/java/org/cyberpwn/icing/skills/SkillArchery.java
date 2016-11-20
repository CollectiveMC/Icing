package org.cyberpwn.icing.skills;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.cyberpwn.icing.abilities.ArcheryPowerShot;
import org.cyberpwn.icing.abilities.ArcheryPrecision;
import org.cyberpwn.icing.skill.BasicSkill;
import org.cyberpwn.icing.xp.XP;
import org.cyberpwn.icing.xp.XPReason;
import org.phantomapi.clust.Keyed;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Ticked;
import org.phantomapi.world.MaterialBlock;

@Ticked(20)
public class SkillArchery extends BasicSkill
{
	@Keyed("blocks-per-xp")
	public int blocksPerXp = 1;
	
	@Keyed("initial")
	public int initial = 3;
	
	@Keyed("initial-non-critical")
	public int initialnc = 1;
	
	@Keyed("initial-multiple")
	public double initialm = 2;
	
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
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void on(EntityDamageByEntityEvent e)
	{
		if(e.isCancelled())
		{
			return;
		}
		
		if(e.getDamager() instanceof Arrow)
		{
			Arrow a = (Arrow) e.getDamager();
			
			if(a.getShooter() instanceof Player)
			{
				Player p = (Player) a.getShooter();
				
				if(!XP.isReady(p))
				{
					return;
				}
				
				if(p.getGameMode().equals(GameMode.CREATIVE))
				{
					return;
				}
				
				XP.dropRandom(p.getLocation());
				int dis = (int) a.getLocation().distance(p.getLocation());
				dis /= blocksPerXp;
				dis *= initialm;
				
				if(a.isCritical())
				{
					addReward(p, (initial + dis));
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
	
	@Override
	public void createControllers()
	{
		register(new ArcheryPrecision(this));
		register(new ArcheryPowerShot(this));
	}
}
