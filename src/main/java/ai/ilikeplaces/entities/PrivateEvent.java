package ai.ilikeplaces.entities;

import ai.ilikeplaces.doc.BIDIRECTIONAL;
import ai.ilikeplaces.doc.CREATED_BY;
import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.WARNING;
import ai.ilikeplaces.util.EntityLifeCyleListener;

import javax.persistence.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: Jan 12, 2010
 * Time: 8:19:40 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@CREATED_BY(who = {PrivateEvent.class},
        note = "We need to return this entity to the user for CRUD, hence cascade creation not possible.")
@Entity
@EntityListeners(EntityLifeCyleListener.class)
public class PrivateEvent {

    private Long privateEventId;

    private String privateEventName;

    private String privateEventInfo;

    private Boolean extendedAccess;

    private List<HumansPrivateEvent> privateEventOwners;
    final static public String privateEventOwnersCOL = "privateEventOwners";

    private List<HumansPrivateEvent> privateEventViewers;
    final static public String privateEventViewersCOL = "privateEventViewers";

    private List<HumansPrivateEvent> privateEventInvites;
    final static public String privateEventInvitesCOL = "privateEventInvites";

    private List<HumansPrivateEvent> privateEventRejects;
    final static public String privateEventRejectsCOL = "privateEventRejects";

    private PrivateLocation privateLocation;
    final static public String privateLocationCOL = "privateLocation";

    private Location location;
    final static public String locationCOL = "location";

    private Album album;
    final static public String albumCOL = "album";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getPrivateEventId() {
        return privateEventId;
    }

    public void setPrivateEventId(final Long privateEventId) {
        this.privateEventId = privateEventId;
    }

    public String getPrivateEventName() {
        return privateEventName;
    }

    public void setPrivateEventName(String privateEventName) {
        this.privateEventName = privateEventName;
    }

    public String getPrivateEventInfo() {
        return privateEventInfo;
    }

    public void setPrivateEventInfo(String privateEventInfo) {
        this.privateEventInfo = privateEventInfo;
    }

    public Boolean isExtendedAccess() {
        return extendedAccess;
    }

    public void setExtendedAccess(Boolean extendedAccess) {
        this.extendedAccess = extendedAccess;
    }

    @BIDIRECTIONAL(ownerside = BIDIRECTIONAL.OWNING.IS)
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    public PrivateLocation getPrivateLocation() {
        return privateLocation;
    }

    public void setPrivateLocation(PrivateLocation privateLocation) {
        this.privateLocation = privateLocation;
    }

    @BIDIRECTIONAL(ownerside = BIDIRECTIONAL.OWNING.IS)
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @BIDIRECTIONAL(ownerside = BIDIRECTIONAL.OWNING.IS)
    @OneToOne(cascade = CascadeType.ALL)
    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    @WARNING(warning = "Owner because once an event needs to be deleted, deleting this entity is easier if owner." +
            "If this entity is not the owner, individual owner viewer accepteee rejectee will have to delete their events individually.")
    @BIDIRECTIONAL(ownerside = BIDIRECTIONAL.OWNING.IS)
    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    public List<HumansPrivateEvent> getPrivateEventInvites() {
        return privateEventInvites;
    }

    public void setPrivateEventInvites(final List<HumansPrivateEvent> privateEventInvites) {
        this.privateEventInvites = privateEventInvites;
    }

    @WARNING(warning = "Owner because once an event needs to be deleted, deleting this entity is easier if owner." +
            "If this entity is not the owner, individual owner viewer accepteee rejectee will have to delete their events individually.")
    @BIDIRECTIONAL(ownerside = BIDIRECTIONAL.OWNING.IS)
    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    public List<HumansPrivateEvent> getPrivateEventViewers() {
        return privateEventViewers;
    }

    public void setPrivateEventViewers(final List<HumansPrivateEvent> privateEventViewers) {
        this.privateEventViewers = privateEventViewers;
    }

    @WARNING(warning = "Owner because once an event needs to be deleted, deleting this entity is easier if owner." +
            "If this entity is not the owner, individual owner viewer accepteee rejectee will have to delete their events individually.")
    @BIDIRECTIONAL(ownerside = BIDIRECTIONAL.OWNING.IS)
    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    public List<HumansPrivateEvent> getPrivateEventOwners() {
        return privateEventOwners;
    }

    public void setPrivateEventOwners(final List<HumansPrivateEvent> privateEventOwners) {
        this.privateEventOwners = privateEventOwners;
    }

    @WARNING(warning = "Owner because once an event needs to be deleted, deleting this entity is easier if owner." +
            "If this entity is not the owner, individual owner viewer accepteee rejectee will have to delete their events individually.")
    @BIDIRECTIONAL(ownerside = BIDIRECTIONAL.OWNING.IS)
    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    public List<HumansPrivateEvent> getPrivateEventRejects() {
        return privateEventRejects;
    }

    public void setPrivateEventRejects(final List<HumansPrivateEvent> privateEventRejects) {
        this.privateEventRejects = privateEventRejects;
    }

    @Override
    public String toString() {
        return "PrivateEvent{" +
                ", privateEventId=" + privateEventId +
                ", privateLocation=" + privateLocation +
                '}';
    }
}