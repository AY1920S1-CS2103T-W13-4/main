package budgetbuddy.logic.commands.loancommands;

import static budgetbuddy.commons.util.CollectionUtil.requireAllNonNull;

import java.util.List;

import budgetbuddy.commons.core.index.Index;
import budgetbuddy.logic.commands.CommandResult;
import budgetbuddy.logic.commands.exceptions.CommandException;
import budgetbuddy.model.Model;
import budgetbuddy.model.loan.Status;

/**
 * Marks one or more loans as paid.
 */
public class PaidLoanCommand extends UpdateStatusCommand {

    public static final String COMMAND_WORD = "loan paid";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Marks one or more loans as paid.\n"
            + "Parameters: "
            + "<person number>[.(<loan numbers...>)] "
            + "...\n"
            + "Example: " + COMMAND_WORD + " "
            + "1.(1 2 5) "
            + "3";

    public static final String MESSAGE_SUCCESS = "Loan(s) marked as paid.";
    public static final String MESSAGE_FAILURE = "No such loan(s) found.";

    public PaidLoanCommand(
            List<Index> targetPersonsIndices, List<Index> targetLoansIndices) throws CommandException {
        super(targetPersonsIndices, targetLoansIndices);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireAllNonNull(model, model.getLoansManager());

        List<PersonLoanIndexPair> pairsNotFound = updateStatuses(model.getLoansManager(), Status.PAID);
        String result = constructMultiLoanResult(pairsNotFound, MESSAGE_SUCCESS, MESSAGE_FAILURE);

        return new CommandResult(result);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof PaidLoanCommand)) {
            return false;
        }

        PaidLoanCommand otherCommand = (PaidLoanCommand) other;
        return personLoanIndexPairs.equals(otherCommand.personLoanIndexPairs);
    }
}
