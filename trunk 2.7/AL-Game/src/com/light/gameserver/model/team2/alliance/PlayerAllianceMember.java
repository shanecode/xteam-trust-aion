/*
 * This file is part of aion-lightning <aion-lightning.com>.
 *
 *  aion-lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-lightning is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-lightning.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.light.gameserver.model.team2.alliance;

import com.light.gameserver.model.gameobjects.player.Player;
import com.light.gameserver.model.team2.PlayerTeamMember;

/**
 * @author ATracer
 */
public class PlayerAllianceMember extends PlayerTeamMember {

	private int allianceId;

	public PlayerAllianceMember(Player player) {
		super(player);
	}

	public int getAllianceId() {
		return allianceId;
	}

	public void setAllianceId(int allianceId) {
		this.allianceId = allianceId;
	}

	public final PlayerAllianceGroup getPlayerAllianceGroup() {
		return getObject().getPlayerAllianceGroup2();
	}

	public final void setPlayerAllianceGroup(PlayerAllianceGroup playerAllianceGroup) {
		getObject().setPlayerAllianceGroup2(playerAllianceGroup);
	}

}