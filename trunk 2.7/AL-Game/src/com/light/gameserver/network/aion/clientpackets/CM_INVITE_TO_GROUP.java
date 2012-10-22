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
package com.light.gameserver.network.aion.clientpackets;

import com.light.gameserver.model.gameobjects.player.DeniedStatus;
import com.light.gameserver.model.gameobjects.player.Player;
import com.light.gameserver.model.team2.alliance.PlayerAllianceService;
import com.light.gameserver.model.team2.group.PlayerGroupService;
import com.light.gameserver.model.team2.league.LeagueService;
import com.light.gameserver.network.aion.AionClientPacket;
import com.light.gameserver.network.aion.AionConnection.State;
import com.light.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.light.gameserver.utils.PacketSendUtility;
import com.light.gameserver.utils.Util;
import com.light.gameserver.world.World;

/**
 * @author Lyahim, ATracer Modified by Simple
 */
public class CM_INVITE_TO_GROUP extends AionClientPacket {

	private String name;
	private int inviteType;

	public CM_INVITE_TO_GROUP(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl() {
		inviteType = readC();
		name = readS();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl() {
		final String playerName = Util.convertName(name);

		final Player inviter = getConnection().getActivePlayer();
		if (inviter.getLifeStats().isAlreadyDead() && !inviter.isGM()) {
			// You cannot issue an invitation while you are dead.
			PacketSendUtility.sendPacket(inviter, new SM_SYSTEM_MESSAGE(1300163));
			return;
		}

		final Player invited = World.getInstance().findPlayer(playerName);
		if (invited != null) {
			if (invited.getPlayerSettings().isInDeniedStatus(DeniedStatus.GROUP) && !inviter.isGM()) {
				sendPacket(SM_SYSTEM_MESSAGE.STR_MSG_REJECTED_INVITE_PARTY(invited.getName()));
				return;
			}
			switch (inviteType) {
				case 0:
					PlayerGroupService.inviteToGroup(inviter, invited);
					break;
				case 12: // 2.5
					PlayerAllianceService.inviteToAlliance(inviter, invited);
					break;
				case 28:
					LeagueService.inviteToLeague(inviter, invited);
					break;
				default:
					PacketSendUtility.sendMessage(inviter, "You used an unknown invite type: " + inviteType);
					break;
			}
		}
		else
			inviter.getClientConnection().sendPacket(SM_SYSTEM_MESSAGE.STR_NO_SUCH_USER(name));
	}
}
