package org.cyberpwn.icing.api;

public interface BoosterInstance
{
	public long getFinishTime();
	
	public boolean isFinished();
	
	public String getName();
	
	public int getLevel();
	
	public String getActivator();
}
