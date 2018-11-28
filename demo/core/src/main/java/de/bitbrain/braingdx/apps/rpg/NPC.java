package de.bitbrain.braingdx.apps.rpg;

public enum NPC {
   PRIEST_MALE(6, 0),
   SAGE_FEMALE(3, 0),
   CLERIC_MALE(0, 0),
   DANCER_FEMALE(9, 0),
   CITIZEN_MALE(0, 4),
   DANCER_FEMALE_ALT(4, 3),
   EXPLORER_MALE(6, 4),
   EXPLORER_FEMALE(9, 4);

   private final int indexX, indexY;

   NPC(int indexX, int indexY) {
      this.indexX = indexX;
      this.indexY = indexY;
   }

   public int getIndexX() {
      return indexX;
   }

   public int getIndexY() {
      return indexY;
   }

   public static NPC random() {
      NPC[] values = values();
      int size = values.length;
      return values[(int) (size * Math.random())];
   }
}
