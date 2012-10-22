/*
 * This file is part of aion-lightning <aion-lightning.org>.
 *
 * aion-lightning is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * aion-lightning is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with aion-lightning.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.light.gameserver.network.aion.serverpackets;


import com.light.gameserver.network.aion.AionConnection;
import com.light.gameserver.network.aion.AionServerPacket;

/**
 * @author xTz
 */
public class SM_TOLL_INFO extends AionServerPacket {

	private int tollCount;

	public SM_TOLL_INFO(int tollCount) {
		this.tollCount = tollCount;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeD(tollCount);
		writeD(0); // unk
	}
}