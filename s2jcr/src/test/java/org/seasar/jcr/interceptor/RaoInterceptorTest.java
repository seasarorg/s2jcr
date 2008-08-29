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

import java.util.List;

import javax.jcr.Repository;

import org.apache.jackrabbit.core.RepositoryImpl;
import org.seasar.framework.unit.S2FrameworkTestCase;
import org.seasar.jcr.dto.FileNodeDto;
import org.seasar.jcr.rao.FileNodeRao;
import org.seasar.jcr.rao.PageNodeRao;
import org.seasar.jcr.repository.RepositoryFactory;

public class RaoInterceptorTest extends S2FrameworkTestCase {

    private FileNodeRao fileNodeRao;

    private PageNodeRao pageNodeRao;

    public void setUp() {
        include("FileNodeRao.dicon");
    }

    protected void tearDownBeforeContainerDestroy() throws Throwable {
        RepositoryFactory repositoryFactory = (RepositoryFactory) getComponent(RepositoryFactory.class);
        if (repositoryFactory != null) {
            Repository repository = repositoryFactory.getRepository();
            if (repository instanceof RepositoryImpl) {
                ((RepositoryImpl) repository).shutdown();
            }
        }

        super.tearDownBeforeContainerDestroy();
    }

    public void testAdd() throws Exception {

        FileNodeDto dto = new FileNodeDto();
        dto.setPath("/test/node");
        dto.setDescription("test");
        dto.setTitle("titleName");
        dto.setPrice(new Double(1.25));
        dto.setHan(new Long(1));
        dto.setInputStream(null);

        fileNodeRao.add(dto);

    }

    public void testAddP() throws Exception {

        FileNodeDto dto = new FileNodeDto();
        dto.setPath("/test/page/node");
        dto.setDescription("test");
        dto.setTitle("titleName");
        dto.setPrice(new Double(1.25));
        dto.setHan(new Long(1));
        dto.setInputStream(null);

        pageNodeRao.add(dto);

    }

    public void testUpdate() throws Exception {

        FileNodeDto dto = new FileNodeDto();
        dto.setPath("/test/node");
        dto.setDescription("test55");
        dto.setTitle("titleName");

        fileNodeRao.update(dto);

        assertEquals(true, true);

    }

    public void testGet() throws Exception {

        FileNodeDto exampleDto = new FileNodeDto();

        exampleDto.setPrice(new Double(1.25));

        // TODO
        FileNodeDto dto = fileNodeRao.get(exampleDto);

        assertEquals("test55", dto.getDescription());
        assertEquals("titleName", dto.getTitle());
        assertEquals(new Double(1.25), dto.getPrice());
        assertEquals(new Long(1), dto.getHan());

        exampleDto.setTitle("titleName2");

        // TODO
        dto = fileNodeRao.get(exampleDto);

        assertNull(dto);

    }

    public void testGetP() throws Exception {

        FileNodeDto exampleDto = new FileNodeDto();

        exampleDto.setPrice(new Double(1.25));

        // TODO
        FileNodeDto dto = pageNodeRao.get(exampleDto);

        assertNull(dto);

    }

    public void testGetWithPrice() throws Exception {

        // TODO
        FileNodeDto dto = fileNodeRao
                .getWithPrice(new Double(1), new Double(2));

        assertEquals("test55", dto.getDescription());
        assertEquals("titleName", dto.getTitle());
        assertEquals(new Double(1.25), dto.getPrice());
        assertEquals(new Long(1), dto.getHan());

    }

    // public void testGetWithNodePath() throws Exception {
    //
    // FileNodeDto dto = rao.getWithNodePath("com4/seasar/jcr/FileNode");
    //        
    // assertEquals("test", dto.getDescription());
    // assertEquals("titleName", dto.getTitle());
    //      
    //        
    // }

    public void testGet2() throws Exception {

        // TODO
        FileNodeDto dto = fileNodeRao.get2();

        assertEquals("test55", dto.getDescription());
        assertEquals("titleName", dto.getTitle());

    }

    public void testGetPath() throws Exception {

        FileNodeDto dto = fileNodeRao.get("/test/node");

        assertEquals("test55", dto.getDescription());
        assertEquals("titleName", dto.getTitle());

    }

    public void testGetList() throws Exception {

        FileNodeDto exampleDto = new FileNodeDto();

        // TODO
        List dtos = fileNodeRao.find(exampleDto);

        assertEquals(new Integer(dtos.size()), new Integer(2));

    }

    public void testDelete() throws Exception {

        // FileNodeDto dto = new FileNodeDto();
        // dto.setDescription("test55");
        // dto.setTitle("titleNameZ");

        fileNodeRao.delete("/test/node");

        assertEquals(true, true);

    }

    public void testDeleteP() throws Exception {

        FileNodeDto dto = new FileNodeDto();
        dto.setPath("/test/page/node");
        pageNodeRao.delete(dto);

        assertEquals(true, true);

    }

    public FileNodeRao getRao() {
        return fileNodeRao;
    }

    public void setRao(FileNodeRao rao) {
        this.fileNodeRao = rao;
    }

    public PageNodeRao getPrao() {
        return pageNodeRao;
    }

    public void setPrao(PageNodeRao prao) {
        this.pageNodeRao = prao;
    }

}