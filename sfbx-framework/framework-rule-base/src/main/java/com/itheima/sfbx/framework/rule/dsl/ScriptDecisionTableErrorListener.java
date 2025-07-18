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
package com.itheima.sfbx.framework.rule.dsl;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

/**
 * @author Jacky.gao
 * @since 2015年5月11日
 */
public class ScriptDecisionTableErrorListener extends BaseErrorListener {
	private StringBuffer sb;
	@Override
	public void syntaxError(Recognizer<?, ?> recognizer,Object offendingSymbol, 
			int line, int charPositionInLine,
			String msg, RecognitionException e) {
		if(sb==null){
			sb=new StringBuffer();
		}
		sb.append("["+offendingSymbol+"] is invalid:"+msg);
		sb.append("\r\n");
	}
	public String getErrorMessage(){
		if(sb==null){
			return null;
		}
		return sb.toString();
	}
}
