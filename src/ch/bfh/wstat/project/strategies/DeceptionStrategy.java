package ch.bfh.wstat.project.strategies;

import ch.bfh.wstat.project.Move;
import ch.bfh.wstat.project.Player;
import ch.bfh.wstat.project.Strategy;

/**
 * Optimised probability (deception) strategy. <br>
 * Never co-operate.
 *
 * @author strut1 &amp; weidj1
 */
public class DeceptionStrategy extends Strategy {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Move determineNextMove(Player currentPlayer, Player otherPlayer) {
		return Move.DECEIVE;
	}
}
