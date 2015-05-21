package ch.bfh.wstat.project.legacy;

/*
 * Strategy: implement different strategies
 *
 * 1 - RAND = collaborate with probability 0.5
 * 2 - PROB = collaborate with probability p
 * 3 - ALT =  collaborate with probability p_CC if he has collaborated the last turn and with a probability p_CD if he has deceived.
 * 4 - REAC = collaborate with probability p_CC if the other player has collaborated the last turn and with a probability p_CD if he has deceived.
 * 5 - WIN =  redo same action with probability p_R if he has won more than the other player the last turn
 * 6 - OWN =  *describe here your own strategy*
 */
public class Strategy {

	//type of strategy
	final public static int RAND = 1;
	final public static int PROB = 2;
	final public static int ALT = 3;
	final public static int REAC = 4;
	final public static int WIN = 5;
	final public static int OWN = 6;

//*** START OF MODIFIED CODE FRAGMENT ***
	final public static int PROB_COP = 7;
	final public static int PROB_OPT = 8;
	final public static int REAC_OPT = 9;
//*** END OF MODIFIED CODE FRAGMENT ***

	// possible MOVEs
	final static int COOPERATE = 1;
	final static int DECEIVE = 0;

	// compute next move according to strategy
	public static int nextMove(Player Player1, Player Player2, int nGame) {

		int Move = -1;

		switch (Player1.PlayerStrategy) {

			case RAND:
				Move = (int)(Math.random() + 0.5); //random number between 0.5 and 1.5 casted to int
				break;

//*** START OF MODIFIED CODE FRAGMENT ***
			case PROB:
				Move = (int)(Math.random() + .4);
				break;

			case PROB_OPT:
				Move = DECEIVE;
				break;

			case REAC:
				double p;
				if (nGame == 0)
					p = .5;
				else if (Player2.MoveHistory[nGame - 1] == COOPERATE)
					p = .6;
				else
					p = .35;
				Move = (int)(Math.random() + p);
				break;

			case REAC_OPT:
				if (nGame == 0)
					p = .5;
				else if (Player2.MoveHistory[nGame - 1] == COOPERATE)
					p = .6;
				else
					p = 0;
				Move = (int)(Math.random() + p);
				break;

			case ALT:
				if (nGame == 0)
					p = .5;
				else if (Player1.MoveHistory[nGame - 1] == COOPERATE)
					p = .4;
				else
					p = .65;
				Move = (int)(Math.random() + p);
				break;

			case OWN:

				break;
//*** END OF MODIFIED CODE FRAGMENT ***

			default:
				System.out.println("Strategy not implemented\n");
				System.exit(-1);
				break;
		}

		return Move;
	}
}
