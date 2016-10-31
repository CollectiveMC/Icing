package org.cyberpwn.icing;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.cyberpwn.icing.cakes.ArrowNone;
import org.cyberpwn.icing.cakes.DeathNone;
import org.cyberpwn.icing.cakes.KillNone;
import org.cyberpwn.icing.cakes.PlayerNone;
import org.cyberpwn.icing.cakes.TeleportEnder;
import org.cyberpwn.icing.cakes.TeleportNone;
import org.cyberpwn.icing.cosmetic.Cake;
import org.cyberpwn.icing.cosmetic.CakeType;
import org.cyberpwn.icing.cosmetic.TeleportSlice;
import org.phantomapi.clust.Configurable;
import org.phantomapi.command.CommandController;
import org.phantomapi.command.PhantomCommand;
import org.phantomapi.command.PhantomCommandSender;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Ticked;
import org.phantomapi.gui.Click;
import org.phantomapi.gui.Element;
import org.phantomapi.gui.Guis;
import org.phantomapi.gui.PhantomElement;
import org.phantomapi.gui.PhantomWindow;
import org.phantomapi.gui.Slot;
import org.phantomapi.gui.Window;
import org.phantomapi.lang.GList;
import org.phantomapi.lang.GMap;
import org.phantomapi.lang.GSound;
import org.phantomapi.sync.TaskLater;
import org.phantomapi.util.C;
import org.phantomapi.util.F;
import org.phantomapi.util.FinalInteger;
import org.phantomapi.util.P;

@Ticked(5)
public class CakeController extends CommandController
{
	private GMap<CakeType, GList<Cake>> cakes;
	private CakeDataController cdc;
	
	public CakeController(Controllable parentController)
	{
		super(parentController, "cosmetic");
		
		cakes = new GMap<CakeType, GList<Cake>>();
		cdc = new CakeDataController(this);
		
		register(cdc);
		
		register(new TeleportNone(this));
		register(new PlayerNone(this));
		register(new KillNone(this));
		register(new DeathNone(this));
		register(new ArrowNone(this));
		
		register(new TeleportEnder(this));
	}
	
	@Override
	public void onStart()
	{
		for(Controllable i : getControllers())
		{
			if(i instanceof Cake)
			{
				Cake cake = (Cake) i;
				CakeType cakeType = cake.getType();
				loadCluster((Configurable) i, cakeType.toString().toLowerCase());
				
				if(!cakes.containsKey(cakeType))
				{
					cakes.put(cakeType, new GList<Cake>());
				}
				
				v("Registering " + cakeType.toString() + " >> " + C.YELLOW + F.color(cake.getName()));
				cakes.get(cakeType).add(cake);
			}
		}
	}
	
	@Override
	public void onStop()
	{
		
	}
	
	public void launchUi(Player p)
	{
		Window w = new PhantomWindow(C.AQUA + "Cosmetics", p)
		{
			@Override
			public boolean onClick(Element element, Player p)
			{
				new GSound(Sound.CLICK, 1f, 1.5f).play(p);
				return true;
			}
			
			@Override
			public void onClose(Window w, Player p)
			{
				new GSound(Sound.WITHER_SPAWN, 0.54f, 1f).play(p);
			}
		};
		
		GList<CakeType> ct = new GList<CakeType>(CakeType.values());
		GList<Slot> slots = Guis.sortLTR(Guis.getCentered(ct.size(), 2));
		Element bg = new PhantomElement(Material.STAINED_GLASS_PANE, new Slot(0), " ");
		bg.setMetadata((byte) 15);
		w.setBackground(bg);
		w.setViewport(3);
		
		for(Slot i : slots)
		{
			CakeType c = ct.pop();
			GList<Cake> caked = getOwnedCakes(p, c);
			Cake eq = getEquippedCake(p, c);
			
			Element e = new PhantomElement(c.getMb().getMaterial(), i, c.getName())
			{
				@Override
				public void onClick(Player p, Click cl, Window w)
				{
					Window cw = new PhantomWindow(c.getName() + " Effects", p)
					{
						@Override
						public boolean onClick(Element element, Player p)
						{
							new GSound(Sound.CLICK, 1f, 1.5f).play(p);
							return true;
						}
						
						@Override
						public void onClose(Window w, Player p)
						{
							new GSound(Sound.WITHER_SPAWN, 0.54f, 1f).play(p);
						}
					};
					
					Element bg = new PhantomElement(Material.STAINED_GLASS_PANE, new Slot(0), " ");
					bg.setMetadata((byte) 15);
					cw.setBackground(bg);
					FinalInteger fi = new FinalInteger(0);
					
					for(Cake i : caked)
					{
						Slot s = new Slot(fi.get());
						Element ex = new PhantomElement(i.getMaterial().getMaterial(), s, F.color(i.getName()))
						{
							@Override
							public void onClick(Player p, Click ccx, Window wx)
							{
								cdc.get(p).equipped.removeDuplicates();
								
								if(eq != null && !eq.equals(i))
								{
									cdc.get(p).equipped.remove(((Configurable) eq).getCodeName());
									cdc.get(p).equipped.add(((Configurable) i).getCodeName());
									launchUi(p);
								}
								
								else
								{
									cdc.get(p).equipped.add(((Configurable) i).getCodeName());
									launchUi(p);
								}
								
								cdc.get(p).equipped.removeDuplicates();
								s(cdc.get(p).equipped.toString(", "));
							}
						};
						
						ex.addText(F.color(i.getDescription()));
						
						if(eq != null && eq.equals(i))
						{
							ex.addText(C.GREEN + "Equipped!");
						}
						
						cw.addElement(ex);
						fi.add(1);
					}
					
					cw.open();
				}
			};
			
			e.setMetadata(c.getMb().getData());
			e.setCount(caked.size());
			e.addText(c.getDescription());
			e.addText(C.getLastColors(c.getDescription()) + "You have unlocked " + caked.size() + " " + c.getName() + C.getLastColors(c.getDescription()) + " " + " effects.");
			e.addText(C.getLastColors(c.getDescription()) + "Equipped: " + (eq == null ? C.RED + "None" : eq.getName()));
			w.addElement(e);
		}
		
		w.open();
	}
	
