package nl.rabobank.mongo.entities;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;

@Document(collection = "powerOfAttornies")
@CompoundIndex(
    name = "unique_grantor_grantee_account_authorization",
    def = "{'grantor': 1, 'grantee': 1, 'account': 1, 'authorization': 1}",
    unique = true
)
@Data
@Builder
public class PowerOfAttorneyDto{

    String grantor;
    @Indexed
    String grantee;
    @DBRef
    AccountDto account;
    String authorization;

}
