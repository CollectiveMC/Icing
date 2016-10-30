package org.cyberpwn.icing;

import java.io.IOException;
import org.cyberpwn.icing.boost.BaseBoost;
import org.cyberpwn.icing.boost.Boost;
import org.cyberpwn.icing.boost.BoostType;
import org.cyberpwn.icing.boost.ExperienceBoost;
import org.cyberpwn.icing.boost.FlyBoost;
import org.phantomapi.command.CommandController;
import org.phantomapi.command.PhantomCommand;
import org.phantomapi.command.PhantomCommandSender;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Ticked;
import org.phantomapi.lang.GList;
import org.phantomapi.lang.GMap;
import org.phantomapi.lang.GTime;
import org.phantomapi.text.MessageBuilder;
import org.phantomapi.transmit.Transmission;
import org.phantomapi.util.C;
import org.phantomapi.util.F;

@Ticked(0)
public class BoostController extends CommandController
{
	private GMap<BoostType, Double> boosts;
	private GMap<BoostType, Integer> timeLeft;
	private GMap<BoostType, Boost> boostMap;
	private static BoostController inst;
	
	public BoostController(Controllable parentController)
	{
		super(parentController, "boost");
		
		inst = this;
		boosts = new GMap<BoostType, Double>();
		timeLeft = new GMap<BoostType, Integer>();
		boostMap = new GMap<BoostType, Boost>();
	}
	
	public void addBoost(BoostType type, Double multiplier, Integer ticks)
	{
		if(boosts.containsKey(type))
		{
			boosts.put(type, boosts.get(type) + multiplier);
			timeLeft.put(type, timeLeft.get(type) + ticks);
		}
		
		else
		{
			boosts.put(type, multiplier);
			timeLeft.put(type, ticks);
			boostMap.get(type).onAdded();
		}
		
		Transmission t = new Transmission("boost-sync-map")
		{
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onResponse(Transmission tr)
			{
				
			}
		};
		
		for(BoostType i : boosts.k())
		{
			t.set("d." + i.toString() + ".multiplier", boosts.get(i));
			t.set("d." + i.toString() + ".ticks", timeLeft.get(i));
		}
		
		try
		{
			t.transmit();
		}
		
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void expireBoost(BoostType type)
	{
		boostMap.get(type).onExpire();
		boosts.remove(type);
		timeLeft.remove(type);
	}
	
	public void tickBoost(BoostType type, double multiplier)
	{
		boostMap.get(type).onTick(multiplier);
	}
	
	@Override
	public void onTick()
	{
		for(BoostType i : boosts.k())
		{
			timeLeft.put(i, timeLeft.get(i) - 1);
			
			if(timeLeft.get(i) <= 0)
			{
				expireBoost(i);
			}
			
			tickBoost(i, boosts.get(i));
		}
	}
	
	public Boost getBoost(BoostType type)
	{
		return boostMap.get(type);
	}
	
	public String getName(BoostType type)
	{
		return getBoost(type).getName();
	}
	
	public String getDescription(BoostType type)
	{
		return getBoost(type).getDescription();
	}
	
	public static BoostController instance()
	{
		return inst;
	}
	
	public static double getMultiple(BoostType type)
	{
		return inst.boosts.get(type);
	}
	
	@Override
	public void onStart()
	{
		boostMap.put(BoostType.FLY, new FlyBoost());
		boostMap.put(BoostType.XP, new ExperienceBoost());
		
		for(BoostType i : boostMap.k())
		{
			loadCluster((BaseBoost) boostMap.get(i), "boosts");
		}
	}
	
	@Override
	public void onStop()
	{
		
	}
	
	@Override
	public boolean onCommand(PhantomCommandSender sender, PhantomCommand cmd)
	{
		sender.setMessageBuilder(new MessageBuilder(this));
		
		if(cmd.getArgs().length == 0)
		{
			sender.sendMessage(C.LIGHT_PURPLE + "There are " + boosts.size() + " boosts active.");
			
			for(BoostType i : boosts.k())
			{
				sender.sendMessage(F.color(getName(i)) + C.GRAY + " - " + F.f(boosts.get(i)) + "x (" + new GTime(50 * timeLeft.get(i)).to(" left") + ")");
			}
		}
		
		else if(cmd.getArgs().length == 2 && sender.hasPermission("boost.god"))
		{
			String type = cmd.getArgs()[0];
			Double mult = Double.valueOf(cmd.getArgs()[1]);
			
			for(BoostType i : BoostType.values())
			{
				if(boostMap.contains(i) && getBoost(i).isEnabled())
				{
					if(i.toString().equalsIgnoreCase(type))
					{
						addBoost(i, mult, getBoost(i).getTicks());
						sender.sendMessage(C.AQUA + "Injected local boost " + i.toString() + " @ " + F.f(mult) + "x");
						return true;
					}
				}
			}
			
			sender.sendMessage(C.RED + "Could not find boost");
		}
		
		return true;
	}
	
	@Override
	public String getChatTag()
	{
		return C.DARK_GRAY + "[" + C.AQUA + "Boost" + C.DARK_GRAY + "]: " + C.GRAY;
	}
	
	@Override
	public String getChatTagHover()
	{
		return C.AQUA + "Server Boosters";
	}
	
	@Override
	public GList<String> getCommandAliases()
	{
		return new GList<String>().qadd("booster").qadd("boosts").qadd("blizzard").qadd("blizzards");
	}
}
