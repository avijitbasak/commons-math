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
package org.apache.commons.math4.ga.crossover;

import org.apache.commons.math4.ga.chromosome.ChromosomePair;
import org.apache.commons.math4.ga.chromosome.IntegralValuedChromosome;
import org.apache.commons.math4.ga.dummy.DummyListChromosomeDecoder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class OnePointCrossoverTest {

    @Test
    public void testCrossover() {
        @SuppressWarnings("boxing")
        Integer[] p1 = new Integer[] {1, 0, 1, 0, 0, 1, 0, 1, 1};
        @SuppressWarnings("boxing")
        Integer[] p2 = new Integer[] {0, 1, 1, 0, 1, 0, 1, 1, 1};

        IntegralValuedChromosome<String> p1c = new IntegralValuedChromosome<>(p1, chromosome -> 0,
                new DummyListChromosomeDecoder<>("0"), 0, 2);
        IntegralValuedChromosome<String> p2c = new IntegralValuedChromosome<>(p2, chromosome -> 0,
                new DummyListChromosomeDecoder<>("0"), 0, 2);

        OnePointCrossover<Integer, String> opc = new OnePointCrossover<>();

        // how to test a stochastic method?
        for (int i = 0; i < 20; i++) {
            ChromosomePair<String> pair = opc.crossover(p1c, p2c, 1.0);

            Integer[] c1 = new Integer[p1.length];
            Integer[] c2 = new Integer[p2.length];

            c1 = ((IntegralValuedChromosome<String>) pair.getFirst()).getRepresentation().toArray(c1);
            c2 = ((IntegralValuedChromosome<String>) pair.getSecond()).getRepresentation().toArray(c2);

            // first and last values will be the same
            Assertions.assertEquals(p1[0], c1[0]);
            Assertions.assertEquals(p2[0], c2[0]);
            Assertions.assertEquals(p1[p1.length - 1], c1[c1.length - 1]);
            Assertions.assertEquals(p2[p2.length - 1], c2[c2.length - 1]);
            // moreover, in the above setting, the 2nd, 3rd and 7th values will be the same
            Assertions.assertEquals(p1[2], c1[2]);
            Assertions.assertEquals(p2[2], c2[2]);
            Assertions.assertEquals(p1[3], c1[3]);
            Assertions.assertEquals(p2[3], c2[3]);
            Assertions.assertEquals(p1[7], c1[7]);
            Assertions.assertEquals(p2[7], c2[7]);
        }
    }

}
