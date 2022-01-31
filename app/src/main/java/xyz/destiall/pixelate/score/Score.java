package xyz.destiall.pixelate.score;

/**
 * Written By Yong Hong
 */
public class Score {
    int score;

    public Score()
    {
        score = 0;
    }

    public void addScore(ScoreType scoreType, int quant) {
        switch(scoreType) {
            case GATHER_XP:
                score += quant;
                break;

            case KILL_HOSTILE:
                score += quant * 15;
                break;

            case BREAK_ORE:
                score += quant * 5;
                break;
        }
    }

    public void clearScore() { score = 0;}

    public int getScore()
    {
        return score;
    }
}
