package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.Album;
import ai.ilikeplaces.entities.PrivatePhoto;
import ai.ilikeplaces.exception.DBDishonourCheckedException;
import ai.ilikeplaces.jpa.CrudServiceLocal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

/**
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Stateless
public class UPrivatePhoto implements UPrivatePhotoLocal {

    @EJB
    private CrudServiceLocal<PrivatePhoto> privatePhotoCrudServiceLocal_;
    @EJB
    private CrudServiceLocal<Album> albumCrudServiceLocal_;

    public UPrivatePhoto() {
        logger.debug("HELLO, I INSTANTIATED {} OF WHICH HASHCODE IS {}.", UPrivatePhoto.class, this.hashCode());
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public PrivatePhoto doNAAddToAlbum(final long privatePhotoId, final long albumId) throws DBDishonourCheckedException {

        final PrivatePhoto privatePhoto = privatePhotoCrudServiceLocal_.findBadly(PrivatePhoto.class, privatePhotoId);

        final Album album = albumCrudServiceLocal_.findBadly(Album.class, albumId);

        privatePhoto.getAlbums().add(album);
        album.getAlbumPhotos().add(privatePhoto);

        return privatePhoto;
    }

    final static Logger logger = LoggerFactory.getLogger(UPrivatePhoto.class);
}