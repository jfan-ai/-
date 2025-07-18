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

import com.itheima.sfbx.framework.rule.model.table.ScriptDecisionTable;
import com.itheima.sfbx.framework.rule.parse.table.ScriptDecisionTableParser;
import org.dom4j.Element;

/**
 * @author Jacky.gao
 * @since 2015年1月19日
 */
public class ScriptDecisionTableDeserializer implements Deserializer<ScriptDecisionTable>{
	public static final String BEAN_ID="urule.scriptDecisionTableDeserializer";
	private ScriptDecisionTableParser scriptDecisionTableParser;
	public ScriptDecisionTable deserialize(Element root) {
		return scriptDecisionTableParser.parse(root);
	}
	public boolean support(Element root) {
		return scriptDecisionTableParser.support(root.getName());
	}
	public void setScriptDecisionTableParser(
			ScriptDecisionTableParser scriptDecisionTableParser) {
		this.scriptDecisionTableParser = scriptDecisionTableParser;
	}
}
