package org.cyberpwn.icing;

import org.cyberpwn.icing.xp.XPDataController;
import org.phantomapi.clust.ConfigurableController;
import org.phantomapi.construct.Controllable;

public class XPController extends ConfigurableController
{
	private XPDataController xpDataController;
	
	public XPController(Controllable parentController)
	{
		super(parentController, "xp");
		
		xpDataController = new XPDataController(this);
		
		register(xpDataController);
	}
	
	@Override
	public void onStart()
	{
		
	}
	
	@Override
	public void onStop()
	{
		
	}
	
	public XPDataController getXpDataController()
	{
		return xpDataController;
	}
}
