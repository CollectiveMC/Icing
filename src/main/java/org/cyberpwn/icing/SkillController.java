package org.cyberpwn.icing;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.cyberpwn.icing.ability.Ability;
import org.cyberpwn.icing.ability.AbilityDataController;
import org.cyberpwn.icing.skill.BasicSkill;
import org.cyberpwn.icing.skill.Skill;
import org.cyberpwn.icing.skill.SkillDataController;
import org.cyberpwn.icing.skills.SkillArchery;
import org.cyberpwn.icing.skills.SkillAxes;
import org.cyberpwn.icing.skills.SkillButcher;
import org.cyberpwn.icing.skills.SkillCombat;
import org.cyberpwn.icing.skills.SkillConstruction;
import org.cyberpwn.icing.skills.SkillEnchanting;
import org.cyberpwn.icing.skills.SkillExcavation;
import org.cyberpwn.icing.skills.SkillFarming;
import org.cyberpwn.icing.skills.SkillFishing;
import org.cyberpwn.icing.skills.SkillHeavyArmor;
import org.cyberpwn.icing.skills.SkillLightArmor;
import org.cyberpwn.icing.skills.SkillMining;
import org.cyberpwn.icing.skills.SkillSmelting;
import org.cyberpwn.icing.skills.SkillStealth;
import org.cyberpwn.icing.skills.SkillSwords;
import org.cyberpwn.icing.skills.SkillTaming;
import org.cyberpwn.icing.skills.SkillUnarmed;
import org.cyberpwn.icing.skills.SkillWoodCutting;
import org.cyberpwn.icing.xp.XP;
import org.cyberpwn.icing.xp.XPPlayer;
import org.phantomapi.Phantom;
import org.phantomapi.clust.Configurable;
import org.phantomapi.clust.ConfigurableController;
import org.phantomapi.command.CommandListener;
import org.phantomapi.command.PhantomCommand;
import org.phantomapi.command.PhantomCommandSender;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Controller;
import org.phantomapi.construct.Ticked;
import org.phantomapi.currency.ExperienceCurrency;
import org.phantomapi.gui.Click;
import org.phantomapi.gui.Element;
import org.phantomapi.gui.Guis;
import org.phantomapi.gui.Notification;
import org.phantomapi.gui.PhantomElement;
import org.phantomapi.gui.PhantomWindow;
import org.phantomapi.gui.Slot;
import org.phantomapi.gui.Window;
import org.phantomapi.lang.GList;
import org.phantomapi.lang.GMap;
import org.phantomapi.lang.GSound;
import org.phantomapi.lang.Priority;
import org.phantomapi.lang.Title;
import org.phantomapi.nms.NMSX;
import org.phantomapi.sfx.Audio;
import org.phantomapi.text.MessageBuilder;
import org.phantomapi.util.C;
import org.phantomapi.util.F;
import org.phantomapi.util.FinalInteger;
import org.phantomapi.util.Players;

@Ticked(20)
public class SkillController extends ConfigurableController implements CommandListener
{
	private SkillDataController skillDataController;
	private AbilityDataController abilityDataController;
	private GList<Skill> skills;
	private GMap<Player, Block> lastInteractionBlock;
	private GMap<Player, Block> lastInteractionEntity;
	private GMap<Player, GList<Block>> lastInteractionPlace;
	