	public GList<Cake> getOwnedCakes(Player p)
	{
		GList<Cake> cake = new GList<Cake>();
		
		for(String i : cdc.get(p).getOwned())
		{
			for(CakeType j : cakes.k())
			{
				for(Cake k : cakes.get(j))
				{
					if(((Configurable) k).getCodeName().equals(i))
					{
						cake.add(k);
					}
				}
			}
		}
		
		return cake;
	}
	
	public GList<Cake> getEquippedCakes(Player p)
	{
		GList<Cake> cake = new GList<Cake>();
		
		for(String i : cdc.get(p).getEquipped())
		{
			for(CakeType j : cakes.k())
			{
				for(Cake k : cakes.get(j))
				{
					if(((Configurable) k).getCodeName().equals(i))
					{
						cake.add(k);
					}
				}
			}
		}
		
		return cake;
	}
	
	public GList<Cake> getOwnedCakes(Player p, CakeType type)
	{
		GList<Cake> cake = new GList<Cake>();
		
		for(String i : cdc.get(p).getOwned())
		{
			if(cakes.containsKey(type))
			{
				for(Cake j : cakes.get(type))
				{
					if(((Configurable) j).getCodeName().equals(i))
					{
						cake.add(j);
					}
				}
			}
		}
		
		return cake;
	}
	
	public Cake getEquippedCake(Player p, CakeType type)
	{
		for(String i : cdc.get(p).getEquipped())
		{
			if(cakes.containsKey(type))
			{
				for(Cake j : cakes.get(type))
				{
					if(((Configurable) j).getCodeName().equals(i))
					{
						return j;
					}
				}
			}
		}
		
		return null;
	}
	
	public boolean hasEquipped(Player p, CakeType type)
	{
		return getEquippedCake(p, type) != null;
	}
	
	@Override
	public boolean onCommand(PhantomCommandSender sender, PhantomCommand cmd)
	{
		if(cmd.getArgs().length > 0 && sender.hasPermission("icing.god"))
		{
			if(cmd.getArgs().length == 3)
			{
				if(cmd.getArgs()[0].equalsIgnoreCase("give"))
				{
					if(P.canFindPlayer(cmd.getArgs()[1]))
					{
						Player p = P.findPlayer(cmd.getArgs()[1]);
						
						for(CakeType i : cakes.k())
						{
							for(Cake j : cakes.get(i))
							{
								if(((Configurable) j).getCodeName().equals(cmd.getArgs()[2]))
								{
									if(!getOwnedCakes(p, i).contains(j))
									{
										cdc.get(p).getOwned().add(((Configurable) j).getCodeName());
										p.sendMessage(C.GREEN + "Unlocked Effect " + F.color(j.getName()) + " - " + F.color(j.getDescription()));
										sender.sendMessage(p.getName() + " " + C.GREEN + "Unlocked Effect " + F.color(j.getName()) + " - " + F.color(j.getDescription()));
									}
									
									else
									{
										sender.sendMessage(C.RED + p.getName() + " Already owns that.");
									}
								}
							}
						}
					}
					
					else
					{
						sender.sendMessage(C.RED + "Cannot find player.");
					}
				}
				
				else
				{
					sender.sendMessage(C.RED + "/icing give <player> <cosmetic>");
				}
			}
		}
		
		else
		{
			launchUi(sender.getPlayer());
		}
		
		return true;
	}
	
	@Override
	public GList<String> getCommandAliases()
	{
		return new GList<String>().qadd("cosmetic").qadd("icing").qadd("ice");
	}
	
	public void scrub(Player p)
	{
		for(CakeType i : cakes.k())
		{
			for(Cake j : cakes.get(i))
			{
				if(((Configurable) j).getCodeName().endsWith("-none"))
				{
					if(!cdc.get(p).getOwned().contains(((Configurable) j).getCodeName()))
					{
						cdc.get(p).getOwned().add(((Configurable) j).getCodeName());
						s("Adding default effect for " + p.getName() + " > " + ((Configurable) j).getCodeName());
					}
				}
			}
		}
		
		for(CakeType i : cakes.k())
		{
			if(getEquippedCake(p, i) == null)
			{
				for(Cake j : cakes.get(i))
				{
					if(((Configurable) j).getCodeName().endsWith("-none"))
					{
						s("Equipping default effect for " + p.getName() + " > " + ((Configurable) j).getCodeName());
						cdc.get(p).getEquipped().add(((Configurable) j).getCodeName());
						break;
					}
				}
			}
		}
		
		cdc.get(p).getEquipped().removeDuplicates();
	}
	
	public void playTeleport(Player p)
	{
		new TaskLater(2)
		{
			@Override
			public void run()
			{
				Cake c = getEquippedCake(p, CakeType.TELEPORT);
				
				if(c != null)
				{
					((TeleportSlice) c).onTeleport(p);
				}
			}
		};
	}
	
	@EventHandler
	public void on(PlayerTeleportEvent e)
	{
		playTeleport(e.getPlayer());
	}
	
	@EventHandler
	public void on(PlayerJoinEvent e)
	{
		new TaskLater(10)
		{
			@Override
			public void run()
			{
				scrub(e.getPlayer());
				playTeleport(e.getPlayer());
			}
		};
	}
}
