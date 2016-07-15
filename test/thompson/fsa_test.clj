(ns thompson.fsa-test
  (:require [clojure.test :refer :all]
            [thompson.fsa :refer :all]
            [thompson.search :refer [nfa-accepts?]]))


(def null-fsa (null))
(deftest null-test
  (testing "Testing null FSA."
    (is (= (nfa-accepts? null-fsa "")
           true)
        "Matches null string.")
    (is (= (nfa-accepts? null-fsa "a")
           false)
        "Fails on non-null string.")))

(def sym-fsa (sym \a))
(deftest sym-test
  (testing "Testing sym FSA."
    (is (= (nfa-accepts? sym-fsa "a")
           true)
        "Matches on same string.")
    (is (= (nfa-accepts? sym-fsa "b")
           false)
        "Fails on different string.")
    (is (= (nfa-accepts? sym-fsa "")
           false)
        "Fails on null string.")))

(def alt-fsa-1 (alt))
(def alt-fsa-2 (alt (sym \a)
                    (sym \b)))
(deftest alt-test
  (testing "Testing alt FSA."
    (is (= (nfa-accepts? alt-fsa-1 "")
           true)
        "Matches null string when using no alternatives.")
    (is (= (nfa-accepts? alt-fsa-2 "a")
           true)
        "Matches first alternative.")
    (is (= (nfa-accepts? alt-fsa-2 "b")
           true)
        "Matches second alternative.")
    (is (= (nfa-accepts? alt-fsa-2 "c")
           false)
        "Fails on unavailable alternative.")
    (is (= (nfa-accepts? alt-fsa-2 "")
           false)
        "Fails on null string.")))


(def succ-fsa-1 (succ))
(def succ-fsa-2 (succ (sym \a)
                      (sym \b)))
(def succ-fsa-3 (succ (sym \a)))
(deftest succ-test
  (testing "Testing succ FSA."
    (is (= (nfa-accepts? succ-fsa-1 "")
           true)
        "Matches on null string when using no sub-FSAs.")
    (is (= (nfa-accepts? succ-fsa-2 "ab")
           true)
        "Matches on valid string.")
    (is (= (nfa-accepts? succ-fsa-2 "ba")
           false)
        "Fails on reverse string.")
    (is (= (nfa-accepts? succ-fsa-2 "a")
           false)
        "Fails on single length string.")
    (is (= (nfa-accepts? succ-fsa-3 "a")
           true)
        "Matches on single length string.")))


(def star-fsa-1 (star (sym \a)))
(deftest star-test
  (testing "Testing star FSA."
    (is (= (nfa-accepts? star-fsa-1 "")
           true)
        "Matches null string.")
    (is (= (nfa-accepts? star-fsa-1 "a")
           true)
        "Matches single length string.")
    (is (= (nfa-accepts? star-fsa-1 "aa")
           true)
        "Matches length-2 string.")
    (is (= (nfa-accepts? star-fsa-1 "aaa")
           true)
        "Matches length-3 string.")
    (is (= (nfa-accepts? star-fsa-1 "ab")
           false)
        "Fails on incorrect string.")))


(def plus-fsa-1 (plus (sym \a)))
(deftest plus-test
  (testing "Testing plus FSA."
    (is (= (nfa-accepts? plus-fsa-1 "")
           false)
        "Fails on null string.")
    (is (= (nfa-accepts? plus-fsa-1 "a")
           true)
        "Matches single length string.")
    (is (= (nfa-accepts? plus-fsa-1 "aa")
           true)
        "Matches length-2 string.")
    (is (= (nfa-accepts? plus-fsa-1 "aaa")
           true)
        "Matches length-3 string.")
    (is (= (nfa-accepts? plus-fsa-1 "ab")
           false)
        "Fails on incorrect string.")))


(def opt-fsa-1 (opt (sym \a)))
(deftest opt-test
  (testing "Testing opt FSA."
    (is (= (nfa-accepts? opt-fsa-1 "")
           true)
        "Matches null string.")
    (is (= (nfa-accepts? opt-fsa-1 "a")
           true)
        "Matches on 'a'")
    (is (= (nfa-accepts? opt-fsa-1 "b")
           false)
        "Fails on 'b'")))


