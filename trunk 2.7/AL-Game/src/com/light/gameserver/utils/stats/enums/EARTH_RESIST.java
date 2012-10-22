/*
*
* ��������� Java EMU �� ������������� Aion Light
* ��������� ���������� ��� ������� Aion-Light.Ru
*
*/ 
package com.light.gameserver.utils.stats.enums;

/**
 * @author Drusik
 */
 
public enum EARTH_RESIST {
	WARRIOR(0),
	GLADIATOR(0),
	TEMPLAR(0),
	SCOUT(0),
	ASSASSIN(0),
	RANGER(0),
	MAGE(0),
	SORCERER(0),
	SPIRIT_MASTER(0),
	PRIEST(0),
	CLERIC(0),
	CHANTER(0);

	private int value;

	private EARTH_RESIST(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
