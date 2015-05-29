package ch.bfh.wstat.project.strategies;

import ch.bfh.wstat.project.Move;
import ch.bfh.wstat.project.Player;
import ch.bfh.wstat.project.Strategy;
import ch.bfh.wstat.project.annotation.Param;

/**
 * Probability strategy. <br>
 * Co-operate with a probability {@code p}.
 *
 * @author strut1 &amp; weidj1
 */
public class ProbabilityStrategy extends Strategy {

	/**
	 * Standard probability {@code p} to co-operate.
	 */
	public static final double STANDARD_PROBABILITY = .4;

	private double probability;

	/**
	 * Construct a new probability strategy.
	 *
	 * @param probability probability to co-operate
	 */
	public ProbabilityStrategy(@Param(name = "probability") double probability) {

		this.probability = probability;
	}

	/**
	 * Get the probability to co-operate.
	 *
	 * @return probability to co-operate
	 */
	public double getProbability() {
		return this.probability;
	}

	/**
	 * Set the probability to co-operate.
	 *
	 * @param probability probability to co-operate
	 */
	public void setProbability(double probability) {
		this.probability = probability;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Move determineNextMove(Player currentPlayer, Player otherPlayer) {
		return random.nextDouble() < this.probability ? Move.COOPERATE : Move.DECEIVE;
	}
}
