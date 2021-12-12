package tasks;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskTemplate implements Serializable {

    public static final int LUONG_ID = -5;
    public static final int XU_ID = -6;
    public static final int EXP_ID = -7;
    private int taskId;
    public String name;
    private String detail;
    public String[] subNames;
    public int[] counts;
    private int[][] rewards;
    private int minLevel;
    private String npcTalk;
    private String[] npcMenu;
    private int[][] mobs;
    private int[][] receiveItems;
    private int[] itemsPick;

    public String getNpcTalk() {
        if (npcTalk == null) {
            return "Hãy nhận nhiệm vụ đi con";
        }
        return npcTalk;
    }

    public int[] getItemsPick() {
        if (itemsPick == null) {
            itemsPick = new int[counts.length];
            for (int i = 0, countsLength = counts.length; i < countsLength; i++) {
                itemsPick[i] = -1;
            }
        }
        return itemsPick;
    }

    public int[][] getReceiveItems() {
        if (receiveItems == null) {
            receiveItems = new int[counts.length][];
            for (int i = 0; i < counts.length; i++) {
                receiveItems[i] = new int[0];
            }
        }
        return receiveItems;
    }

    public int[][] getMobs() {
        if (mobs == null) {
            mobs = new int[getCounts().length][];
            for (int i = 0; i < getCounts().length; i++) {
                mobs[i] = new int[0];
            }
        }
        return mobs;
    }


    public String getMenuByIndex(int index) {
        if (npcMenu == null) {
            return name;
        } else {
            if (npcMenu.length == 0) {
                return name;
            } else {
                if (index < this.npcMenu.length) {
                    final String npcMenu = this.npcMenu[index];
                    if ("".equals(npcMenu)) {
                        return name;
                    }
                    return npcMenu;
                } else {
                    return "Thiếu Menu NPC index " + index;
                }
            }
        }
    }

    public int[][] getRewards() {
        if (rewards == null) {
            return new int[0][];
        }
        return rewards;
    }
}
