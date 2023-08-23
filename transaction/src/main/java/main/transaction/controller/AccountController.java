package main.transaction.controller;

import main.transaction.dto.TransferRequest;
import main.transaction.model.Account;
import main.transaction.service.AccountService;
import main.transaction.service.ConversionService;
import main.transaction.service.MoneyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AccountController {



    private final AccountService accountService;

    private final MoneyService moneyService;

    private final ConversionService conversionService;

    public AccountController(AccountService accountService, MoneyService moneyService, ConversionService conversionService) {
        this.accountService = accountService;
        this.moneyService = moneyService;
        this.conversionService = conversionService;
    }

    @PostMapping("/transfer")
    public void transferMoney(
            @RequestBody TransferRequest body
            ){
        moneyService.transferMoney(
                body.getSenderAccountId(),
                body.getReceiverAccountId(),
                body.getAmount()
        );
    }

    @GetMapping("/accounts")
    public Iterable<Account> getAllAccounts(
            @RequestParam(required = false) String name
    ) {
        if (name == null) {
            return accountService.getAllAccounts();
        } else {
            return accountService.findAccountByName(name);
        }
    }



    @PostMapping("/set")
    public void setAccount(
            @RequestBody Account account
    ){
        accountService.setAccount(account.getName());
    }

    @PostMapping("/delete")
    public void deleteAccount(
            @RequestBody Account account
    ){
        accountService.deleteAccount(account.getName());
    }

    @PostMapping("/add")
    public void addMoney(
            @RequestBody Account account
    ){
        moneyService.addMoney(account.getId(), account.getAmount());
    }

    @PostMapping("/subtract")
    public void subtractMoney(
            @RequestBody Account account
    ){
        moneyService.subtractMoney(account.getId(), account.getAmount());
    }

    @GetMapping("/convert")
    public ResponseEntity<Account> convert(@RequestParam long id,
                                           @RequestParam String valute
    ){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(conversionService.convertMoney(id,valute));
    }

}
