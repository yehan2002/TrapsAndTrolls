package io.github.yehan2002.Traps.api;

import java.io.Serializable;

public class Trap implements Serializable {
    private static final long serialVersionUID = -9201763290823404041L;
    public TrapManager trap;
    private String customName;

    public Trap(TrapManager trapManager, String s) {
        trap = trapManager;
        customName = s;
    }
}
