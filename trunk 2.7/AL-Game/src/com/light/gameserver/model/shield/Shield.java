/*
 * This file is part of aion-unique <aion-unique.org>.
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
package com.light.gameserver.model.shield;

import com.light.gameserver.controllers.ShieldController;
import com.light.gameserver.model.gameobjects.VisibleObject;
import com.light.gameserver.model.templates.shield.ShieldTemplate;
import com.light.gameserver.utils.idfactory.IDFactory;
import com.light.gameserver.world.World;
import com.light.gameserver.world.knownlist.SphereKnownList;

/**
 * @author Wakizashi
 */
public class Shield extends VisibleObject {

	private ShieldTemplate template = null;
	private String name = null;
	private int id = 0;

	public Shield(ShieldTemplate template) {
		super(IDFactory.getInstance().nextId(), new ShieldController(), null, null, World.getInstance().createPosition(
			template.getMap(), template.getCenter().getX(), template.getCenter().getY(), template.getCenter().getZ(),
			(byte) 0, 0));

		((ShieldController) getController()).setOwner(this);
		this.template = template;
		this.name = (template.getName() == null) ? "SHIELD" : template.getName();
		this.id = template.getId();
		setKnownlist(new SphereKnownList(this, template.getRadius() * 2));
	}

	public ShieldTemplate getTemplate() {
		return template;
	}

	@Override
	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}

	public void spawn() {
		World w = World.getInstance();
		w.storeObject(this);
		w.spawn(this);
	}
}