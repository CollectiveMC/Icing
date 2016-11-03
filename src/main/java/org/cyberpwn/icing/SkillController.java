package org.cyberpwn.icing;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
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
import org.cyberpwn.icing.skills.SkillSocial;
import org.cyberpwn.icing.skills.SkillSwords;
import org.cyberpwn.icing.skills.SkillTaming;
import org.cyberpwn.icing.skills.SkillWoodCutting;
import org.cyberpwn.icing.xp.XP;
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
import org.phantomapi.lang.GSound;
import org.phantomapi.lang.Priority;
import org.phantomapi.lang.Title;
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
	
	public SkillController(Controllable parentController)
	{
		super(parentController, "skill");
		
		skillDataController = new SkillDataController(this);
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
		skills.add(new SkillSocial(this));
		skills.add(new SkillHeavyArmor(this));
		skills.add(new SkillLightArmor(this));
		skills.add(new SkillSwords(this));
		skills.add(new SkillAxes(this));
		skills.add(new SkillFishing(this));
		
		register(abilityDataController);
		register(skillDataController);
		
		for(Skill i : skills)
		{
			register((Controller) i);
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
			}
			
			if(i.hasPermission("boost.b"))
			{
				b += 0.1;
			}
			
			if(i.hasPermission("boost.c"))
			{
				b += 0.15;
			}
			
			if(i.hasPermission("boost.d"))
			{
				b += 0.2;
			}
			
			XP.setBoost(i, b);
		}
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
			Element e = new PhantomElement(Material.BARRIER, sl, C.RED + i.name())
			{
				@Override
				public void onClick(Player p, Click c, Window w)
				{
					if(i.isUnlocked(p))
					{
						if(i.getSkill().getShards(p) >= i.getUpgradeCost() && i.getSkill().getLevel(p) >= i.getMinimumUpgradeLevel(p))
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
							Phantom.queueNotification(p, n);
						}
					}
					
					else
					{
						if(i.getSkill().getShards(p) >= i.getUnlockCost() && i.getSkill().getLevel(p) >= i.getLevel())
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
							Phantom.queueNotification(p, n);
						}
					}
				}
			};
			
			e.addText(C.GRAY + i.getDescription());
			
			if(i.isUnlocked(p))
			{
				e.setType(i.getMaterialBlock().getMaterial());
				e.setMetadata(i.getMaterialBlock().getData());
				e.setTitle(C.AQUA + i.fancyName() + " " + i.getLevel(p));
				e.setCount((int) i.getLevel(p));
				e.addText(C.GRAY + "Upgrade Cost: " + (i.getSkill().getShards(p) >= i.getUpgradeCost() ? C.LIGHT_PURPLE.toString() : C.RED.toString()) + i.getUpgradeCost() + " " + i.getSkill().fancyName() + " Shards");
				e.addText(C.GRAY + "Upgrade Requires: " + (i.getSkill().getLevel(p) >= i.getMinimumUpgradeLevel(p) ? C.LIGHT_PURPLE.toString() : C.RED.toString()) + i.getSkill().fancyName() + " " + i.getMinimumUpgradeLevel(p));
				e.addText(getAbilityGraph(30, (double) i.getLevel(p) / (double) i.getMaxLevel()));
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
				pa.addText(getGraph(32, XP.percentToNextLevel(s.getXp(p))));
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
