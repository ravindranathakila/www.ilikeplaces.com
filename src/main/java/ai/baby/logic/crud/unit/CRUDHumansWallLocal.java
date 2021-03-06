package ai.baby.logic.crud.unit;

import ai.baby.util.exception.DBDishonourCheckedException;
import ai.baby.util.exception.DBFetchDataException;
import ai.ilikeplaces.entities.HumansWall;
import ai.ilikeplaces.entities.Msg;
import ai.ilikeplaces.entities.etc.RefreshSpec;
import ai.scribble.License;

import javax.ejb.Local;
import java.util.List;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface CRUDHumansWallLocal {

    /**
     * Warning: lazy initialized fields ignored
     *
     * @param humanId
     * @return
     */
    public HumansWall doDirtyRHumansWall(final String humanId, final RefreshSpec wallRefreshSpec) throws DBFetchDataException;

    public HumansWall doRHumansWallRefreshed(final String humanId);

    public List<Msg> doRHumansWallLastEntries(final String humanId, final Integer numberOfEntriesToFetch);

    public Long doDirtyRHumansWallID(final String humanId) throws DBDishonourCheckedException;

    public HumansWall doRHumansWall(String humanId);

    public void doUpdateMetadata(final long wallId, final String key, final String value);

    public Msg doUHumansWallMsgs(final String humanId, final Msg msg);
}
