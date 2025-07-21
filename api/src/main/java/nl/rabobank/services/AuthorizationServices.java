package nl.rabobank.services;

import java.util.List;

import nl.rabobank.account.Account;
import nl.rabobank.account.PaymentAccount;
import nl.rabobank.account.SavingsAccount;
import nl.rabobank.authorizations.Authorization;
import nl.rabobank.authorizations.PowerOfAttorney;
import nl.rabobank.exceptions.DuplicateAuthorizationException;
import nl.rabobank.exceptions.InvalidAccountException;
import nl.rabobank.model.CreateAuthorizationRequest;
import nl.rabobank.mongo.entities.AccountDto;
import nl.rabobank.mongo.entities.AccountType;
import nl.rabobank.mongo.entities.PowerOfAttorneyDto;
import nl.rabobank.mongo.repositories.AccountsRepository;
import nl.rabobank.mongo.repositories.PowerOfAttorneysRepository;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class AuthorizationServices {

    private final AccountsRepository accountsRepository;
    private final PowerOfAttorneysRepository authorizationsRepository;

    public List<PowerOfAttorney> getAuthorizations(String grantee){

        return authorizationsRepository.findByGrantee(grantee)
                .stream()
                .filter(dto -> dto.getAccount() != null)
                .map(this::mapToPowerOfAttorney)
                .toList();
    }

    public PowerOfAttorney createPowerOfAttorney(CreateAuthorizationRequest request) {
        try {
            var accountDto = validateAccount(request.accountNumber());
            var powerOfAttorneyDto = authorizationsRepository.insert(mapToPowerOfAttorneyDTO(request, accountDto));
            return mapToPowerOfAttorney(powerOfAttorneyDto);
        }
        catch (DuplicateKeyException e) {
            throw new DuplicateAuthorizationException();
        }
    }

    private PowerOfAttorney mapToPowerOfAttorney(PowerOfAttorneyDto dto) {
        return PowerOfAttorney.builder()
            .account(mapToAccount(dto.getAccount()))
            .granteeName(dto.getGrantee())
            .grantorName(dto.getGrantor())
            .authorization(Authorization.valueOf(dto.getAuthorization()))
            .build();
    }

    private Account mapToAccount(AccountDto dto){
        return dto.getAccountType() == AccountType.PAYMENT?mapToAccountType(dto, PaymentAccount.class):mapToAccountType(dto, SavingsAccount.class);
    }

    private <T extends Account> T mapToAccountType(AccountDto dto, Class<T> type) {
        if (type == SavingsAccount.class) {
            return type.cast(new SavingsAccount(dto.getAccountNumber(), dto.getAccountHolderName(), null));
        } else if (type == PaymentAccount.class) {
            return type.cast(new PaymentAccount(dto.getAccountNumber(), dto.getAccountHolderName(), null));
        }
        throw new IllegalArgumentException("Unsupported account type: " + type);
    }

    private AccountDto validateAccount(String accountNumber){
        var accountDto = accountsRepository.findByAccountNumber(accountNumber);
        if (accountDto == null) {
            throw new InvalidAccountException(accountNumber + "does not exist.");
        }
        return accountDto;
    }

    private PowerOfAttorneyDto mapToPowerOfAttorneyDTO(CreateAuthorizationRequest request, AccountDto accountDto) {
       return PowerOfAttorneyDto.builder()
            .grantor(request.grantor())
            .grantee(request.grantee())
            .account(accountDto)
            .authorization(request.authorization().toString())
            .build();
    }
}
