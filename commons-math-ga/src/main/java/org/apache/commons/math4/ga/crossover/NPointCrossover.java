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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math4.ga.chromosome.AbstractListChromosome;
import org.apache.commons.math4.ga.chromosome.ChromosomePair;
import org.apache.commons.math4.ga.internal.exception.GeneticException;
import org.apache.commons.math4.ga.utils.RandomProviderManager;
import org.apache.commons.rng.UniformRandomProvider;

/**
 * N-point crossover policy. For each iteration a random crossover point is
 * selected and the first part from each parent is copied to the corresponding
 * child, and the second parts are copied crosswise.
 *
 * Example (2-point crossover):
 * <pre>
 * -C- denotes a crossover point
 *           -C-       -C-                         -C-        -C-
 * p1 = (1 0  | 1 0 0 1 | 0 1 1)    X    p2 = (0 1  | 1 0 1 0  | 1 1 1)
 *      \----/ \-------/ \-----/              \----/ \--------/ \-----/
 *        ||      (*)       ||                  ||      (**)       ||
 *        VV      (**)      VV                  VV      (*)        VV
 *      /----\ /--------\ /-----\             /----\ /--------\ /-----\
 * c1 = (1 0  | 1 0 1 0  | 0 1 1)    X   c2 = (0 1  | 1 0 0 1  | 0 1 1)
 * </pre>
 *
 * This policy works only on {@link AbstractListChromosome}, and therefore it is
 * parameterized by T. Moreover, the chromosomes must have same lengths.
 *
 * @param <T> generic type of the {@link AbstractListChromosome}s for crossover
 * @param <P> phenotype of chromosome
 * @since 3.1
 */
public class NPointCrossover<T, P> extends AbstractListChromosomeCrossoverPolicy<T, P> {

    /** The number of crossover points. */
    private final int crossoverPoints;

    /**
     * Creates a new {@link NPointCrossover} policy using the given number of
     * points.
     * <p>
     * <b>Note</b>: the number of crossover points must be &lt;
     * <code>chromosome length - 1</code>. This condition can only be checked at
     * runtime, as the chromosome length is not known in advance.
     *
     * @param crossoverPoints the number of crossover points
     */
    public NPointCrossover(final int crossoverPoints) {
        if (crossoverPoints <= 0) {
            throw new GeneticException(GeneticException.NOT_STRICTLY_POSITIVE, crossoverPoints);
        }
        this.crossoverPoints = crossoverPoints;
    }

    /**
     * Returns the number of crossover points used by this {@link CrossoverPolicy}.
     *
     * @return the number of crossover points
     */
    public int getCrossoverPoints() {
        return crossoverPoints;
    }

    /**
     * Performs a N-point crossover. N random crossover points are selected and are
     * used to divide the parent chromosomes into segments. The segments are copied
     * in alternate order from the two parents to the corresponding child
     * chromosomes.
     *
     * Example (2-point crossover):
     * <pre>
     * -C- denotes a crossover point
     *           -C-       -C-                         -C-        -C-
     * p1 = (1 0  | 1 0 0 1 | 0 1 1)    X    p2 = (0 1  | 1 0 1 0  | 1 1 1)
     *      \----/ \-------/ \-----/              \----/ \--------/ \-----/
     *        ||      (*)       ||                  ||      (**)       ||
     *        VV      (**)      VV                  VV      (*)        VV
     *      /----\ /--------\ /-----\             /----\ /--------\ /-----\
     * c1 = (1 0  | 1 0 1 0  | 0 1 1)    X   c2 = (0 1  | 1 0 0 1  | 0 1 1)
     * </pre>
     *
     * @param first  first parent (p1)
     * @param second second parent (p2)
     * @return pair of two children (c1,c2)
     */
    @Override
    protected ChromosomePair<P> mate(final AbstractListChromosome<T, P> first,
            final AbstractListChromosome<T, P> second) {

        final int length = first.getLength();
        if (crossoverPoints >= length) {
            throw new GeneticException(GeneticException.TOO_LARGE, crossoverPoints, length);
        }

        // array representations of the parents
        final List<T> parent1Rep = first.getRepresentation();
        final List<T> parent2Rep = second.getRepresentation();
        // and of the children
        final List<T> child1Rep = new ArrayList<>(length);
        final List<T> child2Rep = new ArrayList<>(length);

        final UniformRandomProvider random = RandomProviderManager.getRandomProvider();

        List<T> c1 = child1Rep;
        List<T> c2 = child2Rep;

        int remainingPoints = crossoverPoints;
        int lastIndex = 0;
        for (int i = 0; i < crossoverPoints; i++, remainingPoints--) {
            // select the next crossover point at random
            final int crossoverIndex = 1 + lastIndex + random.nextInt(length - lastIndex - remainingPoints);

            // copy the current segment
            for (int j = lastIndex; j < crossoverIndex; j++) {
                c1.add(parent1Rep.get(j));
                c2.add(parent2Rep.get(j));
            }

            // swap the children for the next segment
            final List<T> tmp = c1;
            c1 = c2;
            c2 = tmp;

            lastIndex = crossoverIndex;
        }

        // copy the last segment
        for (int j = lastIndex; j < length; j++) {
            c1.add(parent1Rep.get(j));
            c2.add(parent2Rep.get(j));
        }

        return new ChromosomePair<>(first.newChromosome(child1Rep), second.newChromosome(child2Rep));
    }
}
