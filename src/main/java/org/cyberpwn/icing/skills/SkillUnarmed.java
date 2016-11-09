package org.cyberpwn.icing.skills;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.cyberpwn.icing.abilities.UnarmedForce;
import org.cyberpwn.icing.skill.BasicSkill;
import org.cyberpwn.icing.xp.XP;
import org.cyberpwn.icing.xp.XPReason;
import org.phantomapi.clust.Keyed;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Ticked;
import org.phantomapi.world.MaterialBlock;

@Ticked(200)
public class SkillUnarmed extends BasicSkill
{
	@Keyed("initial")
	public int initial = 3;
	
	public SkillUnarmed(Controllable parentController)
	{
		super(parentController, "unarmed", XPReason.UNARMED);
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
		
		if(e.getDamager() instanceof Player)
		{
			Player p = (Player) e.getDamager();
			
			if(p.getItemInHand() == null || p.getItemInHand().getType().equals(Material.AIR))
			{
				addReward(p, (int) (initial * e.getDamage()));
				XP.dropRandom(p.getLocation());
			}
		}
	}
	
	@Override
	public MaterialBlock getSkillMaterial()
	{
		return new MaterialBlock(Material.ROTTEN_FLESH);
	}
	
	@Override
	public void createControllers()
	{
		register(new UnarmedForce(this));
	}
}
