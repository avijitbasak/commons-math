/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.math4.ga.listener;

import org.apache.commons.math4.ga.population.Population;

/**
 * This interface represents a convergence listener. Any implementation of the
 * same will be notified about the population statics.
 * @param <P> phenotype of chromosome
 * @since 4.0
 */
public interface ConvergenceListener<P> {

    /**
     * Notifies about the population statistics.
     * @param generation current generation
     * @param population population of chromosome
     */
    void notify(int generation, Population<P> population);

}
