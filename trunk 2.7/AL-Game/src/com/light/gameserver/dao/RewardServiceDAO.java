package com.light.gameserver.dao;

import javolution.util.FastList;

import com.aionemu.commons.database.dao.DAO;
import com.light.gameserver.model.templates.rewards.RewardEntryItem;

/**
 * @author KID
 */
public abstract class RewardServiceDAO implements DAO {

	@Override
	public final String getClassName() {
		return RewardServiceDAO.class.getName();
	}

	public abstract FastList<RewardEntryItem> getAvailable(int playerId);

	public abstract void uncheckAvailable(FastList<Integer> ids);
}