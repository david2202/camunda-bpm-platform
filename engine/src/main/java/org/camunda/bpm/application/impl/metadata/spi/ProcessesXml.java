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
package org.camunda.bpm.application.impl.metadata.spi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.camunda.bpm.application.impl.metadata.ProcessArchiveXmlImpl;
import org.camunda.bpm.container.impl.metadata.spi.ProcessEngineXml;

/**
 * <p>Java API representation of the {@link ProcessesXml} Metadata.</p>
 *
 * @author Daniel Meyer
 *
 */
public interface ProcessesXml {

  /**
   * @return A {@link List} of {@link ProcessEngineXml} Metadata Items representing process engine configurations.
   */
  public List<ProcessEngineXml> getProcessEngines();

  /**
   * @return A {@link List} of {@link ProcessArchiveXml} Metadata Items representing process archive deployments.
   */
  public List<ProcessArchiveXml> getProcessArchives();

  /**
   * <p>Constant representing the empty processes.xml</p>
   */
  public final static ProcessesXml EMPTY_PROCESSES_XML = new ProcessesXml() {

    public List<ProcessEngineXml> getProcessEngines() {
      return Collections.emptyList();
    }

    public List<ProcessArchiveXml> getProcessArchives() {
      List<ProcessArchiveXml> processArchives = new ArrayList<ProcessArchiveXml>();

      // add single PA
      ProcessArchiveXmlImpl pa = new ProcessArchiveXmlImpl();
      processArchives.add(pa);

      pa.setProcessResourceNames(Collections.<String>emptyList());

      // with default properties
      HashMap<String, String> properties = new HashMap<String, String>();
      pa.setProperties(properties);
      properties.put(ProcessArchiveXml.PROP_IS_DELETE_UPON_UNDEPLOY, Boolean.FALSE.toString());
      properties.put(ProcessArchiveXml.PROP_IS_SCAN_FOR_PROCESS_DEFINITIONS, Boolean.TRUE.toString());

      return processArchives;
    }

  };

}
