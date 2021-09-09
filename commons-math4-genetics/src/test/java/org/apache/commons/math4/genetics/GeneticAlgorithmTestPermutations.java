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
package org.apache.commons.math4.genetics;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math4.genetics.model.Chromosome;
import org.apache.commons.math4.genetics.model.FitnessFunction;
import org.apache.commons.math4.genetics.model.ListPopulation;
import org.apache.commons.math4.genetics.model.Population;
import org.apache.commons.math4.genetics.model.RandomKey;
import org.apache.commons.math4.genetics.operators.FixedGenerationCount;
import org.apache.commons.math4.genetics.operators.OnePointCrossover;
import org.apache.commons.math4.genetics.operators.RandomKeyMutation;
import org.apache.commons.math4.genetics.operators.StoppingCondition;
import org.apache.commons.math4.genetics.operators.TournamentSelection;
import org.junit.Assert;
import org.junit.Test;

/**
 * This is also an example of usage.
 *
 * This algorithm does "stochastic sorting" of a sequence 0,...,N.
 *
 */
public class GeneticAlgorithmTestPermutations {

    // parameters for the GA
    private static final int DIMENSION = 20;
    private static final int POPULATION_SIZE = 80;
    private static final int NUM_GENERATIONS = 200;
    private static final double ELITISM_RATE = 0.2;
    private static final double CROSSOVER_RATE = 1;
    private static final double MUTATION_RATE = 0.08;
    private static final int TOURNAMENT_ARITY = 2;

    // numbers from 0 to N-1
    private static final List<Integer> sequence = new ArrayList<>();
    static {
        for (int i = 0; i < DIMENSION; i++) {
            sequence.add(i);
        }
    }

    @Test
    public void test() {
        // to test a stochastic algorithm is hard, so this will rather be an usage
        // example

        // initialize a new genetic algorithm
        GeneticAlgorithm ga = new GeneticAlgorithm(new OnePointCrossover<Integer>(), CROSSOVER_RATE,
                new RandomKeyMutation(), MUTATION_RATE, new TournamentSelection(TOURNAMENT_ARITY), ELITISM_RATE);

        // initial population
        Population initial = randomPopulation();
        // stopping conditions
        StoppingCondition stopCond = new FixedGenerationCount(NUM_GENERATIONS);

        // best initial chromosome
        Chromosome bestInitial = initial.getFittestChromosome();

        // run the algorithm
        Population finalPopulation = ga.evolve(initial, stopCond);

        // best chromosome from the final population
        Chromosome bestFinal = finalPopulation.getFittestChromosome();

        // the only thing we can test is whether the final solution is not worse than
        // the initial one
        // however, for some implementations of GA, this need not be true :)

        Assert.assertTrue(bestFinal.compareTo(bestInitial) > 0);

    }

    /**
     * Initializes a random population
     */
    private static Population randomPopulation() {
        List<Chromosome> popList = new ArrayList<>();
        for (int i = 0; i < POPULATION_SIZE; i++) {
            Chromosome randChrom = new MinPermutations(RandomKey.randomPermutation(DIMENSION));
            popList.add(randChrom);
        }
        return new ListPopulation(popList, popList.size());
    }

    /**
     * Chromosomes representing a permutation of (0,1,2,...,DIMENSION-1).
     *
     * The goal is to sort the sequence.
     */
    private static class MinPermutations extends RandomKey<Integer> {

        MinPermutations(List<Double> representation) {
            super(representation, new MinPermutationsFitnessFunction());
        }

        @Override
        public RandomKey<Integer> newFixedLengthChromosome(List<Double> chromosomeRepresentation) {
            return new MinPermutations(chromosomeRepresentation);
        }

    }

    private static class MinPermutationsFitnessFunction implements FitnessFunction {

        @Override
        public double compute(Chromosome chromosome) {
            // RandomKey<Integer> minPermutationsChromosome = (RandomKey<Integer>)
            // chromosome;
            MinPermutations minPermutationsChromosome = (MinPermutations) chromosome;
            int res = 0;
            List<Integer> decoded = minPermutationsChromosome.decode(sequence);
            for (int i = 0; i < decoded.size(); i++) {
                int value = decoded.get(i);
                if (value != i) {
                    // bad position found
                    res += Math.abs(value - i);
                }
            }
            // the most fitted chromosome is the one with minimal error
            // therefore we must return negative value
            return -res;
        }

    }

}
