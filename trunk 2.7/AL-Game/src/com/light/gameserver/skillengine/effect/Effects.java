/*
 * This file is part of aion-unique <aion-unique.com>.
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
package com.light.gameserver.skillengine.effect;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;

/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Effects")
public class Effects {

	@XmlElements({
		@XmlElement(name = "root", type = RootEffect.class),
		@XmlElement(name = "buf", type = BufEffect.class),
		@XmlElement(name = "spellatk", type = SpellAttackEffect.class),
		@XmlElement(name = "deform", type = DeformEffect.class),
		@XmlElement(name = "shapechange", type = ShapeChangeEffect.class),
		@XmlElement(name = "polymorph", type = PolymorphEffect.class),
		@XmlElement(name = "poison", type = PoisonEffect.class),
		@XmlElement(name = "stun", type = StunEffect.class),
		@XmlElement(name = "sleep", type = SleepEffect.class),
		@XmlElement(name = "bleed", type = BleedEffect.class),
		@XmlElement(name = "hide", type = HideEffect.class),
		@XmlElement(name = "search", type = SearchEffect.class),
		@XmlElement(name = "statup", type = StatupEffect.class),
		@XmlElement(name = "statdown", type = StatdownEffect.class),
		@XmlElement(name = "statboost", type = StatboostEffect.class),
		@XmlElement(name = "weaponstatboost", type = WeaponStatboostEffect.class),
		@XmlElement(name = "wpnmastery", type = WeaponMasteryEffect.class),
		@XmlElement(name = "snare", type = SnareEffect.class),
		@XmlElement(name = "slow", type = SlowEffect.class),
		@XmlElement(name = "stumble", type = StumbleEffect.class),
		@XmlElement(name = "spin", type = SpinEffect.class),
		@XmlElement(name = "stagger", type = StaggerEffect.class),
		@XmlElement(name = "openaerial", type = OpenAerialEffect.class),
		@XmlElement(name = "closeaerial", type = CloseAerialEffect.class),
		@XmlElement(name = "shield", type = ShieldEffect.class),
		@XmlElement(name = "bind", type = BindEffect.class),
		@XmlElement(name = "dispel", type = DispelEffect.class),
		@XmlElement(name = "skillatk", type = SkillAttackInstantEffect.class),
		@XmlElement(name = "spellatkinstant", type = SpellAttackInstantEffect.class),
		@XmlElement(name = "dash", type = DashEffect.class),
		@XmlElement(name = "backdash", type = BackDashEffect.class),
		@XmlElement(name = "delaydamage", type = DelayedSpellAttackInstantEffect.class),
		@XmlElement(name = "return", type = ReturnEffect.class),
		@XmlElement(name = "healinstant", type = HealInstantEffect.class),
		@XmlElement(name = "mphealinstant", type = MPHealInstantEffect.class),
		@XmlElement(name = "dphealinstant", type = DPHealInstantEffect.class),
		@XmlElement(name = "fphealinstant", type = FPHealInstantEffect.class),
		@XmlElement(name = "prochealinstant", type = ProcHealInstantEffect.class),
		@XmlElement(name = "procmphealinstant", type = ProcMPHealInstantEffect.class),
		@XmlElement(name = "procdphealinstant", type = ProcDPHealInstantEffect.class),
		@XmlElement(name = "procfphealinstant", type = ProcFPHealInstantEffect.class),
		@XmlElement(name = "carvesignet", type = CarveSignetEffect.class),
		@XmlElement(name = "signet", type = SignetEffect.class),
		@XmlElement(name = "signetburst", type = SignetBurstEffect.class),
		@XmlElement(name = "silence", type = SilenceEffect.class),
		@XmlElement(name = "curse", type = CurseEffect.class),
		@XmlElement(name = "blind", type = BlindEffect.class),
		@XmlElement(name = "boosthate", type = BoostHateEffect.class),
		@XmlElement(name = "hostileup", type = HostileUpEffect.class),
		@XmlElement(name = "paralyze", type = ParalyzeEffect.class),
		@XmlElement(name = "confuse", type = ConfuseEffect.class),
		@XmlElement(name = "dispeldebuffphysical", type = DispelDebuffPhysicalEffect.class),
		@XmlElement(name = "dispeldebuffmental", type = DispelDebuffMentalEffect.class),
		@XmlElement(name = "dispeldebuff", type = DispelDebuffEffect.class),
		@XmlElement(name = "alwaysdodge", type = AlwaysDodgeEffect.class),
		@XmlElement(name = "alwaysparry", type = AlwaysParryEffect.class),
		@XmlElement(name = "alwaysresist", type = AlwaysResistEffect.class),
		@XmlElement(name = "alwaysblock", type = AlwaysBlockEffect.class),
		@XmlElement(name = "switchhpmp", type = SwitchHpMpEffect.class),
		@XmlElement(name = "summon", type = SummonEffect.class),
		@XmlElement(name = "aura", type = AuraEffect.class),
		@XmlElement(name = "resurrect", type = ResurrectEffect.class),
		@XmlElement(name = "returnpoint", type = ReturnPointEffect.class),
		@XmlElement(name = "provoker", type = ProvokerEffect.class),
		@XmlElement(name = "reflector", type = ReflectorEffect.class),
		@XmlElement(name = "spellatkdraininstant", type = SpellAtkDrainInstantEffect.class),
		@XmlElement(name = "procatk_instant", type = ProcAtkInstantEffect.class),
		@XmlElement(name = "onetimeboostskillattack", type = OneTimeBoostSkillAttackEffect.class),
		@XmlElement(name = "armormastery", type = ArmorMasteryEffect.class),
		@XmlElement(name = "weaponstatup", type = WeaponStatupEffect.class),
		@XmlElement(name = "boostskillcastingtime", type = BoostSkillCastingTimeEffect.class),
		@XmlElement(name = "summontrap", type = SummonTrapEffect.class),
		@XmlElement(name = "summongroupgate", type = SummonGroupGateEffect.class),
		@XmlElement(name = "summonservant", type = SummonServantEffect.class),
		@XmlElement(name = "skillatkdraininstant", type = SkillAtkDrainInstantEffect.class),
		@XmlElement(name = "petorderuseultraskill", type = PetOrderUseUltraSkillEffect.class),
		@XmlElement(name = "boostheal", type = BoostHealEffect.class),
		@XmlElement(name = "dispelbuff", type = DispelBuffEffect.class),
		@XmlElement(name = "skilllauncher", type = SkillLauncherEffect.class),
		@XmlElement(name = "pulled", type = PulledEffect.class),
		@XmlElement(name = "fear", type = FearEffect.class),
		@XmlElement(name = "movebehind", type = MoveBehindEffect.class),
		@XmlElement(name = "rebirth", type = RebirthEffect.class),
		@XmlElement(name = "boostskillcost", type = BoostSkillCostEffect.class),
		@XmlElement(name = "protect", type = ProtectEffect.class),
		@XmlElement(name = "magiccounteratk", type = MagicCounterAtkEffect.class),
		@XmlElement(name = "randommoveloc", type = RandomMoveLocEffect.class),
		@XmlElement(name = "recallinstant", type = RecallInstantEffect.class),
		@XmlElement(name = "summonhoming", type = SummonHomingEffect.class),
		@XmlElement(name = "dispelbuffcounteratk", type = DispelBuffCounterAtkEffect.class),
		@XmlElement(name = "xpboost", type = XPBoostEffect.class),
		@XmlElement(name = "onetimeboostheal", type = OneTimeBoostHealEffect.class),
		@XmlElement(name = "fpatk", type = FpAttackEffect.class),
		@XmlElement(name = "deboostheal", type = DeboostHealEffect.class),
		@XmlElement(name = "fpatkinstant", type = FpAttackInstantEffect.class),
		@XmlElement(name = "delayedfpatk", type = DelayedFPAttackInstantEffect.class), // TODO in xml
		@XmlElement(name = "summonskillarea", type = SummonSkillAreaEffect.class),
		@XmlElement(name = "mpattackinstant", type = MpAttackInstantEffect.class),
		@XmlElement(name = "onetimeboostskillcritical", type = OneTimeBoostSkillCriticalEffect.class),
		@XmlElement(name = "resurrectpos", type = ResurrectPositionalEffect.class),
		@XmlElement(name = "nofly", type = NoFlyEffect.class),
		@XmlElement(name = "healcastoronatk", type = HealCastorOnAttackedEffect.class),
		@XmlElement(name = "wpndual", type = WeaponDualEffect.class),
		@XmlElement(name = "resurrectbase", type = ResurrectBaseEffect.class),
		@XmlElement(name = "switchhostile", type = SwitchHostileEffect.class),
		@XmlElement(name = "invulnerablewing", type = InvulnerableWingEffect.class),
		@XmlElement(name = "shieldmastery", type = ShieldMasteryEffect.class),
		@XmlElement(name = "simpleroot", type = SimpleRootEffect.class),
		@XmlElement(name = "dptransfer", type = DPTransferEffect.class),
		@XmlElement(name = "mpattack", type = MpAttackEffect.class),
		@XmlElement(name = "boostdroprate", type = BoostDropRateEffect.class),
		@XmlElement(name = "spellatkdrain", type = SpellAtkDrainEffect.class),
		@XmlElement(name = "extendedaurarange", type = ExtendAuraRangeEffect.class),
		@XmlElement(name = "changehateonattacked", type = ChangeHateOnAttackedEffect.class),
		@XmlElement(name = "healcastorontargetdead", type = HealCastorOnTargetDeadEffect.class),
		@XmlElement(name = "noreducespellatk", type = NoReduceSpellATKInstantEffect.class),
		@XmlElement(name = "condskilllauncher", type = CondSkillLauncherEffect.class),
		@XmlElement(name = "fall", type = FallEffect.class),
		@XmlElement(name = "evade", type = EvadeEffect.class),
		@XmlElement(name = "buffbind", type = BuffBindEffect.class),
		@XmlElement(name = "buffsilence", type = BuffSilenceEffect.class),
		@XmlElement(name = "buffsleep", type = BuffSleepEffect.class),
		@XmlElement(name = "buffstun", type = BuffStunEffect.class),
		@XmlElement(name = "heal", type = HealEffect.class),
		@XmlElement(name = "mpheal", type = MPHealEffect.class),
		@XmlElement(name = "fpheal", type = FPHealEffect.class),
		@XmlElement(name = "dpheal", type = DPHealEffect.class),
		@XmlElement(name = "summontotem", type = SummonTotemEffect.class),
		@XmlElement(name = "disease", type = DiseaseEffect.class),
		@XmlElement(name = "boostspellattack", type = BoostSpellAttackEffect.class)})
	protected List<EffectTemplate> effects;

	/**
	 * Gets the value of the effects property.
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
	 * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
	 * the effect property.
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getEffects().add(newItem);
	 * </pre>
	 */
	public List<EffectTemplate> getEffects() {
		if (effects == null) {
			effects = new ArrayList<EffectTemplate>();
		}
		return this.effects;
	}

	public int getEffectsDuration() {
		int duration = 0;
		for (EffectTemplate template : getEffects()) {
			duration = duration > template.getDuration() ? duration : template.getDuration();
		}
		return duration;
	}

	/**
	 * TODO remove after effect types are done !!
	 * 
	 * @return
	 */
	public boolean isResurrect() {
		for (EffectTemplate template : getEffects()) {
			if ((template instanceof ResurrectEffect) || (template instanceof ResurrectPositionalEffect))
				return true;
		}
		return false;
	}

	/**
	 * @return
	 */
	public boolean isItemHealFp() {
		for (EffectTemplate template : getEffects()) {
			if (template instanceof ProcFPHealInstantEffect)
				return true;
		}
		return false;
	}

	public boolean isMpHealInstant() {
		for (EffectTemplate template : getEffects()) {
			if (template instanceof MPHealInstantEffect)
				return true;
		}
		return false;
	}
}
