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

import com.light.gameserver.model.gameobjects.player.Player;
import com.light.gameserver.model.team2.common.events.AlwaysTrueTeamEvent;
import com.light.gameserver.model.team2.common.legacy.LootGroupRules;
import com.light.gameserver.model.team2.group.PlayerGroup;
import com.light.gameserver.network.aion.serverpackets.SM_GROUP_INFO;
import com.light.gameserver.utils.PacketSendUtility;
import com.google.common.base.Predicate;

/**
 * @author ATracer
 */
public class ChangeGroupLootRulesEvent extends AlwaysTrueTeamEvent implements Predicate<Player> {

	private final PlayerGroup group;
	private final LootGroupRules lootGroupRules;

	public ChangeGroupLootRulesEvent(PlayerGroup group, LootGroupRules lootGroupRules) {
		this.group = group;
		this.lootGroupRules = lootGroupRules;
	}

	@Override
	public boolean apply(Player member) {
		PacketSendUtility.sendPacket(member, new SM_GROUP_INFO(group));
		return true;
	}

	@Override
	public void handleEvent() {
		group.setLootGroupRules(lootGroupRules);
		group.applyOnMembers(this);
	}

}
