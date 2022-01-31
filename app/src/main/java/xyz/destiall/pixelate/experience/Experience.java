package xyz.destiall.pixelate.experience;

/**
 * Written by Yong Hong
 */
public class Experience {
    private int xp;
    private int level;

    public Experience() {
        xp = 0;
        level = 0;
    }

    public boolean addXP(int xp) {
        if (xp > 0) {
            int reqXP = getRequiredXP(level);
            this.xp += xp;
            while (this.xp > reqXP) {
                this.xp -= reqXP;
                this.level += 1;
            }
            return true;
        }
        return false;
    }

    public int getXP() {
        return xp;
    }

    public int getLevel() {
        return level;
    }

    public void setXP(int xp) {
        int reqXP = getRequiredXP(level+1);
        if (xp > reqXP) this.xp = xp;
    }

    public void setLevel(int level) {
        if (level < 0) level = 0;
        this.xp = 0;
        this.level = level;
    }

    public static int getRequiredXP(int level) {
        if (level >= 0) return 100 * (level+1);
        return -1;
    }
}
