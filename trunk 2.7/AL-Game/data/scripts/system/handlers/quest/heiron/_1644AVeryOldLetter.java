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

import com.light.gameserver.dataholders.DataManager;
import com.light.gameserver.model.gameobjects.Item;
import com.light.gameserver.model.gameobjects.Npc;
import com.light.gameserver.model.gameobjects.player.Player;
import com.light.gameserver.model.gameobjects.player.RewardType;
import com.light.gameserver.model.templates.quest.Rewards;
import com.light.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.light.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.light.gameserver.network.aion.serverpackets.SM_QUEST_ACTION;
import com.light.gameserver.questEngine.handlers.HandlerResult;
import com.light.gameserver.questEngine.handlers.QuestHandler;
import com.light.gameserver.questEngine.model.QuestEnv;
import com.light.gameserver.questEngine.model.QuestState;
import com.light.gameserver.questEngine.model.QuestStatus;
import com.light.gameserver.services.QuestService;
import com.light.gameserver.utils.PacketSendUtility;
import com.light.gameserver.utils.ThreadPoolManager;

/**
 * @author Balthazar
 * @reworked & modified Gigi
 */
public class _1644AVeryOldLetter extends QuestHandler {

	private final static int questId = 1644;

	public _1644AVeryOldLetter() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(204545).addOnTalkEvent(questId);
		qe.registerQuestNpc(204537).addOnTalkEvent(questId);
		qe.registerQuestNpc(204546).addOnTalkEvent(questId);
		qe.registerQuestItem(182201765, questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);

		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();

		if (targetId == 0) {
			if (env.getDialogId() == 1002) {
				QuestService.startQuest(env);
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(0, 0));
				return true;
			}
		}

		if (qs != null && qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 204545: {
					switch (env.getDialog()) {
						case START_DIALOG: {
							if (qs.getQuestVarById(0) == 0) {
								return sendQuestDialog(env, 1352);
							}
							else if (qs.getQuestVarById(0) == 2) {
								return sendQuestDialog(env, 2034);
							}
						}
						case STEP_TO_1: {
							return defaultCloseDialog(env, 0, 1);
						}
						case STEP_TO_3: {
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);
							return defaultCloseDialog(env, 2, 3);
						}
					}
				}
				case 204537: {
					switch (env.getDialog()) {
						case START_DIALOG: {
							return sendQuestDialog(env, 1693);
						}
						case STEP_TO_2: {
							return defaultCloseDialog(env, 1, 2, 0, 0, 182201765, 1);
						}
					}
				}
			}
		}
		else if (qs != null && qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 204546) {
				switch (env.getDialog()) {
					case START_DIALOG: {
						return sendQuestDialog(env, 2375);
					}
					default: {
						return sendQuestEndDialog(env);
					}
				}
			}
		}
		return false;
	}

	@Override
	public HandlerResult onItemUseEvent(final QuestEnv env, Item item) {
		final Player player = env.getPlayer();
		final int itemObjId = item.getObjectId();
		final int id = item.getItemTemplate().getTemplateId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);

		if (id != 182201765) {
			return HandlerResult.UNKNOWN;
		}

		if (qs != null) {
			if (qs.getStatus() == QuestStatus.COMPLETE) {
				removeQuestItem(env, 182201765, 1);
				return HandlerResult.FAILED;
			}
		}

		PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 3000, 0,
			0), true);
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 0,
					1, 0), true);
				sendQuestDialog(env, 4);
			}
		}, 3000);
		return HandlerResult.SUCCESS;
	}
}