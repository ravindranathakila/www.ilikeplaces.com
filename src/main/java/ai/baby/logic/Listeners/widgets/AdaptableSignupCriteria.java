package ai.baby.logic.Listeners.widgets;

import ai.ilikeplaces.entities.etc.HumanId;
import ai.baby.logic.validators.unit.Email;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 3/23/12
 * Time: 9:54 PM
 */
public class AdaptableSignupCriteria<T extends AdaptableSignupCallback> {

    private HumanId humanId;
    private InviteData inviteData;
    private String widgetTitle;


    private T adaptableSignupCallback;

    public HumanId getHumanId() {
        return humanId;
    }

// -------------------------- OTHER METHODS --------------------------

    public InviteData getInviteData() {
        return inviteData == null ? (inviteData = new InviteData()) : inviteData;
    }


    public AdaptableSignupCriteria setHumanId(final HumanId humanId) {
        this.humanId = humanId;
        return this;
    }


    public T getAdaptableSignupCallback() {
        return adaptableSignupCallback;
    }

    public AdaptableSignupCriteria setAdaptableSignupCallback(final T adaptableSignupCallback) {
        this.adaptableSignupCallback = adaptableSignupCallback;
        return this;
    }

    public String getWidgetTitle() {
        return widgetTitle != null ? widgetTitle : "";
    }

    public AdaptableSignupCriteria setWidgetTitle(final String widgetTitle) {
        this.widgetTitle = widgetTitle;
        return this;
    }

    // -------------------------- INNER CLASSES --------------------------

    public class InviteData {
// ------------------------------ FIELDS ------------------------------

        private Email email;

// ------------------------ ACCESSORS / MUTATORS ------------------------

        public Email getEmail() {
            return email;
        }

        public InviteData setEmail(final Email email) {
            this.email = email;
            return this;
        }
    }
}
