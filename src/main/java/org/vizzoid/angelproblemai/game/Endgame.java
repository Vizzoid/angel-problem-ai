package org.vizzoid.angelproblemai.game;

public enum Endgame {
    ANGEL,
    DEVIL,
    NONE {
        @Override
        public boolean victory() {
            return false;
        }
    };

    public boolean victory() {
        return true;
    }

}
