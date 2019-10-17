package budgetbuddy.logic.commands.loancommands;

import static budgetbuddy.commons.util.CollectionUtil.requireAllNonNull;

import java.util.List;

import budgetbuddy.commons.core.index.Index;
import budgetbuddy.logic.commands.CommandResult;
import budgetbuddy.logic.commands.exceptions.CommandException;
import budgetbuddy.model.Model;
import budgetbuddy.model.loan.Status;

/**
 * Marks one or more loans as unpaid.
 */
public class UnpaidLoanCommand extends UpdateStatusCommand {

    public static final String COMMAND_WORD = "loan unpaid";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Marks one or more loans as unpaid.\n"
            + "Parameters: "
            + "<person number>[.(<loan numbers...>)] "
            + "...\n"
            + "Example: " + COMMAND_WORD + " "
            + "1.(1 2 5) "
            + "3";

    public static final String MESSAGE_SUCCESS = "Loan(s) marked as unpaid.";
    public static final String MESSAGE_FAILURE = "No such loan(s) found.";

    public UnpaidLoanCommand(
            List<Index> targetPersonsIndices, List<Index> targetLoansIndices) throws CommandException {
        super(targetPersonsIndices, targetLoansIndices);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireAllNonNull(model, model.getLoansManager());

        List<PersonLoanIndexPair> pairsNotFound = updateStatuses(model.getLoansManager(), Status.UNPAID);
        String result = constructMultiLoanResult(pairsNotFound, MESSAGE_SUCCESS, MESSAGE_FAILURE);

        return new CommandResult(result);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof UnpaidLoanCommand)) {
            return false;
        }

        UnpaidLoanCommand otherCommand = (UnpaidLoanCommand) other;
        return personLoanIndexPairs.equals(otherCommand.personLoanIndexPairs);
    }
}
