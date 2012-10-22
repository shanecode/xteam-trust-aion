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
package com.light.gameserver.model;

import com.light.gameserver.model.gameobjects.player.Player;

/**
 * @author synchro2
 */

public class Wedding {

	private Player player;
	private Player partner;
	private Player priest;
	private boolean accepted;

	public Wedding(Player player, Player partner, Player priest) {
		super();
		this.player = player;
		this.partner = partner;
		this.priest = priest;
	}

	public void setAccept() {
		this.accepted = true;
	}

	public Player getPlayer() {
		return this.player;
	}

	public Player getPartner() {
		return this.partner;
	}

	public Player getPriest() {
		return this.priest;
	}

	public boolean isAccepted() {
		return this.accepted;
	}

}