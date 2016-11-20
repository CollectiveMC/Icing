package org.cyberpwn.icing.skills;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.cyberpwn.icing.abilities.SwordsBlocking;
import org.cyberpwn.icing.skill.BasicSkill;
import org.cyberpwn.icing.xp.XP;
import org.cyberpwn.icing.xp.XPReason;
import org.phantomapi.clust.Keyed;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Ticked;
import org.phantomapi.world.MaterialBlock;

@Ticked(70)
public class SkillSwords extends BasicSkill
{
	@Keyed("xp-per-hp")
	public int xpPerHp = 1;
	
	public SkillSwords(Controllable parentController)
	{
		super(parentController, "swords", XPReason.SWORDS);
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
			
			if(!XP.isReady(p))
			{
				return;
			}
			
			if(p.getGameMode().equals(GameMode.CREATIVE))
			{
				return;
			}
			
			if(p.getItemInHand() != null && p.getItemInHand().getType().toString().contains("_SWORD"))
			{
				addReward(p, (int) (e.getDamage() * xpPerHp));
				XP.dropRandom(p.getLocation());
			}
		}
	}
	
	@Override
	public MaterialBlock getSkillMaterial()
	{
		return new MaterialBlock(Material.DIAMOND_SWORD);
	}
	
	@Override
	public void createControllers()
	{
		register(new SwordsBlocking(this));
	}
}
