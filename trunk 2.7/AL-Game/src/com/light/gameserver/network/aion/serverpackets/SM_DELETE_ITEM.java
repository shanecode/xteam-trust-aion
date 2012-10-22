/*
 * This file is part of aion-unique <aion-unique.smfnew.com>.
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
package com.light.gameserver.network.aion.serverpackets;


import com.light.gameserver.network.aion.AionConnection;
import com.light.gameserver.network.aion.AionServerPacket;
import com.light.gameserver.services.item.ItemPacketService.ItemDeleteType;

//Author Avol

public class SM_DELETE_ITEM extends AionServerPacket {

	private final int itemObjectId;
	private final ItemDeleteType deleteType;

	public SM_DELETE_ITEM(int itemObjectId) {
		this(itemObjectId, ItemDeleteType.UNKNOWN);
	}
	
	public SM_DELETE_ITEM(int itemObjectId, ItemDeleteType deleteType) {
		this.itemObjectId = itemObjectId;
		this.deleteType = deleteType;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeD(itemObjectId);
		writeC(deleteType.getMask());
	}
}