	public SkillController(Controllable parentController)
	{
		super(parentController, "skill");
		
		skillDataController = new SkillDataController(this);
		abilityDataController = new AbilityDataController(this);
		skills = new GList<Skill>();
		skills.add(new SkillMining(this));
		skills.add(new SkillButcher(this));
		skills.add(new SkillSmelting(this));
		skills.add(new SkillEnchanting(this));
		skills.add(new SkillArchery(this));
		skills.add(new SkillFarming(this));
		skills.add(new SkillConstruction(this));
		skills.add(new SkillWoodCutting(this));
		skills.add(new SkillCombat(this));
		skills.add(new SkillTaming(this));
		skills.add(new SkillExcavation(this));
		skills.add(new SkillHeavyArmor(this));
		skills.add(new SkillLightArmor(this));
		skills.add(new SkillSwords(this));
		skills.add(new SkillAxes(this));
		skills.add(new SkillFishing(this));
		skills.add(new SkillUnarmed(this));
		skills.add(new SkillStealth(this));
		
		register(abilityDataController);
		register(skillDataController);
		
		for(Skill i : skills)
		{
			register((Controller) i);
		}
		
		lastInteractionBlock = new GMap<Player, Block>();
		lastInteractionEntity = new GMap<Player, Block>();
		lastInteractionPlace = new GMap<Player, GList<Block>>();
	}
	
	@EventHandler
	public void on(PlayerQuitEvent e)
	{
		lastInteractionBlock.remove(e.getPlayer());
		lastInteractionPlace.remove(e.getPlayer());
	}
	
	public XPPlayer getXpp(Player p)
	{
		return Icing.inst().getXp().getXpDataController().get(p);
	}
	
	public void interactBlock(Player p, Block b)
	{
		if(p.getGameMode().equals(GameMode.CREATIVE))
		{
			return;
		}
		
		if(lastInteractionBlock.containsKey(p) && lastInteractionBlock.get(p).equals(b))
		{
			getXpp(p).discred(0.03);
		}
		
		lastInteractionBlock.put(p, b);
	}
	
	public void interactEntity(Player p, Block b)
	{
		if(p.getGameMode().equals(GameMode.CREATIVE))
		{
			return;
		}
		
		if(lastInteractionEntity.containsKey(p) && lastInteractionEntity.get(p).equals(b))
		{
			getXpp(p).discred(0.02);
		}
		
		lastInteractionEntity.put(p, b);
	}
	
	@EventHandler
	public void on(EntityDamageByEntityEvent e)
	{
		if(e.getDamager() instanceof Player)
		{
			Player p = (Player) e.getDamager();
			
			interactEntity(p, e.getEntity().getLocation().getBlock());
		}
	}
	
	@EventHandler
	public void on(BlockBreakEvent e)
	{
		if(e.getPlayer().getGameMode().equals(GameMode.CREATIVE))
		{
			return;
		}
		
		interactBlock(e.getPlayer(), e.getBlock());
		
		if(lastInteractionPlace.containsKey(e.getPlayer()))
		{
			if(lastInteractionPlace.get(e.getPlayer()).contains(e.getBlock()))
			{
				getXpp(e.getPlayer()).discred(0.03);
			}
		}
	}
	
	@EventHandler
	public void on(BlockPlaceEvent e)
	{
		if(e.getPlayer().getGameMode().equals(GameMode.CREATIVE))
		{
			return;
		}
		
		interactBlock(e.getPlayer(), e.getBlock());
		
		if(!lastInteractionPlace.containsKey(e.getPlayer()))
		{
			lastInteractionPlace.put(e.getPlayer(), new GList<Block>());
		}
		
		lastInteractionPlace.get(e.getPlayer()).add(e.getBlock());
		lastInteractionPlace.get(e.getPlayer()).removeDuplicates();
		
		while(lastInteractionPlace.get(e.getPlayer()).size() > 12)
		{
			lastInteractionPlace.get(e.getPlayer()).pop();
		}
	}
	
	@Override
	public void onStart()
	{
		for(Skill i : skills)
		{
			loadCluster((Configurable) i, "skills");
		}
	}
	
	@Override
	public void onStop()
	{
		
	}
	
