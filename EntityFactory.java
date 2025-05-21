public class EntityFactory {
    public static Entity createEntity(String type, gameScreen gs, KeyHandler keyHandler) {
        switch (type) {
            case "Player":
                return new Player(gs, keyHandler);
            case "NPC":
                return new NPC(gs);
            case "Boss":
                return new Boss(gs);
            default:
                throw new IllegalArgumentException("Unknown entity type: " + type);
        }
    }
}
