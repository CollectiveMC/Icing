package org.cyberpwn.icing;

import org.phantomapi.construct.Ghost;

public class Icing extends Ghost
{
	private BoostController bc;
	
	@Override
	public void preStart()
	{
		bc = new BoostController(this);
		register(bc);
	}
	
	@Override
	public void onStart()
	{
		
	}
	
	@Override
	public void onStop()
	{
		
	}
	
	@Override
	public void postStop()
	{
		
	}
}
