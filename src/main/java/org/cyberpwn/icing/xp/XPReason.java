package org.cyberpwn.icing.xp;

import org.apache.commons.lang.StringUtils;

public enum XPReason
{
	UNKNOWN,
	BUTCHER,
	SMELTING,
	CONSTRUCTION,
	SOCIAL,
	SKILL_PROGRESSION,
	ENCHANTING,
	WOOD_CUTTING,
	EXCAVATION,
	COMBAT,
	CRITICAL,
	FARMING,
	ARCHERY,
	TAMING,
	VOTING,
	MINING;
	
	public String fancy()
	{
		return StringUtils.capitalize(toString().replaceAll("_", " ").toLowerCase());
	}
}
