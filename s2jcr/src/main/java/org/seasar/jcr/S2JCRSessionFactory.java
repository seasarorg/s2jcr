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
package org.seasar.jcr;

import java.util.Map;

import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.seasar.jcr.event.EventListenerDefinition;

public interface S2JCRSessionFactory {

    public abstract Session getSession() throws RepositoryException;

    /**
     * @return Returns the eventListenerDefinitions.
     */
    public abstract EventListenerDefinition[] getEventListeners();

    /**
     * @param eventListenerDefinitions
     *            The eventListenerDefinitions to set.
     */
    public abstract void setEventListeners(
            EventListenerDefinition[] eventListenerDefinitions);

    public abstract void destroy() throws RepositoryException;

    /**
     * @return Returns the repository.
     */
    public abstract Repository getRepository();

    /**
     * @param repository
     *            The repository to set.
     */
    public abstract void setRepository(Repository repository);

    /**
     * @return Returns the workspaceName.
     */
    public abstract String getWorkspaceName();

    /**
     * @param workspaceName
     *            The workspaceName to set.
     */
    public abstract void setWorkspaceName(String workspaceName);

    /**
     * @return Returns the namespaces.
     */
    public abstract Map getNamespaces();

    /**
     * @param namespaces
     *            The namespaces to set.
     */
    public abstract void setNamespaces(Map namespaces);

}