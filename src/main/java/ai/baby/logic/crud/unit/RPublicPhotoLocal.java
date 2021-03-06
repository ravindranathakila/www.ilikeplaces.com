package ai.baby.logic.crud.unit;

import ai.ilikeplaces.entities.PublicPhoto;
import ai.scribble.License;

import javax.ejb.Local;
import java.util.List;

/**
 * @author Ravindranath Akila
 */

// @License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface RPublicPhotoLocal {

    public PublicPhoto doRPublicPhotoLocal(String humanId, long locationId, PublicPhoto publicPhoto);

    public List<PublicPhoto> doRAllPublicPhotos(final String humanId);
}
