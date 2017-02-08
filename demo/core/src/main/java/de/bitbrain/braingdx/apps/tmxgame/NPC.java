package de.bitbrain.braingdx.apps.tmxgame;

public enum NPC {
    PRIEST_MALE,
    SAGE_FEMALE,
    CLERIC_MALE,
    DANCER_FEMALE,
    CITIZEN_MALE,
    DANCER_FEMALE_ALT,
    EXPLORER_MALE,
    EXPLORER_FEMALE;

    public static NPC random() {
	NPC[] values = values();
	int size = values.length;
	return values[(int) (size * Math.random())];
    }
}