	@Override
	public void onTick()
	{
		for(Player i : onlinePlayers())
		{
			XPPlayer xpp = Icing.inst().getXp().getXpDataController().get(i);
			double b = 0;
			
			if(i.getTicksLived() / 20 > 120)
			{
				b += 0.02;
			}
			
			if(i.getHealth() < 4)
			{
				b += 0.04;
			}
			
			if(i.getFireTicks() > 20)
			{
				b += 0.01;
			}
			
			if(i.getFoodLevel() < 5)
			{
				b += 0.01;
			}
			
			if(i.getActivePotionEffects().size() > 2)
			{
				b += 0.01;
			}
			
			if(onlinePlayers().size() > 40)
			{
				b += 0.01;
			}
			
			if(i.getWorld().getName().contains("nether"))
			{
				b += 0.04;
			}
			
			if(i.getWorld().getName().contains("end"))
			{
				b += 0.04;
			}
			
			if(i.getLocation().getBlockY() < 40)
			{
				b += 0.01;
			}
			
			if(new ExperienceCurrency().get(i) > 1000)
			{
				b += 0.1;
			}
			
			if(i.hasPermission("boost.a"))
			{
				b += 0.05;
				b += (Math.random() * 0.05);
			}
			
			if(i.hasPermission("boost.b"))
			{
				b += 0.1;
				b += (Math.random() * 0.1);
			}
			
			if(i.hasPermission("boost.c"))
			{
				b += 0.15;
				b += (Math.random() * 0.15);
			}
			
			if(i.hasPermission("boost.d"))
			{
				b += 0.2;
				b += (Math.random() * 0.2);
			}
			
			if(xpp.getBoosterTicks() > 0)
			{
				xpp.setBoosterTicks(xpp.getBoosterTicks() - 20);
				b += xpp.getBoosterAmount();
				
				if(xpp.getBoosterTicks() <= 0)
				{
					Notification n = new Notification();
					Title t = new Title();
					t.setTitle(C.RED + "Boost Expired!");
					t.setSubTitle(C.DARK_GRAY + "XP Boost has expired");
					t.setAction("  .  ");
					t.setFadeIn(3);
					t.setFadeOut(80);
					t.setStayTime(40);
					Audio a = new Audio();
					a.add(new GSound(Sound.EXPLODE, 1f, 1.95f));
					n.setAudible(a);
					n.setTitle(t);
					n.setPriority(Priority.LOW);
					XP.q(i, n);
				}
			}
			
			else
			{
				xpp.setBoosterTicks(0);
				xpp.setBoosterAmount(0);
			}
			
			b += (Math.random() * 0.1);
			b -= xpp.getDiscredit();
			
			if(xpp.getDiscredit() > 0)
			{
				xpp.setDiscredit(xpp.getDiscredit() - (Math.random() * 0.012));
			}
			
			if(xpp.getDiscredit() < 0)
			{
				xpp.setDiscredit(0);
			}
			
			XP.setBoost(i, b);
			
			if(Phantom.instance().isBungeecord())
			{
				NMSX.sendTabTitle(i, C.AQUA + "" + C.BOLD + "Glacial" + C.DARK_AQUA + "" + C.BOLD + "Realms\n" + C.AQUA + C.BOLD + F.f(Phantom.getNetworkCount()) + " Online" + C.DARK_AQUA + C.BOLD + " (" + F.f(Phantom.instance().onlinePlayers().size()) + " on " + Phantom.getBungeeNameName() + ") " + ping(i), C.GREEN + "" + C.UNDERLINE + F.f(XP.getLevelForXp(XP.getXp(i))) + " " + getGraph(20, XP.percentToNextLevel(XP.getXp(i))) + " " + F.f(XP.getLevelForXp(XP.getXp(i)) + 1) + "\n\n" + C.RESET + C.GREEN + C.BOLD + F.f(XP.xpToNextLevel(XP.getXp(i))) + " XP" + C.DARK_GRAY + " to level " + C.GREEN + C.BOLD + F.f(XP.getLevelForXp(XP.getXp(i)) + 1));
			}
			
			else
			{
				NMSX.sendTabTitle(i, C.AQUA + "" + C.BOLD + "Glacial" + C.DARK_AQUA + "" + C.BOLD + "Realms\n" + C.AQUA + C.BOLD + F.f(Phantom.instance().onlinePlayers().size()) + " Online " + ping(i), C.GREEN + "" + C.UNDERLINE + F.f(XP.getLevelForXp(XP.getXp(i))) + " " + getGraph(20, XP.percentToNextLevel(XP.getXp(i))) + " " + F.f(XP.getLevelForXp(XP.getXp(i)) + 1) + "\n\n" + C.RESET + C.GREEN + C.BOLD + F.f(XP.xpToNextLevel(XP.getXp(i))) + " XP" + C.DARK_GRAY + " to level " + C.GREEN + C.BOLD + F.f(XP.getLevelForXp(XP.getXp(i)) + 1));
			}
		}
	}
	
