package quickcarpet.utils.extensions;

import quickcarpet.helper.PlayerActionPack;

public interface ActionPackOwner {
    PlayerActionPack getActionPack();
    void setActionPack(PlayerActionPack pack);
}
