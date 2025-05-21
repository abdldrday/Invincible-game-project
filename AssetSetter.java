public class AssetSetter {
    private final gameScreen gs; // Убери static

    public AssetSetter(gameScreen gs) {
        this.gs = gs;
    }

    public void setNPC() {
        gs.npc[0] = new NPC(gs);
        gs.npc[0].worldX = gs.titleSize * 21;
        gs.npc[0].worldY = gs.titleSize * 21;
    }
}