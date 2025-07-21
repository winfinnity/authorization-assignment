package nl.rabobank.model;

import jakarta.validation.constraints.NotNull;

import nl.rabobank.authorizations.Authorization;

public record CreateAuthorizationRequest(@NotNull String grantor,
                                         @NotNull String grantee,
                                         @NotNull String accountNumber,
                                         @NotNull Authorization authorization) {
}
