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

package org.apache.commons.math4.genetics.operators;

import org.apache.commons.math4.genetics.exception.GeneticException;
import org.apache.commons.math4.genetics.model.BinaryChromosome;
import org.apache.commons.math4.genetics.model.Chromosome;
import org.junit.Test;

public class AbstractListChromosomeCrossoverPolicyTest {

    @Test(expected = GeneticException.class)
    public void testCrossoverDimensionMismatchException() {
        final Integer[] p1 = new Integer[] {1, 0, 1, 0, 0, 1, 0, 1, 1};
        final Integer[] p2 = new Integer[] {0, 1, 1, 0, 1};

        final BinaryChromosome p1c = new BinaryChromosome(p1, chromosome -> {
            return 0;
        });
        final BinaryChromosome p2c = new BinaryChromosome(p2, chromosome -> {
            return 0;
        });

        final CrossoverPolicy cp = new CycleCrossover<Integer>();

        cp.crossover(p1c, p2c, 1.0);
    }

    @Test(expected = GeneticException.class)
    public void testCrossoverInvalidFixedLengthChromosomeFirst() {
        final Integer[] p1 = new Integer[] {1, 0, 1, 0, 0, 1, 0, 1, 1};
        final BinaryChromosome p1c = new BinaryChromosome(p1, chromosome -> {
            return 0;
        });
        final Chromosome p2c = new Chromosome(chromosome -> {
            return 0;
        }) {
        };

        final CrossoverPolicy cp = new CycleCrossover<Integer>();
        cp.crossover(p1c, p2c, 1.0);
    }

    @Test(expected = GeneticException.class)
    public void testCrossoverInvalidFixedLengthChromosomeSecond() {
        final Integer[] p1 = new Integer[] {1, 0, 1, 0, 0, 1, 0, 1, 1};
        final BinaryChromosome p2c = new BinaryChromosome(p1, chromosome -> {
            return 0;
        });
        final Chromosome p1c = new Chromosome(chromosome -> {
            return 0;
        }) {
        };

        final CrossoverPolicy cp = new CycleCrossover<Integer>();
        cp.crossover(p1c, p2c, 1.0);
    }

}
