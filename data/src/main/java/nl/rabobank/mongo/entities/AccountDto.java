package nl.rabobank.mongo.entities;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "accounts")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {

    @Id
    ObjectId id;
    @Indexed(unique = true)
    String accountNumber;
    String accountHolderName;
    Double balance;
    AccountType accountType;
}
