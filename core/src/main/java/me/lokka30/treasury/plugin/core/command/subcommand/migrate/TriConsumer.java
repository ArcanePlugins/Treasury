package me.lokka30.treasury.plugin.core.command.subcommand.migrate;

interface TriConsumer<T, U, V> {

    void accept(T t, U u, V v);

}
