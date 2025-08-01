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
package com.itheima.sfbx.framework.rule.runtime.event.impl;

import com.itheima.sfbx.framework.rule.model.flow.ins.ProcessInstance;
import com.itheima.sfbx.framework.rule.runtime.KnowledgeSession;

/**
 * @author Jacky.gao
 * @since 2015年7月21日
 */
public class ProcessAfterStartedEventImpl extends DefaultProcessEvent {
	public ProcessAfterStartedEventImpl(ProcessInstance processInstance,
			KnowledgeSession knowledgeSession) {
		super(processInstance, knowledgeSession);
	}
}
