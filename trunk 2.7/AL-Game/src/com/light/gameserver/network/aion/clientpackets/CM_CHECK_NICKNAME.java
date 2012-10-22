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

package com.light.gameserver.network.aion.clientpackets;

import com.light.gameserver.configs.main.GSConfig;
import com.light.gameserver.network.aion.AionClientPacket;
import com.light.gameserver.network.aion.AionConnection;
import com.light.gameserver.network.aion.AionConnection.State;
import com.light.gameserver.network.aion.serverpackets.SM_CREATE_CHARACTER;
import com.light.gameserver.network.aion.serverpackets.SM_NICKNAME_CHECK_RESPONSE;
import com.light.gameserver.services.player.PlayerService;

/**
 * In this packets aion client is asking if given nickname is ok/free?.
 * 
 * @author -Nemesiss-
 * @modified cura
 */
public class CM_CHECK_NICKNAME extends AionClientPacket {

	/**
	 * nick name that need to be checked
	 */
	private String nick;

	/**
	 * Constructs new instance of <tt>CM_CHECK_NICKNAME </tt> packet
	 * 
	 * @param opcode
	 */
	public CM_CHECK_NICKNAME(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl() {
		nick = readS();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl() {
		AionConnection client = getConnection();

		if (!PlayerService.isFreeName(nick) || PlayerService.isOldName(nick)) {
			if (GSConfig.CHARACTER_FACTIONS_MODE == 2)
				client.sendPacket(new SM_NICKNAME_CHECK_RESPONSE(SM_CREATE_CHARACTER.RESPONSE_NAME_RESERVED));
			else
				client.sendPacket(new SM_NICKNAME_CHECK_RESPONSE(SM_CREATE_CHARACTER.RESPONSE_NAME_ALREADY_USED));
		}
		else if (!PlayerService.isValidName(nick)) {
			client.sendPacket(new SM_NICKNAME_CHECK_RESPONSE(SM_CREATE_CHARACTER.RESPONSE_INVALID_NAME));
		}
		else {
			client.sendPacket(new SM_NICKNAME_CHECK_RESPONSE(SM_CREATE_CHARACTER.RESPONSE_OK));
		}
	}
}