	public String ping(Player p)
	{
		int ping = NMSX.ping(p);
		String v = "";
		
		if(ping > 1000)
		{
			v = C.RED + "" + C.BOLD + F.f((double) ping / 1000.0, 1) + "s";
		}
		
		else if(ping > 500)
		{
			v = C.RED + "" + C.BOLD + ping + "ms";
		}
		
		else if(ping > 300)
		{
			v = C.GOLD + "" + C.BOLD + ping + "ms";
		}
		
		else if(ping > 150)
		{
			v = C.YELLOW + "" + C.BOLD + ping + "ms";
		}
		
		else if(ping > 100)
		{
			v = C.GREEN + "" + C.BOLD + ping + "ms";
		}
		
		else if(ping > 50)
		{
			v = C.AQUA + "" + C.BOLD + ping + "ms";
		}
		
		else
		{
			v = C.LIGHT_PURPLE + "" + C.BOLD + ping + "ms";
		}
		
		return C.DARK_GRAY + "\u2770 " + v + C.RESET + C.DARK_GRAY + " \u2771";
	}
	
	public SkillDataController getSkillDataController()
	{
		return skillDataController;
	}
	
	@Override
	public String getMessageNoPermission()
	{
		return "";
	}
	
	@Override
	public String getMessageNotPlayer()
	{
		return "";
	}
	
	@Override
	public String getMessageNotConsole()
	{
		return "";
	}
	
	@Override
	public String getMessageInvalidArgument(String arg, String neededType)
	{
		return "";
	}
	
	@Override
	public String getMessageInvalidArguments(int given, int expected, int expectedMax)
	{
		return "";
	}
	
	@Override
	public String getMessageUnknownSubCommand(String given)
	{
		return "";
	}
	
	@Override
	public String getChatTag()
	{
		return C.DARK_GRAY + "[" + C.GREEN + "Skill" + C.DARK_GRAY + "]: " + C.GRAY;
	}
	
	@Override
	public String getChatTagHover()
	{
		return C.GREEN + "CHA-CHING! It's a skill. Get farmed.";
	}
	
	public String getGraph(int len, double pc)
	{
		if(pc < 0)
		{
			pc = 0;
		}
		
		String g = C.GREEN.toString() + C.UNDERLINE;
		int mc = (int) ((double) len * pc);
		int vc = len - mc;
		
		for(int i = 0; i < mc; i++)
		{
			g = g + " ";
		}
		
		g = g + C.DARK_GRAY + C.UNDERLINE;
		
		for(int i = 0; i < vc; i++)
		{
			g = g + " ";
		}
		
		return g;
	}
	
	public String getAbilityGraph(int len, double pc)
	{
		if(pc < 0)
		{
			pc = 0;
		}
		
		len = 24;
		String g = C.LIGHT_PURPLE.toString() + C.UNDERLINE;
		int mc = (int) ((double) len * pc);
		int vc = len - mc;
		
		for(int i = 0; i < mc; i++)
		{
			g = g + " ";
		}
		
		g = g + C.DARK_GRAY + C.UNDERLINE;
		
		for(int i = 0; i < vc; i++)
		{
			g = g + " ";
		}
		
		return g;
	}
	
