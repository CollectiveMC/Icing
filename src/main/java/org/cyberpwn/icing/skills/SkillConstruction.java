package org.cyberpwn.icing.skills;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.ItemStack;
import org.cyberpwn.icing.skill.BasicSkill;
import org.cyberpwn.icing.xp.XP;
import org.cyberpwn.icing.xp.XPReason;
import org.phantomapi.clust.Keyed;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.ControllerMessage;
import org.phantomapi.construct.Ticked;
import org.phantomapi.lang.GMap;
import org.phantomapi.sync.TaskLater;
import org.phantomapi.util.Players;
import org.phantomapi.world.MaterialBlock;

@Ticked(20)
public class SkillConstruction extends BasicSkill
{
	private GMap<Player, Integer> exc;
	
	@Keyed("blocks-per-xp")
	public int bpxp = 6;
	
	@Keyed("base")
	public int base = 3;
	
	public SkillConstruction(Controllable parentController)
	{
		super(parentController, "construction", XPReason.CONSTRUCTION);
		
		exc = new GMap<Player, Integer>();
	}
	
	@Override
	public void onStart()
	{
		
	}
	
	@Override
	public void onTick()
	{
		popRewardMap();
	}
	
	@Override
	public void onStop()
	{
		
	}
	
	public int getAmount(Player player)
	{
		int amt = 0;
		
		for(ItemStack is : player.getInventory().getContents())
		{
			if(is != null && !is.getType().equals(Material.AIR))
			{
				amt += is.getAmount();
			}
		}
		
		return amt;
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void on(CraftItemEvent e)
	{
		if(e.isCancelled())
		{
			return;
		}
		
		if(e.getInventory().getType().equals(InventoryType.WORKBENCH))
		{
			if(e.getSlotType().equals(SlotType.RESULT))
			{
				Player p = (Player) e.getWhoClicked();
				
				if(!XP.isReady(p))
				{
					return;
				}
				
				if(p.getGameMode().equals(GameMode.CREATIVE))
				{
					return;
				}
				
				int amta = getAmount((Player) e.getWhoClicked());
				
				new TaskLater()
				{
					@Override
					public void run()
					{
						int amtb = getAmount((Player) e.getWhoClicked());
						int amt = amtb - amta;
						addReward(p, base * amt);
						XP.dropRandom(p.getLocation());
					}
				};
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void on(BlockPlaceEvent e)
	{
		if(e.isCancelled())
		{
			return;
		}
		
		if(!XP.isReady(e.getPlayer()))
		{
			return;
		}
		
		if(!exc.containsKey(e.getPlayer()))
		{
			exc.put(e.getPlayer(), 0);
		}
		
		exc.put(e.getPlayer(), exc.get(e.getPlayer()) + 1);
		
		if(exc.get(e.getPlayer()) > 50)
		{
			addReward(e.getPlayer(), exc.get(e.getPlayer()) / bpxp);
			XP.dropRandom(e.getPlayer().getLocation());
			exc.remove(e.getPlayer());
		}
	}
	
	@Override
	public MaterialBlock getSkillMaterial()
	{
		return new MaterialBlock(Material.STICK);
	}
	
	@Override
	public ControllerMessage onControllerMessageRecieved(ControllerMessage message)
	{
		if(message.contains("e.player"))
		{
			Player p = Players.getPlayer(message.getString("e.player"));
			int level = message.getInt("e.level");
			addReward(p, level);
		}
		
		return message;
	}
	
	@Override
	public void createControllers()
	{
		
	}
}
