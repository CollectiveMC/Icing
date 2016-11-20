package org.cyberpwn.icing.skills;

import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;
import org.cyberpwn.icing.abilities.StealthChameleon;
import org.cyberpwn.icing.abilities.StealthRespiration;
import org.cyberpwn.icing.abilities.StealthSnatching;
import org.cyberpwn.icing.abilities.StealthSwiftness;
import org.cyberpwn.icing.skill.BasicSkill;
import org.cyberpwn.icing.xp.XP;
import org.cyberpwn.icing.xp.XPReason;
import org.phantomapi.clust.Keyed;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Ticked;
import org.phantomapi.event.PlayerDamagePlayerEvent;
import org.phantomapi.event.PlayerKillPlayerEvent;
import org.phantomapi.event.PlayerMovePositionEvent;
import org.phantomapi.lang.GMap;
import org.phantomapi.world.MaterialBlock;

@Ticked(70)
public class SkillStealth extends BasicSkill
{
	@Keyed("dist-per-xps")
	public int base = 12;
	
	@Keyed("xp-per-dmg")
	public int dpx = 2;
	
	@Keyed("blocks-per-xp")
	public int blocksPerXp = 1;
	
	@Keyed("initial")
	public int initial = 3;
	
	@Keyed("initial-non-critical")
	public int initialnc = 1;
	
	@Keyed("initial-multiple")
	public double initialm = 2;
	
	@Keyed("initial-bm")
	public double bminit = 32.64;
	
	private GMap<Player, Integer> vm;
	private GMap<UUID, Double> bm;
	
	public SkillStealth(Controllable parentController)
	{
		super(parentController, "stealth", XPReason.STEALTH);
		vm = new GMap<Player, Integer>();
		bm = new GMap<UUID, Double>();
	}
	
	@Override
	public void onStart()
	{
		
	}
	
	@Override
	public void onTick()
	{
		popRewardMap();
		
		for(UUID i : bm.k())
		{
			if(Bukkit.getPlayer(i) != null)
			{
				bm.put(i, bm.get(i) >= bminit ? bminit : bm.get(i) + (0.12 * Math.random()));
			}
		}
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
	public void on(PlayerDamagePlayerEvent e)
	{
		if(e.isCancelled())
		{
			return;
		}
		
		Player d = e.getDamager();
		Player v = e.getPlayer();
		
		if(!XP.isReady(d))
		{
			return;
		}
		
		if(!XP.isReady(v))
		{
			return;
		}
		
		Vector a = d.getLocation().getDirection();
		Vector b = v.getLocation().getDirection();
		
		if(a.distance(b) <= 0.45)
		{
			if(d.isSneaking())
			{
				addReward(d, 4);
			}
			
			addReward(d, (int) (e.getDamage() * dpx));
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void on(PlayerKillPlayerEvent e)
	{
		Player d = e.getDamager();
		Player v = e.getPlayer();
		
		if(!XP.isReady(d))
		{
			return;
		}
		
		if(!XP.isReady(v))
		{
			return;
		}
		
		Vector a = d.getLocation().getDirection();
		Vector b = v.getLocation().getDirection();
		
		if(a.distance(b) <= 0.45)
		{
			if(d.isSneaking())
			{
				addReward(d, 40);
			}
			
			addReward(d, 100);
		}
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
				
				if(dis > 40)
				{
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
		
		if(!XP.isReady(e.getPlayer()))
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
				addReward(e.getPlayer(), 1);
				vm.remove(e.getPlayer());
			}
		}
		
		if(e.getPlayer().getRemainingAir() < e.getPlayer().getMaximumAir() && e.getPlayer().getLocation().getBlock().getType().toString().contains("WATER") && e.getPlayer().getLocation().add(0, 1, 0).getBlock().getType().toString().contains("WATER") && a.distanceSquared(b) > 0.003)
		{
			if(!vm.containsKey(e.getPlayer()))
			{
				vm.put(e.getPlayer(), 0);
			}
			
			vm.put(e.getPlayer(), vm.get(e.getPlayer()) + 1);
			
			if(vm.get(e.getPlayer()) > base)
			{
				addReward(e.getPlayer(), (int) (3.0 * ((double) (e.getPlayer().getMaximumAir() - e.getPlayer().getRemainingAir()) / (double) e.getPlayer().getMaximumAir())));
				vm.remove(e.getPlayer());
			}
		}
	}
	
	@Override
	public void createControllers()
	{
		register(new StealthSwiftness(this));
		register(new StealthChameleon(this));
		register(new StealthRespiration(this));
		register(new StealthSnatching(this));
	}
	
	@Override
	public void addReward(Player p, Integer r)
	{
		if(!bm.containsKey(p.getUniqueId()))
		{
			bm.put(p.getUniqueId(), bminit);
		}
		
		if(bm.get(p.getUniqueId()) < 1.0)
		{
			bm.put(p.getUniqueId(), 1.0);
		}
		
		r = (int) (r * bm.get(p.getUniqueId()));
		bm.put(p.getUniqueId(), bm.get(p.getUniqueId()) - (((double) r) / 25));
		
		if(bm.get(p.getUniqueId()) < 1.0)
		{
			bm.put(p.getUniqueId(), 1.0);
		}
		
		super.addReward(p, r);
	}
	
	public double getBm(Player p)
	{
		if(!bm.containsKey(p.getUniqueId()))
		{
			return 1.0;
		}
		
		return bm.get(p.getUniqueId());
	}
}
