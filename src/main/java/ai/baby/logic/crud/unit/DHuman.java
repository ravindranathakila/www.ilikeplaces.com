package ai.baby.logic.crud.unit;

import ai.baby.util.AbstractSLBCallbacks;
import ai.ilikeplaces.entities.Human;
import ai.baby.util.jpa.CrudServiceLocal;
import ai.scribble.License;
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
public class DHuman extends AbstractSLBCallbacks implements DHumanLocal {

    @EJB
    private CrudServiceLocal<Human> crudServiceLocal_;

    public DHuman() {
    }

    final static Logger logger = LoggerFactory.getLogger(DHuman.class);

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void doDHuman(final String humanId) {
        crudServiceLocal_.delete(Human.class, humanId);
    }
}
