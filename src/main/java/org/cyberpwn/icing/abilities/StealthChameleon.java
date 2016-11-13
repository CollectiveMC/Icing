package org.cyberpwn.icing.abilities;

import java.awt.Color;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.cyberpwn.icing.ability.BasicAbility;
import org.cyberpwn.icing.skill.Skill;
import org.phantomapi.chromatic.Chromatic;
import org.phantomapi.construct.Ticked;
import org.phantomapi.event.EquipmentUpdateEvent;
import org.phantomapi.lang.GMap;
import org.phantomapi.nms.FakeEquipment.EquipmentSlot;
import org.phantomapi.nms.NMSX;
import org.phantomapi.physics.VectorMath;
import org.phantomapi.statistics.Monitorable;
import org.phantomapi.util.Average;
import org.phantomapi.util.C;
import org.phantomapi.util.F;
import org.phantomapi.util.T;
import org.phantomapi.world.Area;
import org.phantomapi.world.MaterialBlock;
import org.phantomapi.world.W;

@Ticked(0)
public class StealthChameleon extends BasicAbility implements Monitorable
{
	private GMap<Player, GMap<Player, GMap<EquipmentSlot, Color>>> target;
	private double perf;
	private Average a;
	private int rr;
	private int pps;
	
	public StealthChameleon(Skill parent)
	{
		super(parent, "chameleon");
		
		target = new GMap<Player, GMap<Player, GMap<EquipmentSlot, Color>>>();
		maxLevel = 12;
		level = 12;
		levelStep = 2;
		upgradeCost = 6;
		unlockCost = 12;
		perf = 0;
		pps = 0;
		rr = 0;
		a = new Average(40);
	}
	
	public double getMitigation(int level)
	{
		return ((double) level / (double) getMaxLevel());
	}
	
	@Override
	public MaterialBlock getMaterialBlock()
	{
		return new MaterialBlock(Material.MAGMA_CREAM);
	}
	
	@Override
	public String getDescription()
	{
		return "Leather armor changes color to your surroundings.";
	}
	
	@Override
	public void onStart()
	{
		
	}
	
	@Override
	public void onTick()
	{
		rr = 0;
		pps = 0;
		
		T t = new T()
		{
			@Override
			public void onStop(long nsTime, double msTime)
			{
				perf = 0;
				perf += msTime;
			}
		};
		
		for(Player i : onlinePlayers())
		{
			if(isUnlocked(i) && isEnabled(i))
			{
				for(ItemStack j : i.getInventory().getArmorContents())
				{
					if(j != null && j.getType().toString().contains("LEATHER_"))
					{
						Area a = new Area(i.getLocation(), 32);
						
						pps += 4;
						NMSX.updateSelfArmor(i);
						applySelf(i);
						
						if(a.getNearbyPlayers().length > 1)
						{
							NMSX.updateArmor(i);
							pps += (4 * (target.containsKey(i) ? target.get(i).size() : 1));
						}
						
						break;
					}
				}
			}
		}
		
		t.stop();
	}
	
	@EventHandler
	public void on(PlayerQuitEvent e)
	{
		target.remove(e.getPlayer());
		
		for(Player i : target.k())
		{
			target.get(i).remove(e.getPlayer());
		}
	}
	
	public void applySelf(Player p)
	{
		if(p.getInventory().getBoots() != null && p.getInventory().getBoots().getType().equals(Material.LEATHER_BOOTS) && target.containsKey(p) && target.get(p).containsKey(p) && target.get(p).get(p).containsKey(EquipmentSlot.BOOTS))
		{
			Color c = target.get(p).get(p).get(EquipmentSlot.BOOTS);
			ItemStack is = p.getInventory().getBoots().clone();
			W.colorArmor(is, org.bukkit.Color.fromRGB(c.getRed(), c.getGreen(), c.getBlue()));
			NMSX.setItem(p, 8, is);
		}
		
		else
		{
			NMSX.setItem(p, 8, p.getInventory().getBoots());
		}
		
		if(p.getInventory().getLeggings() != null && p.getInventory().getLeggings().getType().equals(Material.LEATHER_LEGGINGS) && target.containsKey(p) && target.get(p).containsKey(p) && target.get(p).get(p).containsKey(EquipmentSlot.LEGGINGS))
		{
			Color c = target.get(p).get(p).get(EquipmentSlot.LEGGINGS);
			ItemStack is = p.getInventory().getLeggings().clone();
			W.colorArmor(is, org.bukkit.Color.fromRGB(c.getRed(), c.getGreen(), c.getBlue()));
			NMSX.setItem(p, 7, is);
		}
		
		else
		{
			NMSX.setItem(p, 7, p.getInventory().getLeggings());
		}
		
		if(p.getInventory().getChestplate() != null && p.getInventory().getChestplate().getType().equals(Material.LEATHER_CHESTPLATE) && target.containsKey(p) && target.get(p).containsKey(p) && target.get(p).get(p).containsKey(EquipmentSlot.CHESTPLATE))
		{
			Color c = target.get(p).get(p).get(EquipmentSlot.CHESTPLATE);
			ItemStack is = p.getInventory().getChestplate().clone();
			W.colorArmor(is, org.bukkit.Color.fromRGB(c.getRed(), c.getGreen(), c.getBlue()));
			NMSX.setItem(p, 6, is);
		}
		
		else
		{
			NMSX.setItem(p, 6, p.getInventory().getChestplate());
		}
		
		if(p.getInventory().getHelmet() != null && p.getInventory().getHelmet().getType().equals(Material.LEATHER_HELMET) && target.containsKey(p) && target.get(p).containsKey(p) && target.get(p).get(p).containsKey(EquipmentSlot.HELMET))
		{
			Color c = target.get(p).get(p).get(EquipmentSlot.HELMET);
			ItemStack is = p.getInventory().getHelmet().clone();
			W.colorArmor(is, org.bukkit.Color.fromRGB(c.getRed(), c.getGreen(), c.getBlue()));
			NMSX.setItem(p, 5, is);
		}
		
		else
		{
			NMSX.setItem(p, 5, p.getInventory().getHelmet());
		}
	}
	
