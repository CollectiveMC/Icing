package org.cyberpwn.icing.skills;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;
import org.cyberpwn.icing.abilities.StealthSwiftness;
import org.cyberpwn.icing.skill.BasicSkill;
import org.cyberpwn.icing.xp.XP;
import org.cyberpwn.icing.xp.XPReason;
import org.phantomapi.clust.Keyed;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Ticked;
import org.phantomapi.event.PlayerDamagePlayerEvent;
import org.phantomapi.event.PlayerKillPlayerEvent;
import org.phantomapi.event.PlayerMoveBlockEvent;
import org.phantomapi.event.PlayerMovePositionEvent;
import org.phantomapi.lang.GMap;
import org.phantomapi.util.M;
import org.phantomapi.vfx.ParticleEffect;
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
	public void on(PlayerDamagePlayerEvent e)
	{
		if(e.isCancelled())
		{
			return;
		}
		
		Player d = e.getDamager();
		Player v = e.getPlayer();
		
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
	public void on(PlayerMoveBlockEvent e)
	{
		if(M.r(0.7) && e.getPlayer().getLocation().subtract(0, 1, 0).getBlock().getType().isSolid() && !e.getPlayer().isSneaking())
		{
			ParticleEffect.FOOTSTEP.display(0, 1, e.getPlayer().getLocation().subtract(0, 1, 0).getBlock().getLocation().add(Math.random() - 0.5, 1.02, Math.random() - 0.5), 32);
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
	}
	
	@Override
	public void createControllers()
	{
		register(new StealthSwiftness(this));
	}
}
