package org.cyberpwn.icing.xp;

import org.apache.commons.lang.StringUtils;

public enum XPReason
{
	UNKNOWN;
	
	public String fancy()
	{
		return StringUtils.capitalize(toString().replaceAll("_", " ").toLowerCase());
	}
}