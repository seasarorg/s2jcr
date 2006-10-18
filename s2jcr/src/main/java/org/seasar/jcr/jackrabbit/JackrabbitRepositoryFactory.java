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
package org.seasar.jcr.jackrabbit;

import java.io.File;

import javax.jcr.Repository;
import javax.jcr.RepositoryException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jackrabbit.core.RepositoryImpl;
import org.apache.jackrabbit.core.config.ConfigurationException;
import org.apache.jackrabbit.core.config.RepositoryConfig;
import org.seasar.jcr.RepositoryFactory;

public class JackrabbitRepositoryFactory extends RepositoryFactory {
    /**
     * Logger for this class
     */
    private static final Log log = LogFactory
            .getLog(JackrabbitRepositoryFactory.class);

    private static final String DEFAULT_CONF_FILE = "repository.xml";

    private static final String DEFAULT_REP_DIR = ".";

    private RepositoryConfig repositoryConfig;

    private String configuration;

    private String home;

    public JackrabbitRepositoryFactory(String configuration, String home) {
        this.configuration = configuration;
        this.home = home;
    }

    protected Repository createRepository() throws RepositoryException {
        try {
            // return Jackrabbit repository.
            return RepositoryImpl.create(repositoryConfig);
        } catch (RepositoryException e) {
            log.error("Could not create Jackrabbit's repository. ", e);
            throw new RepositoryException(
                    "Could not create Jackrabbit's repository. ", e);
        }
    }

    protected void resolveConfigurationResource() throws RepositoryException {
        // read the configuration object
        if (repositoryConfig != null)
            return;

        if (this.configuration == null) {
            if (log.isDebugEnabled())
                log
                        .debug("no configuration resource specified, using the default one:"
                                + DEFAULT_CONF_FILE);
            configuration = DEFAULT_CONF_FILE;
        }

        if (home == null) {
            if (log.isDebugEnabled())
                log
                        .debug("no repository home dir specified, using the default one:"
                                + DEFAULT_REP_DIR);
            home = DEFAULT_REP_DIR;
        }

        try {
            repositoryConfig = RepositoryConfig.create(configuration, new File(
                    home).getAbsolutePath());
        } catch (ConfigurationException e) {
            log.error("Could not create Jackrabbit's repository config. ", e);
            throw new RepositoryException(
                    "Could not create Jackrabbit's repository config. ", e);
        }
    }

    /**
     * Shutdown method.
     * 
     */
    public void destroy() throws RepositoryException {
        Repository repo = getRepository();
        if (repo instanceof RepositoryImpl) {
            ((RepositoryImpl) repo).shutdown();
        }
        log.error("Could not shutdown the repository.");
        throw new RepositoryException(
                "Could not shutodonw Jackrabbit's repository. ");
    }

    /**
     * @return Returns the configuration.
     */
    public String getConfiguration() {
        return configuration;
    }

    /**
     * @param configuration
     *            The configuration to set.
     */
    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }

    /**
     * @return Returns the home.
     */
    public String getHome() {
        return home;
    }

    /**
     * @param home
     *            The home to set.
     */
    public void setHome(String home) {
        this.home = home;
    }

}
