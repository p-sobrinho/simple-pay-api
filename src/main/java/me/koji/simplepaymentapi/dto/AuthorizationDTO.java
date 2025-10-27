package me.koji.simplepaymentapi.dto;

public record AuthorizationDTO(String status, AuthorizationDataDTO data) {
    public record AuthorizationDataDTO(Boolean authorization) {}
}
