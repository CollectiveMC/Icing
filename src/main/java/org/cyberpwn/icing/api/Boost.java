package org.cyberpwn.icing.api;

import org.phantomapi.util.M;

public class Boost implements BoosterInstance
{
	protected final String name;
	protected final String activator;
	protected final int level;
	protected final long finishTime;
	
	public Boost(String name, String activator, int level, long finishTime)
	{
		this.name = name;
		this.activator = activator;
		this.level = level;
		this.finishTime = finishTime;
	}
	
	@Override
	public String getName()
	{
		return name;
	}
	
	@Override
	public int getLevel()
	{
		return level;
	}
	
	@Override
	public String getActivator()
	{
		return activator;
	}
	
	@Override
	public long getFinishTime()
	{
		return finishTime;
	}
	
	@Override
	public boolean isFinished()
	{
		return M.ms() > getFinishTime();
	}
}
