package ai.baby.logic.crud.unit;

import ai.scribble.License;

import javax.ejb.Local;

/**
 * @author Ravindranath Akila
 */

// @License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface UPublicPhotoLocal {

    /**
     * @param publicPhotoId
     * @param publicPhotoDescription
     * @return
     */
    public boolean doNTxUPublicPhotoDescriptionLocal(final long publicPhotoId, final String publicPhotoDescription);

    /**
     * @param publicPhotoId
     * @param publicPhotoDescription
     * @return
     */
    public boolean doUPublicPhotoDescriptionLocal(long publicPhotoId, final String publicPhotoDescription);

    /**
     * @param publicPhotoId
     * @param publicPhotoDescription
     * @return
     */
    public boolean doDirtyUPublicPhotoDescriptionLocal(final long publicPhotoId, final String publicPhotoDescription);
}
