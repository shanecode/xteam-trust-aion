/*
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
package com.light.gameserver.network.aion.clientpackets;

import com.light.gameserver.model.gameobjects.player.Player;
import com.light.gameserver.network.aion.AionClientPacket;
import com.light.gameserver.network.aion.AionConnection.State;

/**
 * Response to SM_QUESTION_WINDOW
 * 
 * @author Ben
 * @author Sarynth
 */
public class CM_QUESTION_RESPONSE extends AionClientPacket {

	private int questionid;
	private int response;
	@SuppressWarnings("unused")
	private int senderid;

	public CM_QUESTION_RESPONSE(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl() {
		questionid = readD();

		response = readC(); // y/n
		readC(); // unk 0x00 - 0x01 ?
		readH();
		senderid = readD();
		readD();
		readH();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl() {
		Player player = getConnection().getActivePlayer();
		if (player.isTrading())
			return;
		player.getResponseRequester().respond(questionid, response);
	}

}
