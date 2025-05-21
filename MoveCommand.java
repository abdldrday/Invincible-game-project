public abstract class MoveCommand implements Command {
    protected Player player;
    protected String direction;

    public MoveCommand(Player player, String direction) {
        this.player = player;
        this.direction = direction;
    }

    @Override
    public void execute() {
        player.direction = direction;
        player.checkTileCollision();
        if (!player.collisionOn) {

            switch (direction) {
                case "up":
                    player.worldY -= player.speed;
                    break;
                case "down":
                    player.worldY += player.speed;
                    break;
                case "left":
                    player.worldX -= player.speed;
                    break;
                case "right":
                    player.worldX += player.speed;
                    break;
            }
        }
    }
}

