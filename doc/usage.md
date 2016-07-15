# Usage

Begin with the following in a REPL to get the necessary functions:

```clojure
(require '[thompson.core :refer :all])
```

With simple constructors, you can build FSA rather quickly. For
example, we can create the FSA for the regular expression
`(a|b|c)*` easily:

```clojure
(def fsa (star (alt (sym \a)
                    (sym \b)
                    (sym \c))))
```

Now we can check if this FSA can accepts certain strings:

```clojure
(accepts? fsa "a") ;; => true
(accepts? fsa "b") ;; => true
(accepts? fsa "c") ;; => true
(accepts? fsa "abc") ;; => true
(accepts? fsa "bbc") ;; => true
(accepts? fsa "cab") ;; => true

(accepts? fsa "these") ;; => false
(accepts? fsa "will") ;; => false
(accepts? fsa "fail") ;; => false
```

When using the constructors, the resulting FSAs are non-deterministic.
To get deterministic FSAs (which tend to be faster
than their equivalent non-deterministic counterparts), we simply call the
provided transform function:

```clojure
(def dfa (nfa->dfa fsa))
```

We can run the same check as above with `dfa` in place of `fsa`
and get the same results.

We can check more than strings. Any finited ordered sequence
(i.e. lists, vectors, strings) can be checking targets, provided
the FSA is build correctly. For example, we'll change the characters
to string in the original FSA:

```clojure
(def fsa (star (alt (sym "a")
                    (sym "b")
                    (sym "c"))))
```

And now we can check if other data types are accepted:

```clojure
(accepts? fsa '("a" "b" "c")) ;; => true
(accepts? fsa ["a" "b" "c"]) ;; => true
```

Finally, it is possible to inspect an FSA using a number of
getter functions like `get-start-state` and `get-alphabet`.

```clojure
(def start-start (get-start-state)) ;; => #object[clojure.lang.Atom 0x577b8d3d {:status :ready, :val false}]
(get-alphabet fsa) ;; => #{\a \b \c}
```
