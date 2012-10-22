/*
 * This file is part of aion-unique <aion-unique.com>.
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

import com.light.gameserver.model.gameobjects.Npc;
import com.light.gameserver.model.gameobjects.VisibleObject;
import com.light.gameserver.model.gameobjects.player.Player;
import com.light.gameserver.network.aion.AionClientPacket;
import com.light.gameserver.network.aion.AionConnection.State;

/**
 * @author alexa026, Avol modified by ATracer
 */
public class CM_SHOW_DIALOG extends AionClientPacket {

	private int targetObjectId;

	/**
	 * Constructs new instance of <tt>CM_SHOW_DIALOG </tt> packet
	 * 
	 * @param opcode
	 */
	public CM_SHOW_DIALOG(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	@Override
	protected void readImpl() {
		targetObjectId = readD();
	}

	@Override
	protected void runImpl() {
		Player player = getConnection().getActivePlayer();
		if (player.isTrading())
			return;
		
		VisibleObject obj = player.getKnownList().getObject(targetObjectId);

		if (obj instanceof Npc) {
			((Npc) obj).getController().onDialogRequest(player);
		}
	}
}