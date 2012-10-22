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
import com.light.gameserver.questEngine.handlers.QuestHandler;
import com.light.gameserver.questEngine.model.QuestEnv;
import com.light.gameserver.questEngine.model.QuestState;
import com.light.gameserver.questEngine.model.QuestStatus;

/**
 * @author Balthazar
 */

public class _1626LightThePath extends QuestHandler {

	private final static int questId = 1626;

	public _1626LightThePath() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(204592).addOnQuestStart(questId);
		qe.registerQuestNpc(204592).addOnTalkEvent(questId);
		qe.registerQuestNpc(700221).addOnTalkEvent(questId);
		qe.registerQuestNpc(700222).addOnTalkEvent(questId);
		qe.registerQuestNpc(700223).addOnTalkEvent(questId);
		qe.registerQuestNpc(700224).addOnTalkEvent(questId);
		qe.registerQuestNpc(700225).addOnTalkEvent(questId);
		qe.registerQuestNpc(700226).addOnTalkEvent(questId);
		qe.registerQuestNpc(700227).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(final QuestEnv env) {
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);

		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();

		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 204592) {
				switch (env.getDialog()) {
					case START_DIALOG: {
						return sendQuestDialog(env, 4762);
					}
					case ACCEPT_QUEST: {
						if (player.getInventory().getItemCountByItemId(182201788) == 0) {
							if (!giveQuestItem(env, 182201788, 1)) {
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
				case 700221: {
					switch (env.getDialog()) {
						case USE_OBJECT: {
							long itemCount1 = player.getInventory().getItemCountByItemId(182201788);
							if (itemCount1 == 1) {
								return useQuestObject(env, 0, 1, false, 0); // 1
							}
						}
					}
					break;
				}
				case 700222: {
					switch (env.getDialog()) {
						case USE_OBJECT: {
							long itemCount1 = player.getInventory().getItemCountByItemId(182201788);
							if (itemCount1 == 1) {
								return useQuestObject(env, 1, 2, false, 0); // 2
							}
						}
					}
					break;
				}
				case 700223: {
					switch (env.getDialog()) {
						case USE_OBJECT: {
							long itemCount1 = player.getInventory().getItemCountByItemId(182201788);
							if (itemCount1 == 1) {
								return useQuestObject(env, 2, 3, false, 0); // 3
							}
						}
					}
				}
				case 700224: {
					switch (env.getDialog()) {
						case USE_OBJECT: {
							long itemCount1 = player.getInventory().getItemCountByItemId(182201788);
							if (itemCount1 == 1) {
								return useQuestObject(env, 3, 4, false, 0); // 4
							}
						}
					}
				}
				case 700225: {
					switch (env.getDialog()) {
						case USE_OBJECT: {
							long itemCount1 = player.getInventory().getItemCountByItemId(182201788);
							if (itemCount1 == 1) {
								return useQuestObject(env, 4, 5, false, 0); // 5
							}
						}
					}
				}
				case 700226: {
					switch (env.getDialog()) {
						case USE_OBJECT: {
							long itemCount1 = player.getInventory().getItemCountByItemId(182201788);
							if (itemCount1 == 1) {
								return useQuestObject(env, 5, 6, false, 0); // 6
							}
						}
					}
				}
				case 700227: {
					switch (env.getDialog()) {
						case USE_OBJECT: {
							long itemCount1 = player.getInventory().getItemCountByItemId(182201788);
							if (itemCount1 == 1) {
								return useQuestObject(env, 6, 6, true, 0); // reward
							}
						}
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 204592) {
				if (env.getDialogId() == 1009)
					return sendQuestDialog(env, 5);
				else
					return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}
