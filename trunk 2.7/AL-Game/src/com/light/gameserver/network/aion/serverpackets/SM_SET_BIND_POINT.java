/**
 * This file is part of aion-emu <aion-emu.com>.
 *
 *  aion-emu is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-emu is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-emu.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.light.gameserver.network.aion.serverpackets;

import com.light.gameserver.model.gameobjects.Kisk;
import com.light.gameserver.model.gameobjects.player.Player;
import com.light.gameserver.network.aion.AionConnection;
import com.light.gameserver.network.aion.AionServerPacket;

/*
 * @author sweetkr, Sarynth
 */
public class SM_SET_BIND_POINT extends AionServerPacket {

	private final int mapId;
	private final float x;
	private final float y;
	private final float z;
	private final Kisk kisk;

	public SM_SET_BIND_POINT(int mapId, float x, float y, float z, Player player) {
		this.mapId = mapId;
		this.x = x;
		this.y = y;
		this.z = z;
		this.kisk = player.getKisk();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con) {
		// Appears 0x04 if bound to a kisk. 0x00 if not.
		writeC((kisk == null ? 0x00 : 0x04));

		writeC(0x01);// unk
		writeD(mapId);// map id
		writeF(x); // coordinate x
		writeF(y); // coordinate y
		writeF(z); // coordinate z
		writeD((kisk == null ? 0x00 : (kisk.isActive() ? kisk.getObjectId() : 0))); // kisk object id
	}

}