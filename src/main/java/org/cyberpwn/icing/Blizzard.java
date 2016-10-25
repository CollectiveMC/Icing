package org.cyberpwn.icing;

import java.io.Serializable;
import org.phantomapi.clust.ConfigurableObject;
import org.phantomapi.clust.Keyed;
import org.phantomapi.lang.GList;

public abstract class Blizzard extends ConfigurableObject implements BlizzardInstance, Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Keyed("blizzard.name")
	public String name = "Name";
	
	@Keyed("blizzard.description")
	public String description = "What it does";
	
	@Keyed("blizzard.global-multiplier")
	public double multiplier = 1.0;
	
	@Keyed("blizzard.stackable")
	public boolean stackable = true;
	
	@Keyed("blizzard.remaining")
	public int minutesLeft = 30;
	
	@Keyed("blizzard.type")
	public String type = "NULL";
	
	@Keyed("blizzard.diration")
	public int minutesDuration = 30;
	
	@Keyed("blizzard.sources")
	public GList<String> sources = new GList<String>();
	
	public Blizzard(String cn)
	{
		super(cn);
	}
	
	@Override
	public void tick()
	{
		minutesLeft--;
		
		if(minutesLeft <= 0)
		{
			onExpire();
			
			return;
		}
		
		onTick();
	}
	
	@Override
	public boolean isExpired()
	{
		return minutesLeft <= 0;
	}
	
	@Override
	public String getSource()
	{
		return sources.toString(", ");
	}
	
	@Override
	public double getMultiplier()
	{
		return multiplier;
	}
	
	@Override
	public int minutesLeft()
	{
		return minutesLeft;
	}
	
	@Override
	public void onStart()
	{
		
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + minutesDuration;
		result = prime * result + minutesLeft;
		long temp;
		temp = Double.doubleToLongBits(multiplier);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((sources == null) ? 0 : sources.hashCode());
		result = prime * result + (stackable ? 1231 : 1237);
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(this == obj)
		{
			return true;
		}
		
		if(obj == null)
		{
			return false;
		}
		
		if(getClass() != obj.getClass())
		{
			return false;
		}
		
		Blizzard other = (Blizzard) obj;
		
		if(description == null)
		{
			if(other.description != null)
			{
				return false;
			}
		}
		
		else if(!description.equals(other.description))
		{
			return false;
		}
		
		if(minutesDuration != other.minutesDuration)
		{
			return false;
		}
		
		if(minutesLeft != other.minutesLeft)
		{
			return false;
		}
		
		if(Double.doubleToLongBits(multiplier) != Double.doubleToLongBits(other.multiplier))
		{
			return false;
		}
		
		if(name == null)
		{
			if(other.name != null)
			{
				return false;
			}
		}
		
		else if(!name.equals(other.name))
		{
			return false;
		}
		
		if(sources == null)
		{
			if(other.sources != null)
			{
				return false;
			}
		}
		
		else if(!sources.equals(other.sources))
		{
			return false;
		}
		
		if(stackable != other.stackable)
		{
			return false;
		}
		
		if(type == null)
		{
			if(other.type != null)
			{
				return false;
			}
		}
		
		else if(!type.equals(other.type))
		{
			return false;
		}
		
		return true;
	}
}
