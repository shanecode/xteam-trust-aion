/*
 * This file is part of aion-unique <aionunique.smfnew.com>.
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


import com.light.gameserver.model.gameobjects.Npc;
import com.light.gameserver.model.gameobjects.player.Player;
import com.light.gameserver.model.templates.teleport.TeleporterTemplate;
import com.light.gameserver.network.aion.AionConnection;
import com.light.gameserver.network.aion.AionServerPacket;
import com.light.gameserver.utils.PacketSendUtility;
import com.light.gameserver.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author alexa026 , orz
 */
public class SM_TELEPORT_MAP extends AionServerPacket {

	private int targetObjectId;
	private Player player;
	private TeleporterTemplate teleport;
	public Npc npc;

	private static final Logger log = LoggerFactory.getLogger(SM_TELEPORT_MAP.class);

	public SM_TELEPORT_MAP(Player player, int targetObjectId, TeleporterTemplate teleport) {
		this.player = player;
		this.targetObjectId = targetObjectId;
		this.npc = (Npc) World.getInstance().findVisibleObject(targetObjectId);
		this.teleport = teleport;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		if ((teleport != null) && (teleport.getNpcId() != 0) && (teleport.getTeleportId() != 0)) {
			writeD(targetObjectId);
			writeH(teleport.getTeleportId());
		}
		else {
			PacketSendUtility.sendMessage(player, "Missing info at npc_teleporter.xml with npcid: " + npc.getNpcId());
			log.info(String.format("Missing teleport info with npcid: %d", npc.getNpcId()));
		}
	}
}
