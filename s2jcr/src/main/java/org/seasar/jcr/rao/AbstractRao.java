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
package org.seasar.jcr.rao;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.seasar.jcr.S2JCRSessionFactory;
import org.seasar.jcr.converter.JcrConverter;

/**
 * @author waki41
 *
 */
public abstract class AbstractRao {

    private S2JCRSessionFactory sessionFactory;

    private JcrConverter jcrConverter;
    
    public JcrConverter getJcrConverter() {
        return jcrConverter;
    }

    public void setJcrConverter(JcrConverter jcrConverter) {
        this.jcrConverter = jcrConverter;
    }

    public S2JCRSessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(S2JCRSessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
 
    public Session getSession() throws RepositoryException {
        return this.getSessionFactory().getSession();
    }
}