	@EventHandler
	public void on(EquipmentUpdateEvent e)
	{
		T t = new T()
		{
			@Override
			public void onStop(long nsTime, double msTime)
			{
				perf += msTime;
			}
		};
		
		if(e.getEntity() instanceof Player)
		{
			Player p = (Player) e.getEntity();
			
			if(isUnlocked(p) && isEnabled(p))
			{
				int level = (int) getLevel(p);
				Color c = null;
				
				if(e.getItem() != null && e.getItem().getType().toString().contains("LEATHER_"))
				{
					if(e.getItem().getType().toString().endsWith("_HELMET"))
					{
						if(e.getEntity().getEntityId() == e.getViewer().getEntityId())
						{
							Location k = e.getViewer().getLocation().add(VectorMath.reverse(e.getViewer().getLocation().getDirection().multiply(2)));
							c = Chromatic.getVisibleColor(e.getEntity().getLocation().clone().add(0, 1.7, 0), VectorMath.direction(k.clone().add(0, 1.7, 0), e.getEntity().getLocation().clone().add(0, 1.7, 0)), 64);
						}
						
						else
						{
							c = Chromatic.getVisibleColor(e.getEntity().getLocation().clone().add(0, 1.7, 0), VectorMath.direction(e.getViewer().getLocation().clone().add(0, 1.7, 0), e.getEntity().getLocation().clone().add(0, 1.7, 0)), 64);
						}
					}
					
					if(e.getItem().getType().toString().endsWith("_CHESTPLATE"))
					{
						if(e.getEntity().getEntityId() == e.getViewer().getEntityId())
						{
							Location k = e.getViewer().getLocation().add(VectorMath.reverse(e.getViewer().getLocation().getDirection().multiply(2)));
							c = Chromatic.getVisibleColor(e.getEntity().getLocation().clone().add(0, 1.4, 0), VectorMath.direction(k.clone().add(0, 1.7, 0), e.getEntity().getLocation().clone().add(0, 1.4, 0)), 64);
						}
						
						else
						{
							c = Chromatic.getVisibleColor(e.getEntity().getLocation().clone().add(0, 1.4, 0), VectorMath.direction(e.getViewer().getLocation().clone().add(0, 1.7, 0), e.getEntity().getLocation().clone().add(0, 1.4, 0)), 64);
						}
					}
					
					if(e.getItem().getType().toString().endsWith("_LEGGINGS"))
					{
						if(e.getEntity().getEntityId() == e.getViewer().getEntityId())
						{
							Location k = e.getViewer().getLocation().add(VectorMath.reverse(e.getViewer().getLocation().getDirection().multiply(2)));
							c = Chromatic.getVisibleColor(e.getEntity().getLocation().clone().add(0, 1, 0), VectorMath.direction(k.clone().add(0, 1.7, 0), e.getEntity().getLocation().clone().add(0, 1, 0)), 64);
						}
						
						else
						{
							c = Chromatic.getVisibleColor(e.getEntity().getLocation().clone().add(0, 1, 0), VectorMath.direction(e.getViewer().getLocation().clone().add(0, 1.7, 0), e.getEntity().getLocation().clone().add(0, 1, 0)), 64);
						}
					}
					
					if(e.getItem().getType().toString().endsWith("_BOOTS"))
					{
						if(e.getEntity().getEntityId() == e.getViewer().getEntityId())
						{
							Location k = e.getViewer().getLocation().add(VectorMath.reverse(e.getViewer().getLocation().getDirection().multiply(2)));
							c = Chromatic.getVisibleColor(e.getEntity().getLocation().clone().add(0, 0.5, 0), VectorMath.direction(k.clone().add(0, 1.7, 0), e.getEntity().getLocation().clone().add(0, 0.5, 0)), 64);
						}
						
						else
						{
							c = Chromatic.getVisibleColor(e.getEntity().getLocation().clone().add(0, 0.7, 0), VectorMath.direction(e.getViewer().getLocation().clone().add(0, 1.7, 0), e.getEntity().getLocation().clone().add(0, 0.7, 0)), 64);
						}
					}
					
					if(target.containsKey(p) && target.get(p).containsKey(e.getViewer()) && target.get(p).get(e.getViewer()).containsKey(e.getSlot()))
					{
						Color x = target.get(p).get(e.getViewer()).get(e.getSlot());
						W.colorArmor(e.getItem(), org.bukkit.Color.fromRGB(x.getRed(), x.getGreen(), x.getBlue()));
						
						if(e.getViewer().getEntityId() == e.getEntity().getEntityId())
						{
							e.setCancelled(true);
						}
					}
					
					if(c == null)
					{
						if(isDay(p.getWorld()))
						{
							c = new Color(DyeColor.LIGHT_BLUE.getColor().getRed(), DyeColor.LIGHT_BLUE.getColor().getGreen(), DyeColor.LIGHT_BLUE.getColor().getBlue());
						}
						
						else
						{
							c = Color.BLACK;
						}
					}
					
					else
					{
						rr += 1;
					}
					
					if(c != null)
					{
						if(!target.containsKey(p))
						{
							target.put(p, new GMap<Player, GMap<EquipmentSlot, Color>>());
						}
						
						if(!target.get(p).containsKey(e.getViewer()))
						{
							target.get(p).put(e.getViewer(), new GMap<EquipmentSlot, Color>());
						}
						
						if(!target.get(p).get(e.getViewer()).containsKey(e.getSlot()))
						{
							target.get(p).get(e.getViewer()).put(e.getSlot(), Color.BLACK);
						}
						
						Color cu = target.get(p).get(e.getViewer()).get(e.getSlot());
						
						int r = cu.getRed();
						int g = cu.getGreen();
						int b = cu.getBlue();
						
						if(c.getRed() > r)
						{
							r = ((r + level) > c.getRed() ? c.getRed() : r + level);
						}
						
						else if(c.getRed() < r)
						{
							r = ((r - level) < c.getRed() ? c.getRed() : r - level);
						}
						
						if(c.getGreen() > g)
						{
							g = ((g + level) > c.getGreen() ? c.getGreen() : g + level);
						}
						
						else if(c.getGreen() < g)
						{
							g = ((g - level) < c.getGreen() ? c.getGreen() : g - level);
						}
						
						if(c.getBlue() > b)
						{
							b = ((b + level) > c.getBlue() ? c.getBlue() : b + level);
						}
						
						else if(c.getBlue() < r)
						{
							b = ((b - level) < c.getBlue() ? c.getBlue() : b - level);
						}
						
						Color cb = new Color(r, g, b);
						target.get(p).get(e.getViewer()).put(e.getSlot(), cb);
					}
				}
			}
		}
		
		t.stop();
	}
	
