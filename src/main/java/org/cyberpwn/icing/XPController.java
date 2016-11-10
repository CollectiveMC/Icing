package org.cyberpwn.icing;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.cyberpwn.icing.xp.XP;
import org.cyberpwn.icing.xp.XPDataController;
import org.cyberpwn.icing.xp.XPEvent;
import org.cyberpwn.icing.xp.XPPlayer;
import org.cyberpwn.icing.xp.XPReason;
import org.phantomapi.clust.ConfigurableController;
import org.phantomapi.command.CommandListener;
import org.phantomapi.command.PhantomCommand;
import org.phantomapi.command.PhantomCommandSender;
import org.phantomapi.construct.Controllable;
import org.phantomapi.gui.Notification;
import org.phantomapi.lang.GList;
import org.phantomapi.lang.GSound;
import org.phantomapi.lang.GTime;
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
	private PlaceholderController pc;
	
	public XPController(Controllable parentController)
	{
		super(parentController, "xp");
		
		xpDataController = new XPDataController(this);
		
		register(xpDataController);
		
		pc = new PlaceholderController();
		pc.hook();
	}
	
	@Override
	public void onStart()
	{
		
	}
	
	@Override
	public void onStop()
	{
		pc.unhook();
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
			
			if(e.getXp() < 0)
			{
				e.setXp(0);
				return;
			}
			
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
			XP.q(e.getPlayer(), n);
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
		
		if(cmd.getArgs().length == 1 && cmd.getArgs()[0].equalsIgnoreCase("stfu"))
		{
			Icing.getInst().getSk().getXpp(sender.getPlayer()).setStfu(!Icing.getInst().getSk().getXpp(sender.getPlayer()).isStfu());
			sender.sendMessage("Silent: " + Icing.getInst().getSk().getXpp(sender.getPlayer()).isStfu());
		}
		
		if(cmd.getArgs().length == 0)
		{
			if(sender.isPlayer())
			{
				XPPlayer xpp = Icing.inst().getXp().getXpDataController().get(sender.getPlayer());
				sender.sendMessage("XP: " + C.GREEN + F.f(XP.getXp(sender.getPlayer())));
				sender.sendMessage("Boost: " + C.GREEN + "+ " + F.pc(XP.getBoost(sender.getPlayer())) + " " + C.RED + "(Discred: " + F.pc(xpp.getDiscredit()) + ")");
				sender.sendMessage("Booster Time Left: " + C.GREEN + new GTime(50 * xpp.getBoosterTicks()).to("left"));
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
				if(cmd.getArgs().length >= 3 && cmd.getArgs()[0].equalsIgnoreCase("createboost"))
				{
					try
					{
						double boost = Double.valueOf(cmd.getArgs()[1]);
						int ticks = Integer.valueOf(cmd.getArgs()[2]);
						ItemStack is = createBoost(ticks, boost);
						sender.getPlayer().getInventory().addItem(is);
						return true;
					}
					
					catch(Exception e)
					{
						sender.sendMessage(C.RED + "Looking for a number. Not Garbage.");
						return true;
					}
				}
				
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
	
	public int getTicks(ItemStack is)
	{
		if(is.getType().equals(Material.GOLDEN_APPLE))
		{
			ItemMeta im = is.getItemMeta();
			
			if(im.getDisplayName().equals(C.LIGHT_PURPLE + "XP Boost"))
			{
				if(im.getLore().size() == 3 && im.getLore().get(2).contains("//"))
				{
					try
					{
						String v = C.stripColor(im.getLore().get(2));
						return Integer.valueOf(v.split("//")[0]);
					}
					
					catch(Exception e)
					{
						
					}
					
					return -1;
				}
			}
		}
		
		return -1;
	}
	
	public double getBoost(ItemStack is)
	{
		if(is.getType().equals(Material.GOLDEN_APPLE))
		{
			ItemMeta im = is.getItemMeta();
			
			if(im.getDisplayName().equals(C.LIGHT_PURPLE + "XP Boost"))
			{
				if(im.getLore().size() == 3 && im.getLore().get(2).contains("//"))
				{
					try
					{
						String v = C.stripColor(im.getLore().get(2));
						return Double.valueOf(v.split("//")[1]);
					}
					
					catch(Exception e)
					{
						
					}
					
					return -1;
				}
			}
		}
		
		return -1;
	}
	
	public boolean isBoost(ItemStack is)
	{
		if(is.getType().equals(Material.GOLDEN_APPLE))
		{
			ItemMeta im = is.getItemMeta();
			
			if(im.getDisplayName().equals(C.LIGHT_PURPLE + "XP Boost"))
			{
				if(im.getLore().size() == 3 && im.getLore().get(2).contains("//"))
				{
					try
					{
						String v = C.stripColor(im.getLore().get(2));
						Integer.valueOf(v.split("//")[0]);
						Double.valueOf(v.split("//")[1]);
					}
					
					catch(Exception e)
					{
						
					}
					
					return true;
				}
			}
		}
		
		return false;
	}
	
	@SuppressWarnings("deprecation")
	public ItemStack createBoost(int ticks, double boost)
	{
		ItemStack is = new ItemStack(Material.GOLDEN_APPLE);
		is.getData().setData((byte) 1);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(C.LIGHT_PURPLE + "XP Boost");
		im.setLore(new GList<String>().qadd(C.GREEN + "Boost: " + F.pc(boost)).qadd(C.AQUA + "Duration: " + new GTime(ticks * 50).to()).qadd(C.BLACK + "" + ticks + "//" + boost));
		is.setItemMeta(im);
		is.addUnsafeEnchantment(Enchantment.DURABILITY, 1337);
		
		return is;
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
	
	@EventHandler
	public void on(PlayerItemConsumeEvent e)
	{
		if(e.getItem() != null && isBoost(e.getItem()))
		{
			int ticks = getTicks(e.getItem());
			double boost = getBoost(e.getItem());
			XPPlayer xpp = Icing.inst().getXp().getXpDataController().get(e.getPlayer());
			xpp.setBoosterTicks(xpp.getBoosterTicks() + ticks);
			xpp.setBoosterAmount(xpp.getBoosterAmount() + boost);
			Notification n = new Notification();
			Title t = new Title();
			t.setTitle(C.DARK_GRAY + "+ " + C.GREEN + F.pc(xpp.getBoosterAmount()));
			t.setSubTitle(C.DARK_GRAY + "XP Boost increased by " + C.GREEN + F.pc(xpp.getBoosterAmount()) + C.DARK_GRAY + " for " + C.GOLD + new GTime(50 * xpp.getBoosterTicks()).to());
			t.setAction("  .  ");
			t.setFadeIn(3);
			t.setFadeOut(80);
			t.setStayTime(40);
			Audio a = new Audio();
			a.add(new GSound(Sound.EXPLODE, 1f, 1.95f));
			n.setAudible(a);
			n.setTitle(t);
			n.setPriority(Priority.LOW);
			n.play(e.getPlayer());
			e.setCancelled(true);
			
			if(e.getPlayer().getItemInHand().getAmount() > 1)
			{
				e.getPlayer().getItemInHand().setAmount(e.getPlayer().getItemInHand().getAmount() - 1);
			}
			
			else
			{
				e.getPlayer().setItemInHand(null);
			}
		}
	}
}
