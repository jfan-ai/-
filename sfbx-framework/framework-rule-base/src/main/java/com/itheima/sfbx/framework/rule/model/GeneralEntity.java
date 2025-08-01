/*******************************************************************************
 * Copyright 2017 Bstek
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package com.itheima.sfbx.framework.rule.model;

import com.itheima.sfbx.framework.rule.RuleException;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;

/**
 * @author Jacky.gao
 * @since 2016年6月2日
 */
public class GeneralEntity extends HashMap<String, Object>{
	private static final long serialVersionUID = 2778576006420277518L;
	private String targetClass;
	
	public GeneralEntity(String targetClass) {
		if(StringUtils.isBlank(targetClass)){
			throw new RuleException("Target class cannot be null.");
		}
		this.targetClass = targetClass;
	}

	public String getTargetClass() {
		return targetClass;
	}
	
	@Override
	public boolean equals(Object other) {
		boolean classEquals=false;
		if(other instanceof GeneralEntity){
			GeneralEntity entity=(GeneralEntity)other;
			if(targetClass.equals(entity.getTargetClass())){
				classEquals=true;
			}
		}
		if(classEquals){
			return super.equals(other);			
		}
		return false;
	}
}
