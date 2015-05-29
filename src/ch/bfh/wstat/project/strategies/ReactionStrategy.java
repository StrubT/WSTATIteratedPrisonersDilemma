package ch.bfh.wstat.project.strategies;

import ch.bfh.wstat.project.Move;
import ch.bfh.wstat.project.Player;
import ch.bfh.wstat.project.Strategy;
import ch.bfh.wstat.project.annotation.Param;

/**
 * Reaction strategy. <br>
 * Co-operate with a probability {@code p_CC} if the other player co-operated in the last round and with a probability {@code p_CD} if he deceived.
 *
 * @author strut1 &amp; weidj1
 */
public class ReactionStrategy extends Strategy {

	/**
	 * Standard probability to co-operate in the first round.
	 */
	public static final double STANDARD_PROBABILITY_INITAL = .5;

	/**
	 * Standard probability {@code p_CC} to co-operate if the other player co-operated in last round.
	 */
	public static final double STANDARD_PROBABILITY_COOPERATED = .6;

	/**
	 * Standard probability {@code p_CD} to co-operate if the other player deceived in last round.
	 */
	public static final double STANDARD_PROBABILITY_DECEIVED = .35;

	private double probabilityInitial, probabilityCooperated, probabilityDeceived;

	/**
	 * Construct a new reaction strategy.
	 */
	public ReactionStrategy() {
		this(STANDARD_PROBABILITY_INITAL, STANDARD_PROBABILITY_COOPERATED, STANDARD_PROBABILITY_DECEIVED);
	}

	/**
	 * Construct a new reaction strategy.
	 *
	 * @param probabilityCooperated probability {@code p_CC} to co-operate if the other player co-operated in last round
	 * @param probabilityDeceived   probability {@code p_CD} to co-operate if the other player deceived in last round
	 */
	public ReactionStrategy(@Param(name = "probability (co-operated)") double probabilityCooperated,
													@Param(name = "probability (deceived)") double probabilityDeceived) {
		this(STANDARD_PROBABILITY_INITAL, probabilityCooperated, probabilityDeceived);
	}

	/**
	 * Construct a new reaction strategy.
	 *
	 * @param probabilityInitial    probability to co-operate in the first round
	 * @param probabilityCooperated probability {@code p_CC} to co-operate if the other player co-operated in last round
	 * @param probabilityDeceived   probability {@code p_CD} to co-operate if the other player deceived in last round
	 */
	public ReactionStrategy(@Param(name = "probability (initial)") double probabilityInitial,
													@Param(name = "probability (co-operated)") double probabilityCooperated,
													@Param(name = "probability (deceived)") double probabilityDeceived) {

		this.probabilityInitial = probabilityInitial;
		this.probabilityCooperated = probabilityCooperated;
		this.probabilityDeceived = probabilityDeceived;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Move determineNextMove(Player currentPlayer, Player otherPlayer) {

		double p;
		if (otherPlayer.getNumberOfRounds() == 0)
			p = this.probabilityInitial;
		else
			p = otherPlayer.getRound(-1).getMove() == Move.COOPERATE ? this.probabilityCooperated : this.probabilityDeceived;

		return random.nextDouble() < p ? Move.COOPERATE : Move.DECEIVE;
	}
}