	public void showAbilities(Player p, Skill s)
	{
		Window w = new PhantomWindow(C.LIGHT_PURPLE + s.fancyName() + " Abilities", p)
		{
			@Override
			public boolean onClick(Element element, Player p)
			{
				new GSound(Sound.CLICK, 1f, 1.6f).play(p);
				return true;
			}
			
			@Override
			public void onClose(Window w, Player p)
			{
				new GSound(Sound.FIREWORK_LARGE_BLAST2, 1f, 0.1f).play(p);
			}
		};
		
		GList<Slot> slots = Guis.getCentered(s.getAbilities().size(), 2);
		
		for(Ability i : s.getAbilities())
		{
			Slot sl = slots.pop();
			Element e = new PhantomElement(Material.BARRIER, sl, C.RED + i.fancyName())
			{
				@Override
				public void onClick(Player p, Click c, Window w)
				{
					if(c.equals(Click.RIGHT))
					{
						if(i.isUnlocked(p))
						{
							if(i.isEnabled(p))
							{
								i.setEnabled(p, false);
							}
							
							else
							{
								i.setEnabled(p, true);
							}
							
							w.close();
							showAbilities(p, s);
						}
					}
					
					else if(c.equals(Click.LEFT))
					{
						if(i.isUnlocked(p))
						{
							if(i.getLevel(p) < i.getMaxLevel() && i.getSkill().getShards(p) >= i.getUpgradeCost() && i.getSkill().getLevel(p) >= i.getMinimumUpgradeLevel(p))
							{
								i.addLevel(p);
								i.getSkill().takeShards(p, i.getUpgradeCost());
								
								Notification n = new Notification();
								Title t = new Title();
								t.setTitle("   ");
								t.setSubTitle(C.DARK_GRAY + "Improved " + C.AQUA + i.getSkill().fancyName() + " " + i.fancyName());
								t.setAction(C.AQUA + i.getSkill().fancyName() + " " + i.fancyName() + " increased to level " + i.getLevel(p));
								t.setFadeIn(0);
								t.setStayTime(0);
								t.setFadeOut(25);
								n.setTitle(t);
								n.setAudible(new GSound(Sound.FIREWORK_LARGE_BLAST2, 1f, 0.38f));
								n.setPriority(Priority.LOW);
								XP.q(p, n);
								w.close();
								showAbilities(p, s);
							}
						}
						
						else
						{
							if(i.getLevel(p) < i.getMaxLevel() && i.getSkill().getShards(p) >= i.getUnlockCost() && i.getSkill().getLevel(p) >= i.getLevel())
							{
								i.addLevel(p);
								i.getSkill().takeShards(p, i.getUnlockCost());
								
								Notification n = new Notification();
								Title t = new Title();
								t.setTitle("   ");
								t.setSubTitle(C.DARK_GRAY + "Unlocked " + C.AQUA + i.getSkill().fancyName() + " " + i.fancyName());
								t.setAction(C.AQUA + i.getSkill().fancyName() + " " + i.fancyName() + " unlocked!");
								t.setFadeIn(0);
								t.setStayTime(0);
								t.setFadeOut(25);
								n.setTitle(t);
								n.setAudible(new GSound(Sound.FIREWORK_LARGE_BLAST2, 1f, 0.38f));
								n.setPriority(Priority.LOW);
								XP.q(p, n);
								w.close();
								showAbilities(p, s);
							}
						}
					}
				}
			};
			
			e.addText(C.GRAY + i.getDescription());
			
			if(((Configurable) i).getCodeName().equals("chameleon"))
			{
				e.addText(C.WHITE + " ");
				e.addText(C.WHITE + "Each player sees different colors on you");
				e.addText(C.WHITE + "depending on what they see behind you");
				e.addText(C.WHITE + " ");
				e.addText(C.WHITE + "Leveling up this ability increases the speed");
				e.addText(C.WHITE + "at which you blend in with your surroundings");
				e.addText(C.WHITE + " ");
			}
			
			if(((Configurable) i).getCodeName().equals("swiftness"))
			{
				e.addText(C.WHITE + " ");
				e.addText(C.WHITE + "While crouching, your will move faster");
				e.addText(C.WHITE + "along with the added fov boost");
				e.addText(C.WHITE + " ");
				e.addText(C.WHITE + "Pro Tip: Try looking around corners while");
				e.addText(C.WHITE + "sneaking. You may see someone you diddnt before.");
				e.addText(C.WHITE + " ");
			}
			
			if(((Configurable) i).getCodeName().equals("block"))
			{
				e.addText(C.WHITE + " ");
				e.addText(C.WHITE + "By default, swords block 50% damage. This ");
				e.addText(C.WHITE + "ability allows you to block up to 75% damage.");
				e.addText(C.WHITE + " ");
			}
			
			if(((Configurable) i).getCodeName().equals("training strength"))
			{
				e.addText(C.WHITE + " ");
				e.addText(C.WHITE + "By taiming animals with this ability, you");
				e.addText(C.WHITE + "train them better. They have more health.");
				e.addText(C.WHITE + " ");
				e.addText(C.WHITE + "Pro Tip: Bring some wolves to your next fight.");
				e.addText(C.WHITE + " ");
			}
			
			if(((Configurable) i).getCodeName().equals("force"))
			{
				e.addText(C.WHITE + " ");
				e.addText(C.WHITE + "By improving your fist and swing, you can");
				e.addText(C.WHITE + "punch a deafing blow.");
				e.addText(C.WHITE + " ");
				e.addText(C.WHITE + "Leveling up this ability increases your damage.");
				e.addText(C.WHITE + "max level is near 6.5 hearts of damage.");
				e.addText(C.WHITE + " ");
			}
			
			if(((Configurable) i).getCodeName().equals("precision"))
			{
				e.addText(C.WHITE + " ");
				e.addText(C.WHITE + "Upon aiming your next shot, you will see");
				e.addText(C.WHITE + "a distance and target of what you are aiming at");
				e.addText(C.WHITE + " ");
				e.addText(C.WHITE + "Leveling up this ability increases the scanning");
				e.addText(C.WHITE + "range");
				e.addText(C.WHITE + " ");
			}
			
			if(((Configurable) i).getCodeName().equals("tiller"))
			{
				e.addText(C.WHITE + " ");
				e.addText(C.WHITE + "This ability allows a hoe to till up to 7x7");
				e.addText(C.WHITE + "b;pcls at a given time.");
				e.addText(C.WHITE + " ");
				e.addText(C.WHITE + "Pro Tip: Right click and drag to literally shred");
				e.addText(C.WHITE + "the land of grass to soil");
				e.addText(C.WHITE + " ");
			}
			
			if(((Configurable) i).getCodeName().equals("acrobatics"))
			{
				e.addText(C.WHITE + " ");
				e.addText(C.WHITE + "This ability reduces fall damage and other");
				e.addText(C.WHITE + "ambient damage causes");
				e.addText(C.WHITE + " ");
				e.addText(C.WHITE + "Note: Mobs and Players still deal full damage.");
				e.addText(C.WHITE + " ");
			}
			
			if(((Configurable) i).getCodeName().equals("power shot"))
			{
				e.addText(C.WHITE + " ");
				e.addText(C.WHITE + "This ability increases the speed of");
				e.addText(C.WHITE + "arrows shot.");
				e.addText(C.WHITE + " ");
				e.addText(C.WHITE + "Leading targets becomes easier and the arrow");
				e.addText(C.WHITE + "can reach it's destination quicker");
				e.addText(C.WHITE + " ");
			}
			
			if(i.isUnlocked(p))
			{
				e.setType(i.getMaterialBlock().getMaterial());
				e.setMetadata(i.getMaterialBlock().getData());
				e.setTitle(C.AQUA + i.fancyName() + " " + i.getLevel(p));
				e.setCount((int) i.getLevel(p));
				
				if(i.getLevel() < i.getMaxLevel())
				{
					e.addText(C.GRAY + "Upgrade Cost: " + (i.getSkill().getShards(p) >= i.getUpgradeCost() ? C.LIGHT_PURPLE.toString() : C.RED.toString()) + i.getUpgradeCost() + " " + i.getSkill().fancyName() + " Shards");
					e.addText(C.GRAY + "Upgrade Requires: " + (i.getSkill().getLevel(p) >= i.getMinimumUpgradeLevel(p) ? C.LIGHT_PURPLE.toString() : C.RED.toString()) + i.getSkill().fancyName() + " " + i.getMinimumUpgradeLevel(p));
				}
				
				else
				{
					e.addText(C.LIGHT_PURPLE + "MAX LEVEL");
				}
				
				if(i.isEnabled(p))
				{
					Slot ss = new Slot(sl.getX(), sl.getY() - 1);
					Element ee = new PhantomElement(Material.STAINED_GLASS_PANE, ss, C.GREEN + "Enabled");
					ee.setMetadata((byte) 5);
					w.addElement(ee);
					e.addText(C.GREEN + "Enabled (right click to disable)");
				}
				
				else
				{
					Slot ss = new Slot(sl.getX(), sl.getY() - 1);
					Element ee = new PhantomElement(Material.STAINED_GLASS_PANE, ss, C.RED + "Disabled");
					ee.setMetadata((byte) 14);
					w.addElement(ee);
					e.addText(C.RED + "Disabled (right click to enable)");
				}
				
				e.addText(i.getStatGraph(p));
			}
			
			else
			{
				e.addText(C.GRAY + "Unlock Cost: " + (i.getSkill().getShards(p) >= i.getUnlockCost() ? C.LIGHT_PURPLE.toString() : C.RED.toString()) + i.getUpgradeCost() + " " + i.getSkill().fancyName() + " Shards");
				e.addText(C.GRAY + "Unlock Requires: " + (i.getSkill().getLevel(p) >= i.getLevel() ? C.LIGHT_PURPLE.toString() : C.RED.toString()) + i.getSkill().fancyName() + " " + i.getMinimumUpgradeLevel(p));
			}
			
			w.addElement(e);
		}
		
		Element bg = new PhantomElement(Material.STAINED_GLASS_PANE, new Slot(0), "  ");
		bg.setMetadata((byte) 15);
		w.setBackground(bg);
		w.setViewport(3);
		w.open();
	}
	
