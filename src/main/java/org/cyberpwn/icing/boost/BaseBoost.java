package org.cyberpwn.icing.boost;

import org.bukkit.event.Listener;
import org.phantomapi.clust.Comment;
import org.phantomapi.clust.ConfigurableObject;
import org.phantomapi.clust.Keyed;

public abstract class BaseBoost extends ConfigurableObject implements Boost, Listener
{
	@Comment("The duration in ticks for the boost")
	@Keyed("boost.duration")
	public int ticks = 36000;
	
	@Comment("The max multiplier a boost can ever have")
	@Keyed("boost.max-multiplier")
	public double maxMultiplier = 4.0;
	
	@Comment("The colored name of this boost")
	@Keyed("boost.name")
	public String name = "&aBoost";
	
	@Comment("The colored description of the boost")
	@Keyed("boost.description")
	public String description = "&bThe boost description";
	
	@Comment("Should this boost be enabled")
	@Keyed("boost.enabled")
	public boolean enabled = true;
	
	public BaseBoost(BoostType type)
	{
		super(type.toString().toLowerCase().replaceAll("_", "-"));
	}
	
	@Override
	public int getTicks()
	{
		return ticks;
	}
	
	@Override
	public double getMaxMultiplier()
	{
		return maxMultiplier;
	}
	
	@Override
	public String getName()
	{
		return name;
	}
	
	@Override
	public String getDescription()
	{
		return description;
	}
	
	@Override
	public boolean isEnabled()
	{
		return enabled;
	}
}
