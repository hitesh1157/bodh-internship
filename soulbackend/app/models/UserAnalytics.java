package models;

import org.mongodb.morphia.annotations.Reference;

import java.util.Date;

/**
 * Created by harshitjain on 10/06/17.
 */
public class UserAnalytics extends BaseModel {
    @Reference
    private User user;
    private String firstApkVersion;
    private Date firstAppOpened;
    private String currentApkVersion;
    private String currentAndroidVersion;
    private Long durationLecturePlayedInSec;
    private Date lastAppOpened;
    private Integer uniqueLecturePlayed;
    private Integer lectureDownloaded;
    private Integer appOpenedCount;
    private String deviceId;

    public String getCurrentAndroidVersion() {
        return currentAndroidVersion;
    }

    public void setCurrentAndroidVersion(String currentAndroidVersion) {
        this.currentAndroidVersion = currentAndroidVersion;
    }

    public String getCurrentApkVersion() {
        return currentApkVersion;
    }

    public void setCurrentApkVersion(String currentApkVersion) {
        this.currentApkVersion = currentApkVersion;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getFirstApkVersion() {
        return firstApkVersion;
    }

    public void setFirstApkVersion(String firstApkVersion) {
        this.firstApkVersion = firstApkVersion;
    }

    public Long getDurationLecturePlayedInSec() {
        return durationLecturePlayedInSec;
    }

    public void setDurationLecturePlayedInSec(Long durationLecturePlayedInSec) {
        this.durationLecturePlayedInSec = durationLecturePlayedInSec;
    }

    public Date getFirstAppOpened() {
        return firstAppOpened;
    }

    public void setFirstAppOpened(Date firstAppOpened) {
        this.firstAppOpened = firstAppOpened;
    }

    public Date getLastAppOpened() {
        return lastAppOpened;
    }

    public void setLastAppOpened(Date lastAppOpened) {
        this.lastAppOpened = lastAppOpened;
    }

    public Integer getUniqueLecturePlayed() {
        return uniqueLecturePlayed;
    }

    public void setUniqueLecturePlayed(Integer uniqueLecturePlayed) {
        this.uniqueLecturePlayed = uniqueLecturePlayed;
    }

    public Integer getLectureDownloaded() {
        return lectureDownloaded;
    }

    public void setLectureDownloaded(Integer lectureDownloaded) {
        this.lectureDownloaded = lectureDownloaded;
    }

    public Integer getAppOpenedCount() {
        return appOpenedCount;
    }

    public void setAppOpenedCount(Integer appOpenedCount) {
        this.appOpenedCount = appOpenedCount;
    }
}
