package org.cyberpwn.icing.skills;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.cyberpwn.icing.skill.BasicSkill;
import org.cyberpwn.icing.xp.XPReason;
import org.phantomapi.clust.Keyed;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Ticked;
import org.phantomapi.lang.GMap;
import org.phantomapi.world.MaterialBlock;

@Ticked(20)
public class SkillCombat extends BasicSkill
{
	@Keyed("base")
	public int base = 100;
	
	@Keyed("base-multiple")
	public int bm = 70;
	
	GMap<Player, GMap<Player, Double>> assists;
	GMap<Player, Integer> kills;
	
	public SkillCombat(Controllable parentController)
	{
		super(parentController, "combat", XPReason.COMBAT);
		
		assists = new GMap<Player, GMap<Player, Double>>();
		kills = new GMap<Player, Integer>();
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
		try
		{
			if(e.getEntity() instanceof Player)
			{
				Player p = (Player) e.getEntity();
				Player d = null;
				
				if(e.getDamager() instanceof Player)
				{
					d = (Player) e.getDamager();
				}
				
				else if(e.getDamager() instanceof Arrow)
				{
					d = (Player) ((Arrow) e.getDamager()).getShooter();
				}
				
				else
				{
					return;
				}
				
				if(!assists.containsKey(p))
				{
					assists.put(p, new GMap<Player, Double>());
				}
				
				if(!assists.get(p).containsKey(d))
				{
					assists.get(p).put(d, 0.0);
				}
				
				assists.get(p).put(d, assists.get(p).get(d) + e.getDamage());
			}
		}
		
		catch(Exception ex)
		{
			
		}
	}
	
	@EventHandler
	public void on(PlayerDeathEvent e)
	{
		try
		{
			Player p = e.getEntity();
			Player d = e.getEntity().getKiller();
			
			if(p == d || p == null || d == null)
			{
				return;
			}
			
			double kv = base;
			
			if(kills.containsKey(p))
			{
				kv += kills.get(p) * bm;
				kills.remove(p);
			}
			
			addReward(d, (int) kv);
			
			if(assists.containsKey(p))
			{
				double total = 0.0;
				
				for(Player i : assists.get(p).k())
				{
					total += assists.get(p).get(i);
				}
				
				for(Player i : assists.get(p).k())
				{
					double dmg = assists.get(p).get(i);
					double pc = dmg / total;
					addReward(i, (int) (pc * kv));
				}
			}
			
			if(!kills.containsKey(d))
			{
				kills.put(d, 0);
			}
			
			kills.put(d, kills.get(d) + 1);
		}
		
		catch(Exception ex)
		{
			
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
		
	}
}
