package org.cyberpwn.icing;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.cyberpwn.icing.xp.XP;
import org.cyberpwn.icing.xp.XPDataController;
import org.cyberpwn.icing.xp.XPEvent;
import org.cyberpwn.icing.xp.XPReason;
import org.phantomapi.Phantom;
import org.phantomapi.clust.ConfigurableController;
import org.phantomapi.command.CommandListener;
import org.phantomapi.command.PhantomCommand;
import org.phantomapi.command.PhantomCommandSender;
import org.phantomapi.construct.Controllable;
import org.phantomapi.gui.Notification;
import org.phantomapi.lang.GList;
import org.phantomapi.lang.GSound;
import org.phantomapi.lang.Priority;
import org.phantomapi.lang.Title;
import org.phantomapi.sfx.Audio;
import org.phantomapi.text.MessageBuilder;
import org.phantomapi.util.C;
import org.phantomapi.util.F;
import org.phantomapi.util.Players;

public class XPController extends ConfigurableController implements CommandListener
{
	private XPDataController xpDataController;
	
	public XPController(Controllable parentController)
	{
		super(parentController, "xp");
		
		xpDataController = new XPDataController(this);
		
		register(xpDataController);
	}
	
	@Override
	public void onStart()
	{
		
	}
	
	@Override
	public void onStop()
	{
		
	}
	
	public XPDataController getXpDataController()
	{
		return xpDataController;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onNotify(XPEvent e)
	{
		if(!e.isCancelled())
		{
			e.setXp((long) (e.getXp() + (e.getXp() * XP.getBoost(e.getPlayer()))));
			XPReason reason = e.getReason();
			Notification n = new Notification();
			Title t = new Title();
			t.setTitle("    ");
			t.setSubTitle(C.DARK_GRAY + "+ " + C.GREEN + C.BOLD + F.f(e.getXp()) + C.RESET + C.GREEN + " XP");
			t.setAction(C.DARK_GRAY + "+ " + C.GREEN + C.BOLD + F.f(e.getXp()) + C.RESET + C.GREEN + " XP" + " (" + F.pc(XP.getBoost(e.getPlayer())) + ") " + C.YELLOW + reason.fancy());
			t.setFadeIn(0);
			t.setFadeOut(8);
			t.setStayTime(0);
			Audio a = new Audio();
			a.add(new GSound(Sound.SUCCESSFUL_HIT, 1f, 1.95f));
			n.setAudible(a);
			n.setTitle(t);
			n.setPriority(Priority.LOW);
			Phantom.queueNotification(e.getPlayer(), n);
		}
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
		return C.DARK_GRAY + "[" + C.GREEN + "X" + C.DARK_GRAY + "]: " + C.GRAY;
	}
	
	@Override
	public String getChatTagHover()
	{
		return C.GREEN + "CHA-CHING! It's a farm. Get farmed.";
	}
	
	@Override
	public boolean onCommand(PhantomCommandSender sender, PhantomCommand cmd)
	{
		sender.setMessageBuilder(new MessageBuilder(this));
		
		if(cmd.getArgs().length == 0)
		{
			if(sender.isPlayer())
			{
				sender.sendMessage("XP: " + C.GREEN + F.f(XP.getXp(sender.getPlayer())));
				sender.sendMessage("Boost: " + C.GREEN + "+ " + F.pc(XP.getBoost(sender.getPlayer())));
				sender.sendMessage("Level: " + C.GREEN + "+ " + F.f(XP.getLevelForXp(XP.getXp(sender.getPlayer()))));
				sender.sendMessage("XP For Level Up: " + C.GREEN + "+ " + F.f(XP.xpToNextLevel(XP.getXp(sender.getPlayer()))));
				sender.sendMessage("XP Percent Level Up: " + C.GREEN + "+ " + F.pc(XP.percentToNextLevel(XP.getXp(sender.getPlayer()))));
			}
			
			else
			{
				sender.sendMessage("/x give <player> <xp>");
				sender.sendMessage("/x get <player>");
				XP.printLeveling(30);
			}
		}
		
		else if(sender.hasPermission("x.god"))
		{
			if(cmd.getArgs().length >= 2)
			{
				Player p = Players.getPlayer(cmd.getArgs()[1]);
				
				if(p == null)
				{
					sender.sendMessage(C.RED + "Cannot find player.");
					return true;
				}
				
				if(cmd.getArgs()[0].equalsIgnoreCase("get"))
				{
					sender.sendMessage("XP: " + C.GREEN + F.f(XP.getXp(p)));
					sender.sendMessage("Boost: " + C.GREEN + "+ " + F.pc(XP.getBoost(p)));
				}
				
				else if(cmd.getArgs().length >= 3 && cmd.getArgs()[0].equalsIgnoreCase("give"))
				{
					try
					{
						long amt = Long.valueOf(cmd.getArgs()[2]);
						XP.giveXp(p, amt, XPReason.UNKNOWN);
					}
					
					catch(Exception e)
					{
						sender.sendMessage(C.RED + "Looking for a number. Not Garbage.");
					}
				}
			}
		}
		
		else
		{
			sender.sendMessage("Use it like this: " + C.GREEN + "/x");
		}
		
		return true;
	}
	
	@Override
	public String getCommandName()
	{
		return "x";
	}
	
	@Override
	public GList<String> getCommandAliases()
	{
		return new GList<String>().qadd("experience");
	}
}
