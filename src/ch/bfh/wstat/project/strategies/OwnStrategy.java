package ch.bfh.wstat.project.strategies;

import java.math.BigDecimal;
import ch.bfh.wstat.project.Move;
import ch.bfh.wstat.project.Player;
import ch.bfh.wstat.project.Strategy;
import ch.bfh.wstat.project.annotation.Param;

/**
 * Our own strategy. <br>
 * Co-operate with a high probability if the player gained more than the competitor in the majority of the last rounds.
 *
 * @author strut1 &amp; weidj1
 */
public class OwnStrategy extends Strategy {

	/**
	 * Standard number of rounds to analyse.
	 */
	public static final int STANDARD_HISTORY_COUNT = 7;

	/**
	 * Standard threshold when to consider a (number of) round(s) to be won.
	 */
	public static final double STANDARD_THRESHOLD_WON = .53;

	/**
	 * Standard probability to co-operate if the player won more (often) than the other player.
	 */
	public static final double STANDARD_PROBABILITY_WON = .93;

	/**
	 * Standard probability to co-operate if the player won less (often) than the other player.
	 */
	public static final double STANDARD_PROBABILITY_LOST = .03;

	private int historyCount;
	private double thresholdWon, probabilityWon, probabilityLost;

	/**
	 * Construct a new custom strategy.
	 */
	public OwnStrategy() {
		this(STANDARD_HISTORY_COUNT, STANDARD_THRESHOLD_WON, STANDARD_PROBABILITY_WON, STANDARD_PROBABILITY_LOST);
	}

	/**
	 * Construct a new custom strategy.
	 *
	 * @param probabilityWon  probability to co-operate if the player won more (often) than the other player
	 * @param probabilityLost probability to co-operate if the player won less (often) than the other player
	 */
	public OwnStrategy(@Param(name = "probability (won)") double probabilityWon,
										 @Param(name = "probability (lost)") double probabilityLost) {
		this(STANDARD_HISTORY_COUNT, STANDARD_THRESHOLD_WON, probabilityWon, probabilityLost);
	}

	/**
	 * Construct a new custom strategy.
	 *
	 * @param thresholdWon    threshold when to consider a (number of) round(s) to be won
	 * @param probabilityWon  probability to co-operate if the player won more (often) than the other player
	 * @param probabilityLost probability to co-operate if the player won less (often) than the other player
	 */
	public OwnStrategy(@Param(name = "winning threshold") double thresholdWon,
										 @Param(name = "probability (won)") double probabilityWon,
										 @Param(name = "probability (lost)") double probabilityLost) {
		this(STANDARD_HISTORY_COUNT, thresholdWon, probabilityWon, probabilityLost);
	}

	/**
	 * Construct a new custom strategy.
	 *
	 * @param historyCount    number of rounds to analyse
	 * @param thresholdWon    threshold when to consider a (number of) round(s) to be won
	 * @param probabilityWon  probability to co-operate if the player won more (often) than the other player
	 * @param probabilityLost probability to co-operate if the player won less (often) than the other player
	 */
	public OwnStrategy(@Param(name = "history count") int historyCount,
										 @Param(name = "winning threshold") double thresholdWon,
										 @Param(name = "probability (won)") double probabilityWon,
										 @Param(name = "probability (lost)") double probabilityLost) {

		this.historyCount = historyCount;
		this.thresholdWon = thresholdWon;
		this.probabilityWon = probabilityWon;
		this.probabilityLost = probabilityLost;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Move determineNextMove(Player currentPlayer, Player otherPlayer) {

		int ttl = Math.min(this.historyCount, Math.min(currentPlayer.getNumberOfRounds(), otherPlayer.getNumberOfRounds()));
		double p;

		if (ttl == 0)
			p = this.thresholdWon;
		else {
			int won = 0;
			for (int i = 0; i < ttl; i++) {
				BigDecimal curGin = currentPlayer.getRound(-i - 1).getGain();
				BigDecimal othGin = otherPlayer.getRound(-i - 1).getGain();
				if (curGin.doubleValue() / curGin.add(othGin).doubleValue() > this.thresholdWon)
					won++;
			}

			p = (double)won / ttl > this.thresholdWon ? this.probabilityWon : this.probabilityLost;
//		if (currentPlayer.getNumberOfRounds() > 0 && currentPlayer.getRound(- 1).getMove() != Move.COOPERATE)
//			p = 1 - p;
		}

		return random.nextDouble() < p ? Move.COOPERATE : Move.DECEIVE;
	}
}
