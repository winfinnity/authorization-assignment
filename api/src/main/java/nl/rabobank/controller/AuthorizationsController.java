package nl.rabobank.controller;

import java.util.List;

import nl.rabobank.authorizations.PowerOfAttorney;
import nl.rabobank.model.CreateAuthorizationRequest;
import nl.rabobank.services.AuthorizationServices;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/authorizations")
public class AuthorizationsController {

    private final AuthorizationServices authorizationServices;

    @GetMapping
    public ResponseEntity<List<PowerOfAttorney>> getAuthorizations(@RequestParam String grantee)  {
        return new ResponseEntity<>(authorizationServices.getAuthorizations(grantee), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PowerOfAttorney> createAuthorization (@RequestBody CreateAuthorizationRequest request)  {
        return new ResponseEntity<>(authorizationServices.createPowerOfAttorney(request), HttpStatus.CREATED);
    }
}
