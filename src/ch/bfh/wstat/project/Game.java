package ch.bfh.wstat.project;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Arrays;

/**
 * Implementation of the 'Iterated Prisoner's Dilemma Game'.
 *
 * @author strut1 &amp; weidj1
 */
public class Game {

	/**
	 * Standard gain for mutual co-operation.
	 */
	public static final BigDecimal STANDARD_GAIN_COOPERATION = BigDecimal.valueOf(3);

	/**
	 * Standard gain for deception when other player co-operates.
	 */
	public static final BigDecimal STANDARD_GAIN_WIN = BigDecimal.valueOf(5);

	/**
	 * Standard gain for co-operation when other player deceives.
	 */
	public static final BigDecimal STANDARD_GAIN_LOSS = BigDecimal.valueOf(0);

	/**
	 * Standard gain for mutual deception.
	 */
	public static final BigDecimal STANDARD_GAIN_DECEPTION = BigDecimal.valueOf(1);

	/**
	 * Standard number of rounds to play.
	 */
	public static final int STANDARD_ROUNDS_TO_PLAY = 100;

	private BigDecimal gainCooperation, gainWin, gainLoss, gainDeception;

	private final int rounds;
	private int round = 0, competitions = 0;
	private final int[][] outcomes = {{0, 0}, {0, 0}};

	private final Player[] players;

	/**
	 * Construct a game.
	 *
	 * @param gainCooperation gain for mutual co-operation
	 * @param gainWin         gain for deception when other player co-operates
	 * @param gainLoss        gain for co-operation when other player deceives
	 * @param gainDeception   gain for mutual deception
	 * @param rounds          number of rounds to play
	 * @param players         participants / players
	 */
	public Game(BigDecimal gainCooperation, BigDecimal gainWin, BigDecimal gainLoss, BigDecimal gainDeception, int rounds, Player... players) {

		this.gainCooperation = gainCooperation;
		this.gainWin = gainWin;
		this.gainLoss = gainLoss;
		this.gainDeception = gainDeception;

		this.rounds = rounds;

		this.players = players;
	}

	/**
	 * Construct a game.
	 *
	 * @param gainCooperation gain for mutual co-operation
	 * @param gainWin         gain for deception when other player co-operates
	 * @param gainLoss        gain for co-operation when other player deceives
	 * @param gainDeception   gain for mutual deception
	 * @param players         participants / players
	 */
	public Game(BigDecimal gainCooperation, BigDecimal gainWin, BigDecimal gainLoss, BigDecimal gainDeception, Player... players) {
		this(gainCooperation, gainWin, gainLoss, gainDeception, STANDARD_ROUNDS_TO_PLAY, players);
	}

	/**
	 * Construct a game.
	 *
	 * @param rounds  number of rounds to play
	 * @param players participants / players
	 */
	public Game(int rounds, Player... players) {
		this(STANDARD_GAIN_COOPERATION, STANDARD_GAIN_WIN, STANDARD_GAIN_LOSS, STANDARD_GAIN_DECEPTION, rounds, players);
	}

	/**
	 * Construct a game.
	 *
	 * @param players participants / players
	 */
	public Game(Player... players) {
		this(STANDARD_GAIN_COOPERATION, STANDARD_GAIN_WIN, STANDARD_GAIN_LOSS, STANDARD_GAIN_DECEPTION, STANDARD_ROUNDS_TO_PLAY, players);
	}

	/**
	 * Get the gain for mutual co-operation.
	 *
	 * @return gain for mutual co-operation
	 */
	public BigDecimal getGainCooperation() {
		return this.gainCooperation;
	}

	/**
	 * Get the gain for deception when other player co-operates.
	 *
	 * @return gain for deception when other player co-operates
	 */
	public BigDecimal getGainWin() {
		return this.gainWin;
	}

	/**
	 * Get the gain for co-operation when other player deceives.
	 *
	 * @return gain for co-operation when other player deceives
	 */
	public BigDecimal getGainLoss() {
		return this.gainLoss;
	}

	/**
	 * Get the gain for mutual deception.
	 *
	 * @return gain for mutual deception
	 */
	public BigDecimal getGainDeception() {
		return this.gainDeception;
	}

	/**
	 * Get the total number of rounds.
	 *
	 * @return total number of rounds
	 */
	public int getNumberOfRounds() {
		return this.rounds;
	}

	/**
	 * Get the number of rounds left to play.
	 *
	 * @return number of rounds left to play
	 */
	public int getNumberOfRoundsToPlay() {
		return this.rounds - this.round;
	}

	/**
	 * Get the (absolute) frequency of a specific event. <br>
	 * The event is defined by the moves of the two players.
	 *
	 * @param move1 move of first player
	 * @param move2 move of second player
	 *
	 * @return the absolute frequency of the event {@code (move1, move2)}
	 */
	public int getFrequency(Move move1, Move move2) {

		return this.outcomes[move1.ordinal()][move2.ordinal()];
	}

	/**
	 * Get the relative frequency of a specific event. <br>
	 * The event is defined by the moves of the two players.
	 *
	 * @param move1 move of first player
	 * @param move2 move of second player
	 *
	 * @return the relative frequency of the event {@code (move1, move2)}
	 */
	public double getRelativeFrequency(Move move1, Move move2) {

		return this.round > 0 ? (double)this.getFrequency(move1, move2) / this.round : 0.;
	}

	/**
	 * Get the players' total gain up to the current round.
	 *
	 * @return players' total gain up to the current round
	 */
	public BigDecimal getTotalGain() {
		return Arrays.stream(this.players).map(p -> p.getTotalGain()).reduce(BigDecimal.ZERO, (d, e) -> d.add(e));
	}

	/**
	 * Get the players' middle gain during the rounds already played.
	 *
	 * @return players' middle gain during the rounds already played
	 */
	public BigDecimal getMiddleGain() {
		return this.getTotalGain().divide(BigDecimal.valueOf(this.round), MathContext.DECIMAL128);
	}

	/**
	 * Play all remaining rounds. (complete game)
	 */
	public void playRounds() {

		while (this.round < this.rounds)
			this.playRound();
	}

	/**
	 * Play a single round.
	 */
	public void playRound() {

		if (this.round >= this.rounds) //check if there are rounds left to play
			throw new IllegalStateException("Maximum number of rounds reached.");

		for (int i = 0; i < this.players.length; i++)
			for (int j = i + 1; j < this.players.length; j++) {
				Player player1 = this.players[i];
				Player player2 = this.players[j];

				Move move1 = player1.getStrategy().determineNextMove(player1, player2); //determine the players' next moves
				Move move2 = player2.getStrategy().determineNextMove(player2, player1);

				if (move1 == Move.COOPERATE) //determine the round's gains and update players' histories
					if (move2 == Move.COOPERATE) {
						player1.addRound(move1, this.gainCooperation); //mutual co-operation
						player2.addRound(move2, this.gainCooperation);

					} else {
						player1.addRound(move1, this.gainLoss); //player 1 loses
						player2.addRound(move2, this.gainWin);
					}

				else if (move2 == Move.COOPERATE) {
					player1.addRound(move1, this.gainWin); //player 2 loses
					player2.addRound(move2, this.gainLoss);

				} else {
					player1.addRound(move1, this.gainDeception); //mutual deception
					player2.addRound(move2, this.gainDeception);
				}

				this.competitions++; //update statistics
				this.outcomes[move1.ordinal()][move2.ordinal()]++;
			}

		this.round++; //increment round index
	}
}
