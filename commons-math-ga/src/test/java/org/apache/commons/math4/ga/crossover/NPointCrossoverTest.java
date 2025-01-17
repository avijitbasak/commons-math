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

import java.util.List;

import org.apache.commons.math4.ga.chromosome.AbstractChromosome;
import org.apache.commons.math4.ga.chromosome.ChromosomePair;
import org.apache.commons.math4.ga.chromosome.IntegralValuedChromosome;
import org.apache.commons.math4.ga.dummy.DummyListChromosomeDecoder;
import org.apache.commons.math4.ga.internal.exception.GeneticException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class NPointCrossoverTest {

    @Test
    public void testNumberIsTooLargeException() {
        final Integer[] p1 = new Integer[] {1, 0, 1, 0, 0, 1, 0, 1, 1};
        final Integer[] p2 = new Integer[] {0, 1, 1, 0, 1, 0, 1, 1, 1};

        final IntegralValuedChromosome<String> p1c = new IntegralValuedChromosome<String>(p1, c -> 0,
                new DummyListChromosomeDecoder<Integer>("0"), 0, 2);
        final IntegralValuedChromosome<String> p2c = new IntegralValuedChromosome<String>(p2, c -> 0,
                new DummyListChromosomeDecoder<Integer>("0"), 0, 2);

        final CrossoverPolicy<String> cp = new NPointCrossover<Integer, String>(15);
        Assertions.assertThrows(GeneticException.class, () -> {
            cp.crossover(p1c, p2c, 1.0);
        });
    }

    @Test
    public void testCrossoverInvalidFixedLengthChromosomeFirst() {
        final Integer[] p1 = new Integer[] {1, 0, 1, 0, 0, 1, 0, 1, 1};
        final IntegralValuedChromosome<String> p1c = new IntegralValuedChromosome<String>(p1, chromosome -> 0,
                new DummyListChromosomeDecoder<>("0"), 0, 2);
        final AbstractChromosome<String> p2c = new AbstractChromosome<String>(chromosome -> 0,
                new DummyListChromosomeDecoder<>("0")) {
        };

        final CrossoverPolicy<String> cp = new NPointCrossover<Integer, String>(1);

        Assertions.assertThrows(GeneticException.class, () -> {
            cp.crossover(p1c, p2c, 1.0);
        });

    }

    @Test
    public void testCrossoverInvalidFixedLengthChromosomeSecond() {
        final Integer[] p1 = new Integer[] {1, 0, 1, 0, 0, 1, 0, 1, 1};
        final IntegralValuedChromosome<String> p2c = new IntegralValuedChromosome<String>(p1, chromosome -> 0,
                new DummyListChromosomeDecoder<>("0"), 0, 2);
        final AbstractChromosome<String> p1c = new AbstractChromosome<String>(chromosome -> 0,
                new DummyListChromosomeDecoder<>("0")) {
        };

        final CrossoverPolicy<String> cp = new NPointCrossover<Integer, String>(1);

        Assertions.assertThrows(GeneticException.class, () -> {
            cp.crossover(p1c, p2c, 1.0);
        });

    }

    @Test
    public void testCrossover() {
        Integer[] p1 = new Integer[] {1, 0, 1, 0, 1, 0, 1, 0, 1};
        Integer[] p2 = new Integer[] {0, 1, 0, 1, 0, 1, 0, 1, 0};

        IntegralValuedChromosome<String> p1c = new IntegralValuedChromosome<>(p1, chromosome -> 0,
                new DummyListChromosomeDecoder<>("0"), 0, 2);
        IntegralValuedChromosome<String> p2c = new IntegralValuedChromosome<>(p2, chromosome -> 0,
                new DummyListChromosomeDecoder<>("0"), 0, 2);

        final int order = 3;
        NPointCrossover<Integer, String> npc = new NPointCrossover<>(order);

        // the two parent chromosomes are different at each position, so it is easy to
        // detect
        // the number of crossovers that happened for each child
        for (int i = 0; i < 20; i++) {
            ChromosomePair<String> pair = npc.crossover(p1c, p2c, 1.0);
            Assertions.assertEquals(order,
                    detectCrossoverPoints(p1c, p2c, (IntegralValuedChromosome<String>) pair.getFirst()));
            Assertions.assertEquals(order,
                    detectCrossoverPoints(p2c, p1c, (IntegralValuedChromosome<String>) pair.getSecond()));
        }
    }

    private int detectCrossoverPoints(IntegralValuedChromosome<String> p1,
            IntegralValuedChromosome<String> p2,
            IntegralValuedChromosome<String> c) {
        int crossovers = 0;
        final int length = p1.getLength();

        final List<Integer> p1Rep = p1.getRepresentation();
        final List<Integer> p2Rep = p2.getRepresentation();
        final List<Integer> cRep = c.getRepresentation();

        List<Integer> rep = p1Rep;
        for (int i = 0; i < length; i++) {
            if (rep.get(i) != cRep.get(i)) {
                crossovers++;
                rep = rep == p1Rep ? p2Rep : p1Rep;
            }
        }

        return crossovers;
    }

}
