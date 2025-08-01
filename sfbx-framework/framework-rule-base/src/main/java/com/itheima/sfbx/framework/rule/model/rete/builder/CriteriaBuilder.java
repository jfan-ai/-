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
package com.itheima.sfbx.framework.rule.model.rete.builder;

import com.itheima.sfbx.framework.rule.model.rete.BaseReteNode;
import com.itheima.sfbx.framework.rule.model.rule.lhs.BaseCriterion;
import com.itheima.sfbx.framework.rule.model.rule.lhs.Criteria;
import com.itheima.sfbx.framework.rule.model.rule.lhs.Criterion;

/**
 * @author Jacky.gao
 * @since 2016年9月9日
 */
public class CriteriaBuilder extends CriterionBuilder{
	@Override
	public BaseReteNode buildCriterion(BaseCriterion c, BuildContext context) {
		Criteria criteria=(Criteria)c;
		return buildCriteria(criteria,null,context);
	}
	@Override
	public boolean support(Criterion criterion) {
		return criterion instanceof Criteria;
	}
}
