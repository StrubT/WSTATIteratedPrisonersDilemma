package ch.bfh.wstat.project;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import ch.bfh.wstat.project.strategies.AlternatingStrategy;
import ch.bfh.wstat.project.strategies.CooperationStrategy;
import ch.bfh.wstat.project.strategies.DeceptionStrategy;
import ch.bfh.wstat.project.strategies.OwnStrategy;
import ch.bfh.wstat.project.strategies.ProbabilityStrategy;
import ch.bfh.wstat.project.strategies.RandomStrategy;
import ch.bfh.wstat.project.strategies.ReactionStrategy;
import ch.bfh.wstat.project.strategies.WinningStrategy;

/**
 * Interface to be implemented by all strategies a player (prisoner) can follow in the 'Iterated Prisoner's Dilemma Game'.
 *
 * @author strut1 &amp; weidj1
 */
public abstract class Strategy {

	/**
	 * A single random instance to be used by all strategies.
	 */
	protected static Random random = new Random();

	@SuppressWarnings("unchecked")
	protected static final Class<? extends Strategy>[] WELL_KNOWN_STRATEGIES = new Class[]{
		RandomStrategy.class,
		CooperationStrategy.class,
		DeceptionStrategy.class,
		ProbabilityStrategy.class,
		ReactionStrategy.class,
		AlternatingStrategy.class,
		WinningStrategy.class,
		OwnStrategy.class
	};

	private static final List<Class<? extends Strategy>> strategies;

	static {
		strategies = new ArrayList<>(WELL_KNOWN_STRATEGIES.length);
		Arrays.stream(WELL_KNOWN_STRATEGIES).forEach(strategies::add);
	}

	/**
	 * Get all known (well known as well as registered) strategies.
	 *
	 * @return a {@link List} of all known (well known as well as registered) strategies
	 *
	 * @see #registerStrategy(Class)
	 */
	public static List<Class<? extends Strategy>> getKnownStrategies() {
		return Collections.unmodifiableList(strategies);
	}

	/**
	 * Register an additional strategy.
	 *
	 * @param strategy strategy to register
	 *
	 * @return whether or not the strategy was added to the list of known strategies,
	 *         {@code false} indicates the strategy was known already
	 *
	 * @see #getKnownStrategies()
	 */
	public static boolean registerStrategy(Class<? extends Strategy> strategy) {
		return !strategies.contains(strategy) ? strategies.add(strategy) : false;
	}

	/**
	 * Determine the player's next move.
	 *
	 * @param currentPlayer player to determine next move for
	 * @param otherPlayer   player to compete against
	 *
	 * @return the player's next move
	 */
	public abstract Move determineNextMove(Player currentPlayer, Player otherPlayer);
}
