/*
 * This file is part of aion-unique <aion-unique.org>.
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.light.gameserver.model.gameobjects.Creature;
import com.light.gameserver.model.gameobjects.Summon;
import com.light.gameserver.model.gameobjects.VisibleObject;
import com.light.gameserver.model.gameobjects.player.Player;
import com.light.gameserver.network.aion.AionClientPacket;
import com.light.gameserver.network.aion.AionConnection.State;

/**
 * @author ATracer, KID
 */
public class CM_SUMMON_CASTSPELL extends AionClientPacket {
	private static final Logger log = LoggerFactory.getLogger(CM_SUMMON_CASTSPELL.class);
	private int summonObjId;
	private int targetObjId;
	private int skillId;
	@SuppressWarnings("unused")
	private int skillLvl;
	@SuppressWarnings("unused")
	private float unk;

	public CM_SUMMON_CASTSPELL(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	@Override
	protected void readImpl() {
		summonObjId = readD();
		skillId = readH();
		skillLvl = readC();
		targetObjId = readD();
		unk = readF();
	}

	@Override
	protected void runImpl() {
		Player player = getConnection().getActivePlayer();

		long currentTime = System.currentTimeMillis();
		if (player.getNextSummonSkillUse() > currentTime) {
			return;
		}

		Summon summon = player.getSummon();
		if (summon == null) {
			log.warn("summon castspell without active summon on "+player.getName()+".");
			return;
		}
		if(summon.getObjectId() != summonObjId) {
			log.warn("summon castspell from a different summon instance on "+player.getName()+".");
			return;
		}
		
		Creature target = null;
		if(targetObjId != summon.getObjectId()) {
		  VisibleObject obj = summon.getKnownList().getObject(targetObjId);
		  if(obj instanceof Creature) {
		  	target = (Creature)obj;
		  }
		}
		else {
			target = summon;
		}
			
		if(target != null) {
			player.setNextSummonSkillUse(currentTime + 1100);
			summon.getController().useSkill(skillId, target);
		}
		else
			log.warn("summon castspell on a wrong target on "+player.getName());
	}
}