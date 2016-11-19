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
	UNARMED,
	EXCAVATION,
	FISHING,
	SWORDS,
	AXES,
	STEALTH,
	LIGHT_ARMOR,
	HEAVY_ARMOR,
	COMBAT,
	CRITICAL,
	GATHERING,
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
