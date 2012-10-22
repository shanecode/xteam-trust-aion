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
package com.light.gameserver.model.team2.alliance.events;

import com.light.gameserver.model.gameobjects.player.Player;
import com.light.gameserver.model.team2.TeamEvent;
import com.light.gameserver.model.team2.alliance.PlayerAlliance;
import com.light.gameserver.model.team2.alliance.PlayerAllianceMember;
import com.light.gameserver.model.team2.alliance.PlayerAllianceService;
import com.light.gameserver.model.team2.common.legacy.PlayerAllianceEvent;
import com.light.gameserver.network.aion.serverpackets.SM_ALLIANCE_INFO;
import com.light.gameserver.network.aion.serverpackets.SM_ALLIANCE_MEMBER_INFO;
import com.light.gameserver.network.aion.serverpackets.SM_INSTANCE_INFO;
import com.light.gameserver.network.aion.serverpackets.SM_SHOW_BRAND;
import com.light.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.light.gameserver.utils.PacketSendUtility;
import com.google.common.base.Predicate;

/**
 * @author ATracer
 */
public class PlayerEnteredEvent implements Predicate<PlayerAllianceMember>, TeamEvent {

	private final PlayerAlliance alliance;
	private final Player invited;
	private PlayerAllianceMember invitedMember;

	public PlayerEnteredEvent(PlayerAlliance alliance, Player player) {
		this.alliance = alliance;
		this.invited = player;
	}

	/**
	 * Entered player should not be in group yet
	 */
	@Override
	public boolean checkCondition() {
		return !alliance.hasMember(invited.getObjectId());
	}

	@Override
	public void handleEvent() {
		PlayerAllianceService.addPlayerToAlliance(alliance, invited);

		invitedMember = alliance.getMember(invited.getObjectId());

		PacketSendUtility.sendPacket(invited, new SM_ALLIANCE_INFO(alliance));
		PacketSendUtility.sendPacket(invited, new SM_SHOW_BRAND(0, 0));
		PacketSendUtility.sendPacket(invited, SM_SYSTEM_MESSAGE.STR_FORCE_ENTERED_FORCE);
		PacketSendUtility.sendPacket(invited, new SM_ALLIANCE_MEMBER_INFO(invitedMember, PlayerAllianceEvent.JOIN));
		PacketSendUtility.sendPacket(invited, new SM_INSTANCE_INFO(invited, true, true));

		alliance.apply(this);
	}

	@Override
	public boolean apply(PlayerAllianceMember member) {
		Player player = member.getObject();
		if (!invited.getObjectId().equals(player.getObjectId())) {
			PacketSendUtility.sendPacket(player, new SM_ALLIANCE_MEMBER_INFO(invitedMember, PlayerAllianceEvent.JOIN));
			PacketSendUtility.sendPacket(player, new SM_INSTANCE_INFO(invited, true, true));
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_FORCE_HE_ENTERED_FORCE(invited.getName()));

			PacketSendUtility.sendPacket(invited, new SM_ALLIANCE_MEMBER_INFO(member, PlayerAllianceEvent.ENTER));
			PacketSendUtility.sendPacket(invited, new SM_INSTANCE_INFO(player, true, true));
		}
		return true;
	}

}