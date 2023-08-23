package main.transaction.service;


import main.transaction.exception.AccountNotFoundException;
import main.transaction.httprequest.HttpRequest;
import main.transaction.model.Account;
import main.transaction.parser.MyParser;
import main.transaction.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class ConversionService {

    private final AccountRepository accountRepository;

    public ConversionService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account convertMoney(long id, String valute) {

        Account account = accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException());

        BigDecimal valuteAmount = MyParser.parse(valute, new InputSource(new StringReader(HttpRequest.getRequest()))).getValue();

        account.setAmount(account.getAmount().divide(valuteAmount, 2, RoundingMode.HALF_UP));

        return account;

    }

}