(def rng-fsa-1 (rng (sym \a) 0))
(def rng-fsa-2 (rng (sym \a) 4 6))
(def rng-fsa-3 (rng (sym \a) 2 :inf))
(deftest rng-test
  (testing "Testing rng FSA."
    (is (= (nfa-accepts? rng-fsa-1 "")
           true)
        "Matches null string.")
    (is (= (nfa-accepts? rng-fsa-1 "a")
           false)
        "Fails on 'a' string.")
    (is (= (nfa-accepts? rng-fsa-2 "aaaa")
           true)
        "Matches 'aaaa' string.")
    (is (= (nfa-accepts? rng-fsa-2 "aaaaa")
           true)
        "Matches 'aaaaa' string.")
    (is (= (nfa-accepts? rng-fsa-2 "aaaaaa")
           true)
        "Matches 'aaaaaa' string.")
    (is (= (nfa-accepts? rng-fsa-2 "aaa")
           false)
        "Fails on 'aaa' string.")
    (is (= (nfa-accepts? rng-fsa-2 "aaaaaaa")
           false)
        "Fails on 'aaaaaaa' string.")
    (is (= (nfa-accepts? rng-fsa-3 "a")
           false)
        "Fails on 'a' string.")
    (is (= (nfa-accepts? rng-fsa-3 "aa")
           true)
        "Matches 'aa' string.")
    (is (= (nfa-accepts? rng-fsa-3 "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
           true)
        "Matches 'aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa' string.")))


(comment
  (def any-fsa (any))
  (deftest any-test
    (testing "Testing null FSA."
      (is (= (nfa-accepts? any-fsa "")
           false)
          "Fails on null string.")
      (is (= (nfa-accepts? any-fsa "a")
             true)
          "Matches on 'a' string.")
      (is (= (nfa-accepts? any-fsa "b")
             true)
          "Matches on 'b' string."))))


(def complex-fsa-1 (alt (null)
                        (succ (star (sym \a))
                              (sym \b))))
(def complex-fsa-2 (succ (sym \a)
                         (alt (sym \b)
                              (star (sym \c)))
                         (sym \d)))
(def complex-fsa-3 (succ (rng (succ (sym \a)
                                    (sym \b))
                              2 :inf)
                         (opt (alt (sym \c)
                                   (sym \d)))))
(deftest complex-test
  (testing "Testing complex FSAs."
    (is (= (nfa-accepts? complex-fsa-1 "")
           true)
        "Testing 'null|(a*b)' on null string")
    (is (= (nfa-accepts? complex-fsa-1 "b")
           true)
        "Testing 'null|(a*b)' on 'b' string.")
    (is (= (nfa-accepts? complex-fsa-1 "ab")
           true)
        "Testing 'null|(a*b)' on 'ab' string.")
    (is (= (nfa-accepts? complex-fsa-1 "aab")
           true)
        "Testing 'null|(a*b)' on 'aab' string.")
    (is (= (nfa-accepts? complex-fsa-1 "aba")
           false)
        "Testing 'null|(a*b)' on 'aba' string.")
    (is (= (nfa-accepts? complex-fsa-2 "ad")
           true)
        "Testing 'a(b|c*)d' on 'ad' string.")
    (is (= (nfa-accepts? complex-fsa-2 "abd")
           true)
        "Testing 'a(b|c*)d' on 'abd' string.")
    (is (= (nfa-accepts? complex-fsa-2 "acd")
           true)
        "Testing 'a(b|c*)d' on 'acd' string.")
    (is (= (nfa-accepts? complex-fsa-2 "accd")
           true)
        "Testing 'a(b|c*)d' on 'accd' string.")
    (is (= (nfa-accepts? complex-fsa-2 "acccd")
           true)
        "Testing 'a(b|c*)d' on 'acccd' string.")
    (is (= (nfa-accepts? complex-fsa-2 "abcd")
           false)
        "Testing 'a(b|c*)d' on 'abcd' string.")
    (is (= (nfa-accepts? complex-fsa-2 "acbd")
           false)
        "Testing 'a(b|c*)d' on 'acbd' string.")
    (is (= (nfa-accepts? complex-fsa-2 "abbbd")
           false)
        "Testing 'a(b|c*)d' on 'abbbd' string.")
    (is (= (nfa-accepts? complex-fsa-3 "abab")
           true)
        "Testing '(ab){2,}(c|d)?' on 'abab' string.")
    (is (= (nfa-accepts? complex-fsa-3 "abab")
           true)
        "Testing '(ab){2,}(c|d)?' on 'abab' string.")
    (is (= (nfa-accepts? complex-fsa-3 "ababababab")
           true)
        "Testing '(ab){2,}(c|d)?' on 'ababababab' string.")
    (is (= (nfa-accepts? complex-fsa-3 "ababc")
           true)
        "Testing '(ab){2,}(c|d)?' on 'ababc' string.")
    (is (= (nfa-accepts? complex-fsa-3 "ababd")
           true)
        "Testing '(ab){2,}(c|d)?' on 'ababd' string.")
    (is (= (nfa-accepts? complex-fsa-3 "ab")
           false)
        "Testing '(ab){2,}(c|d)?' on 'ab' string.")
    (is (= (nfa-accepts? complex-fsa-3 "aba")
           false)
        "Testing '(ab){2,}(c|d)?' on 'aba' string.")
    (is (= (nfa-accepts? complex-fsa-3 "ababa")
           false)
        "Testing '(ab){2,}(c|d)?' on 'ababa' string.")
    (is (= (nfa-accepts? complex-fsa-3 "abababcd")
           false)
        "Testing '(ab){2,}(c|d)?' on 'abababcd' string.")))