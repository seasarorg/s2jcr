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
package org.seasar.jcr.interceptor;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInvocation;
import org.seasar.framework.aop.interceptors.AbstractInterceptor;
import org.seasar.framework.util.MethodUtil;
import org.seasar.jcr.exception.S2JCRCommonException;
import org.seasar.jcr.rao.JCRCommand;
import org.seasar.jcr.rao.RaoMetaData;
import org.seasar.jcr.rao.RaoMetaDataFactory;

public class RaoInterceptor extends AbstractInterceptor {

    private static final long serialVersionUID = 1L;

    private RaoMetaDataFactory raoMetaDataFactory;

    public RaoInterceptor(RaoMetaDataFactory raoMetaDataFactory) {
        this.raoMetaDataFactory = raoMetaDataFactory;
    }

    /**
     * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
     */
    public Object invoke(MethodInvocation invocation) throws Throwable {
        
        Method method = invocation.getMethod();

        if (!MethodUtil.isAbstract(method)) {
            return invocation.proceed();
        }
        
        try {
            
            Class targetClass = getTargetClass(invocation);
            RaoMetaData rmd = raoMetaDataFactory.getRaoMetaData(targetClass);
            JCRCommand cmd = rmd.getJcrCommand(method.getName());

            return cmd.execute(invocation.getArguments());
            
        } catch (Throwable e) {
            
            throw new S2JCRCommonException("EJCR0001", e);
        
        } 
        
    }

}
