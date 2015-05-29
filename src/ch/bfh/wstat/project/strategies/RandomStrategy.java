package ch.bfh.wstat.project.strategies;

import ch.bfh.wstat.project.Move;
import ch.bfh.wstat.project.Player;
import ch.bfh.wstat.project.Strategy;

/**
 * Random strategy. <br>
 * Co-operate with a probability of 50%.
 *
 * @author strut1 &amp; weidj1
 */
public class RandomStrategy extends Strategy {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Move determineNextMove(Player currentPlayer, Player otherPlayer) {
		return random.nextInt(2) < 1 ? Move.COOPERATE : Move.DECEIVE;
	}
}
