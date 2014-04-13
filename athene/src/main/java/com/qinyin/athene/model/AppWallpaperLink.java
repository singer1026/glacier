package com.qinyin.athene.model;

public class AppWallpaperLink extends AdvanceModel {
    public static final String FILE_PATH = "filePath";
    private String filePath;
    public static final String FILE_LINK = "fileLink";
    private String fileLink;

    public static final String SQL_FILE_NAME = "AppWallpaperLink";

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileLink() {
        return fileLink;
    }

    public void setFileLink(String fileLink) {
        this.fileLink = fileLink;
    }
}