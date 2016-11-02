package org.cyberpwn.icing;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.cyberpwn.icing.skill.Skill;
import org.cyberpwn.icing.skill.SkillDataController;
import org.cyberpwn.icing.skills.SkillButcher;
import org.cyberpwn.icing.skills.SkillMining;
import org.cyberpwn.icing.skills.SkillSmelting;
import org.cyberpwn.icing.xp.XP;
import org.phantomapi.clust.Configurable;
import org.phantomapi.clust.ConfigurableController;
import org.phantomapi.command.CommandListener;
import org.phantomapi.command.PhantomCommand;
import org.phantomapi.command.PhantomCommandSender;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Controller;
import org.phantomapi.gui.Element;
import org.phantomapi.gui.PhantomElement;
import org.phantomapi.gui.PhantomWindow;
import org.phantomapi.gui.Slot;
import org.phantomapi.gui.Window;
import org.phantomapi.lang.GList;
import org.phantomapi.lang.GSound;
import org.phantomapi.text.MessageBuilder;
import org.phantomapi.util.C;
import org.phantomapi.util.F;
import org.phantomapi.util.FinalInteger;

public class SkillController extends ConfigurableController implements CommandListener
{
	private SkillDataController skillDataController;
	private GList<Skill> skills;
	
	public SkillController(Controllable parentController)
	{
		super(parentController, "skill");
		
		skillDataController = new SkillDataController(this);
		skills = new GList<Skill>();
		skills.add(new SkillMining(this));
		skills.add(new SkillButcher(this));
		skills.add(new SkillSmelting(this));
		
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
				Element pa = new PhantomElement(s.getSkillMaterial().getMaterial(), new Slot(ix.get()), C.GRAY.toString() + C.BOLD + s.fancyName() + C.LIGHT_PURPLE + " " + s.getLevel(p));
				pa.addText(C.GRAY + "XP: " + C.LIGHT_PURPLE + F.f(s.getXp(p)));
				pa.addText(C.GREEN + F.f(s.getXp(p) - XP.getXpForLevel(s.getLevel(p))) + " XP " + C.GRAY + "/ " + C.RED + F.f(XP.getXpForLevel(s.getLevel(p) + 1) - XP.getXpForLevel(s.getLevel(p))) + " XP " + C.YELLOW + "(" + F.pc(s.getProgress(p)) + ")");
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
			
			else
			{
				// TODO skill command help
			}
		}
		
		else if(sender.hasPermission("x.god"))
		{
			// TODO Commands
			// /skill give <player> <skill> <xp>
			// /skill get <player> [skill]
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
}
