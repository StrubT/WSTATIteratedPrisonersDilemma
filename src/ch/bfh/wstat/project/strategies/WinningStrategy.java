package ch.bfh.wstat.project.strategies;

import ch.bfh.wstat.project.Move;
import ch.bfh.wstat.project.Player;
import ch.bfh.wstat.project.Strategy;
import ch.bfh.wstat.project.annotation.Param;

/**
 * Winning strategy. <br>
 * Repeat the same move with a probability {@code p_R} if the player won more than the other player in the last round.
 *
 * @author strut1 &amp; weidj1
 */
public class WinningStrategy extends Strategy {

	/**
	 * Standard probability to co-operate if the player won less than the other player in last round.
	 */
	public static final double STANDARD_PROBABILITY_DEFAULT = .5;

	/**
	 * Standard probability {@code p_R} to repeat the last action if the player won more than the other player in last round.
	 */
	public static final double STANDARD_PROBABILITY_WON = .6;

	private double probabilityDefault, probabilityWon;

	/**
	 * Construct a new winning strategy.
	 *
	 */
	public WinningStrategy() {
		this(STANDARD_PROBABILITY_DEFAULT, STANDARD_PROBABILITY_WON);
	}

	/**
	 * Construct a new winning strategy.
	 *
	 * @param probabilityWon probability {@code p_R} to repeat the last action if the player won more than the other player in last round
	 */
	public WinningStrategy(@Param(name = "probability (won)") double probabilityWon) {
		this(STANDARD_PROBABILITY_DEFAULT, probabilityWon);
	}

	/**
	 * Construct a new winning strategy.
	 *
	 * @param probabilityDefault probability to co-operate if the player won less than the other player in last round
	 * @param probabilityWon     probability {@code p_R} to repeat the last action if the player won more than the other player in last round
	 */
	public WinningStrategy(@Param(name = "probability (default)") double probabilityDefault,
												 @Param(name = "probability (won)") double probabilityWon) {

		this.probabilityDefault = probabilityDefault;
		this.probabilityWon = probabilityWon;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Move determineNextMove(Player currentPlayer, Player otherPlayer) {

		double p;
		if (currentPlayer.getNumberOfRounds() > 0 && otherPlayer.getNumberOfRounds() > 0
				&& currentPlayer.getRound(-1).getGain().compareTo(otherPlayer.getRound(-1).getGain()) > 0)
			p = currentPlayer.getRound(-1).getMove() == Move.COOPERATE ? this.probabilityWon : 1. - this.probabilityWon;
		else
			p = this.probabilityDefault;

		return random.nextDouble() < p ? Move.COOPERATE : Move.DECEIVE;
	}
}