	public boolean isDay(World world)
	{
		return world.getTime() < 12300 || world.getTime() > 23850;
	}
	
	@Override
	public void onStop()
	{
		
	}
	
	@Override
	public String getStatGraph(Player p)
	{
		if(getLevel() == getMaxLevel())
		{
			return C.LIGHT_PURPLE + F.pc(getMitigation(1)) + " " + getAbilityGraph(20, (double) getLevel(p) / (double) getMaxLevel(), "") + C.LIGHT_PURPLE + " " + F.pc(getMitigation(getMaxLevel())) + " Speed";
		}
		
		return C.LIGHT_PURPLE + F.pc(getMitigation(1)) + " " + getAbilityGraph(20, (double) getLevel(p) / (double) getMaxLevel(), F.pc(getMitigation((int) getLevel(p)))) + C.LIGHT_PURPLE + " " + F.pc(getMitigation(getMaxLevel())) + " Speed";
	}
	
	@Override
	public String getMonitorableData()
	{
		a.put(perf);
		
		return "Perf: " + C.LIGHT_PURPLE + F.fd(a.getAverage(), 2) + " ms" + " " + C.DARK_GRAY + " Ray: " + C.LIGHT_PURPLE + (F.f(rr * 20)) + C.DARK_GRAY + " Outbound: " + C.LIGHT_PURPLE + F.f(pps * 20) + "/s";
	}
	
	@Override
	public String getGraphInitial()
	{
		return "255 \u03A9";
	}
	
	@Override
	public String getGraphMax()
	{
		return F.f((255 * getMaxLevel())) + " \u03A9";
	}
	
	@Override
	public String getGraphCurrent(int level)
	{
		return F.f((255 * level)) + " \u03A9";
	}
}
