package main.transaction.service;

import main.transaction.exception.AccountNotFoundException;
import main.transaction.model.Account;
import main.transaction.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional
public class MoneyService {

    private final AccountRepository accountRepository;

    public MoneyService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void addMoney(long id, BigDecimal amount) {
        if (accountRepository.checkAccount(id) && amount.compareTo(BigDecimal.valueOf(0)) >= 0) {
            Account account = accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException());
            BigDecimal newAccountAmount = account.getAmount().add(amount);
            accountRepository.changeAmount(id, newAccountAmount);
        } else {
            throw new AccountNotFoundException();
        }
    }

    public void subtractMoney(long id, BigDecimal amount) {
        if (accountRepository.checkAccount(id)) {
            Account account = accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException());
            BigDecimal newAccountAmount = account.getAmount().subtract(amount);
            if (newAccountAmount.compareTo(BigDecimal.valueOf(0)) < 0 || amount.compareTo(BigDecimal.valueOf(0)) < 0) {
                throw new RuntimeException();
            }
            accountRepository.changeAmount(id, newAccountAmount);
        } else {
            throw new AccountNotFoundException();
        }
    }

    public void transferMoney(
            long idSender,
            long idReceiver,
            BigDecimal amount
    ) {
        Account sender = accountRepository.findById(idSender).orElseThrow(() -> new AccountNotFoundException());

        Account receiver = accountRepository.findById(idReceiver).orElseThrow(() -> new AccountNotFoundException());

        BigDecimal senderNewAmount = sender.getAmount().subtract(amount);

        BigDecimal receiverNewAmount = receiver.getAmount().add(amount);

        if (amount.compareTo(BigDecimal.valueOf(0)) < 0 || senderNewAmount.compareTo(BigDecimal.valueOf(0)) < 0){
            throw new RuntimeException();
        }

        accountRepository.changeAmount(idSender, senderNewAmount);

        accountRepository.changeAmount(idReceiver, receiverNewAmount);

    }

}
