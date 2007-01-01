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

import java.util.ArrayList;
import java.util.List;

import javax.jcr.RepositoryException;

import org.seasar.jcr.S2JCRSessionFactory;

public abstract class AbstractAutoJCRXPathCommand extends AbstractJCRXPathCommand {

    /**
     * @param sessionFactory
     */
    public AbstractAutoJCRXPathCommand(S2JCRSessionFactory sessionFactory) {
        super(sessionFactory);
    }

    protected List criteriaCommandList_ = new ArrayList();

    public Object execute(Object[] args) throws RepositoryException {
        List ret = null;

        return ret;
        
    }

}