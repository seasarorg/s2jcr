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
package org.seasar.jcr.util;

import java.io.InputStream;
import java.util.Calendar;

import javax.jcr.Value;

import org.apache.jackrabbit.value.ValueFactoryImpl;

public class ValueConverter {
    
    private ValueConverter() {}
    
    //TODO support jackrabbit type
    public static Value convert(Object object) {
        
        if (object instanceof String) {
            return ValueFactoryImpl.getInstance().createValue((String)object);
        }
        
        if (object instanceof InputStream) {
            return ValueFactoryImpl.getInstance().createValue((InputStream)object);
        }

        if (object instanceof Calendar) {
            return ValueFactoryImpl.getInstance().createValue((Calendar)object);
        }
    
        return null;
    }

}
