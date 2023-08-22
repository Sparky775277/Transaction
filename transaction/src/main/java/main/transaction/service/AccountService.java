package main.transaction.service;

import main.transaction.model.Account;
import main.transaction.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }


    public void setAccount(String name){
        setAccount(name, new BigDecimal(0));
    }
    public void setAccount(String name, BigDecimal amount){
        Account account = new Account();
        account.setName(name);
        account.setAmount(amount);
        accountRepository.save(account);
    }

    public void deleteAccount(String name){
        accountRepository.deleteAccountByName(name);
    }

    public Iterable<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public List<Account> findAccountByName(String name) {
        return accountRepository.findAccountsByName(name);
    }


}
