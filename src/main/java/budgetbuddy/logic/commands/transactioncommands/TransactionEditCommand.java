package budgetbuddy.logic.commands.transactioncommands;

import static budgetbuddy.commons.util.CollectionUtil.requireAllNonNull;
import static budgetbuddy.logic.parser.CliSyntax.DATE_EXAMPLE;
import static budgetbuddy.logic.parser.CliSyntax.KEYWORD_SINGLE_ID;
import static budgetbuddy.logic.parser.CliSyntax.PREFIX_ACCOUNT;
import static budgetbuddy.logic.parser.CliSyntax.PREFIX_AMOUNT;
import static budgetbuddy.logic.parser.CliSyntax.PREFIX_CATEGORY;
import static budgetbuddy.logic.parser.CliSyntax.PREFIX_DATE;
import static budgetbuddy.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static budgetbuddy.logic.parser.CliSyntax.PREFIX_DIRECTION;
import static budgetbuddy.logic.parser.CliSyntax.PREFIX_RECURRENCE;

import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import budgetbuddy.commons.core.index.Index;
import budgetbuddy.logic.commands.Command;
import budgetbuddy.logic.commands.CommandCategory;
import budgetbuddy.logic.commands.CommandResult;
import budgetbuddy.logic.commands.exceptions.CommandException;
import budgetbuddy.model.AccountsManager;
import budgetbuddy.model.Model;
import budgetbuddy.model.account.Account;
import budgetbuddy.model.attributes.Category;
import budgetbuddy.model.attributes.Description;
import budgetbuddy.model.attributes.Direction;
import budgetbuddy.model.transaction.Amount;
import budgetbuddy.model.transaction.Transaction;
import budgetbuddy.model.transaction.exceptions.TransactionNotFoundException;
import javafx.collections.ObservableList;

/**
 * Represents a command to edit transactions.
 */
public class TransactionEditCommand extends Command {

    public static final String COMMAND_WORD = "txn edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits a transaction.\n"
            + "Parameters: "
            + KEYWORD_SINGLE_ID + " "
            + "Parameters: "
            + "[" + PREFIX_DIRECTION + "out|in] "
            + "[" + PREFIX_AMOUNT + "<amount>] "
            + "[" + PREFIX_DESCRIPTION + "<description>] "
            + "[" + PREFIX_ACCOUNT + "<account>] "
            + "[" + PREFIX_CATEGORY + "<category>] "
            + "[" + PREFIX_DATE + "<date>] "
            + "[" + PREFIX_RECURRENCE + "<d|w|m|y>]\n"
            + "Example: " + COMMAND_WORD + " "
            + "3 "
            + PREFIX_DIRECTION + "out "
            + PREFIX_AMOUNT + "10 "
            + PREFIX_DESCRIPTION + "apple  "
            + PREFIX_ACCOUNT + "food  "
            + PREFIX_CATEGORY + "fruits "
            + PREFIX_DATE + DATE_EXAMPLE + " "
            + PREFIX_RECURRENCE + "d\n";

    public static final String MESSAGE_SUCCESS = "Transaction %1$d edited.";
    public static final String MESSAGE_UNEDITED = "The edited transaction cannot be the same as the target";
    public static final String MESSAGE_FAILURE = "The transaction targeted for editing could not be found.";

    private final Index targetTransactionIndex;
    private final TransactionEditDescriptor updatedTransactionDescriptor;
    private Account targetAccount;

