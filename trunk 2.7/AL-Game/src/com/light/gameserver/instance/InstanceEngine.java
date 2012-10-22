/*
 * This file is part of aion-lightning <aion-lightning.com>.
 *
 *  aion-lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-lightning is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-lightning.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.light.gameserver.instance;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.scripting.classlistener.AggregatedClassListener;
import com.aionemu.commons.scripting.classlistener.OnClassLoadUnloadListener;
import com.aionemu.commons.scripting.classlistener.ScheduledTaskClassListener;
import com.aionemu.commons.scripting.scriptmanager.ScriptManager;
import com.light.gameserver.GameServerError;
import com.light.gameserver.instance.handlers.GeneralInstanceHandler;
import com.light.gameserver.instance.handlers.InstanceHandler;
import com.light.gameserver.instance.handlers.InstanceID;
import com.light.gameserver.model.GameEngine;
import com.light.gameserver.utils.audit.AuditLogger;
import com.light.gameserver.world.WorldMapInstance;

/**
 * @author ATracer
 */
public class InstanceEngine implements GameEngine {

	private static final Logger log = LoggerFactory.getLogger(InstanceEngine.class);
	private static ScriptManager scriptManager = new ScriptManager();
	public static final File INSTANCE_DESCRIPTOR_FILE = new File("./data/scripts/system/instancehandlers.xml");
	public static final InstanceHandler DUMMY_INSTANCE_HANDLER = new GeneralInstanceHandler();

	private Map<Integer, Class<? extends InstanceHandler>> handlers = new HashMap<Integer, Class<? extends InstanceHandler>>();

	@Override
	public void load() {
		log.info("Instance engine load started");
		scriptManager = new ScriptManager();

		AggregatedClassListener acl = new AggregatedClassListener();
		acl.addClassListener(new OnClassLoadUnloadListener());
		acl.addClassListener(new ScheduledTaskClassListener());
		acl.addClassListener(new InstanceHandlerClassListener());
		scriptManager.setGlobalClassListener(acl);

		try {
			scriptManager.load(INSTANCE_DESCRIPTOR_FILE);
		}
		catch (Exception e) {
			throw new GameServerError("Can't initialize instance handlers.", e);
		}
		log.info("Loaded " + handlers.size() + " instance handlers.");
	}

	@Override
	public void shutdown() {
		log.info("Instance engine shutdown started");
		scriptManager.shutdown();
		scriptManager = null;
		handlers.clear();
		log.info("Instance engine shutdown complete");
	}

	public InstanceHandler getNewInstanceHandler(int worldId) {
		Class<? extends InstanceHandler> instanceClass = handlers.get(worldId);
		InstanceHandler instanceHandler = null;
		if (instanceClass != null) {
			try {
				instanceHandler = instanceClass.newInstance();
			}
			catch (Exception ex) {
				log.warn("Can't instantiate instance handler " + worldId, ex);
			}
		}
		if (instanceHandler == null) {
			instanceHandler = DUMMY_INSTANCE_HANDLER;
		}
		return instanceHandler;
	}

	/**
	 * @param handler
	 */
	final void addInstanceHandlerClass(Class<? extends InstanceHandler> handler) {
		InstanceID idAnnotation = handler.getAnnotation(InstanceID.class);
		if (idAnnotation != null) {
			handlers.put(idAnnotation.value(), handler);
		}
	}

	/**
	 * @param instance
	 */
	public void onInstanceCreate(WorldMapInstance instance) {
		instance.getInstanceHandler().onInstanceCreate(instance);
	}

	public static final InstanceEngine getInstance() {
		return SingletonHolder.instance;
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder {

		protected static final InstanceEngine instance = new InstanceEngine();
	}
}