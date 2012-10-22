/*
 * This file is part of aion-unique <aion-unique.org>.
 *
 *  aion-unique is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-unique is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.light.gameserver.model.siege;

import com.light.gameserver.configs.main.SiegeConfig;
import com.light.gameserver.dataholders.DataManager;
import com.light.gameserver.model.DescriptionId;
import com.light.gameserver.model.templates.siegelocation.ArtifactActivation;
import com.light.gameserver.model.templates.siegelocation.SiegeLocationTemplate;
import com.light.gameserver.services.SiegeService;
import com.light.gameserver.skillengine.model.SkillTemplate;

/**
 * @author Source
 */
public class ArtifactLocation extends SiegeLocation 
{

	private ArtifactStatus status;

	public ArtifactLocation() 
	{
		this.status = ArtifactStatus.IDLE;
	}

	public ArtifactLocation(SiegeLocationTemplate template) 
	{
		super(template);
		// Artifacts Always Vulnerable
		setVulnerable(true);
	}

	@Override
	public int getNextState() 
	{
		return STATE_VULNERABLE;
	}

	@Override
	public int getInfluenceValue() 
	{
		return SiegeConfig.SIEGE_POINTS_ARTIFACT;
	}

	public long getLastActivation() 
	{
		return this.lastArtifactActivation;
	}

	public void setLastActivation(long paramLong) 
	{
		this.lastArtifactActivation = paramLong;
	}

	public int getCoolDown() 
	{
		long i = this.template.getActivation().getCd();
		long l = System.currentTimeMillis() - this.lastArtifactActivation;
		if (l > i)
			return 0;
		else
			return (int) ((i - l) / 1000);
	}

	/**
	 * Returns DescriptionId that describes name of this artifact.<br>
	 *
	 * @return DescriptionId with name
	 */
	public DescriptionId getNameAsDescriptionId() 
	{
		// Get Skill id, item, count and target defined for each artifact.
		ArtifactActivation activation = getTemplate().getActivation();
		int skillId = activation.getSkillId();
		SkillTemplate skillTemplate = DataManager.SKILL_DATA.getSkillTemplate(skillId);
		return new DescriptionId(skillTemplate.getNameId());
	}

	public boolean isStandAlone() 
	{
		return !SiegeService.getInstance().getFortresses().containsKey(getLocationId());
	}

	public FortressLocation getOwningFortress() 
	{
		return SiegeService.getInstance().getFortressById(getLocationId());
	}

	/**
	 * @return the status
	 */
	public ArtifactStatus getStatus() 
	{
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(ArtifactStatus status) 
	{
		this.status = status;
	}

}