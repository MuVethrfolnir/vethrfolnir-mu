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
package com.vethrfolnir.game.staticdata;

import gnu.trove.map.hash.TIntObjectHashMap;

import java.util.ArrayList;

import com.vethrfolnir.game.module.MuSkill;
import com.vethrfolnir.game.templates.SkillTemplate;
import com.vethrfolnir.logging.MuLogger;
import com.vethrfolnir.services.DataMappingService;
import com.vethrfolnir.tools.Disposable;

import corvus.corax.processing.annotation.Inject;

/**
 * @author Vlad
 *
 */
public class SkillData {

	private static final MuLogger log = MuLogger.getLogger(SkillData.class);
	
	private TIntObjectHashMap<MuSkill> skills = new TIntObjectHashMap<>();
	
	@Inject
	private void load(DataMappingService dms) {
		try {
			ArrayList<SkillTemplate> data =  dms.asArrayList(SkillTemplate.class, "system/static/skills/skills.json");
			for (int i = 0; i < data.size(); i++) {
				SkillTemplate st = data.get(i);
				skills.put(st.SkillId, new MuSkill(st));
			}
			log.info("Loaded "+skills.size()+" Skill(s)");
			Disposable.dispose(data);
		}
		catch (Exception e) {
			log.fatal("Failed loading skills!", e);
		}
	}

	/**
	 * @param skillid
	 * @return
	 */
	public MuSkill getSkill(int skillid) {
		return skills.get(skillid);
	}
}
