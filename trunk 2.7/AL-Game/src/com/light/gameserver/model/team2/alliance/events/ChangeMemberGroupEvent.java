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

import com.light.gameserver.model.team2.alliance.PlayerAlliance;
import com.light.gameserver.model.team2.alliance.PlayerAllianceGroup;
import com.light.gameserver.model.team2.alliance.PlayerAllianceMember;
import com.light.gameserver.model.team2.common.events.AlwaysTrueTeamEvent;
import com.light.gameserver.model.team2.common.legacy.PlayerAllianceEvent;
import com.light.gameserver.network.aion.serverpackets.SM_ALLIANCE_MEMBER_INFO;
import com.light.gameserver.utils.PacketSendUtility;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;

/**
 * @author ATracer
 */
public class ChangeMemberGroupEvent extends AlwaysTrueTeamEvent implements Predicate<PlayerAllianceMember> {

	private final PlayerAlliance alliance;
	private final int firstMemberId;
	private final int secondMemberId;
	private final int allianceGroupId;

	private PlayerAllianceMember firstMember;
	private PlayerAllianceMember secondMember;

	public ChangeMemberGroupEvent(PlayerAlliance alliance, int firstMemberId, int secondMemberId, int allianceGroupId) {
		this.alliance = alliance;
		this.firstMemberId = firstMemberId;
		this.secondMemberId = secondMemberId;
		this.allianceGroupId = allianceGroupId;
	}

	@Override
	public void handleEvent() {
		firstMember = alliance.getMember(firstMemberId);
		secondMember = alliance.getMember(secondMemberId);
		Preconditions.checkNotNull(firstMember, "First member should not be null");
		Preconditions.checkArgument(secondMemberId == 0 || secondMember != null, "Second member should not be null");
		if (secondMember != null) {
			swapMembersInGroup(firstMember, secondMember);
		}
		else {
			moveMemberToGroup(firstMember, allianceGroupId);
		}
		alliance.apply(this);
	}

	@Override
	public boolean apply(PlayerAllianceMember member) {
		PacketSendUtility.sendPacket(member.getObject(), new SM_ALLIANCE_MEMBER_INFO(firstMember,
			PlayerAllianceEvent.MEMBER_GROUP_CHANGE));
		if (secondMember != null) {
			PacketSendUtility.sendPacket(member.getObject(), new SM_ALLIANCE_MEMBER_INFO(secondMember,
				PlayerAllianceEvent.MEMBER_GROUP_CHANGE));
		}
		return true;
	}

	private void swapMembersInGroup(PlayerAllianceMember firstMember, PlayerAllianceMember secondMember) {
		PlayerAllianceGroup firstAllianceGroup = firstMember.getPlayerAllianceGroup();
		PlayerAllianceGroup secondAllianceGroup = secondMember.getPlayerAllianceGroup();
		firstAllianceGroup.removeMember(firstMember);
		secondAllianceGroup.removeMember(secondMember);
		firstAllianceGroup.addMember(secondMember);
		secondAllianceGroup.addMember(firstMember);
	}

	private void moveMemberToGroup(PlayerAllianceMember firstMember, int allianceGroupId) {
		PlayerAllianceGroup firstAllianceGroup = firstMember.getPlayerAllianceGroup();
		firstAllianceGroup.removeMember(firstMember);
		PlayerAllianceGroup newAllianceGroup = alliance.getAllianceGroup(allianceGroupId);
		newAllianceGroup.addMember(firstMember);
	}
}
