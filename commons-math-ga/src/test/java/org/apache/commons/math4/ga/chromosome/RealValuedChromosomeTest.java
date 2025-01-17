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
package org.apache.commons.math4.ga.chromosome;

import org.apache.commons.math4.ga.dummy.DummyListChromosomeDecoder;
import org.apache.commons.math4.ga.internal.exception.GeneticException;
import org.apache.commons.math4.ga.utils.ChromosomeRepresentationUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RealValuedChromosomeTest {

    @Test
    public void test() {
        for (int i = 0; i < 10; i++) {
            new RealValuedChromosome<>(ChromosomeRepresentationUtils.randomDoubleRepresentation(10, 0, 1), c1 -> 1,
                    new DummyListChromosomeDecoder<>("1"));
            new RealValuedChromosome<>(
                    ChromosomeRepresentationUtils.randomDoubleRepresentation(10, 0, 1).toArray(new Double[10]), c -> 0,
                    new DummyListChromosomeDecoder<>("0"));
        }
    }

    @Test
    public void testNewChromosome() {
        for (int i = 0; i < 10; i++) {
            RealValuedChromosome<String> chromosome = new RealValuedChromosome<>(
                    ChromosomeRepresentationUtils.randomDoubleRepresentation(10, 0, 1), c1 -> 1,
                    new DummyListChromosomeDecoder<>("1"));
            chromosome.newChromosome(ChromosomeRepresentationUtils.randomDoubleRepresentation(10, 0, 1));
        }
    }

    @Test
    public void testRandomChromosome() {
        for (int i = 0; i < 10; i++) {
            RealValuedChromosome.randomChromosome(5, c -> 0, new DummyListChromosomeDecoder<>("0"), 0, 2);
        }
    }

    @Test
    public void testCheckValidity() {
        int min = 0;
        int max = 10;
        Assertions.assertThrows(GeneticException.class, () -> {
            new RealValuedChromosome<>(ChromosomeRepresentationUtils.randomDoubleRepresentation(10, min, max), c -> 0,
                    new DummyListChromosomeDecoder<>("0"), max, min);
        });
    }

    @Test
    public void testCheckValidity1() {
        int min = 0;
        int max = 10;
        Assertions.assertThrows(GeneticException.class, () -> {
            new RealValuedChromosome<>(ChromosomeRepresentationUtils.randomDoubleRepresentation(10, min - 10, max + 10),
                c -> 0, new DummyListChromosomeDecoder<>("0"), min, max);
        });
    }

}
