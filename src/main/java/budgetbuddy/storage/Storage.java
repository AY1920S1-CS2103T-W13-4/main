package budgetbuddy.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import budgetbuddy.commons.exceptions.DataConversionException;
import budgetbuddy.model.AccountsManager;
import budgetbuddy.model.LoansManager;
import budgetbuddy.model.Model;
import budgetbuddy.model.ReadOnlyUserPrefs;
import budgetbuddy.model.RuleManager;
import budgetbuddy.model.UserPrefs;
import budgetbuddy.storage.accounts.AccountsStorage;
import budgetbuddy.storage.loans.LoansStorage;
import budgetbuddy.storage.rules.RuleStorage;
import budgetbuddy.storage.scripts.ScriptsStorage;

/**
 * API of the Storage component
 */
public interface Storage extends LoansStorage, AccountsStorage, RuleStorage, ScriptsStorage, UserPrefsStorage {


    void save(Model model) throws IOException;

    @Override
    Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException;

    @Override
    void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException;

    @Override
    Path getLoansFilePath();

    @Override
    Path getAccountsFilePath();

    void saveLoans(LoansManager loansManager) throws IOException;

    @Override
    Path getRuleFilePath();

    @Override
    void saveLoans(LoansManager loansManager) throws IOException;

    @Override
    void saveAccounts(AccountsManager accountsManager) throws IOException;

    void saveRules(RuleManager ruleManager) throws IOException;
}
