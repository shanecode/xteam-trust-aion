package com.light.gameserver.dao;

import javolution.util.FastList;

import com.aionemu.commons.database.dao.DAO;
import com.light.gameserver.model.templates.survey.SurveyItem;

/**
 * @author KID
 */
public abstract class SurveyControllerDAO implements DAO {

	@Override
	public final String getClassName() {
		return SurveyControllerDAO.class.getName();
	}

	public abstract boolean useItem(int id);

	public abstract FastList<SurveyItem> getAllNew();
}