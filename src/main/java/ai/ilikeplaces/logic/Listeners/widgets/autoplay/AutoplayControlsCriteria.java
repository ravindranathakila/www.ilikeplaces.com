package ai.ilikeplaces.logic.Listeners.widgets.autoplay;

import ai.ilikeplaces.logic.role.HumanUserLocal;
import ai.ilikeplaces.logic.validators.unit.HumanId;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 1/13/12
 * Time: 7:17 PM
 */
public class AutoplayControlsCriteria {
    private HumanId humanId;
    private HumanUserLocal humanUserLocal;
    public enum AUTOPLAY_STATE{
        PLAYING,
        PAUSED,
    }

    public HumanUserLocal getHumanUserLocal() {
        return humanUserLocal;
    }

    public AutoplayControlsCriteria setHumanUserLocal(HumanUserLocal humanUserLocal) {
        this.humanUserLocal = humanUserLocal;
        return this;
    }

    public HumanId getHumanId() {
        return humanId;
    }

    public AutoplayControlsCriteria setHumanId(final HumanId humanId) {
        this.humanId = humanId;
        return this;
    }
}