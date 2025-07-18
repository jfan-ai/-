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
package com.itheima.sfbx.framework.rule.model.rule.lhs;

import com.itheima.sfbx.framework.rule.model.library.Datatype;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * @author Jacky.gao
 * @since 2015年3月14日
 */
public class VariableLeftPart implements LeftPart{
	@JsonIgnore
	private String id;
	private String variableName;
	private String variableLabel;
	private String variableCategory;
	private Datatype datatype;
	public String getVariableName() {
		return variableName;
	}
	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}
	public String getVariableLabel() {
		return variableLabel;
	}
	public void setVariableLabel(String variableLabel) {
		this.variableLabel = variableLabel;
	}
	public String getVariableCategory() {
		return variableCategory;
	}
	public void setVariableCategory(String variableCategory) {
		this.variableCategory = variableCategory;
	}
	public Datatype getDatatype() {
		return datatype;
	}
	public void setDatatype(Datatype datatype) {
		this.datatype = datatype;
	}
	@Override
	public String getId() {
		if(id==null){
			id="[变量]"+getVariableCategory()+"."+getVariableLabel();
		}
		return id;
	}
}
