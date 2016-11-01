package org.cyberpwn.icing;

import org.cyberpwn.icing.skill.SkillDataController;
import org.cyberpwn.icing.skills.SkillButcher;
import org.cyberpwn.icing.skills.SkillMining;
import org.phantomapi.clust.ConfigurableController;
import org.phantomapi.command.CommandListener;
import org.phantomapi.command.PhantomCommand;
import org.phantomapi.command.PhantomCommandSender;
import org.phantomapi.construct.Controllable;
import org.phantomapi.lang.GList;
import org.phantomapi.text.MessageBuilder;
import org.phantomapi.util.C;

public class SkillController extends ConfigurableController implements CommandListener
{
	private SkillDataController skillDataController;
	
	public SkillController(Controllable parentController)
	{
		super(parentController, "skill");
		
		skillDataController = new SkillDataController(this);
		
		register(skillDataController);
		
		register(new SkillMining(this));
		register(new SkillButcher(this));
	}
	
	@Override
	public void onStart()
	{
		
	}
	
	@Override
	public void onStop()
	{
		
	}
	
	public SkillDataController getSkillDataController()
	{
		return skillDataController;
	}
	
	@Override
	public String getMessageNoPermission()
	{
		return "";
	}
	
	@Override
	public String getMessageNotPlayer()
	{
		return "";
	}
	
	@Override
	public String getMessageNotConsole()
	{
		return "";
	}
	
	@Override
	public String getMessageInvalidArgument(String arg, String neededType)
	{
		return "";
	}
	
	@Override
	public String getMessageInvalidArguments(int given, int expected, int expectedMax)
	{
		return "";
	}
	
	@Override
	public String getMessageUnknownSubCommand(String given)
	{
		return "";
	}
	
	@Override
	public String getChatTag()
	{
		return C.DARK_GRAY + "[" + C.GREEN + "Skill" + C.DARK_GRAY + "]: " + C.GRAY;
	}
	
	@Override
	public String getChatTagHover()
	{
		return C.GREEN + "CHA-CHING! It's a skill. Get farmed.";
	}
	
	@Override
	public boolean onCommand(PhantomCommandSender sender, PhantomCommand cmd)
	{
		sender.setMessageBuilder(new MessageBuilder(this));
		
		if(cmd.getArgs().length == 0)
		{
			if(sender.isPlayer())
			{
				// TODO show skills
			}
			
			else
			{
				// TODO skill command help
			}
		}
		
		else if(sender.hasPermission("x.god"))
		{
			// TODO Commands
			// /skill give <player> <skill> <xp>
			// /skill get <player> [skill]
		}
		
		else
		{
			sender.sendMessage("Use it like this: " + C.GREEN + "/skill");
		}
		
		return true;
	}
	
	@Override
	public String getCommandName()
	{
		return "skill";
	}
	
	@Override
	public GList<String> getCommandAliases()
	{
		return new GList<String>().qadd("sk").qadd("ski").qadd("skills").qadd("perk").qadd("perks");
	}
}
