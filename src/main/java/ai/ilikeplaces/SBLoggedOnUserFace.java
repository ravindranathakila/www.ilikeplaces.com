package ai.ilikeplaces;

import javax.ejb.Local;

/**
 *
 * @author Ravindranath Akila
 */
@Local
public interface SBLoggedOnUserFace extends javax.servlet.http.HttpSessionBindingListener {

    public String getLoggedOnUserId();

    public void setLoggedOnUserId(String loggedOnUserId);
}