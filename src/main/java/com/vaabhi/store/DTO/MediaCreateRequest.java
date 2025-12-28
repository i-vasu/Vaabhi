package com.vaabhi.store.DTO;

import jakarta.validation.constraints.NotBlank;


public class MediaCreateRequest {
    @NotBlank
    public String url;
    @NotBlank public String type; // IMAGE or VIDEO
    public String altText;
    public Boolean primaryImage = false;
}
