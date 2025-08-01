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
package com.itheima.sfbx.framework.rule.builder.resource;

import com.itheima.sfbx.framework.rule.model.table.DecisionTable;
import com.itheima.sfbx.framework.rule.parse.deserializer.DecisionTableDeserializer;
import org.dom4j.Element;

/**
 * @author Jacky.gao
 * @since 2015年2月9日
 */
public class DecisionTableResourceBuilder implements ResourceBuilder<DecisionTable> {
	private DecisionTableDeserializer decisionTableDeserializer;
	public DecisionTable build(Element root) {
		return decisionTableDeserializer.deserialize(root);
	}
	public ResourceType getType() {
		return ResourceType.DecisionTable;
	}
	public boolean support(Element root) {
		return decisionTableDeserializer.support(root);
	}
	public void setDecisionTableDeserializer(
			DecisionTableDeserializer decisionTableDeserializer) {
		this.decisionTableDeserializer = decisionTableDeserializer;
	}
}
