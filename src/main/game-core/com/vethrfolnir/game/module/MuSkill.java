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

import com.vethrfolnir.game.templates.SkillTemplate;


/**
 * @author Seth
 */
public final class MuSkill {

	private final int skillId;
	private final String name;
	private final SkillTemplate template;

	public MuSkill(SkillTemplate template) {
		this(template.SkillId, template);
	}
	
	/**
	 * @param skillId
	 * @param template
	 */
	public MuSkill(int skillId, SkillTemplate template) {
		this.skillId = skillId;
		this.template = template;
		this.name = template.Name;
	}
	
	/**
	 * @return the skillId
	 */
	public int getSkillId() {
		return skillId;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return the template
	 */
	public SkillTemplate getTemplate() {
		return template;
	}

	/**
	 * @return
	 */
	public boolean isMassSkill() {
		switch (skillId) {
			case 9: // Evil Spirits
			case 41: // Twisting Slash
				return true;
			default:
				return false;
		}
	}
}
