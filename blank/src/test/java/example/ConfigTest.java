/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package example;

import org.apache.struts2.ActionSupport;
import org.apache.struts2.config.RuntimeConfiguration;
import org.apache.struts2.config.StrutsXmlConfigurationProvider;
import org.apache.struts2.config.entities.ActionConfig;
import org.apache.struts2.config.entities.ResultConfig;
import org.apache.struts2.config.providers.XmlConfigurationProvider;
import org.apache.struts2.junit.StrutsTestCase;

import java.util.List;
import java.util.Map;

public class ConfigTest extends StrutsTestCase {

    protected void assertSuccess(String result) {
        assertEquals("Expected a success result!", ActionSupport.SUCCESS, result);
    }

    protected void assertInput(String result) {
        assertEquals("Expected an input result!", ActionSupport.INPUT, result);
    }

    protected Map<String, List<String>> assertFieldErrors(ActionSupport action) {
        assertTrue(action.hasFieldErrors());
        return action.getFieldErrors();
    }

    protected void assertFieldError(Map<String, List<String>> field_errors, String field_name, String error_message) {
        List<String> errors = field_errors.get(field_name);
        assertNotNull("Expected errors for " + field_name, errors);
        assertTrue("Expected errors for " + field_name, errors.size()>0);
        // TODO: Should be a loop
        assertEquals(error_message,errors.get(0));
    }

    protected void setUp() throws Exception {
        super.setUp();
        XmlConfigurationProvider c = new StrutsXmlConfigurationProvider("struts.xml");
        configurationManager.addContainerProvider(c);
        configurationManager.reload();
    }

    protected ActionConfig assertClass(String namespace, String action_name, String class_name) {
        RuntimeConfiguration configuration = configurationManager.getConfiguration().getRuntimeConfiguration();
        ActionConfig config = configuration.getActionConfig(namespace, action_name);
        assertNotNull("Mssing action", config);
        assertEquals("Wrong class name: [" + config.getClassName() + "]", class_name, config.getClassName());
        return config;
    }

    protected void assertResult(ActionConfig config, String result_name, String result_value) {
        Map<String, ResultConfig> results = config.getResults();
        ResultConfig result = results.get(result_name);
        Map<String, String> params = result.getParams();
        String value = params.get("actionName");
        if (value == null) {
            value = params.get("location");
        }
        assertEquals("Wrong result value: [" + value + "]", result_value, value);
    }

    public void testConfig() {
        assertNotNull(configurationManager);
    }

}
