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

import com.light.gameserver.model.gameobjects.Item;
import com.light.gameserver.model.gameobjects.Npc;
import com.light.gameserver.model.gameobjects.player.Player;
import com.light.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.light.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.light.gameserver.questEngine.handlers.HandlerResult;
import com.light.gameserver.questEngine.handlers.QuestHandler;
import com.light.gameserver.questEngine.model.QuestDialog;
import com.light.gameserver.questEngine.model.QuestEnv;
import com.light.gameserver.questEngine.model.QuestState;
import com.light.gameserver.questEngine.model.QuestStatus;
import com.light.gameserver.services.instance.InstanceService;
import com.light.gameserver.services.teleport.TeleportService;
import com.light.gameserver.utils.PacketSendUtility;
import com.light.gameserver.utils.ThreadPoolManager;
import com.light.gameserver.world.WorldMapInstance;

/**
 * @author kecimis
 */
public class _3200PriceOfGoodwill extends QuestHandler {

	private final static int questId = 3200;
	private final static int[] npc_ids = { 204658, 798332, 700522, 279006, 798322 };

	/*
	 * 204658 - Roikinerk 798332 - Haorunerk 700522 - Haorunerks Bag 279006 - Garkbinerk 798322 - Kuruminerk
	 */

	public _3200PriceOfGoodwill() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(204658).addOnQuestStart(questId); // Roikinerk
		qe.registerQuestItem(182209082, questId);// Teleport Scroll
		for (int npc_id : npc_ids)
			qe.registerQuestNpc(npc_id).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(final QuestEnv env) {
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);

		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();

		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 204658)// Roikinerk
			{
				if (env.getDialog() == QuestDialog.START_DIALOG)
					return sendQuestDialog(env, 4762);
				else
					return sendQuestStartDialog(env);

			}
			return false;
		}

		int var = qs.getQuestVarById(0);

		if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 798322)// Kuruminerk
			{
				if (env.getDialog() == QuestDialog.USE_OBJECT)
					return sendQuestDialog(env, 10002);
				else if (env.getDialogId() == 1009)
					return sendQuestDialog(env, 5);
				else
					return sendQuestEndDialog(env);
			}
			return false;
		}
		else if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 204658)// Roikinerk
			{
				switch (env.getDialog()) {
					case START_DIALOG:
						return sendQuestDialog(env, 1003);
					case SELECT_ACTION_1011:
						return sendQuestDialog(env, 1011);
					case STEP_TO_1:
						// Create instance
						WorldMapInstance newInstance = InstanceService.getNextAvailableInstance(300100000);
						InstanceService.registerPlayerWithInstance(newInstance, player);
						// teleport to cell in steel rake: 300100000 403.55 508.11 885.77 0
						TeleportService.teleportTo(player, 300100000, newInstance.getInstanceId(), 403.55f, 508.11f, 885.77f, 3000, true);
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						return true;
				}

			}
			else if (targetId == 798332 && var == 1)// Haorunerk
			{
				switch (env.getDialog()) {
					case START_DIALOG:
						return sendQuestDialog(env, 1352);
					case SELECT_ACTION_1353:
						playQuestMovie(env, 431);
						break;
					case STEP_TO_2:
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
				}
			}
			else if (targetId == 700522 && var == 2)// Haorunerks Bag, loc: 401.24 503.19 885.76 119
			{
				ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						updateQuestStatus(env);
					}
				}, 3000);
				return true;
			}
			else if (targetId == 279006 && var == 3)// Garkbinerk
			{
				switch (env.getDialog()) {
					case START_DIALOG:
						return sendQuestDialog(env, 2034);
					case SET_REWARD:
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						return true;

				}
			}
		}
		return false;
	}

	@Override
	public HandlerResult onItemUseEvent(final QuestEnv env, Item item) {
		final Player player = env.getPlayer();
		final int id = item.getItemTemplate().getTemplateId();
		final int itemObjId = item.getObjectId();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);

		if (id != 182209082 || qs == null || qs.getQuestVarById(0) != 2)
			return HandlerResult.UNKNOWN;

		PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 3000, 0,
			0), true);
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 0,
					1, 0), true);
				removeQuestItem(env, 182209082, 1);
				// teleport location(BlackCloudIsland): 400010000 3419.16 2445.43 2766.54 57
				TeleportService.teleportTo(player, 400010000, 3419.16f, 2445.43f, 2766.54f, 57);
				qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
				updateQuestStatus(env);
			}
		}, 3000);
		return HandlerResult.SUCCESS;
	}
}