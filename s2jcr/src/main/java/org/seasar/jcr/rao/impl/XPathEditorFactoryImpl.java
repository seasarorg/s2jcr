/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.jcr.rao.impl;

import org.seasar.jcr.rao.CommandType;
import org.seasar.jcr.rao.XPathEditStrategy;
import org.seasar.jcr.rao.XPathEditorFactory;

/**
 * @author waki41
 *
 */
public class XPathEditorFactoryImpl implements XPathEditorFactory {

    public static XPathEditStrategy createXPathEditor(CommandType cmdType) {
        
        if (cmdType == CommandType.DEFAULT) {
            return new DefaultStrategy();
        } else if (cmdType == CommandType.AUTO_DTO) {
            return new QBEStrategy();
        } else if (cmdType == CommandType.AUTO_XPATH_ANNOTATION) {
            return new XPathStrategy();
        } else {
            return null;
        }
        
    }
}
