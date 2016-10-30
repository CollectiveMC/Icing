package org.cyberpwn.icing;

public interface Boost
{
	public void onTick(double multiplier);
	
	public void onAdded();
	
	public void onExpire();
	
	public int getTicks();
	
	public double getMaxMultiplier();
	
	public String getName();
	
	public String getDescription();
	
	public boolean isEnabled();
}