    public TransactionEditCommand(Index targetTransactionIndex,
                                  TransactionEditDescriptor updatedTransactionDescriptor,
                                  Account targetAccount) {
        requireAllNonNull(targetTransactionIndex, updatedTransactionDescriptor);
        this.targetTransactionIndex = targetTransactionIndex;
        this.updatedTransactionDescriptor = new TransactionEditDescriptor(updatedTransactionDescriptor);
        this.targetAccount = targetAccount;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireAllNonNull(model, model.getAccountsManager());

        AccountsManager accountsManager = model.getAccountsManager();
        try {

            ObservableList<Transaction> targetTransactionList = model.getFilteredTransactions();
            Transaction targetTransaction = targetTransactionList.get(targetTransactionIndex.getZeroBased());

            //the transaction will be deleted and re-added to allow for changing of accounts.
            //this is because transactions do not have references to their respective accounts.
            accountsManager.getActiveAccount().deleteTransaction(targetTransaction);

            //target account is set to the active account if not provided.
            if (targetAccount == null) {
                targetAccount = model.getAccountsManager().getActiveAccount();
            }

            Transaction updatedTransaction = updateTransaction(targetTransaction, updatedTransactionDescriptor,
                    targetAccount, model.getAccountsManager().getActiveAccount());


            accountsManager.getAccount(targetAccount.getName()).addTransaction(updatedTransaction);

        } catch (TransactionNotFoundException e) {
            throw new CommandException(MESSAGE_FAILURE);
        }

        return new CommandResult(
                String.format(MESSAGE_SUCCESS, targetTransactionIndex.getOneBased()), CommandCategory.TRANSACTION);
    }


    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof TransactionEditCommand)) {
            return false;
        }

        TransactionEditCommand otherCommand = (TransactionEditCommand) other;
        return targetTransactionIndex.equals(otherCommand.targetTransactionIndex)
                && updatedTransactionDescriptor.equals(otherCommand.updatedTransactionDescriptor)
                && targetAccount.equals(otherCommand.targetAccount);
    }

    /**
     * Creates and returns a new {@code Transaction} with the details of {@code targetTransaction},
     * edited with {@code transactionEditDescriptor}. Also validates that changes were made to the
     * transaction.
     */
    private Transaction updateTransaction(Transaction targetTransaction,
                                          TransactionEditDescriptor transactionEditDescriptor,
                                          Account targetAccount,
                                          Account updatedAccount) throws CommandException {

        Date updatedDate = transactionEditDescriptor.getDate().isPresent()
                ? transactionEditDescriptor.getDate().get()
                : targetTransaction.getDate();

        Amount updatedAmount = transactionEditDescriptor.getAmount().isPresent()
                ? transactionEditDescriptor.getAmount().get()
                : targetTransaction.getAmount();

        Direction updatedDirection = transactionEditDescriptor.getDirection().isPresent()
                ? transactionEditDescriptor.getDirection().get()
                : targetTransaction.getDirection();

        Description updatedDescription = transactionEditDescriptor.getDescription().isPresent()
                ? transactionEditDescriptor.getDescription().get()
                : targetTransaction.getDescription();

        Set<Category> updatedCategories = !transactionEditDescriptor.getCategories().isEmpty()
                ? transactionEditDescriptor.getCategories()
                : targetTransaction.getCategories();

        Transaction updatedTransaction = new Transaction(updatedDate, updatedAmount, updatedDirection,
                updatedDescription, updatedCategories);

        if (targetTransaction.equals(updatedTransaction) && targetAccount.equals(updatedAccount)) {
            //no updates were made
            throw new CommandException(MESSAGE_UNEDITED);
        }

        return updatedTransaction;
    }

    /**
     * Stores the details of the new Transaction.
     */
    public static class TransactionEditDescriptor {

        private Direction direction;
        private Amount amount;
        private Description description;
        private Set<Category> categories = new HashSet<>();
        private Date date;

        public TransactionEditDescriptor(){};

        public TransactionEditDescriptor (TransactionEditDescriptor toCopy) {
            this.date = toCopy.date;
            this.amount = toCopy.amount;
            this.direction = toCopy.direction;
            this.description = toCopy.description;
            this.categories = toCopy.categories;
        }

        public Optional<Date> getDate() {
            return Optional.ofNullable(date);
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public Optional<Amount> getAmount() {
            return Optional.ofNullable(amount);
        }

        public void setAmount(Amount amount) {
            this.amount = amount;
        }

        public Optional<Direction> getDirection() {
            return Optional.ofNullable(direction);
        }

        public void setDirection(Direction direction) {
            this.direction = direction;
        }

        public Optional<Description> getDescription() {
            return Optional.ofNullable(description);
        }

        public void setDescription(Description description) {
            this.description = description;
        }

        public Set<Category> getCategories() {
            return categories;
        }

        public void setCategories (Set<Category> categories) {
            this.categories = categories;
        }
    }
}