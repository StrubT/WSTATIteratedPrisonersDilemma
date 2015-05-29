package ch.bfh.wstat.project;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EmptyStackException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;
import ch.bfh.wstat.project.annotation.Param;

/**
 * Main class of the project 'Iterated Prisoner's Dilemma Game'.
 *
 * @author strut1 &amp; weidj1
 */
public class Project {

	/**
	 * Main method &amp; entry point of the application.
	 *
	 * @param args arguments from the command line ({@code <strategy of player n>*} {@code <number of rounds>})
	 */
	public static void main(String[] args) {

		int rounds = -1;
		List<Strategy> strategies = new ArrayList<>();

		if (args.length > 2) //if three or more arguments have been passed, try to create game with these settings
			try {
				rounds = Integer.parseInt(args[args.length - 1]); //parse the number of rounds
				for (int i = 0; i < args.length - 1; i++)
					strategies.add(recogniseStrategy(args[i]).getConstructor().newInstance()); //try to recognise strategy ordinals / names

			} catch (NumberFormatException | NoSuchElementException ex) {
				//throw new IllegalArgumentException("The 3 arguments must be: <strategy of player n>* <number of rounds>.");

			} catch (NoSuchMethodException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
				//throw new IllegalArgumentException("The selected strategies must have default configurations.");
			}

		if (rounds <= 0) //if settings have not been passed from the command line, allow the user to interactively specify them
			try (Scanner in = new Scanner(System.in)) { //create a scanner to read from the console
				System.out.println("Please choose the player's strategies.\nYou have the following options:");
				List<Class<? extends Strategy>> stgs = Strategy.getKnownStrategies();
				for (int i = 0; i < stgs.size(); i++) //display the strategy ordinals & numbers
					System.out.printf(">%d: %s%n", i + 1, getStrategyName(stgs.get(i)));
				System.out.println();

				System.out.print("Please enter the number of rounds to play: "); //show the user a hint what to enter
				while (true) {
					try {
						rounds = Integer.parseInt(in.nextLine()); //try to parse the string to a number
						if (rounds > 0) //if the user entered a positive number of rounds
							break; //quit loop and proceed

					} catch (NumberFormatException ex) { //if the user did not enter a number, proceed
					}

					System.out.print("Please enter a positive number: "); //if the user entered an invalid string, show a message and try again
				}

				System.out.println("Please choose the players' strategies [empty string to finish]:"); //show the user a hint what to enter
				boolean qt1 = false;
				for (int i = 1; !qt1; i++) { //read strategies for players
					System.out.printf("Please choose the strategy of player %d: ", i); //show the user a hint what to enter
					boolean qt2 = false;
					while (!qt2)
						try {
							String line = in.nextLine();
							if (line.isEmpty())
								if (strategies.size() > 1)
									qt1 = qt2 = true; //if finished, quit both loops and proceed
								else
									throw new EmptyStackException();

							else {
								@SuppressWarnings("unchecked")
								Constructor<? extends Strategy> cnt = Arrays.stream((Constructor<? extends Strategy>[])recogniseStrategy(line).getConstructors())
									.sorted((c, d) -> -Integer.compare(c.getParameterCount(), d.getParameterCount())).findFirst().get();
								Parameter[] prms = cnt.getParameters();
								Object[] vals = new Object[prms.length];

								for (int j = 0; j < prms.length; j++) {
									Parameter prm = prms[j];
									System.out.printf("Please specify a value for strategy parameter %s [%s]: ", prm.getAnnotation(Param.class).name(), prm.getType().getSimpleName()); //show the user a hint what to enter
									boolean qt3 = false;
									while (!qt3)
										try {
											line = in.nextLine();
											if (prm.getType().equals(Integer.class) || prm.getType().equals(Integer.TYPE))
												vals[j] = Integer.parseInt(line);
											else if (prm.getType().equals(Double.class) || prm.getType().equals(Double.TYPE))
												vals[j] = Double.parseDouble(line);
											else if (prm.getType().equals(String.class))
												vals[j] = line;
											else
												throw new IllegalArgumentException(String.format("Unhandled constructor parameter type %s.", prm.getType().getSimpleName()));
											qt3 = true;

										} catch (NumberFormatException ex) { //if the user entered an invalid string
											System.out.print("Please enter a valid strategy ordinal or name: "); //show a message and try again
										}
								}

								strategies.add(cnt.newInstance(vals)); //try to recognise the strategy
								qt2 = true; //if recognition succeeded, quit loop and proceed
							}

						} catch (ArrayIndexOutOfBoundsException | NoSuchElementException ex) { //if the user entered an invalid string
							System.out.print("Please enter a valid strategy ordinal or name: "); //show a message and try again

						} catch (EmptyStackException ex) { //if the user did not specifiy enough strategies
							System.out.print("Please choose at least two strategies: "); //show a message and try again

						} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) { //if strategy instantiation failed
							System.out.printf("An exception occurred: %s%nPlease choose a different strategy: ", ex.toString()); //show a message and try again
						}
				}

				System.out.println();
			}

