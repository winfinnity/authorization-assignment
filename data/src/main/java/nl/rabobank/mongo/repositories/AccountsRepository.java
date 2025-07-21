package nl.rabobank.mongo.repositories;

import nl.rabobank.mongo.entities.AccountDto;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountsRepository extends MongoRepository<AccountDto, ObjectId> {

    @Query(value = "{ 'accountNumber' : ?0 }", fields = "{ 'accountHolderName' : 1, 'accountNumber' : 1 }")
    AccountDto findByAccountNumber(String accountNumber);

}
