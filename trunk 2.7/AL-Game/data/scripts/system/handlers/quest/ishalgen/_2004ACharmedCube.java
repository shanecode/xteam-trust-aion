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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with aion-unique. If not, see <http://www.gnu.org/licenses/>.
 */
package quest.ishalgen;

import com.light.gameserver.model.gameobjects.Npc;
import com.light.gameserver.model.gameobjects.player.Player;
import com.light.gameserver.questEngine.handlers.QuestHandler;
import com.light.gameserver.questEngine.model.QuestDialog;
import com.light.gameserver.questEngine.model.QuestEnv;
import com.light.gameserver.questEngine.model.QuestState;
import com.light.gameserver.questEngine.model.QuestStatus;
import com.light.gameserver.services.QuestService;

/**
 * @author Mr. Poke
 * @reworked vlog
 */
public class _2004ACharmedCube extends QuestHandler {

	private final static int questId = 2004;
	private int[] mobs = { 210402, 210403 };

	public _2004ACharmedCube() {
		super(questId);
	}

	@Override
	public void register() {
		int[] npcs = { 203539, 700047, 203550 };
		qe.registerOnEnterZoneMissionEnd(questId);
		qe.registerOnLevelUp(questId);
		for (int npc : npcs) {
			qe.registerQuestNpc(npc).addOnTalkEvent(questId);
		}
		for (int mob : mobs) {
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null)
			return false;
		int var = qs.getQuestVarById(0);
		int targetId = env.getTargetId();
		QuestDialog dialog = env.getDialog();

		if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 203539: { // Derot
					switch (dialog) {
						case START_DIALOG: {
							if (var == 0) {
								return sendQuestDialog(env, 1011);
							}
							else if (var == 1) {
								return sendQuestDialog(env, 1352);
							}
						}
						case STEP_TO_1: {
							return defaultCloseDialog(env, 0, 1); // 1
						}
						case STEP_TO_2: {
							giveQuestItem(env, 182203005, 1);
							return sendQuestSelectionDialog(env);
						}
						case CHECK_COLLECTED_ITEMS: {
							return checkQuestItems(env, 1, 2, false, 1438, 1353); // 2
						}
						case FINISH_DIALOG: {
							return sendQuestSelectionDialog(env);
						}
					}
					break;
				}
				case 700047: { // Tombstone
					if (var == 1 && env.getVisibleObject().getObjectTemplate().getTemplateId() == 700047
						&& dialog == QuestDialog.USE_OBJECT) {
						Npc npc = (Npc) env.getVisibleObject();
						QuestService.addNewSpawn(player.getWorldId(), player.getInstanceId(), 211755, npc.getX(), npc.getY(),
							npc.getZ(), npc.getHeading());
						return true;
					}
				}
				case 203550: { // Munin
					switch (dialog) {
						case START_DIALOG: {
							if (var == 2) {
								return sendQuestDialog(env, 1693);
							}
							else if (var == 6) {
								return sendQuestDialog(env, 2034);
							}
						}
						case STEP_TO_3: {
							return defaultCloseDialog(env, 2, 3, 0, 0, 182203005, 1); // 3
						}
						case STEP_TO_4: {
							return defaultCloseDialog(env, 6, 6, true, false); // reward
						}
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 203539) { // Derot
				if (dialog == QuestDialog.USE_OBJECT) {
					return sendQuestDialog(env, 2375);
				}
				else {
					return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}

	@Override
	public boolean onKillEvent(QuestEnv env) {
		return defaultOnKillEvent(env, mobs, 3, 6); // 3 - 6
	}

	@Override
	public boolean onZoneMissionEndEvent(QuestEnv env) {
		return defaultOnZoneMissionEndEvent(env);
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env, 2100, true);
	}
}