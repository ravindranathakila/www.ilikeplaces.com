package ai.baby.logic.crud.unit;

import ai.ilikeplaces.entities.HumansPrivateEvent;
import ai.baby.util.exception.DBDishonourCheckedException;
import ai.baby.util.jpa.CrudServiceLocal;
import ai.baby.util.AbstractSLBCallbacks;
import ai.scribble.License;
import ai.scribble._note;
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
@_note(note = "SEE CRUDSERVICE WHERE TO SUPPORT READ AND DIRTY READ, THE TX TYPE IS SUPPORTS.")
@Stateless
public class RHumansPrivateEvent extends AbstractSLBCallbacks implements RHumansPrivateEventLocal {


    @EJB
    private CrudServiceLocal<HumansPrivateEvent> humansPrivateEventCrudServiceLocal_;

    public RHumansPrivateEvent() {
    }


    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public HumansPrivateEvent doNTxRHumansPrivateEvent(String humanId) throws DBDishonourCheckedException {
        final HumansPrivateEvent hpe = humansPrivateEventCrudServiceLocal_.findBadly(HumansPrivateEvent.class, humanId);
        hpe.getPrivateEventsViewed().size();
        hpe.getPrivateEventsOwned().size();
        hpe.getPrivateEventsInvited().size();
        hpe.getPrivateEventsRejected().size();
        return hpe;
    }

    final static Logger logger = LoggerFactory.getLogger(RHumansPrivateEvent.class);
}
