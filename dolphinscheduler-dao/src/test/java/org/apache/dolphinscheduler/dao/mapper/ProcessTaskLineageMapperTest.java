/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.dolphinscheduler.dao.mapper;

import org.apache.dolphinscheduler.common.enums.FailureStrategy;
import org.apache.dolphinscheduler.common.enums.ReleaseState;
import org.apache.dolphinscheduler.common.enums.WarningType;
import org.apache.dolphinscheduler.dao.BaseDaoTest;
import org.apache.dolphinscheduler.dao.entity.ProcessDefinition;
import org.apache.dolphinscheduler.dao.entity.ProcessTaskLineage;
import org.apache.dolphinscheduler.dao.entity.ProcessTaskRelation;
import org.apache.dolphinscheduler.dao.entity.Schedule;
import org.apache.dolphinscheduler.dao.entity.WorkFlowRelationDetail;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ProcessTaskLineageMapperTest extends BaseDaoTest {

    @Autowired
    private ProcessTaskLineageMapper processTaskLineageMapper;

    @Autowired
    private ProcessDefinitionMapper processDefinitionMapper;

    @Autowired
    private ScheduleMapper scheduleMapper;

    @Autowired
    private ProcessTaskRelationMapper processTaskRelationMapper;

    /**
     * insert
     */
    private void insertOneProcessTaskRelation() {
        // insertOne
        ProcessTaskRelation processTaskRelation = new ProcessTaskRelation();
        processTaskRelation.setName("def 1");

        processTaskRelation.setProjectCode(1L);
        processTaskRelation.setProcessDefinitionCode(1L);
        processTaskRelation.setPostTaskCode(3L);
        processTaskRelation.setPostTaskVersion(1);
        processTaskRelation.setPreTaskCode(2L);
        processTaskRelation.setPreTaskVersion(1);
        processTaskRelation.setUpdateTime(new Date());
        processTaskRelation.setCreateTime(new Date());
        processTaskRelationMapper.insert(processTaskRelation);
    }

    /**
     * insert
     *
     */
    private void insertOneProcessDefinition() {
        // insertOne
        ProcessDefinition processDefinition = new ProcessDefinition();
        processDefinition.setCode(1L);
        processDefinition.setName("def 1");
        processDefinition.setProjectCode(1L);
        processDefinition.setUserId(101);
        processDefinition.setUpdateTime(new Date());
        processDefinition.setCreateTime(new Date());
        processDefinitionMapper.insert(processDefinition);
    }

    private void insertOneProcessLineage() {
        // insertOne
        ProcessTaskLineage processTaskLineage = new ProcessTaskLineage();
        processTaskLineage.setProcessDefinitionCode(1L);
        processTaskLineage.setProcessDefinitionVersion(1);
        processTaskLineage.setTaskDefinitionCode(1L);
        processTaskLineage.setTaskDefinitionVersion(1);
        processTaskLineage.setDeptProjectCode(1L);
        processTaskLineage.setDeptProcessDefinitionCode(1L);
        processTaskLineage.setDeptTaskDefinitionCode(1L);
        processTaskLineage.setUpdateTime(new Date());
        processTaskLineage.setCreateTime(new Date());
        processTaskLineageMapper.insert(processTaskLineage);
    }

    /**
     * insert
     *
     */
    private void insertOneSchedule(int id) {
        // insertOne
        Schedule schedule = new Schedule();
        schedule.setStartTime(new Date());
        schedule.setEndTime(new Date());
        schedule.setCrontab("");
        schedule.setFailureStrategy(FailureStrategy.CONTINUE);
        schedule.setReleaseState(ReleaseState.OFFLINE);
        schedule.setWarningType(WarningType.NONE);
        schedule.setCreateTime(new Date());
        schedule.setUpdateTime(new Date());
        schedule.setProcessDefinitionCode(id);
        scheduleMapper.insert(schedule);
    }

    @Test
    public void testQueryWorkFlowLineageByName() {
        insertOneProcessDefinition();
        ProcessDefinition processDefinition = processDefinitionMapper.queryByCode(1L);
        insertOneSchedule(processDefinition.getId());
        List<WorkFlowRelationDetail> workFlowLineages = processTaskLineageMapper
                .queryWorkFlowLineageByName(processDefinition.getProjectCode(), processDefinition.getName());
        Assertions.assertNotEquals(0, workFlowLineages.size());
    }

    @Test
    public void testQueryWorkFlowLineage() {
        insertOneProcessDefinition();
        ProcessDefinition processDefinition = processDefinitionMapper.queryByCode(1L);
        insertOneProcessTaskRelation();
        insertOneProcessLineage();
        List<ProcessTaskLineage> processTaskLineages =
                processTaskLineageMapper.queryByProjectCode(processDefinition.getProjectCode());
        Assertions.assertNotEquals(0, processTaskLineages.size());
    }

    @Test
    public void testQueryWorkFlowLineageByCode() {
        insertOneProcessDefinition();
        ProcessDefinition processDefinition = processDefinitionMapper.queryByCode(1L);
        insertOneSchedule(processDefinition.getId());
        List<WorkFlowRelationDetail> workFlowLineages = processTaskLineageMapper
                .queryWorkFlowLineageByCode(processDefinition.getCode());
        Assertions.assertNotNull(workFlowLineages);
    }

}
