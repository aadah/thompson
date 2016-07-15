(ns thompson.core
  (:require [thompson.fsa :as tfsa]
            [thompson.search :as tsearch]
            [thompson.morph :as tmorph]
            [thompson.util :as tutil]))


(def null tfsa/null)
(def sym tfsa/sym)
(def alt tfsa/alt)
(def succ tfsa/succ)
(def star tfsa/star)
(def ^:macro plus #'tfsa/plus)
(def ^:macro opt #'tfsa/opt)
(def ^:macro rng #'tfsa/rng)
(comment (def any tfsa/any))

(def accepts? tsearch/accepts?)
(def nfa-accepts? tsearch/nfa-accepts?)
(def dfa-accepts? tsearch/dfa-accepts?)
(comment (def search tsearch/search))

(def null-closure tmorph/null-closure)
(def alpha-null-closure tmorph/null-closure)
(def nfa->dfa tmorph/nfa->dfa)

(def get-graph-type tutil/get-graph-type)
(def get-start-state tutil/get-start-state)
(def get-final-state tutil/get-final-state)
(def get-alphabet tutil/get-alphabet)
(def get-states tutil/get-states)
(def get-transition-map tutil/get-transition-map)
(def final? tutil/final?)
