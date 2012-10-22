/*
 * This file is part of aion-unique <aion-unique.smfnew.com>.
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
package com.light.gameserver.services;

import java.util.concurrent.Future;

import com.light.gameserver.model.gameobjects.Creature;
import com.light.gameserver.model.stats.container.CreatureLifeStats;
import com.light.gameserver.model.stats.container.PlayerLifeStats;
import com.light.gameserver.model.templates.zone.ZoneType;
import com.light.gameserver.utils.ThreadPoolManager;
import com.light.gameserver.world.World;

/**
 * @author ATracer
 */
public class LifeStatsRestoreService {

	private static final int DEFAULT_DELAY = 6000;
	private static final int DEFAULT_FPREDUCE_DELAY = 2000;
	private static final int DEFAULT_FPRESTORE_DELAY = 2000;

	private static LifeStatsRestoreService instance = new LifeStatsRestoreService();

	/**
	 * HP and MP restoring task
	 * 
	 * @param creature
	 * @return Future<?>
	 */
	public Future<?> scheduleRestoreTask(CreatureLifeStats<? extends Creature> lifeStats) {
		return ThreadPoolManager.getInstance().scheduleAtFixedRate(new HpMpRestoreTask(lifeStats), 1700, DEFAULT_DELAY);
	}

	/**
	 * HP restoring task
	 * 
	 * @param lifeStats
	 * @return
	 */
	public Future<?> scheduleHpRestoreTask(CreatureLifeStats<? extends Creature> lifeStats) {
		return ThreadPoolManager.getInstance().scheduleAtFixedRate(new HpRestoreTask(lifeStats), 1700, DEFAULT_DELAY);
	}

	/**
	 * @param lifeStats
	 * @return
	 */
	public Future<?> scheduleFpReduceTask(final PlayerLifeStats lifeStats) {
		return ThreadPoolManager.getInstance().scheduleAtFixedRate(new FpReduceTask(lifeStats), 2000,
			DEFAULT_FPREDUCE_DELAY);
	}

	/**
	 * @param lifeStats
	 * @return
	 */
	public Future<?> scheduleFpRestoreTask(PlayerLifeStats lifeStats) {
		return ThreadPoolManager.getInstance().scheduleAtFixedRate(new FpRestoreTask(lifeStats), 2000,
			DEFAULT_FPRESTORE_DELAY);
	}

	public static LifeStatsRestoreService getInstance() {
		return instance;
	}

	private static class HpRestoreTask implements Runnable {

		private CreatureLifeStats<?> lifeStats;

		private HpRestoreTask(CreatureLifeStats<?> lifeStats) {
			this.lifeStats = lifeStats;
		}

		@Override
		public void run() {
			boolean inWorld = World.getInstance().isInWorld(lifeStats.getOwner());
			if (!inWorld || lifeStats.isAlreadyDead() || lifeStats.isFullyRestoredHp()) {
				lifeStats.cancelRestoreTask();
				lifeStats = null;
			}
			else {
				lifeStats.restoreHp();
			}
		}
	}

	private static class HpMpRestoreTask implements Runnable {

		private CreatureLifeStats<?> lifeStats;

		private HpMpRestoreTask(CreatureLifeStats<?> lifeStats) {
			this.lifeStats = lifeStats;
		}

		@Override
		public void run() {
			boolean inWorld = World.getInstance().isInWorld(lifeStats.getOwner());
			if (!inWorld || lifeStats.isAlreadyDead() || lifeStats.isFullyRestoredHpMp()) {
				lifeStats.cancelRestoreTask();
				lifeStats = null;
			}
			else {
				lifeStats.restoreHp();
				lifeStats.restoreMp();
			}
		}
	}

	private static class FpReduceTask implements Runnable {

		private PlayerLifeStats lifeStats;

		private FpReduceTask(PlayerLifeStats lifeStats) {
			this.lifeStats = lifeStats;
		}

		@Override
		public void run() {
			boolean inWorld = World.getInstance().isInWorld(lifeStats.getOwner());
			if (!inWorld || lifeStats.isAlreadyDead()){
				lifeStats.cancelFpReduce();
				lifeStats = null;
				return;
			}

			if (lifeStats.getCurrentFp() == 0) {
				if (lifeStats.getOwner().getFlyState() > 0) {
					lifeStats.getOwner().getFlyController().endFly();
				}
				else {
					lifeStats.triggerFpRestore();
				}
			}
			else {
				int reduceFp = lifeStats.getOwner().getFlyState() == 2 && lifeStats.getOwner().isInsideZoneType(ZoneType.FLY) ? 1 : 2;
				lifeStats.reduceFp(reduceFp);
				lifeStats.specialrestoreFp();
			}
		}
	}

	private static class FpRestoreTask implements Runnable {

		private PlayerLifeStats lifeStats;

		private FpRestoreTask(PlayerLifeStats lifeStats) {
			this.lifeStats = lifeStats;
		}

		@Override
		public void run() {
			if (lifeStats.isAlreadyDead() || lifeStats.isFlyTimeFullyRestored()) {
				lifeStats.cancelFpRestore();
				lifeStats = null;
			}
			else {
				lifeStats.restoreFp();
			}
		}
	}
}