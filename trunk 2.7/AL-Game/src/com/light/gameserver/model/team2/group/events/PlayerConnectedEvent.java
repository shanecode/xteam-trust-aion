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
package com.light.gameserver.model.team2.group.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.light.gameserver.model.gameobjects.player.Player;
import com.light.gameserver.model.team2.common.events.AlwaysTrueTeamEvent;
import com.light.gameserver.model.team2.common.legacy.GroupEvent;
import com.light.gameserver.model.team2.group.PlayerGroup;
import com.light.gameserver.model.team2.group.PlayerGroupMember;
import com.light.gameserver.network.aion.serverpackets.SM_GROUP_INFO;
import com.light.gameserver.network.aion.serverpackets.SM_GROUP_MEMBER_INFO;
import com.light.gameserver.utils.PacketSendUtility;
import com.google.common.base.Predicate;

/**
 * @author ATracer
 */
public class PlayerConnectedEvent extends AlwaysTrueTeamEvent implements Predicate<Player> {

	private static final Logger log = LoggerFactory.getLogger(PlayerConnectedEvent.class);
	private final PlayerGroup group;
	private final Player player;

	public PlayerConnectedEvent(PlayerGroup group, Player player) {
		this.group = group;
		this.player = player;
	}

	@Override
	public void handleEvent() {
		group.removeMember(player.getObjectId());
		group.addMember(new PlayerGroupMember(player));
		// TODO this probably should never happen
		if (player.sameObjectId(group.getLeader().getObjectId())) {
			log.warn("[TEAM2] leader connected {}", group.size());
			group.changeLeader(new PlayerGroupMember(player));
		}
		PacketSendUtility.sendPacket(player, new SM_GROUP_INFO(group));
		PacketSendUtility.sendPacket(player, new SM_GROUP_MEMBER_INFO(group, player, GroupEvent.JOIN));
		group.applyOnMembers(this);
	}

	@Override
	public boolean apply(Player member) {
		if (!player.equals(member)) {
			PacketSendUtility.sendPacket(member, new SM_GROUP_MEMBER_INFO(group, player, GroupEvent.ENTER));
			PacketSendUtility.sendPacket(player, new SM_GROUP_MEMBER_INFO(group, member, GroupEvent.ENTER));
		}
		return true;
	}

}
