/*
 * This file is part of aion-unique <aion-unique.org>.
 *
 * aion-unique is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * aion-unique is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package quest.heiron;

import com.light.gameserver.model.gameobjects.Npc;
import com.light.gameserver.model.gameobjects.player.Player;
import com.light.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.light.gameserver.questEngine.handlers.QuestHandler;
import com.light.gameserver.questEngine.model.QuestEnv;
import com.light.gameserver.questEngine.model.QuestState;
import com.light.gameserver.questEngine.model.QuestStatus;
import com.light.gameserver.services.QuestService;
import com.light.gameserver.utils.PacketSendUtility;
import com.light.gameserver.utils.ThreadPoolManager;

/**
 * @author Balthazar
 */

public class _1643TheStarOfHeiron extends QuestHandler {

	private final static int questId = 1643;

	public _1643TheStarOfHeiron() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerOnEnterWorld(questId);
		qe.registerQuestNpc(204545).addOnQuestStart(questId);
		qe.registerQuestNpc(204545).addOnTalkEvent(questId);
		qe.registerQuestNpc(204630).addOnTalkEvent(questId);
		qe.registerQuestNpc(204614).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);

		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();

		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 204545) {
				switch (env.getDialog()) {
					case START_DIALOG: {
						return sendQuestDialog(env, 4762);
					}
					case ACCEPT_QUEST: {
						if (player.getInventory().getItemCountByItemId(182201764) == 0) {
							if (!giveQuestItem(env, 182201764, 1)) {
								return true;
							}
						}
					}
					default:
						return sendQuestStartDialog(env);
				}
			}
		}

		if (qs == null)
			return false;

		if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 204630: {
					switch (env.getDialog()) {
						case START_DIALOG: {
							if (qs.getQuestVarById(0) == 0) {
								return sendQuestDialog(env, 1011);
							}
							else if (qs.getQuestVarById(0) == 2) {
								return sendQuestDialog(env, 1693);
							}
						}
						case STEP_TO_1: {
							qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
							removeQuestItem(env, 182201764, 1);
							updateQuestStatus(env);
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));
							QuestService.addNewSpawn(210040000, 1, 204614, (float) 1591.4327, (float) 2774.2283, (float) 127.63001,
								(byte) 0);
							return true;
						}
						case SET_REWARD: {
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));
							return true;
						}
					}
				}
				case 204614: {
					switch (env.getDialog()) {
						case START_DIALOG: {
							if (qs.getQuestVarById(0) == 1) {
								return sendQuestDialog(env, 1011);
							}
						}
						case STEP_TO_1: {
							qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
							updateQuestStatus(env);
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
							final Npc npc = (Npc) env.getVisibleObject();
							ThreadPoolManager.getInstance().schedule(new Runnable() {

								@Override
								public void run() {
									npc.getController().onDelete();
								}
							}, 40000);
							return true;
						}
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 204545) {
				if (env.getDialogId() == 1009)
					return sendQuestDialog(env, 5);
				else
					return sendQuestEndDialog(env);
			}
		}
		return false;
	}

	@Override
	public boolean onEnterWorldEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);

		if (qs == null) {
			return false;
		}

		if (qs.getStatus() == QuestStatus.START) {
			if (qs.getQuestVarById(0) == 1) {
				qs.setQuestVar(0);
				updateQuestStatus(env);
			}
		}
		return false;
	}
}