package DTO;

import java.sql.Timestamp;

public class ReplyDTO {
    private long id;
    private String writer;
    private String contents;
    private Timestamp writeDate;
    private long parentId;
    private int likeCount;

    public ReplyDTO() {
    }

    public ReplyDTO(long id, String writer, String contents, Timestamp writeDate, long parentId, int likeCount) {
        this.id = id;
        this.writer = writer;
        this.contents = contents;
        this.writeDate = writeDate;
        this.parentId = parentId;
        this.likeCount = likeCount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public Timestamp getWriteDate() {
        return writeDate;
    }

    public void setWriteDate(Timestamp writeDate) {
        this.writeDate = writeDate;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }
}
