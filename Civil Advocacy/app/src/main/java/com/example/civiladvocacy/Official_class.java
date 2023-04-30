package com.example.civiladvocacy;

import java.io.Serializable;

public class Official_class implements Serializable {
    String off_imageUrl;
    private String off_party;
    private String off_position;
    private String off_address;
    private String off_name;
    private String off_email;
    private String off_phoneNumber;
    private String off_link_fb;
    private String off_link_tw;
    private static String off_link_youtube;
    private String off_partyLogo;
    private String off_presentLocation;
    private String off_webUrl;

    public Official_class(String imageUrl, String party, String position, String phoneNumber, String name, String address, String email, String presentLocation, String facebook_Link, String youtube_Link, String twitter_Link, String webUrl) {
        this.off_imageUrl = imageUrl;
        this.off_party = party;
        this.off_position = position;
        this.off_phoneNumber = phoneNumber;
        this.off_name = name;
        this.off_address = address;
        this.off_email = email;
        this.off_presentLocation = presentLocation;
        this.off_link_fb = facebook_Link;
        this.off_link_tw = twitter_Link;
        this.off_link_youtube = youtube_Link;
        this.off_webUrl = webUrl;
    }

    public String getImageUrl() {
        return off_imageUrl;
    }

    public String getParty() {
        return off_party;
    }

    public String getPosition() {
        return off_position;
    }

    public String getAddress() {
        return off_address;
    }

    public String getName() {
        return off_name;
    }

    public String getEmail() {
        return off_email;
    }

    public String getPhoneNumber() {
        return off_phoneNumber;
    }

    public String getLinkFacebook() {
        return off_link_fb;
    }

    public String getLinkTwitter() {return off_link_tw;}

    public static String getLinkYouTube() {
        return off_link_youtube;
    }

    public String getPartyLogo() {
        return off_partyLogo;
    }

    public String getPresentLocation() {
        return off_presentLocation;
    }

    public String getWebUrl() {
        return off_webUrl;
    }

    public void setPartyLogo(String partyLogo) {
        this.off_partyLogo = partyLogo;
    }
}
