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
package org.seasar.jcr.creator;

import org.seasar.framework.container.ComponentCustomizer;
import org.seasar.framework.container.creator.ComponentCreatorImpl;
import org.seasar.framework.container.deployer.InstanceDefFactory;
import org.seasar.framework.convention.NamingConvention;

/**
 * @author waki41
 * 
 */
public class RaoCreator extends ComponentCreatorImpl {

    public RaoCreator(NamingConvention namingConvention) {
        super(namingConvention);
        setNameSuffix("Rao");
        setInstanceDef(InstanceDefFactory.PROTOTYPE);
        setEnableInterface(true);
        setEnableAbstract(true);
    }

    public ComponentCustomizer getRaoCustomizer() {
        return getCustomizer();
    }

    public void setRaoCustomizer(ComponentCustomizer customizer) {
        setCustomizer(customizer);
    }
}
