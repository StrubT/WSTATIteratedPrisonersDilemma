package ch.bfh.wstat.project.strategies;

import ch.bfh.wstat.project.Move;
import ch.bfh.wstat.project.Player;
import ch.bfh.wstat.project.Strategy;

/**
 * Co-operation strategy. <br>
 * Always co-operate.
 *
 * @author strut1 &amp; weidj1
 */
public class CooperationStrategy extends Strategy {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Move determineNextMove(Player currentPlayer, Player otherPlayer) {
		return Move.COOPERATE;
	}
}
