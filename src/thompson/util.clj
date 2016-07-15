(ns thompson.util)

;;; getters
(declare get-graph-type)
(declare get-start-state)
(declare get-final-state)
(declare get-alphabet)
(declare get-states)
(declare get-transition-map)

(declare final?)


(defn get-graph-type [fsa]
  (:graph-type fsa))

(defn get-start-state [fsa]
  (:start fsa))

(defn get-final-state [fsa]
  (:finish fsa))

(defn get-alphabet [fsa]
  (:alphabet fsa))

(defn get-states [fsa]
  (:states fsa))

(defn get-transition-map [fsa]
  (:transition fsa))

(defn final? [state]
  (if (set? state)
    (reduce #(or %1 @%2) false state)
    @state))
