package org.cyberpwn.icing;

import org.phantomapi.construct.Ghost;
import org.phantomapi.text.TagProvider;
import org.phantomapi.util.C;

public class Icing extends Ghost implements TagProvider
{
	private static Icing inst;
	private BoostController bc;
	private CakeController cc;
	private XPController xp;
	private SkillController sk;
	
	@Override
	public void preStart()
	{
		inst = this;
		bc = new BoostController(this);
		cc = new CakeController(this);
		xp = new XPController(this);
		sk = new SkillController(this);
		
		register(bc);
		register(cc);
		register(xp);
		register(sk);
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
	
	public static Icing getInst()
	{
		return inst;
	}
	
	public BoostController getBc()
	{
		return bc;
	}
	
	public CakeController getCc()
	{
		return cc;
	}
	
	public XPController getXp()
	{
		return xp;
	}
	
	public SkillController getSk()
	{
		return sk;
	}
}
