package ch.bfh.wstat.project;

import java.util.Random;

/**
 * Possible strategies a player (prisoner) can follow in the 'Iterated Prisoner's Dilemma Game'.
 *
 * @author strut1 &amp; weidj1
 */
public enum Strategy {

	/**
	 * Random strategy. <br>
	 * Co-operate with a probability of 50%.
	 */
	RAND {

			/**
			 * {@inheritDoc}
			 */
			@Override
			public Move determineNextMove(Player currentPlayer, Player otherPlayer) {

				return random.nextInt(2) < 1 ? Move.COOPERATE : Move.DECEIVE;
			}
		},

	/**
	 * Probability strategy. <br>
	 * Co-operate with a probability {@code p}.
	 */
	PROB {

			/**
			 * Probability {@code p} to co-operate.
			 */
			public static final double PROBABILITY = .4;

			/**
			 * {@inheritDoc}
			 */
			@Override
			public Move determineNextMove(Player currentPlayer, Player otherPlayer) {

				return random.nextDouble() < PROBABILITY ? Move.COOPERATE : Move.DECEIVE;
			}
		},

	/**
	 * Reaction strategy. <br>
	 * Co-operate with a probability {@code p_CC} if the other player co-operated in the last round and with a probability {@code p_CD} if he deceived.
	 */
	REAC {

			/**
			 * Probability to co-operate in the first round.
			 */
			public static final double PROBABILITY_INITAL = .5;

			/**
			 * Probability {@code p_CC} to co-operate if the other player co-operated in last round.
			 */
			public static final double PROBABILITY_COOPERATED = .6;

			/**
			 * Probability {@code p_CD} to co-operate if the other player deceived in last round.
			 */
			public static final double PROBABILITY_DECEIVED = .35;

			/**
			 * {@inheritDoc}
			 */
			@Override
			public Move determineNextMove(Player currentPlayer, Player otherPlayer) {

				double p;
				if (otherPlayer.getNumberOfRounds() == 0)
					p = PROBABILITY_INITAL;
				else
					p = otherPlayer.getRound(-1).getMove() == Move.COOPERATE ? PROBABILITY_COOPERATED : PROBABILITY_DECEIVED;

				return random.nextDouble() < p ? Move.COOPERATE : Move.DECEIVE;
			}
		},

	/**
	 * Alternating strategy. <br>
	 * Co-operate with a probability {@code p_CC} if the player also co-operated in the last round and with a probability {@code p_CD} if he deceived.
	 */
	ALTE {

			/**
			 * Probability to co-operate in the first round.
			 */
			public static final double PROBABILITY_INITAL = .5;

			/**
			 * Probability {@code p_CC} to co-operate if the player co-operated in last round as well.
			 */
			public static final double PROBABILITY_COOPERATED = .4;

			/**
			 * Probability {@code p_CD} to co-operate if the player deceived in last round.
			 */
			public static final double PROBABILITY_DECEIVED = .65;

			/**
			 * {@inheritDoc}
			 */
			@Override
			public Move determineNextMove(Player currentPlayer, Player otherPlayer) {

				double p;
				if (otherPlayer.getNumberOfRounds() == 0)
					p = PROBABILITY_INITAL;
				else
					p = otherPlayer.getRound(-1).getMove() == Move.COOPERATE ? PROBABILITY_COOPERATED : PROBABILITY_DECEIVED;

				return random.nextDouble() < p ? Move.COOPERATE : Move.DECEIVE;
			}
		},

	/**
	 * Winning strategy. <br>
	 * Repeat the same move with a probability {@code p_R} if the player won more than the other player in the last round.
	 */
	WIN {

			/**
			 * Probability to co-operate in the first round.
			 */
			public static final double PROBABILITY_INITAL = .5;

			/**
			 * Probability {@code p_R} to co-operate if the player won more than the other player in last round.
			 */
			public static final double PROBABILITY_WON = .6;

			/**
			 * Probability to co-operate if the player won less than the other player in last round.
			 */
			public static final double PROBABILITY_LOST = .35;

			/**
			 * {@inheritDoc}
			 */
			@Override
			public Move determineNextMove(Player currentPlayer, Player otherPlayer) {

				double p;
				if (currentPlayer.getNumberOfRounds() == 0 || otherPlayer.getNumberOfRounds() == 0)
					p = PROBABILITY_INITAL;
				else
					p = currentPlayer.getRound(-1).getGain().compareTo(otherPlayer.getRound(-1).getGain()) > 0 ? PROBABILITY_WON : PROBABILITY_LOST;

				return random.nextDouble() < p ? Move.COOPERATE : Move.DECEIVE;
			}
		},

	/**
	 * Our own strategy. <br>
	 * Not implemented yet.
	 */
	OWN {

			/**
			 * {@inheritDoc}
			 */
			@Override
			public Move determineNextMove(Player currentPlayer, Player otherPlayer) {
				throw new UnsupportedOperationException("Strategy not yet implemented."); //TODO: implement strategy
			}
		},

	/**
	 * Co-operation strategy. <br>
	 * Always co-operate.
	 */
	COOP {

			/**
			 * {@inheritDoc}
			 */
			@Override
			public Move determineNextMove(Player currentPlayer, Player otherPlayer) {
				return Move.COOPERATE;
			}
		},

	/**
	 * Deception strategy. <br>
	 * Never co-operate.
	 */
	DECV {

			/**
			 * {@inheritDoc}
			 */
			@Override
			public Move determineNextMove(Player currentPlayer, Player otherPlayer) {
				return Move.DECEIVE;
			}
		};

	private static Random random = new Random();

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
