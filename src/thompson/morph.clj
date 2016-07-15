(ns thompson.morph
  (:require [thompson.util :as tutil]))


(declare null-closure)
(declare alpha-null-closure)
(declare nfa->dfa)


(defn null-closure [state nfa]
  (loop [transition (:transition nfa)
         closure #{state}
         checkpoints #{state}]
    (let [next-checkpoints (apply concat
                                  (map #(get-in transition [%1 :null])
                                       checkpoints))
          next-closure (into closure next-checkpoints)]
      (if (= closure next-closure)
        closure
        (recur transition
               next-closure
               (set next-checkpoints))))))

(defn alpha-null-closure [states nfa c]
  (let [transition (:transition nfa)
        alpha-states (set (apply concat
                                 (map #(get-in transition [%1 c])
                                      states)))]
    (set (apply concat
                (map #(null-closure %1 nfa)
                     alpha-states)))))

(defn nfa->dfa [nfa]
  (loop [queue (list (null-closure (:start nfa) nfa))
         alphabet (:alphabet nfa)
         dfa-states #{}
         dfa-transition {}]
    (if (empty? queue)
      {:graph-type :DFA
       :start (null-closure (:start nfa) nfa)
       :finish (set (filter #(tutil/final? %1) dfa-states))
       :alphabet alphabet
       :states dfa-states
       :transition dfa-transition}
      (let [state (peek queue)
            queue (pop queue)]
        (if (contains? dfa-states state)
          (recur queue alphabet dfa-states dfa-transition)
          (let [entries (for [c alphabet]
                          (list c (alpha-null-closure state nfa c)))
                entries (filter #(not (empty? (second %1))) entries)
                queue (into queue (map second entries))
                dfa-states (conj dfa-states state)
                dfa-transition (reduce #(assoc-in %1
                                                  [state (first %2)]
                                                  (second %2))
                                       dfa-transition
                                       entries)]
            (recur queue alphabet dfa-states dfa-transition)))))))
