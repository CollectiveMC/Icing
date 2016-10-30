package org.cyberpwn.icing;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.cyberpwn.icing.cakes.TeleportEnder;
import org.cyberpwn.icing.cosmetic.Cake;
import org.cyberpwn.icing.cosmetic.CakeType;
import org.phantomapi.clust.Configurable;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Controller;
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
import org.phantomapi.util.C;
import org.phantomapi.util.FinalInteger;

@Ticked(5)
public class CakeController extends Controller
{
	private GMap<CakeType, GList<Cake>> cakes;
	private CakeDataController cdc;
	
	public CakeController(Controllable parentController)
	{
		super(parentController);
		
		cakes = new GMap<CakeType, GList<Cake>>();
		cdc = new CakeDataController(this);
		
		register(cdc);
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
						Element ex = new PhantomElement(i.getMaterial().getMaterial(), s, i.getName())
						{
							@Override
							public void onClick(Player p, Click ccx, Window wx)
							{
								if(eq != null && !eq.equals(i))
								{
									cdc.get(p).getEquipped().remove(((Configurable) eq).getCodeName());
									cdc.get(p).getEquipped().add(((Configurable) i).getCodeName());
									launchUi(p);
								}
								
								else
								{
									cdc.get(p).getEquipped().add(((Configurable) i).getCodeName());
									launchUi(p);
								}
							}
						};
						
						ex.addText(i.getDescription());
						
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
			e.addText(C.getLastColors(c.getDescription()) + "You have unlocked " + caked.size() + " " + c.getName() + C.AQUA + " " + " effects.");
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
			for(Cake j : cakes.get(type))
			{
				if(((Configurable) j).getCodeName().equals(i))
				{
					cake.add(j);
				}
			}
		}
		
		return cake;
	}
	
	public Cake getEquippedCake(Player p, CakeType type)
	{
		for(String i : cdc.get(p).getEquipped())
		{
			for(Cake j : cakes.get(type))
			{
				if(((Configurable) j).getCodeName().equals(i))
				{
					return j;
				}
			}
		}
		
		return null;
	}
	
	public boolean hasEquipped(Player p, CakeType type)
	{
		return getEquippedCake(p, type) != null;
	}
}
