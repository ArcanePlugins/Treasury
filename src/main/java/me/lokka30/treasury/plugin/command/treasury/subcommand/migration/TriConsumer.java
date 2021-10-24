package me.lokka30.treasury.plugin.command.treasury.subcommand.migration;

interface TriConsumer<T, U, V> {

    void accept(T t, U u, V v);

}
