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
package org.seasar.jcr.converter;

import java.util.List;

import javax.jcr.Node;
import javax.jcr.Value;
import javax.jcr.query.QueryResult;

import org.seasar.jcr.AnnotationReaderFactory;
import org.seasar.jcr.JCRCommandDesc;

/**
 * @author waki41
 *
 */
public interface JcrConverter {

    public abstract Node convertPathToNode(Node baseNode, String[] path)
            throws Throwable;

    public abstract void convertDtoToNode(Node targetNode, JCRCommandDesc cmdDesc)
            throws Throwable;

    public abstract Value convertToValue(Object object);

    public abstract List convertQResultToDto(QueryResult qr, JCRCommandDesc cmdDesc) throws Throwable;

    public abstract void convertDtoToQResult(QueryResult qr, JCRCommandDesc cmdDesc) throws Throwable;
    
    public abstract void setAnnotationReaderFactory(AnnotationReaderFactory annotationReaderFactory);
    
}