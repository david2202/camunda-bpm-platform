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
package org.camunda.bpm.engine.impl.pvm.runtime.operation;

import org.camunda.bpm.engine.impl.core.mapping.IoMapping;
import org.camunda.bpm.engine.impl.pvm.delegate.CompositeActivityBehavior;
import org.camunda.bpm.engine.impl.pvm.process.ActivityImpl;
import org.camunda.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;


/**
 * <p>Base Atomic operation for implementing atomic operations which mark the creation
 * of a new activity instance.</p>
 *
 * <p>The new activity instance is created *before* the START listeners are invoked
 * on the execution.</p>
 *
 * @author Daniel Meyer
 *
 */
public abstract class PvmAtomicOperationActivityInstanceStart extends AbstractPvmEventAtomicOperation {

  @Override
  protected PvmExecutionImpl eventNotificationsStarted(PvmExecutionImpl execution) {
    execution.enterActivityInstance();

    // execute Input Mappings (if they exist).
    IoMapping ioMapping = getScope(execution).getIoMapping();
    if(ioMapping != null) {
      ioMapping.executeInputParameters(execution);
    }

    return execution;
  }

  protected void eventNotificationsCompleted(PvmExecutionImpl execution) {

    // hack around execution tree structure not being in sync with activity instance concept:
    // if we start a scope activity, remember current activity instance in parent
    PvmExecutionImpl parent = execution.getParent();
    ActivityImpl activity = execution.getActivity();
    if(parent != null && execution.isScope() && activity.isScope() && (activity.getActivityBehavior() instanceof CompositeActivityBehavior)) {
      parent.setActivityInstanceId(execution.getActivityInstanceId());
    }

  }

}
