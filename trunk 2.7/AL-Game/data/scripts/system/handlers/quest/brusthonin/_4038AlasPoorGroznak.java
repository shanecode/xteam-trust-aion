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
package quest.brusthonin;

import com.light.gameserver.model.gameobjects.player.Player;
import com.light.gameserver.questEngine.handlers.QuestHandler;
import com.light.gameserver.questEngine.model.QuestDialog;
import com.light.gameserver.questEngine.model.QuestEnv;
import com.light.gameserver.questEngine.model.QuestState;
import com.light.gameserver.questEngine.model.QuestStatus;

/**
 * @author Nephis
 * @reworked vlog
 */
public class _4038AlasPoorGroznak extends QuestHandler {

	private final static int questId = 4038;

	public _4038AlasPoorGroznak() {
		super(questId);
	}

	@Override
	public void register() {
		int[] npcs = { 205150, 730155, 700380, 700381, 700382 };
		qe.registerQuestNpc(205150).addOnQuestStart(questId);
		for (int npc : npcs) {
			qe.registerQuestNpc(npc).addOnTalkEvent(questId);
		}
	}

	@Override
	public boolean onDialogEvent(final QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		QuestDialog dialog = env.getDialog();
		int targetId = env.getTargetId();

		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 205150) { // Surt
				if (dialog == QuestDialog.START_DIALOG) {
					return sendQuestDialog(env, 4762);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			switch (targetId) {
				case 730155: { // Groznak's Skull
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
						case CHECK_COLLECTED_ITEMS: {
							return checkQuestItems(env, 1, 2, false, 10000, 10001); // 2
						}
						case STEP_TO_3: {
							return defaultCloseDialog(env, 2, 2, true, false); // reward
						}
						case FINISH_DIALOG: {
							return sendQuestSelectionDialog(env);
						}
					}
					break;
				}
				case 700380: { // Weathered Skeleton
					if (var == 1) {
						return true; // loot
					}
					break;
				}
				case 700381: { // Intact Skeleton
					if (var == 1) {
						return true; // loot
					}
					break;
				}
				case 700382: { // Muddy Skeleton
					if (var == 1) {
						return true; // loot
					}
				}
			}
		}
		else if (qs != null && qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 205150) { // Surt
				if (env.getDialog() == QuestDialog.USE_OBJECT) {
					return sendQuestDialog(env, 10002);
				}
				else {
					return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}
}
