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

import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import org.apache.commons.math4.genetics.model.Chromosome;
import org.apache.commons.math4.genetics.model.Population;
import org.apache.commons.math4.genetics.operators.FixedElapsedTime;
import org.junit.Assert;
import org.junit.Test;

public class FixedElapsedTimeTest {

	@Test
	public void testIsSatisfied() {
		final Population pop = new Population() {

			@Override
			public Iterator<Chromosome> iterator() {
				return null;
			}

			@Override
			public Population nextGeneration(double elitismRate) {
				return null;
			}

			@Override
			public int getPopulationSize() {
				return 0;
			}

			@Override
			public int getPopulationLimit() {
				return 0;
			}

			@Override
			public Chromosome getFittestChromosome() {
				return null;
			}

			@Override
			public void addChromosome(Chromosome chromosome) {

			}
		};

		final long start = System.nanoTime();
		final long duration = 3;
		final FixedElapsedTime tec = new FixedElapsedTime(duration, TimeUnit.SECONDS);

		while (!tec.isSatisfied(pop)) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// ignore
			}
		}

		final long end = System.nanoTime();
		final long elapsedTime = end - start;
		final long diff = Math.abs(elapsedTime - TimeUnit.SECONDS.toNanos(duration));

		Assert.assertTrue(diff < TimeUnit.MILLISECONDS.toNanos(100));
	}
}
