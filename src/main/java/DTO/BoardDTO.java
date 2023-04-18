package DTO;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public class BoardDTO {
    private long id;
    private String writer;
    private String title;
    private String contents;
    private int viewCount;
    private Timestamp writeDate;

    public BoardDTO(long id, String writer, String title, String contents, int viewCount, Timestamp writeDate) {
        this.id = id;
        this.writer = writer;
        this.title = title;
        this.contents = contents;
        this.viewCount = viewCount;
        this.writeDate = writeDate;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public void setWriteDate(Timestamp writeDate) {
        this.writeDate = writeDate;
    }

    public String getWriteDate(){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm");
        return writeDate.toLocalDateTime().plusHours(9).format(dateTimeFormatter);
    }

    public String getFormattedWriteDate(){
        Duration afterWrite = Duration.between(writeDate.toLocalDateTime() ,LocalDateTime.now());
        if (afterWrite.toMinutes() < 1L){
            return "1분 이내";
        } else if (afterWrite.toMinutes() < 5L) {
            return "5분 이내";
        } else if (afterWrite.toHours() < 1L) {
            return "1시간 이내";
        } else if (afterWrite.toDays() < 1L) {
            return "오늘 내";
        } else {
            return getWriteDate();
        }

    }

}
