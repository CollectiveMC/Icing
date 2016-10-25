package org.cyberpwn.icing;

import java.io.File;
import java.io.IOException;
import org.cyberpwn.icing.boost.FlyBoost;
import org.phantomapi.Phantom;
import org.phantomapi.clust.ConfigurableController;
import org.phantomapi.clust.DataCluster;
import org.phantomapi.command.Command;
import org.phantomapi.command.CommandAlias;
import org.phantomapi.command.PhantomCommand;
import org.phantomapi.command.PhantomSender;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Ticked;
import org.phantomapi.lang.GList;
import org.phantomapi.lang.GTime;
import org.phantomapi.text.MessageBuilder;
import org.phantomapi.transmit.Transmission;
import org.phantomapi.util.C;

@Ticked(1200)
public class BoostController extends ConfigurableController
{
	private GList<BlizzardInstance> instances;
	private GList<BlizzardInstance> base;
	
	public BoostController(Controllable parentController)
	{
		super(parentController, "boosters");
		
		instances = new GList<BlizzardInstance>();
		base = new GList<BlizzardInstance>();
		
		base.add(new FlyBoost());
	}
	
	@Override
	public void onStart()
	{
		getPlugin().getDataFolder().mkdirs();
		File active = new File(getPlugin().getDataFolder(), "active");
		File config = new File(getPlugin().getDataFolder(), "types");
		
		config.mkdirs();
		active.mkdirs();
		
		for(BlizzardInstance i : base)
		{
			i.getConfiguration().set("boost.type", i.getCodeName());
			loadCluster(i, "types");
		}
	}
	
	@Override
	public void onTick()
	{
		for(BlizzardInstance i : instances.copy())
		{
			if(i.isExpired())
			{
				instances.remove(i);
			}
			
			i.tick();
		}
	}
	
	@Override
	public void onStop()
	{
		
	}
	
	public BlizzardInstance create(BlizzardInstance instance, double mult)
	{
		for(BlizzardInstance i : base)
		{
			if(i.getCodeName().equals(instance.getCodeName()))
			{
				instance.getConfiguration().setData(i.getConfiguration().copy().getData());
				setTime(instance, i.getConfiguration().getInt("blizzard.diration"));
				setMult(instance, mult);
				
				return instance;
			}
		}
		
		return null;
	}
	
	public BlizzardInstance getInstance(byte[] b)
	{
		try
		{
			DataCluster cc = new DataCluster(b);
			
			for(BlizzardInstance i : base)
			{
				if(cc.getString("blizzard.type").equals(i.getConfiguration().getString("blizzard.type")))
				{
					BlizzardInstance ins = i.copy();
					ins.getConfiguration().setData(cc.getData());
					return ins;
				}
			}
		}
		
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Command("boost")
	@CommandAlias("booster")
	public void onBoost(PhantomSender sender, PhantomCommand cmd)
	{
		sender.setMessageBuilder(new MessageBuilder(Phantom.instance()));
		
		if(sender.hasPermission("boost.god"))
		{
			if(cmd.getArgs().length == 0)
			{
				sender.sendMessage("There are " + C.WHITE + instances.size() + C.GRAY + " Boosts active");
				
				for(BlizzardInstance i : instances)
				{
					sender.sendMessage("- " + i.getCodeName() + " " + C.WHITE + new GTime(i.minutesLeft() * 60000).to() + " " + C.GRAY + "x" + getMult(i));
				}
			}
			
			else if(cmd.getArgs().length == 2)
			{
				for(BlizzardInstance i : base)
				{
					if(i.getCodeName().equalsIgnoreCase(cmd.getArgs()[0]))
					{
						double d = Double.valueOf(cmd.getArgs()[1]);
						sender.sendMessage("Created Boost");
						dispatchInject(create(i, d));
						
						return;
					}
				}
				
				sender.sendMessage("Choose a boost type");
				sender.sendMessage("Then use /boost <TYPE> <MULTIPLIER>");
				
				for(BlizzardInstance i : base)
				{
					sender.sendMessage(C.YELLOW + i.getCodeName());
				}
			}
		}
		
		else
		{
			sender.sendMessage("There are " + C.WHITE + instances.size() + C.GRAY + " Boosts active");
			
			for(BlizzardInstance i : instances)
			{
				sender.sendMessage("- " + i.getCodeName() + " " + C.WHITE + new GTime(i.minutesLeft() * 60000).to() + " " + C.GRAY + "x" + getMult(i));
			}
		}
	}
	
	public void dispatchInject(BlizzardInstance instance)
	{
		if(Phantom.instance().isBungeecord())
		{
			Transmission t = new Transmission("blizzard-dispatch")
			{
				private static final long serialVersionUID = 1L;
				
				@Override
				public void onResponse(Transmission r)
				{
					
				}
			};
			
			t.add(instance.getConfiguration());
			
			try
			{
				t.transmit();
			}
			
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
		
		inject(instance);
	}
	
	public void inject(BlizzardInstance instance)
	{
		for(BlizzardInstance i : instances)
		{
			if(i.getConfiguration().getString("blizzard.type").equals(instance.getConfiguration().getString("blizzard.type")))
			{
				double mult = getMult(i) + getMult(instance);
				int ml = i.minutesLeft() + instance.minutesLeft();
				
				i.getConfiguration().set("blizzard.remaining", ml);
				setMult(i, mult);
				return;
			}
		}
		
		instances.add(instance);
	}
	
	public double getMult(BlizzardInstance instance)
	{
		return instance.getConfiguration().getDouble("blizzard.global-multiplier");
	}
	
	public void setTime(BlizzardInstance instance, int time)
	{
		instance.getConfiguration().set("blizzard.remaining", time);
	}
	
	public void setMult(BlizzardInstance instance, double mult)
	{
		instance.getConfiguration().set("blizzard.global-multiplier", mult);
	}
}
