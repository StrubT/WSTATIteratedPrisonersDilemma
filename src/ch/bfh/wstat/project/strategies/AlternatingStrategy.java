package ch.bfh.wstat.project.strategies;

import ch.bfh.wstat.project.Move;
import ch.bfh.wstat.project.Player;
import ch.bfh.wstat.project.Strategy;
import ch.bfh.wstat.project.annotation.Param;

/**
 * Alternating strategy. <br>
 * Co-operate with a probability {@code p_CC} if the player also co-operated in the last round and with a probability {@code p_CD} if he deceived.
 *
 * @author strut1 &amp; weidj1
 */
public class AlternatingStrategy extends Strategy {

	/**
	 * Standard probability to co-operate in the first round.
	 */
	public static final double STANDARD_PROBABILITY_INITAL = .5;

	/**
	 * Standard probability {@code p_CC} to co-operate if the player co-operated in last round as well.
	 */
	public static final double STANDARD_PROBABILITY_COOPERATED = .4;

	/**
	 * Standard probability {@code p_CD} to co-operate if the player deceived in last round.
	 */
	public static final double STANDARD_PROBABILITY_DECEIVED = .65;

	private double probabilityInitial, probabilityCooperated, probabilityDeceived;

	/**
	 * Construct a new alternating strategy.
	 */
	public AlternatingStrategy() {
		this(STANDARD_PROBABILITY_INITAL, STANDARD_PROBABILITY_COOPERATED, STANDARD_PROBABILITY_DECEIVED);
	}

	/**
	 * Construct a new alternating strategy.
	 *
	 * @param probabilityCooperated probability {@code p_CC} to co-operate if the player co-operated in last round as well
	 * @param probabilityDeceived   probability {@code p_CD} to co-operate if the player deceived in last round
	 */
	public AlternatingStrategy(@Param(name = "probability (co-operated)") double probabilityCooperated,
														 @Param(name = "probability (deceived)") double probabilityDeceived) {
		this(STANDARD_PROBABILITY_INITAL, probabilityCooperated, probabilityDeceived);
	}

	/**
	 * Construct a new alternating strategy.
	 *
	 * @param probabilityInitial    probability to co-operate in the first round
	 * @param probabilityCooperated probability {@code p_CC} to co-operate if the player co-operated in last round as well
	 * @param probabilityDeceived   probability {@code p_CD} to co-operate if the player deceived in last round
	 */
	public AlternatingStrategy(@Param(name = "probability (initial)") double probabilityInitial,
														 @Param(name = "probability (co-operated)") double probabilityCooperated,
														 @Param(name = "probability (deceived)") double probabilityDeceived) {

		this.probabilityInitial = probabilityInitial;
		this.probabilityCooperated = probabilityCooperated;
		this.probabilityDeceived = probabilityDeceived;
	}

	/**
	 * Get the probability to co-operate in the first round.
	 *
	 * @return probability to co-operate in the first round
	 */
	public double getProbabilityInitial() {
		return this.probabilityInitial;
	}

	/**
	 * Set the probability to co-operate in the first round.
	 *
	 * @param probabilityInitial probability to co-operate in the first round
	 */
	public void setProbabilityInitial(double probabilityInitial) {
		this.probabilityInitial = probabilityInitial;
	}

	/**
	 * Get the probability {@code p_CC} to co-operate if the player co-operated in last round as well.
	 *
	 * @return probability {@code p_CC} to co-operate if the player co-operated in last round as well
	 */
	public double getProbabilityCooperated() {
		return this.probabilityCooperated;
	}

	/**
	 * Set the probability {@code p_CC} to co-operate if the player co-operated in last round as well.
	 *
	 * @param probabilityCooperated probability {@code p_CC} to co-operate if the player co-operated in last round as well
	 */
	public void setProbabilityCooperated(double probabilityCooperated) {
		this.probabilityCooperated = probabilityCooperated;
	}

	/**
	 * Get the probability {@code p_CD} to co-operate if the player deceived in last round.
	 *
	 * @return probability {@code p_CD} to co-operate if the player deceived in last round
	 */
	public double getProbabilityDeceived() {
		return this.probabilityDeceived;
	}

	/**
	 * Set the probability {@code p_CD} to co-operate if the player deceived in last round.
	 *
	 * @param probabilityDeceived probability {@code p_CD} to co-operate if the player deceived in last round
	 */
	public void setProbabilityDeceived(double probabilityDeceived) {
		this.probabilityDeceived = probabilityDeceived;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Move determineNextMove(Player currentPlayer, Player otherPlayer) {

		double p;
		if (currentPlayer.getNumberOfRounds() == 0)
			p = this.probabilityInitial;
		else
			p = currentPlayer.getRound(-1).getMove() == Move.COOPERATE ? this.probabilityCooperated : this.probabilityDeceived;

		return random.nextDouble() < p ? Move.COOPERATE : Move.DECEIVE;
	}
}