	public void openSkillView(Player p)
	{
		Window w = new PhantomWindow(C.GREEN + "Skills", p)
		{
			@Override
			public boolean onClick(Element element, Player p)
			{
				new GSound(Sound.CLICK, 1f, 1.6f).play(p);
				return true;
			}
			
			@Override
			public void onClose(Window w, Player p)
			{
				new GSound(Sound.FIREWORK_LARGE_BLAST2, 1f, 0.1f).play(p);
			}
		};
		
		FinalInteger ix = new FinalInteger(0);
		
		for(String i : Icing.inst().getSk().getSkillDataController().get(p).getKnownSkills())
		{
			Skill s = null;
			
			for(Skill j : skills)
			{
				if(((Configurable) j).getCodeName().equals(i))
				{
					s = j;
					break;
				}
			}
			
			if(s != null)
			{
				Skill v = s;
				Element pa = new PhantomElement(s.getSkillMaterial().getMaterial(), new Slot(ix.get()), C.GRAY.toString() + C.BOLD + s.fancyName() + C.LIGHT_PURPLE + " " + s.getLevel(p))
				{
					@Override
					public void onClick(Player p, Click c, Window w)
					{
						showAbilities(p, v);
					}
				};
				
				pa.addText(C.GRAY + "XP: " + C.LIGHT_PURPLE + F.f(s.getXp(p)));
				pa.addText(C.GREEN + F.f(s.getXp(p) - XP.getXpForLevel(s.getLevel(p))) + " XP " + C.GRAY + "/ " + C.RED + F.f(XP.getXpForLevel(s.getLevel(p) + 1) - XP.getXpForLevel(s.getLevel(p))) + " XP " + C.YELLOW + "(" + F.pc(s.getProgress(p)) + ")");
				pa.addText(C.AQUA + "Contains " + F.f(Icing.getInst().getSk().getSkillDataController().get(p).getSkillPoints(((BasicSkill) s).getCodeName())) + " Shards");
				pa.addText(getGraph(18, XP.percentToNextLevel(s.getXp(p))));
				w.addElement(pa);
				ix.add(1);
			}
		}
		
		Element bg = new PhantomElement(Material.STAINED_GLASS_PANE, new Slot(0), "  ");
		bg.setMetadata((byte) 15);
		w.setBackground(bg);
		w.open();
	}
	
