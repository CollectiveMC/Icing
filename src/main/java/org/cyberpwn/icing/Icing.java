package org.cyberpwn.icing;

import org.phantomapi.construct.Ghost;
import org.phantomapi.text.TagProvider;
import org.phantomapi.util.C;

public class Icing extends Ghost implements TagProvider
{
	private static Icing inst;
	private BoostController bc;
	
	@Override
	public void preStart()
	{
		inst = this;
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
	
	@Override
	public String getChatTag()
	{
		return C.DARK_GRAY + "[" + C.AQUA + "Icing" + C.DARK_GRAY + "]: " + C.GRAY;
	}
	
	@Override
	public String getChatTagHover()
	{
		return C.AQUA + "Icing under the cake!";
	}
	
	public static Icing inst()
	{
		return inst;
	}
}
