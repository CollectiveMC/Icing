package org.cyberpwn.icing.skills;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDeathEvent;
import org.cyberpwn.icing.abilities.ButcherLooting;
import org.cyberpwn.icing.skill.BasicSkill;
import org.cyberpwn.icing.xp.XP;
import org.cyberpwn.icing.xp.XPReason;
import org.phantomapi.clust.Keyed;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Ticked;
import org.phantomapi.world.MaterialBlock;

@Ticked(40)
public class SkillButcher extends BasicSkill
{
	@Keyed("health-per-xp")
	public int hpxp = 1;
	
	public SkillButcher(Controllable parentController)
	{
		super(parentController, "butcher", XPReason.BUTCHER);
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
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void on(EntityDeathEvent e)
	{
		if(e.getEntity().getKiller() != null)
		{
			Player p = e.getEntity().getKiller();
			
			if(!XP.isReady(p))
			{
				return;
			}
			
			if(p.getGameMode().equals(GameMode.CREATIVE))
			{
				return;
			}
			
			addReward(p, (int) e.getEntity().getMaxHealth() / hpxp);
			
			XP.dropRandom(p.getLocation());
			
			switch(e.getEntityType())
			{
				case ARMOR_STAND:
					break;
				case ARROW:
					break;
				case BAT:
					addReward(p, 1);
					break;
				case BLAZE:
					addReward(p, 28);
					break;
				case BOAT:
					break;
				case CAVE_SPIDER:
					addReward(p, 24);
					break;
				case CHICKEN:
					addReward(p, 4);
					break;
				case COMPLEX_PART:
					break;
				case COW:
					addReward(p, 4);
					break;
				case CREEPER:
					addReward(p, 20);
					break;
				case DROPPED_ITEM:
					break;
				case EGG:
					break;
				case ENDERMAN:
					addReward(p, 20);
					break;
				case ENDERMITE:
					addReward(p, 8);
					break;
				case ENDER_CRYSTAL:
					break;
				case ENDER_DRAGON:
					addReward(p, 500);
					break;
				case ENDER_PEARL:
					break;
				case ENDER_SIGNAL:
					break;
				case EXPERIENCE_ORB:
					break;
				case FALLING_BLOCK:
					break;
				case FIREBALL:
					break;
				case FIREWORK:
					break;
				case FISHING_HOOK:
					break;
				case GHAST:
					addReward(p, 150);
					break;
				case GIANT:
					addReward(p, 60);
					break;
				case GUARDIAN:
					addReward(p, 80);
					break;
				case HORSE:
					addReward(p, 6);
					break;
				case IRON_GOLEM:
					addReward(p, 36);
					break;
				case ITEM_FRAME:
					break;
				case LEASH_HITCH:
					break;
				case LIGHTNING:
					break;
				case MAGMA_CUBE:
					addReward(p, 12);
					break;
				case MINECART:
					break;
				case MINECART_CHEST:
					break;
				case MINECART_COMMAND:
					break;
				case MINECART_FURNACE:
					break;
				case MINECART_HOPPER:
					break;
				case MINECART_MOB_SPAWNER:
					break;
				case MINECART_TNT:
					break;
				case MUSHROOM_COW:
					addReward(p, 100);
					break;
				case OCELOT:
					addReward(p, 3);
					break;
				case PAINTING:
					break;
				case PIG:
					addReward(p, 4);
					break;
				case PIG_ZOMBIE:
					addReward(p, 22);
					break;
				case PLAYER:
					addReward(p, 100);
					break;
				case PRIMED_TNT:
					break;
				case RABBIT:
					addReward(p, 4);
					break;
				case SHEEP:
					addReward(p, 4);
					break;
				case SILVERFISH:
					addReward(p, 8);
					break;
				case SKELETON:
					addReward(p, 20);
					break;
				case SLIME:
					addReward(p, 12);
					break;
				case SMALL_FIREBALL:
					break;
				case SNOWBALL:
					break;
				case SNOWMAN:
					addReward(p, 1);
					break;
				case SPIDER:
					addReward(p, 20);
					break;
				case SPLASH_POTION:
					break;
				case SQUID:
					addReward(p, 4);
					break;
				case THROWN_EXP_BOTTLE:
					break;
				case UNKNOWN:
					break;
				case VILLAGER:
					addReward(p, 4);
					break;
				case WEATHER:
					break;
				case WITCH:
					addReward(p, 36);
					break;
				case WITHER:
					addReward(p, 250);
					break;
				case WITHER_SKULL:
					break;
				case WOLF:
					addReward(p, 8);
					break;
				case ZOMBIE:
					addReward(p, 20);
					break;
				default:
					break;
			}
		}
	}
	
	@Override
	public MaterialBlock getSkillMaterial()
	{
		return new MaterialBlock(Material.STONE_SWORD);
	}
	
	@Override
	public void createControllers()
	{
		register(new ButcherLooting(this));
	}
}
