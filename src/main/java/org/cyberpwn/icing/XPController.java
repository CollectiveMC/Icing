package org.cyberpwn.icing;

import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.cyberpwn.icing.xp.XP;
import org.cyberpwn.icing.xp.XPDataController;
import org.cyberpwn.icing.xp.XPEvent;
import org.cyberpwn.icing.xp.XPReason;
import org.phantomapi.Phantom;
import org.phantomapi.clust.ConfigurableController;
import org.phantomapi.construct.Controllable;
import org.phantomapi.gui.Notification;
import org.phantomapi.lang.GSound;
import org.phantomapi.lang.Priority;
import org.phantomapi.lang.Title;
import org.phantomapi.sfx.Audio;
import org.phantomapi.util.C;
import org.phantomapi.util.F;

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
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onNotify(XPEvent e)
	{
		if(!e.isCancelled())
		{
			e.setXp((long) (e.getXp() + (e.getXp() * XP.getBoost(e.getPlayer()))));
			XPReason reason = e.getReason();
			Notification n = new Notification();
			Title t = new Title();
			t.setTitle("    ");
			t.setSubTitle(C.DARK_GRAY + "+ " + C.AQUA + C.BOLD + F.f(e.getXp()) + C.RESET + C.AQUA + " XP");
			t.setAction(C.DARK_GRAY + "+ " + C.AQUA + F.pc(XP.getBoost(e.getPlayer())) + C.YELLOW + " " + reason.fancy());
			t.setFadeIn(4);
			t.setFadeOut(10);
			t.setStayTime(15);
			Audio a = new Audio();
			a.add(new GSound(Sound.ORB_PICKUP, 1f, 1.35f));
			a.add(new GSound(Sound.ORB_PICKUP, 1f, 1.0f));
			a.add(new GSound(Sound.ORB_PICKUP, 1f, 1.85f));
			n.setAudible(a);
			n.setTitle(t);
			n.setPriority(Priority.LOW);
			Phantom.queueNotification(n);
		}
	}
}
