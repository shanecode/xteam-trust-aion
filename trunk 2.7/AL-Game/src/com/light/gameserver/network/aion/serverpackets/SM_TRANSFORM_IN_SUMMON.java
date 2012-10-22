/*
 * This file is part of aion-lightning <aion-lightning.org>
 *
 * aion-lightning is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * aion-lightning is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with aion-lightning. If not, see <http://www.gnu.org/licenses/>.
 */
package com.light.gameserver.network.aion.serverpackets;

import com.light.gameserver.model.gameobjects.Creature;
import com.light.gameserver.model.gameobjects.player.Player;
import com.light.gameserver.network.aion.AionConnection;
import com.light.gameserver.network.aion.AionServerPacket;

/**
 * @author xTz
 */
public class SM_TRANSFORM_IN_SUMMON extends AionServerPacket {

	private Player player;
	private int summonObject;

	public SM_TRANSFORM_IN_SUMMON(Player player, Creature creature) {
		this(player, creature.getObjectId());
	}

	public SM_TRANSFORM_IN_SUMMON(Player player, int creatureObjectId) {
		this.player = player;
		this.summonObject = creatureObjectId;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeD(summonObject);
		writeS(player.getName());
		writeD(player.getObjectId());
	}
}