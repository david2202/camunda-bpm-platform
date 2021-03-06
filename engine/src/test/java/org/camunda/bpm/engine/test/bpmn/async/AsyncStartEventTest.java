/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.camunda.bpm.engine.test.bpmn.async;

import org.camunda.bpm.engine.history.HistoricActivityInstance;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.test.PluggableProcessEngineTestCase;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.test.Deployment;
import org.junit.Assert;

public class AsyncStartEventTest extends PluggableProcessEngineTestCase {

  @Deployment
  public void testAsyncStartEvent() {
    runtimeService.startProcessInstanceByKey("asyncStartEvent");

    Task task = taskService.createTaskQuery().singleResult();
    Assert.assertNull("The user task should not have been reached yet", task);

    Assert.assertEquals(1, runtimeService.createExecutionQuery().activityId("startEvent").count());

    executeAvailableJobs();
    task = taskService.createTaskQuery().singleResult();

    Assert.assertEquals(0, runtimeService.createExecutionQuery().activityId("startEvent").count());

    Assert.assertNotNull("The user task should have been reached", task);
  }

  @Deployment
  public void testAsyncStartEventListeners() {
    ProcessInstance instance = runtimeService.startProcessInstanceByKey("asyncStartEvent");

    Assert.assertNull(runtimeService.getVariable(instance.getId(), "listener"));

    executeAvailableJobs();

    Assert.assertNotNull(runtimeService.getVariable(instance.getId(), "listener"));
  }

  @Deployment(resources = "org/camunda/bpm/engine/test/bpmn/async/AsyncStartEventTest.testAsyncStartEvent.bpmn20.xml")
  public void testAsyncStartEventHistory() {
    if(processEngineConfiguration.getHistoryLevel() > ProcessEngineConfigurationImpl.HISTORYLEVEL_NONE) {
      runtimeService.startProcessInstanceByKey("asyncStartEvent");

      HistoricProcessInstance historicInstance = historyService.createHistoricProcessInstanceQuery().singleResult();
      Assert.assertNotNull(historicInstance);
      Assert.assertNotNull(historicInstance.getStartTime());

      HistoricActivityInstance historicStartEvent = historyService.createHistoricActivityInstanceQuery().singleResult();
      Assert.assertNull(historicStartEvent);
    }
  }
}
