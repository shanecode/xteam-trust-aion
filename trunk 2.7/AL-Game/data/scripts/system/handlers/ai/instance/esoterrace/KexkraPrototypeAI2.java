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
package ai.instance.esoterrace;

import ai.AggressiveNpcAI2;

import com.light.gameserver.ai2.AI2Actions;
import com.light.gameserver.ai2.AIName;
import com.light.gameserver.model.gameobjects.Creature;
import com.light.gameserver.model.gameobjects.player.Player;
import com.light.gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import com.light.gameserver.utils.PacketSendUtility;
import com.light.gameserver.world.knownlist.Visitor;

/**
 * @author xTz
 */
@AIName("kexkraprototype")
public class KexkraPrototypeAI2 extends AggressiveNpcAI2 {

	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
		checkPercentage(getLifeStats().getHpPercentage());
	}

	private void checkPercentage(int hpPercentage) {
		if (hpPercentage <= 75) {
			getKnownList().doOnAllPlayers(new Visitor<Player>() {

				@Override
				public void visit(Player player) {
					if (player.isOnline() && !player.getLifeStats().isAlreadyDead()) {
						PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 472));
					}
				}
			});
			spawn(217206, 1320.639282f, 1171.063354f, 51.494003f, (byte) 0);
			AI2Actions.deleteOwner(this);
		}
	}

}