		new Project().playGame(rounds, strategies); //create an object and play the game
	}

	private static String getStrategyName(Class<? extends Strategy> strategy) {

		String nme = strategy.getSimpleName();
		if (nme.toLowerCase().endsWith("strategy"))
			nme = nme.substring(0, nme.length() - 8);

		return nme;
	}

	private static Class<? extends Strategy> recogniseStrategy(String name) {

		List<Class<? extends Strategy>> stgs = Strategy.getKnownStrategies();

		try {
			return stgs.get(Integer.parseInt(name) - 1); //try to parse the string to a number and retreive the strategy

		} catch (NumberFormatException ex) { //if the user did not specify a number, try to find a strategy with the specified name
			Optional<Class<? extends Strategy>> stg = stgs.stream().filter(s -> getStrategyName(s).equalsIgnoreCase(name)).findAny();
			if (stg.isPresent())
				return stg.get();

			stgs = stgs.stream().filter(s -> getStrategyName(s).toLowerCase().startsWith(name.toLowerCase())).collect(Collectors.toList());
			if (stgs.size() == 1)
				return stgs.get(0);
			else
				throw new NoSuchElementException();
		}
	}

	/**
	 * Play a specific game.
	 *
	 * @param rounds     number of rounds
	 * @param strategies strategies of players
	 */
	public void playGame(int rounds, List<Strategy> strategies) {

		System.out.println("GAME INFORMATION:"); //print information about the game
		System.out.printf("game will be played over %,d rounds%n", rounds);
		for (int i = 0; i < strategies.size(); i++)
			System.out.printf("player 1 follows strategy %d (%s)%n", i + 1, getStrategyName(strategies.get(i).getClass()));

		List<Player> players = strategies.stream().map(Player::new).collect(Collectors.toList()); //create the players

		Game game = new Game(rounds, players.toArray(new Player[players.size()])); //create the game
		game.playRounds(); //play all rounds

		Move[] moves = {Move.COOPERATE, Move.DECEIVE}; //create an array of the moves in the desired display order

		System.out.println("\nGAME STATISTICS:"); //print information about the game
		for (Move m: moves)
			for (Move n: moves)
				System.out.printf("(%d, %d): %,7d time(s) - %5.1f%%%n", m.ordinal(), n.ordinal(), game.getFrequency(m, n), game.getRelativeFrequency(m, n) * 100.);
		System.out.printf("total: %,8d time(s)%n", rounds);

		System.out.println("\nPLAYER STATISTICS:\n          total gain | middle gain"); //print information about the individual players
		for (int i = 0; i < players.size(); i++)
			System.out.printf("player %d: %,10.2f | %,11.2f%n", i + 1, players.get(i).getTotalGain(), players.get(i).getMiddleGain());
		System.out.printf("total: %,13.2f | %,11.2f%n", game.getTotalGain(), game.getMiddleGain());
	}
}
