package com.example.springboot.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.pojo.DormBuild;
import com.example.springboot.service.impl.DormBuildImpl;
import com.example.springboot.mapper.DormBuildMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class DormBuildServiceTest {

    @Mock
    private DormBuildMapper dormBuildMapper;

    @Spy
    @InjectMocks
    private DormBuildImpl dormBuildService;

    private DormBuild testDormBuild;

    @BeforeEach
    void setUp() {
        testDormBuild = new DormBuild();
        testDormBuild.setId(1);
        testDormBuild.setDormBuildId(101);
        testDormBuild.setDormBuildName("1号楼");
        testDormBuild.setDormBuildDetail("男生宿舍楼");
    }

    @Test
    void testAddNewBuilding_Success() {
        when(dormBuildMapper.insert(any(DormBuild.class))).thenReturn(1);
        int result = dormBuildService.addNewBuilding(testDormBuild);
        assertEquals(1, result);
        verify(dormBuildMapper).insert(testDormBuild);
    }

    @Test
    void testAddNewBuilding_Failure() {
        when(dormBuildMapper.insert(any(DormBuild.class))).thenReturn(0);
        int result = dormBuildService.addNewBuilding(testDormBuild);
        assertEquals(0, result);
        verify(dormBuildMapper).insert(testDormBuild);
    }

    @Test
    void testUpdateNewBuilding_Success() {
        when(dormBuildMapper.updateById(any(DormBuild.class))).thenReturn(1);
        int result = dormBuildService.updateNewBuilding(testDormBuild);
        assertEquals(1, result);
        verify(dormBuildMapper).updateById(testDormBuild);
    }

    @Test
    void testUpdateNewBuilding_Failure() {
        when(dormBuildMapper.updateById(any(DormBuild.class))).thenReturn(0);
        int result = dormBuildService.updateNewBuilding(testDormBuild);
        assertEquals(0, result);
        verify(dormBuildMapper).updateById(testDormBuild);
    }

    @Test
    void testDeleteBuilding_Success() {
        when(dormBuildMapper.deleteById(1)).thenReturn(1);
        int result = dormBuildService.deleteBuilding(1);
        assertEquals(1, result);
        verify(dormBuildMapper).deleteById(1);
    }

    @Test
    void testDeleteBuilding_Failure() {
        when(dormBuildMapper.deleteById(1)).thenReturn(0);
        int result = dormBuildService.deleteBuilding(1);
        assertEquals(0, result);
        verify(dormBuildMapper).deleteById(1);
    }

    @Test
    void testFind_Success() {
        Page<DormBuild> page = new Page<>(1, 10);
        List<DormBuild> list = Arrays.asList(testDormBuild);
        page.setRecords(list);
        page.setTotal(1);
        when(dormBuildMapper.selectPage(any(Page.class), any())).thenReturn(page);
        Page result = dormBuildService.find(1, 10, "101");
        assertNotNull(result);
        assertEquals(1, result.getTotal());
        assertEquals(1, result.getRecords().size());
        verify(dormBuildMapper).selectPage(any(Page.class), any());
    }

    @Test
    void testGetBuildingId_Success() {
        List<DormBuild> expectedList = Arrays.asList(testDormBuild);
        when(dormBuildMapper.selectList(any())).thenReturn(expectedList);
        List<DormBuild> result = dormBuildService.getBuildingId();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testDormBuild, result.get(0));
        verify(dormBuildMapper).selectList(any());
    }

    @Test
    void testGetBuildingId_Empty() {
        when(dormBuildMapper.selectList(any())).thenReturn(Arrays.asList());
        List<DormBuild> result = dormBuildService.getBuildingId();
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(dormBuildMapper).selectList(any());
    }
} 