package budgetbuddy.logic.parser;

/**
 * Contains Command Line Interface (CLI) syntax definitions common to multiple commands
 */
public class CliSyntax {

    /* Prefix definitions */
    public static final Prefix PREFIX_ACCOUNT = new Prefix("a/");
    public static final Prefix PREFIX_ACTION = new Prefix("act/");
    public static final Prefix PREFIX_AMOUNT = new Prefix("x/");
    public static final Prefix PREFIX_CATEGORY = new Prefix("c/");
    public static final Prefix PREFIX_FROM = new Prefix("f/");
    public static final Prefix PREFIX_UNTIL = new Prefix("u/");
    public static final Prefix PREFIX_DATE = new Prefix("w/");
    public static final Prefix PREFIX_DESCRIPTION = new Prefix("d/");
    public static final Prefix PREFIX_NAME = new Prefix("n/");
    public static final Prefix PREFIX_PREDICATE = new Prefix("pred/");
    public static final Prefix PREFIX_SORT = new Prefix("s/");

    // Loans
    public static final Prefix PREFIX_PERSON = new Prefix("p/");
    public static final Prefix PREFIX_USER = new Prefix("me/");

    // Transactions
    public static final Prefix PREFIX_RECURRENCE = new Prefix("r/");

    // Date examples
    public static final String DATE_EXAMPLE = "4/20/2420";
}
