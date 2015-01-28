/**
 * Copyright (C) 2013-2014 Project-Vethrfolnir
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.vethrfolnir.game.module;

import com.vethrfolnir.game.entitys.ComponentIndex;
import com.vethrfolnir.game.entitys.GameObject;
import com.vethrfolnir.game.entitys.components.Positioning;
import com.vethrfolnir.game.entitys.components.creature.CreatureMapping;
import com.vethrfolnir.game.entitys.components.creature.CreatureStats;
import com.vethrfolnir.game.entitys.components.player.PlayerMapping;
import com.vethrfolnir.game.network.mu.MuPackets;
import com.vethrfolnir.game.network.mu.send.ActionUpdate;
import com.vethrfolnir.game.staticdata.world.Region;
import com.vethrfolnir.tools.Rnd;

/**
 * @author Vlad
 * 
 * Badass name for a badass module
 */
public class CombatEngine {
	
	public enum DamageType {
		
		NormalDamage(0x00),
		IgnoreDamage(0x01),
		ExcelentDamage(0x02),
		CriticalDamage(0x03),
		DoubleDamage(0x03),
		ReflectDamage(0x04),
		
		// Not used here
		DPSPoison(0x05),
		DamageOverTime(0x06),
		Error(0xFF);
		
		private final int _flag;
		
		private DamageType(int flag) {
			_flag = flag;
		}
		
		public int getFlag() {
			return _flag;
		}
	}

	public static final void computeSimpleAttack(GameObject attacker, GameObject target) {
		
		ComponentIndex<?> index = target.isPlayer() ? PlayerMapping.PlayerStats : CreatureMapping.CreatureStats;
		CreatureStats targetStats = (CreatureStats) target.get(index);
		
		int rndDmg = target.isPlayer() ? Rnd.get(20, 500) : Rnd.get(1, 50);

		if(!targetStats.takeDamageHp(attacker, rndDmg))
			return;
		
		Region region = attacker.get(PlayerMapping.Positioning).getCurrentRegion();
		
		region.broadcastToKnown(attacker, MuPackets.ActionUpdate, ActionUpdate.AttackTarget, attacker);
		
		attacker.sendPacket(MuPackets.DamageInfo, target, rndDmg, rndDmg > 30 ? DamageType.CriticalDamage : DamageType.NormalDamage);
		target.sendPacket(MuPackets.DamageInfo, target, rndDmg, DamageType.NormalDamage);
	}
	
	public static final void useSkill(GameObject entity, int skillid, GameObject target) {

		MuSkill skill = StaticData.getSkill(skillid);
		
		if(skill == null)
			return;

		ComponentIndex<?> index = target.isPlayer() ? PlayerMapping.PlayerStats : CreatureMapping.CreatureStats;
		CreatureStats targetStats = (CreatureStats) target.get(index);

		int rndDmg = target.isPlayer() ? Rnd.get(20, 500) : Rnd.get(1, 50);

		if(!targetStats.takeDamageHp(entity, rndDmg))
			return;

		Positioning positioning = entity.get(CreatureMapping.Positioning);
		Region region = positioning.getCurrentRegion();

		if(skill.isMassSkill()) {
			region.broadcastToKnown(entity, MuPackets.MassSkillUse, entity, skillid);
			entity.sendPacket(MuPackets.MassSkillUse, entity, skillid);
		}
		else {
			region.broadcastToKnown(entity, MuPackets.SkillUse, skillid, entity.getWorldIndex(), target.getWorldIndex());
			entity.sendPacket(MuPackets.SkillUse, skillid, entity.getWorldIndex(), target.getWorldIndex());
		}
		
		entity.sendPacket(MuPackets.DamageInfo, target, rndDmg, rndDmg > 30 ? DamageType.CriticalDamage : DamageType.NormalDamage);
		target.sendPacket(MuPackets.DamageInfo, target, rndDmg, DamageType.NormalDamage);

	}
}
