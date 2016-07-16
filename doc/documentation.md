# Documentation

## Constructors

`(null)`: Returns an FSA that only accepts an empty sequence.

`(sym s)`: Returns an FSA that only accepts `s`, where `s` can be
a string or single character.

`(alt & graphs)`: Returns an FSA that accepts anything that is
accepted by any of the FSAs `graphs`. Behaves as `null`
if no subgraphs are given.

`(succ & graphs)`: Returns an FSA that accepts anything that
is accepted by all the FSAs `graphs` in series. Behaves as `null`
if no subgraphs are given.

`(star graph)`: Returns an FSA that accepts anything that
is accepted by the FSA `graph` zero or more times.

`(plus graph)`: Returns an FSA that accepts anything that
is accepted by the FSA `graph` one or more times.

`(opt graph)`: Returns an FSA that accepts anything that
is accepted by the FSA `graph` or an empty sequence.

`(rng graph number)`: Returns an FSA that accepts anything that
is accepted by the FSA `graph` replicated `number` times in series.

`(rng graph lower upper)`: Returns an FSA that accepts anything that
is accepted by the FSA `graph` replicated between `lower` and
`upper` (inclusive) times in series. If `upper` is `:inf`, the FSA
accepts anything that is accepted by `graph` replicated at least
`lower` times in series.

## Transformers

`(nfa->dfa nfa)`: Returns a deterministic FSA that accepts everything
that is accepted by the non-deterministic FSA `nfa`. If `nfa` is
already deterministic, an exception is thrown.

## Inspectors

`(get-graph-type fsa)`: Returns the keyword `:NFA` if `fsa` is non-deterministic
or `:DFA` if deterministic.

`(get-start-state fsa)`: Returns the start state of the `fsa`.

`(get-final-state fsa)`: Returns the final state(s) of the `fsa`.

`(get-alphabet fsa)`: Returns the alphabet of the `fsa`.

`(get-states fsa)`: Returns all the states of the `fsa`.

`(get-transition-map fsa)`: Returns the transition map for the `fsa`.

## Checkers

`(accepts? fsa stream)`: Returns true if and only if the FSA `fsa`
accepts `stream`, which may be a string, list, or vector.

`(final? state)`: Returns true if and only `state` is a final state.
