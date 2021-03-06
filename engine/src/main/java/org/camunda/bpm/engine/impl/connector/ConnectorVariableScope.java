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
package org.camunda.bpm.engine.impl.connector;

import java.util.Map;
import java.util.Map.Entry;

import org.camunda.bpm.connect.ConnectorRequest;
import org.camunda.bpm.connect.ConnectorResponse;
import org.camunda.bpm.engine.impl.core.variable.CoreVariableInstance;
import org.camunda.bpm.engine.impl.core.variable.CoreVariableScope;
import org.camunda.bpm.engine.impl.core.variable.CoreVariableStore;

/**
 * Exposes a connector request as variableScope.
 *
 * @author Daniel Meyer
 *
 */
public class ConnectorVariableScope extends CoreVariableScope {

  private static final long serialVersionUID = 1L;

  protected CoreVariableScope parent;

  protected ConnectorVariableStore variableStore;

  public ConnectorVariableScope(CoreVariableScope parent) {
    this.parent = parent;
    this.variableStore = new ConnectorVariableStore();
  }

  protected CoreVariableStore getVariableStore() {
    return variableStore;
  }

  public CoreVariableScope getParentVariableScope() {
    return parent;
  }

  public void writeToRequest(ConnectorRequest<?> request) {
    for (CoreVariableInstance variable : variableStore.getVariableInstancesValues()) {
      request.setRequestParameter(variable.getName(), variable.getValue());
    }
  }

  public void readFromResponse(ConnectorResponse response) {
    Map<String, Object> responseParameters = response.getResponseParameters();
    for (Entry<String, Object> entry : responseParameters.entrySet()) {
      setVariableLocal(entry.getKey(), entry.getValue());
    }
  }

}
