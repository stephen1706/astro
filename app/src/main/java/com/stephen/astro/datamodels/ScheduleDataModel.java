package com.stephen.astro.datamodels;

import java.util.List;

/**
 * Created by stephenadipradhana on 12/30/16.
 */
public class ScheduleDataModel {
    /**
     * eventID : 24242766
     * channelId : 50
     * channelStbNumber : 612
     * channelHD : false
     * channelTitle : Nickelodeon
     * epgEventImage : null
     * certification : U
     * displayDateTimeUtc : 2016-12-31 03:43:00.0
     * displayDateTime : 2016-12-31 11:43:00.0
     * displayDuration : 00:25:00
     * siTrafficKey : 1:1026:29561553
     * programmeTitle : New Year Special: Sanjay & Craig S3 Ep307
     * programmeId : 106925
     * episodeId : 106926
     * shortSynopsis : Time to party and hang out with your favourite Nick characters! The dudes celebrate their "friend-iversary" at a Frycade sleepover event, but the night doesn't go as Sanjay plans.
     * longSynopsis : null
     * actors :
     * directors :
     * producers :
     * genre : Kids
     * subGenre : Animation
     * live : false
     * premier : false
     * ottBlackout : false
     * highlight : null
     * contentId : null
     * contentImage : null
     * groupKey : 20024451
     * vernacularData : []
     */

    private String eventID;
    private int channelId;
    private String channelStbNumber;
    private String channelHD;
    private String channelTitle;
    private Object epgEventImage;
    private String certification;
    private String displayDateTimeUtc;
    private String displayDateTime;
    private String displayDuration;
    private String siTrafficKey;
    private String programmeTitle;
    private String programmeId;
    private String episodeId;
    private String shortSynopsis;
    private String longSynopsis;
    private String actors;
    private String directors;
    private String producers;
    private String genre;
    private String subGenre;
    private boolean live;
    private boolean premier;
    private boolean ottBlackout;
    private Object highlight;
    private int contentId;
    private Object contentImage;
    private int groupKey;
    private List<?> vernacularData;

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public String getChannelStbNumber() {
        return channelStbNumber;
    }

    public void setChannelStbNumber(String channelStbNumber) {
        this.channelStbNumber = channelStbNumber;
    }

    public String getChannelHD() {
        return channelHD;
    }

    public void setChannelHD(String channelHD) {
        this.channelHD = channelHD;
    }

    public String getChannelTitle() {
        return channelTitle;
    }

    public void setChannelTitle(String channelTitle) {
        this.channelTitle = channelTitle;
    }

    public Object getEpgEventImage() {
        return epgEventImage;
    }

    public void setEpgEventImage(Object epgEventImage) {
        this.epgEventImage = epgEventImage;
    }

    public String getCertification() {
        return certification;
    }

    public void setCertification(String certification) {
        this.certification = certification;
    }

    public String getDisplayDateTimeUtc() {
        return displayDateTimeUtc;
    }

    public void setDisplayDateTimeUtc(String displayDateTimeUtc) {
        this.displayDateTimeUtc = displayDateTimeUtc;
    }

    public String getDisplayDateTime() {
        return displayDateTime;
    }

    public void setDisplayDateTime(String displayDateTime) {
        this.displayDateTime = displayDateTime;
    }

    public String getDisplayDuration() {
        return displayDuration;
    }

    public void setDisplayDuration(String displayDuration) {
        this.displayDuration = displayDuration;
    }

    public String getSiTrafficKey() {
        return siTrafficKey;
    }

    public void setSiTrafficKey(String siTrafficKey) {
        this.siTrafficKey = siTrafficKey;
    }

    public String getProgrammeTitle() {
        return programmeTitle;
    }

    public void setProgrammeTitle(String programmeTitle) {
        this.programmeTitle = programmeTitle;
    }

    public String getProgrammeId() {
        return programmeId;
    }

    public void setProgrammeId(String programmeId) {
        this.programmeId = programmeId;
    }

    public String getEpisodeId() {
        return episodeId;
    }

    public void setEpisodeId(String episodeId) {
        this.episodeId = episodeId;
    }

    public String getShortSynopsis() {
        return shortSynopsis;
    }

    public void setShortSynopsis(String shortSynopsis) {
        this.shortSynopsis = shortSynopsis;
    }

    public String getLongSynopsis() {
        return longSynopsis;
    }

    public void setLongSynopsis(String longSynopsis) {
        this.longSynopsis = longSynopsis;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getDirectors() {
        return directors;
    }

    public void setDirectors(String directors) {
        this.directors = directors;
    }

    public String getProducers() {
        return producers;
    }

    public void setProducers(String producers) {
        this.producers = producers;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getSubGenre() {
        return subGenre;
    }

    public void setSubGenre(String subGenre) {
        this.subGenre = subGenre;
    }

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    public boolean isPremier() {
        return premier;
    }

    public void setPremier(boolean premier) {
        this.premier = premier;
    }

    public boolean isOttBlackout() {
        return ottBlackout;
    }

    public void setOttBlackout(boolean ottBlackout) {
        this.ottBlackout = ottBlackout;
    }

    public Object getHighlight() {
        return highlight;
    }

    public void setHighlight(Object highlight) {
        this.highlight = highlight;
    }

    public int getContentId() {
        return contentId;
    }

    public void setContentId(int contentId) {
        this.contentId = contentId;
    }

    public Object getContentImage() {
        return contentImage;
    }

    public void setContentImage(Object contentImage) {
        this.contentImage = contentImage;
    }

    public int getGroupKey() {
        return groupKey;
    }

    public void setGroupKey(int groupKey) {
        this.groupKey = groupKey;
    }

    public List<?> getVernacularData() {
        return vernacularData;
    }

    public void setVernacularData(List<?> vernacularData) {
        this.vernacularData = vernacularData;
    }
}
