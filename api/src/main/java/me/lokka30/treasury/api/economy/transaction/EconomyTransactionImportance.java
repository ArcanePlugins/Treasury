package me.lokka30.treasury.api.economy.transaction;

/**
 * Allocates a certain 'importance' to a {@link EconomyTransaction}, which could be useful
 * information for the economy provider and consumers which process the transaction.
 * For example, a stream of low-importance transactions can be discarded from the transaction
 * history, which can save a significant amount of database space usage if there are plugins
 * installed that frequently send low-importance transactions.
 *
 * @author Nemo_64, lokka30
 * @since v1.0.0
 */
public enum EconomyTransactionImportance {

    /**
     * The associated {@link EconomyTransaction} is of low importance.
     * Such transactions can be discarded from long-term and even short-term history whenever
     * desired.
     * Example transaction: {@code Notch} earns {@code $0.23} from mining a {@code Diamond Ore}
     * block.
     *
     * @since v1.0.0
     */
    LOW,

    /**
     * The associated {@link EconomyTransaction} is of normal importance.
     * Most transactions shall fall under this importance.
     * Example transaction: {@code Dinnerbone} earns {@code $30} from completing a farming quest.
     *
     * @since v1.0.0
     */
    NORMAL,

    /**
     * The associated {@link EconomyTransaction} is of high importance.
     * It is suggested that the economy provider stores these transactions for as long as feasible.
     * Example transaction: {@code jeb_} earns {@code $50} from a payment sent by {@code Notch}.
     *
     * @since v1.0.0
     */
    HIGH
}
