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
package com.itheima.sfbx.framework.rule.model.flow;

import com.itheima.sfbx.framework.rule.model.flow.ins.FlowContext;
import com.itheima.sfbx.framework.rule.model.flow.ins.FlowInstance;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jacky.gao
 * @since 2015年4月20日
 */
public class ForkNode extends FlowNode {
	private FlowNodeType type=FlowNodeType.Fork;
	public ForkNode() {
	}
	public ForkNode(String name) {
		super(name);
	}
	@Override
	public FlowNodeType getType() {
		return type;
	}
	@Override
	public void enterNode(FlowContext context,FlowInstance instance) {
		instance.setCurrentNode(this);
		executeNodeEvent(EventType.enter, context, instance);
		List<Connection> forkConnections=new ArrayList<Connection>();
		for(Connection connection:connections){
			if(connection.evaluate(context)){
				forkConnections.add(connection);
			}
		}
		executeNodeEvent(EventType.leave, context, instance);
		int childCount=forkConnections.size();
		for(Connection connection:forkConnections){
			FlowInstance newChildInstance=instance.newChildInstance(childCount);
			connection.execute(context, newChildInstance);
		}
	}
}
