package ai.ilikeplaces.entities;

import ai.ilikeplaces.doc.*;
import ai.ilikeplaces.util.EntityLifeCycleListener;

import javax.persistence.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: Jan 25, 2010
 * Time: 1:01:22 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Entity
@EntityListeners({EntityLifeCycleListener.class})
@NOTE(note = "Wall is initially a plain String. Each text is appended, hence non editable." +
        "A wall can be 'cleared' by a owner." +
        "This approach was taken to reduce TTM.")
public class Wall implements Clearance {
    public Long wallId = null;
    public Long clearance = 0L;
    public String wallContent = null;
    public List<Msg> wallMsgs = null;
    public Integer wallType = null;
    public String wallMetadata = null;

    final static public int wallTypeMISC = 0;
    final static public int wallTypeHuman = 1;


    final static public int WALL_LENGTH = 10240;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getWallId() {
        return wallId;
    }

    public void setWallId(Long wallId) {
        this.wallId = wallId;
    }

    @Column(length = WALL_LENGTH)
    public String getWallContent() {
        return wallContent;
    }

    public void setWallContent(String wallContent) {
        this.wallContent = wallContent;
    }

    @Transient
    public Wall setWallRContent(String wallContent) {
        this.wallContent = wallContent;
        return this;
    }

    @FIXME(issue = "Find out how to limit resultset to say, last 20, in order to limit the results fetched")
    @UNIDIRECTIONAL
    @TODO(task = "Move DESC ASC TO SOME STATIC CLASS FOR REUSE")
    @OrderBy(Msg.msgIdCOL + " DESC")
    @OneToMany(
            cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE},
            fetch = FetchType.EAGER)
    public List<Msg> getWallMsgs() {
        return wallMsgs;
    }

    public void setWallMsgs(final List<Msg> wallMsgs) {
        this.wallMsgs = wallMsgs;
    }

    @Override
    public Long getClearance() {
        return this.clearance;
    }

    public Integer getWallType() {
        return wallType;
    }

    public void setWallType(final Integer wallType) {
        this.wallType = wallType;
    }

    public Wall setWallTypeR(final Integer wallType) {
        this.wallType = wallType;
        return this;
    }

    public String getWallMetadata() {
        return wallMetadata;
    }

    public void setWallMetadata(final String wallMetadata) {
        this.wallMetadata = wallMetadata;
    }

    @Override
    public void setClearance(final Long clearance) {
        this.clearance = clearance;
    }


    @Override
    public String toString() {
        return "Wall{" +
                "wallId=" + wallId +
                '}';
    }

}