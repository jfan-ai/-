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
package com.itheima.sfbx.framework.rule.parse.deserializer;

import com.itheima.sfbx.framework.rule.model.library.variable.VariableCategory;
import com.itheima.sfbx.framework.rule.parse.VariableLibraryParser;
import org.dom4j.Element;

import java.util.List;


/**
 * @author Jacky.gao
 * @since 2014年12月23日
 */
public class VariableLibraryDeserializer implements Deserializer<List<VariableCategory>>{
	public static final String BEAN_ID="urule.variableLibraryDeserializer";
	private VariableLibraryParser variableLibraryParser;
	public List<VariableCategory> deserialize(Element root) {
		return variableLibraryParser.parse(root);
	}
	public boolean support(Element root) {
		if(variableLibraryParser.support(root.getName())){
			return true;
		}else{
			return false;
		}
	}
	
	public void setVariableLibraryParser(VariableLibraryParser variableLibraryParser) {
		this.variableLibraryParser = variableLibraryParser;
	}
}
