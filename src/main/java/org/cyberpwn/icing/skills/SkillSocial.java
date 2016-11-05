package org.cyberpwn.icing.skills;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.cyberpwn.icing.skill.BasicSkill;
import org.cyberpwn.icing.xp.XPReason;
import org.phantomapi.clust.Keyed;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Ticked;
import org.phantomapi.lang.GMap;
import org.phantomapi.world.MaterialBlock;

@Ticked(200)
public class SkillSocial extends BasicSkill
{
	private GMap<Player, Integer> exc;
	
	@Keyed("chats-per-xp")
	public int bpxp = 1;
	
	public SkillSocial(Controllable parentController)
	{
		super(parentController, "social", XPReason.SOCIAL);
		
		exc = new GMap<Player, Integer>();
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
	public void on(AsyncPlayerChatEvent e)
	{
		if(e.isCancelled())
		{
			return;
		}
		
		if(!exc.containsKey(e.getPlayer()))
		{
			exc.put(e.getPlayer(), 0);
		}
		
		exc.put(e.getPlayer(), exc.get(e.getPlayer()) + 1);
		
		if(exc.get(e.getPlayer()) > 12)
		{
			addReward(e.getPlayer(), exc.get(e.getPlayer()) / bpxp);
			exc.remove(e.getPlayer());
		}
	}
	
	@Override
	public MaterialBlock getSkillMaterial()
	{
		return new MaterialBlock(Material.SIGN);
	}
	
	@Override
	public void createControllers()
	{
		
	}
}
