(ns thompson.fsa
  (:require [clojure.set :as cset]))

;; atomic NFAs
(declare null)
(declare sym)

;; combinator NFAs
(declare alt)
(declare succ)
(declare star)

;; macro NFAs
(declare plus)
(declare opt)
(declare rng)

;; other NFAs
(comment (declare any))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn null []
  (let [start (atom false)
        finish (atom true)]
    {:graph-type :NFA
     :start start
     :finish finish
     :alphabet #{}
     :states #{start finish}
     :transition {start {:null #{finish}}}}))

(defn sym [s]
  (let [start (atom false)
        finish (atom true)]
    {:graph-type :NFA
     :start start
     :finish finish
     :alphabet #{s}
     :states #{start finish}
     :transition {start {s #{finish}}}}))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn alt
  ([] (null))
  ([& graphs]
   (let [start (atom false)
         finish (atom true)
         alphabet (apply cset/union (map :alphabet graphs))
         states (into #{start finish} (apply cset/union (map :states graphs)))
         transition (apply merge
                           (map :transition graphs))
         transition (reduce (fn [transition graph]
                              (update-in transition
                                         [start :null]
                                         (comp set conj) (:start graph)))
                            transition
                            graphs)
         transition (reduce (fn [transition graph]
                              (update-in transition
                                         [(:finish graph) :null]
                                         (comp set conj) finish))
                            transition
                            graphs)]
     (doseq [graph graphs]
       (reset! (:finish graph) false))
     {:graph-type :NFA
      :start start
      :finish finish
      :alphabet alphabet
      :states states
      :transition transition})))

(defn succ
  ([] (null))
  ([& graphs]
   (let [start (atom false)
         finish (atom true)
         alphabet (apply cset/union (map :alphabet graphs))
         states (into #{start finish} (apply cset/union (map :states graphs)))
         transition (apply merge
                           (map :transition graphs))
         pairs (map list
                    (butlast graphs)
                    (rest graphs))
         transition (reduce (fn [transition pair]
                              (let [f (:finish (first pair))
                                    s (:start (second pair))]
                                (update-in transition
                                           [f :null]
                                           (comp set conj) s)))
                            transition
                            pairs)
         transition (update-in transition
                               [start :null]
                               (comp set conj) (:start (first graphs)))
         transition (update-in transition
                               [(:finish (last graphs)) :null]
                               (comp set conj) finish)]
     (doseq [graph graphs]
       (reset! (:finish graph) false))
     {:graph-type :NFA
      :start start
      :finish finish
      :alphabet alphabet
      :states states
      :transition transition})))

(defn star [graph]
  (let [start (atom false)
        finish (atom true)
        alphabet (:alphabet graph)
        states (into #{start finish} (:states graph))
        transition (:transition graph)
        transition (update-in transition
                              [start :null]
                              (comp set conj) (:start graph))
        transition (update-in transition
                              [(:finish graph) :null]
                              (comp set conj) finish)
        transition (update-in transition
                              [start :null]
                              (comp set conj) finish)
        transition (update-in transition
                              [(:finish graph) :null]
                              (comp set conj) (:start graph))]
    (reset! (:finish graph) false)
    {:graph-type :NFA
     :start start
     :finish finish
     :alphabet alphabet
     :states states
     :transition transition}))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defmacro plus [graph]
  `(succ ~graph
         (star ~graph)))

(defmacro opt [graph]
  `(alt (null)
        ~graph))

(defmacro rng
  ([graph lower]
   `(succ ~@(repeat lower graph)))
  ([graph lower upper]
   (if (= upper :inf)
     `(succ ~@(repeat lower graph) (star ~graph))
     `(alt ~@(map #(apply list `succ (repeat %1 graph))
                  (range lower (+ upper 1)))))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(comment
  (defn any []
    (let [start (atom false)
          finish (atom true)]
      {:graph-type :NFA
       :start start
       :finish finish
       :alphabet #{:any}
       :states #{start finish}
       :transition {start {:any #{finish}}}})))
