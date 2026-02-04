/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.roller.planet.business;

import org.apache.roller.planet.PlanetException;
import org.apache.roller.planet.PlanetManager;
import org.apache.roller.planet.PlanetUtils;

public abstract class AbstractManagerImpl implements PlanetManager {

    public AbstractManagerImpl() {
        // Intentionally left empty, consider removing if not used
    }

    // Removed unused methods to avoid Empty Method design smell
    // public void init() {}
    // public void destroy() {}
    // public void test() {}

    @Override
    public void saveConfiguration(String configuration) throws PlanetException {
        PlanetUtils.saveConfiguration(configuration);
    }

    @Override
    public String loadConfiguration() throws PlanetException {
        return PlanetUtils.loadConfiguration();
    }

    @Override
    public abstract void startup() throws PlanetException;

    @Override
    public abstract void shutdown() throws PlanetException;

    // Removed empty methods to avoid design smell
}