	@Override
	public boolean onCommand(PhantomCommandSender sender, PhantomCommand cmd)
	{
		sender.setMessageBuilder(new MessageBuilder(this));
		
		if(cmd.getArgs().length == 0)
		{
			if(sender.isPlayer())
			{
				openSkillView(sender.getPlayer());
			}
		}
		
		else if(sender.hasPermission("x.god"))
		{
			if(cmd.getArgs().length == 1 && cmd.getArgs()[0].equalsIgnoreCase("list"))
			{
				for(Skill i : skills)
				{
					sender.sendMessage(((Configurable) i).getCodeName());
				}
			}
			
			if(cmd.getArgs().length > 1)
			{
				Player p = Players.getPlayer(cmd.getArgs()[1]);
				
				if(p == null)
				{
					sender.sendMessage(C.RED + "Cannot Find Player");
					return true;
				}
				
				if(cmd.getArgs()[0].equalsIgnoreCase("get"))
				{
					if(cmd.getArgs().length == 2)
					{
						for(String i : skillDataController.get(p).getKnownSkills())
						{
							sender.sendMessage(i + ": " + C.GREEN + F.f(skillDataController.get(p).getSkill(i)) + " XP " + C.YELLOW + F.f(XP.getLevelForXp(skillDataController.get(p).getSkill(i))));
						}
					}
					
					else
					{
						sender.sendMessage(C.RED + "/skill get <player> <skill>");
					}
				}
				
				if(cmd.getArgs()[0].equalsIgnoreCase("give") && cmd.getArgs().length == 4)
				{
					String skill = cmd.getArgs()[2];
					Integer xp = Integer.valueOf(cmd.getArgs()[3]);
					
					for(Skill i : skills)
					{
						if(skill.equalsIgnoreCase(((Configurable) i).getCodeName()) || skill.replaceAll("-", " ").equalsIgnoreCase(((Configurable) i).getCodeName()))
						{
							sender.sendMessage("Reward Dispatched");
							i.addReward(p, xp);
						}
					}
				}
			}
		}
		
		else
		{
			sender.sendMessage("Use it like this: " + C.GREEN + "/skill");
		}
		
		return true;
	}
	
	@Override
	public String getCommandName()
	{
		return "skill";
	}
	
	@Override
	public GList<String> getCommandAliases()
	{
		return new GList<String>().qadd("sk").qadd("ski").qadd("skills").qadd("perk").qadd("perks");
	}
	
	public AbilityDataController getAbilityDataController()
	{
		return abilityDataController;
	}
	
	public GList<Skill> getSkills()
	{
		return skills;
	}
}
