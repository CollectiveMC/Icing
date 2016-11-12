package org.cyberpwn.icing.skills;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.cyberpwn.icing.abilities.StealthChameleon;
import org.cyberpwn.icing.abilities.StealthSwiftness;
import org.cyberpwn.icing.skill.BasicSkill;
import org.cyberpwn.icing.xp.XPReason;
import org.phantomapi.clust.Keyed;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Ticked;
import org.phantomapi.event.PlayerMovePositionEvent;
import org.phantomapi.lang.GMap;
import org.phantomapi.world.MaterialBlock;

@Ticked(200)
public class SkillStealth extends BasicSkill
{
	@Keyed("dist-per-xps")
	public int base = 12;
	
	private GMap<Player, Integer> vm;
	
	public SkillStealth(Controllable parentController)
	{
		super(parentController, "stealth", XPReason.STEALTH);
		vm = new GMap<Player, Integer>();
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
	public MaterialBlock getSkillMaterial()
	{
		return new MaterialBlock(Material.EYE_OF_ENDER);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void on(PlayerMovePositionEvent e)
	{
		if(e.isCancelled())
		{
			return;
		}
		
		if(e.getPlayer().getGameMode().equals(GameMode.CREATIVE))
		{
			return;
		}
		
		Location a = e.getFrom().clone();
		Location b = e.getTo().clone();
		a.setY(0);
		b.setY(0);
		
		if(e.getPlayer().isSneaking() && a.distanceSquared(b) > 0.003)
		{
			if(!vm.containsKey(e.getPlayer()))
			{
				vm.put(e.getPlayer(), 0);
			}
			
			vm.put(e.getPlayer(), vm.get(e.getPlayer()) + 1);
			
			if(vm.get(e.getPlayer()) > base)
			{
				addReward(e.getPlayer(), 100000);
				vm.remove(e.getPlayer());
			}
		}
	}
	
	@Override
	public void createControllers()
	{
		register(new StealthSwiftness(this));
		register(new StealthChameleon(this));
	}
}
