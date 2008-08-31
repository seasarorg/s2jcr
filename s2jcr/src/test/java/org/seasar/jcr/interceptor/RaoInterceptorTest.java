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
import org.seasar.jcr.dto.FileNode1Dto;
import org.seasar.jcr.dto.FileNode2Dto;
import org.seasar.jcr.dto.FileNode3Dto;
import org.seasar.jcr.rao.FileNode1Rao;
import org.seasar.jcr.rao.FileNode2Rao;
import org.seasar.jcr.rao.FileNode3Rao;
import org.seasar.jcr.repository.RepositoryFactory;

public class RaoInterceptorTest extends S2FrameworkTestCase {

    private FileNode1Rao fileNode1Rao;

    private FileNode2Rao fileNode2Rao;

    private FileNode3Rao fileNode3Rao;

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

    public void testAdd1() throws Exception {

        FileNode1Dto dto = new FileNode1Dto();
        dto.setPath("/test/one/node");
        dto.setDescription("test");
        dto.setTitle("titleName");
        dto.setPrice(new Double(1.25));
        dto.setHan(new Long(1));
        dto.setInputStream(null);

        fileNode1Rao.add(dto);

    }

    public void testAdd2() throws Exception {

        FileNode2Dto dto = new FileNode2Dto();
        dto.setPath("/test/two/node");
        dto.setDescription("test");
        dto.setTitle("titleName");
        dto.setPrice(new Double(1.25));
        dto.setHan(new Long(1));
        dto.setInputStream(null);

        fileNode2Rao.add(dto);

    }

    public void testAdd3() throws Exception {

        FileNode3Dto dto = new FileNode3Dto();
        dto.setLocation("/test/three/node");
        dto.setDescription("test");
        dto.setTitle("titleName");
        dto.setPrice(new Double(1.25));
        dto.setHan(new Long(1));
        dto.setInputStream(null);

        fileNode3Rao.add(dto);

    }

    public void testUpdate1() throws Exception {

        FileNode1Dto dto = new FileNode1Dto();
        dto.setPath("/test/one/node");
        dto.setDescription("test55");
        dto.setTitle("titleName");

        fileNode1Rao.update(dto);

        assertEquals(true, true);

    }

    public void testUpdate3() throws Exception {

        FileNode3Dto dto = new FileNode3Dto();
        dto.setLocation("/test/three/node");
        dto.setDescription("test55");
        dto.setTitle("titleName");

        fileNode3Rao.update(dto);

        assertEquals(true, true);

    }

    public void testGet1() throws Exception {

        FileNode1Dto exampleDto = new FileNode1Dto();

        exampleDto.setPrice(new Double(1.25));

        FileNode1Dto dto = fileNode1Rao.get(exampleDto);

        assertEquals("test55", dto.getDescription());
        assertEquals("titleName", dto.getTitle());
        assertEquals(new Double(1.25), dto.getPrice());
        assertEquals(new Long(1), dto.getHan());

        exampleDto.setTitle("titleName2");

        dto = fileNode1Rao.get(exampleDto);

        assertNull(dto);

    }

    public void testGet2() throws Exception {

        FileNode2Dto exampleDto = new FileNode2Dto();

        exampleDto.setPath("/test/two/node");

        FileNode2Dto dto = fileNode2Rao.get(exampleDto);

        assertNull(dto);

    }

    public void testGet3() throws Exception {

        FileNode3Dto exampleDto = new FileNode3Dto();

        exampleDto.setPrice(new Double(1.25));

        FileNode3Dto dto = fileNode3Rao.get(exampleDto);

        assertEquals("test55", dto.getDescription());
        assertEquals("titleName", dto.getTitle());
        assertEquals(new Double(1.25), dto.getPrice());
        assertEquals(new Long(1), dto.getHan());

        exampleDto.setTitle("titleName2");

        dto = fileNode3Rao.get(exampleDto);

        assertNull(dto);

    }

    public void testGetWithPrice1() throws Exception {

        FileNode1Dto dto = fileNode1Rao.getWithPrice(new Double(1), new Double(
                2));

        assertEquals("test55", dto.getDescription());
        assertEquals("titleName", dto.getTitle());
        assertEquals(new Double(1.25), dto.getPrice());
        assertEquals(new Long(1), dto.getHan());

    }

    public void testGetWithPrice3() throws Exception {

        FileNode3Dto dto = fileNode3Rao.getWithPrice(new Double(1), new Double(
                2));

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

    public void testGetFoo1() throws Exception {

        FileNode1Dto dto = fileNode1Rao.getFoo();

        assertEquals("test55", dto.getDescription());
        assertEquals("titleName", dto.getTitle());

    }

    public void testGetXPath1() throws Exception {

        FileNode1Dto dto = fileNode1Rao.get("//test/one/node");

        assertEquals("test55", dto.getDescription());
        assertEquals("titleName", dto.getTitle());

    }

    public void testGetList1() throws Exception {

        FileNode1Dto exampleDto = new FileNode1Dto();

        List dtos = fileNode1Rao.find(exampleDto);

        assertEquals(new Integer(dtos.size()), new Integer(1));

    }

    public void testDelete1() throws Exception {

        fileNode1Rao.delete("/test/one/node");

        assertEquals(true, true);

    }

    public void testDelete2() throws Exception {

        FileNode2Dto dto = new FileNode2Dto();
        dto.setPath("/test/two/node");
        fileNode2Rao.delete(dto);

        assertEquals(true, true);

    }

    public void testDelete3() throws Exception {

        fileNode1Rao.delete("/test/three/node");

        assertEquals(true, true);

    }

    public FileNode1Rao getFileNode1Rao() {
        return fileNode1Rao;
    }

    public void setFileNode1Rao(FileNode1Rao fileNode1Rao) {
        this.fileNode1Rao = fileNode1Rao;
    }

    public FileNode2Rao getFileNode2Rao() {
        return fileNode2Rao;
    }

    public void setFileNode2Rao(FileNode2Rao fileNode2Rao) {
        this.fileNode2Rao = fileNode2Rao;
    }

    public FileNode3Rao getFileNode3Rao() {
        return fileNode3Rao;
    }

    public void setFileNode3Rao(FileNode3Rao fileNode3Rao) {
        this.fileNode3Rao = fileNode3Rao;
    }

}