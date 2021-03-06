package ai.baby.logic.crud.unit;

import ai.ilikeplaces.entities.*;
import ai.ilikeplaces.entities.etc.DBRefreshDataException;
import ai.baby.util.exception.DBDishonourCheckedException;
import ai.baby.util.exception.DBFetchDataException;
import ai.baby.util.jpa.CrudServiceLocal;
import ai.baby.util.AbstractSLBCallbacks;
import ai.scribble.License;
import ai.scribble.WARNING;
import ai.scribble._fix;

import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Jan 14, 2010
 * Time: 12:04:30 AM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Stateless
public class CPrivateEvent extends AbstractSLBCallbacks implements CPrivateEventLocal {

    @EJB
    private CrudServiceLocal<PrivateLocation> privateLocationCrudServiceLocal_;

    @EJB
    private CrudServiceLocal<PrivateEvent> privateEventCrudServiceLocal_;

    @EJB
    private CrudServiceLocal<HumansPrivateLocation> humansPrivateLocationCrudServiceLocal_;

    @EJB
    private CrudServiceLocal<Human> humanCrudServiceLocal_;

    @EJB
    private CrudServiceLocal<HumansPrivateEvent> humansPrivateEventCrudServiceLocal_;

    @EJB
    private RPrivateLocationLocal rPrivateLocationLocal_;


    @Override
    public PrivateEvent doNTxCPrivateEvent(final String humanId, final long privateLocationId, final String locationName, final String locationInfo, final String startDate, final String endDate) throws DBDishonourCheckedException, DBFetchDataException, DBRefreshDataException {
        final HumansPrivateEvent managedOwner = humansPrivateEventCrudServiceLocal_.find(HumansPrivateEvent.class, humanId);


        @WARNING(warning = "This call will throw an exception if user has not rights to the location So do not move it to AFTER creation of PrivateEvent." +
                "Even so, the transaction manager will roll this back too, but do it the safe way.")
        @_fix(issue = "Make crud classes based on roles. For example, this call should be made to a method, doRPrivateLocationAsOwner")
        final PrivateLocation managedPrivateLocation = rPrivateLocationLocal_.doRPrivateLocationAsOwner(humanId, privateLocationId);

        final PrivateEvent managedPrivateEvent = privateEventCrudServiceLocal_.create(
                new PrivateEvent()
                        .setPrivateEventNameR(locationName)
                        .setPrivateEventInfoR(locationInfo)
                        .setPrivateEventStartDateR(startDate)
                        .setPrivateEventEndDateR(endDate)
                        .setPrivateEventAlbumR(new Album().setAlbumNameR(locationName))
                        .setPrivateEventWallR(new Wall().setWallContentR("")));

        wireLocation:
        {
            managedPrivateEvent.setPrivateLocation(managedPrivateLocation);
            managedPrivateLocation.refresh().getPrivateEvents().add(managedPrivateEvent);
        }

        wireOwnership:
        {
            managedPrivateEvent.getPrivateEventOwners().add(managedOwner);
            managedOwner.getPrivateEventsOwned().add(managedPrivateEvent);
        }

        wireViewership:
        {
            managedPrivateEvent.getPrivateEventViewers().add(managedOwner);
            managedOwner.getPrivateEventsViewed().add(managedPrivateEvent);
        }
        wireInvitedship:
        {
            managedPrivateEvent.getPrivateEventInvites().add(managedOwner);
            managedOwner.getPrivateEventsInvited().add(managedPrivateEvent);
        }

        return managedPrivateEvent;

    }
}
