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

import java.lang.reflect.Method;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.util.ArrayUtil;

public class ArgsMetaData {

    private String[] argNames_;

    private boolean dtoFlg_ = false;

    private BeanDesc dtoBeanDesc_;

    private Argument[] arguments = {};

    public ArgsMetaData(Method method, String[] argNames, String[] propertyNames) {
        argNames_ = argNames;

        if (((argNames.length == 0) || (argNames.length == 1 && argNames[0]
                .equals("dto")))
                && method.getParameterTypes().length == 1) {
            dtoBeanDesc_ = BeanDescFactory.getBeanDesc(method
                    .getParameterTypes()[0]);
            if (propertyNames == null || propertyNames.length == 0) {
                argNames_ = getPropertyName(dtoBeanDesc_);
            } else {
                argNames_ = propertyNames;
            }
            dtoFlg_ = true;
        }
        for (int i = 0; i < argNames_.length; i++) {
            arguments = (Argument[]) ArrayUtil.add(arguments, new Argument(
                    argNames_[i]));
        }

    }

    public int getArgsCount() {
        return argNames_.length;
    }

    public String[] getArgNames() {
        return argNames_;
    }

    public BeanDesc getDtoBeanDesc() {
        return dtoBeanDesc_;
    }

    public void setDtoBeanDesc(BeanDesc dtoBeanDesc) {
        dtoBeanDesc_ = dtoBeanDesc;
    }

    public boolean isDtoFlg() {
        return dtoFlg_;
    }

    public Object getValue(Object[] args, int index) {
        if (dtoFlg_ == true) {
            return dtoBeanDesc_.getPropertyDesc(
                    arguments[index].getDtoFieldName()).getValue(args[0]);

        } else {
            return args[index];
        }
    }

    private String[] getPropertyName(BeanDesc dtoBeanDesc) {

        String[] args = new String[dtoBeanDesc.getPropertyDescSize()];
        for (int i = 0; i < dtoBeanDesc.getPropertyDescSize(); i++) {
            args[i] = dtoBeanDesc.getPropertyDesc(i).getPropertyName();
        }
        return args;
    }

    public Argument getArgument(int index) {
        return arguments[index];
    }
}
