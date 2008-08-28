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

import org.seasar.framework.util.StringUtil;

public class Argument {

    private String fieldName;

    private String expression;

    private String dtoFieldName;

    public Argument(String argument) {
        String[] arguments = StringUtil.split(argument, " ");

        fieldName = arguments[0];

        if (arguments.length == 1) {
            expression = "";
        } else {
            expression = arguments[1];
        }

        if (arguments.length == 3) {
            dtoFieldName = arguments[2];
        } else {
            dtoFieldName = fieldName;
        }

    }

    public String getDtoFieldName() {
        return dtoFieldName;
    }

    public void setDtoFieldName(String dtoFieldName) {
        this.dtoFieldName = dtoFieldName;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
}
