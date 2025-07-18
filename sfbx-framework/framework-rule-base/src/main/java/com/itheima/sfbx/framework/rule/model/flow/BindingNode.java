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
import com.itheima.sfbx.framework.rule.model.flow.ins.ProcessInstance;
import com.itheima.sfbx.framework.rule.runtime.KnowledgePackage;
import com.itheima.sfbx.framework.rule.runtime.KnowledgePackageWrapper;
import com.itheima.sfbx.framework.rule.runtime.KnowledgeSession;
import com.itheima.sfbx.framework.rule.runtime.KnowledgeSessionFactory;
import com.itheima.sfbx.framework.rule.runtime.response.ExecutionResponseImpl;
import com.itheima.sfbx.framework.rule.runtime.response.FlowExecutionResponse;
import com.itheima.sfbx.framework.rule.runtime.response.RuleExecutionResponse;

import java.util.List;
import java.util.Map;

/**
 * @author Jacky.gao
 * @since 2015年4月20日
 */
public abstract class BindingNode extends FlowNode {
	private KnowledgePackageWrapper knowledgePackageWrapper;
	public BindingNode() {
	}
	public BindingNode(String name) {
		super(name);
	}
	
	protected KnowledgeSession executeKnowledgePackage(FlowContext context,ProcessInstance instance){
		KnowledgeSession parentSession=(KnowledgeSession)context.getWorkingMemory();
		List<Object> facts=parentSession.getAllFacts();
		KnowledgePackage knowledgePackage=knowledgePackageWrapper.getKnowledgePackage();
		KnowledgeSession session=KnowledgeSessionFactory.newKnowledgeSession(knowledgePackage,context.getDebugMessageItems());
		for(Object fact:facts){
			session.insert(fact);
		}
		if(knowledgePackage.getFlowMap()==null || knowledgePackage.getFlowMap().size()==0){
			RuleExecutionResponse ruleExecutionResponse=session.fireRules(context.getVariables());
			((ExecutionResponseImpl)context.getResponse()).addRuleExecutionResponse(ruleExecutionResponse);
		}else{
			String processId=knowledgePackage.getFlowMap().values().iterator().next().getId();
			FlowExecutionResponse flowExecutionResponse=session.startProcess(processId,context.getVariables());
			((ExecutionResponseImpl)context.getResponse()).addFlowExecutionResponse(flowExecutionResponse);
		}
		Map<String,Object> parameters=session.getParameters();
		Map<String,Object> variables=context.getVariables();
		for(String key:parameters.keySet()){
			if(key.equals(DecisionItem.RETURN_VALUE_KEY)){
				continue;
			}
			variables.put(key, parameters.get(key));
		}
		return session;
	}
	public KnowledgePackageWrapper getKnowledgePackageWrapper() {
		return knowledgePackageWrapper;
	}
	public void setKnowledgePackageWrapper(KnowledgePackageWrapper knowledgePackageWrapper) {
		this.knowledgePackageWrapper = knowledgePackageWrapper;
	}
}
