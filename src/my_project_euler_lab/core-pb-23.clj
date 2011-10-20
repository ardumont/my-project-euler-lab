(ns my-project-euler-lab.core-pb-23
  (:use [clojure.test               :only [run-tests]])
  (:use [midje.sweet])
  (:use [clojure.contrib.repl-utils :only [show]])
  (:use [clojure.set :only [difference]] )
  (:use clojure.contrib.math)
  (:use [my-project-euler-lab.primes :only [all-divisors-bl]] )
  )

;A perfect number is a number for which the sum of its proper divisors
;is exactly equal to the number. 

;For example, the sum of the proper divisors of 28 would be 1 + 2 + 4 + 7 + 14 = 28, which means that 28 is a perfect number.
;
;A number n is called deficient if the sum of its proper divisors is less than n 
;and it is called abundant if this sum exceeds n.

;As 12 is the smallest abundant number, 1 + 2 + 3 + 4 + 6 = 16, the
;smallest number that can be written as the sum of two abundant
;numbers is 24. By mathematical analysis, it can be shown that all
;integers greater than 28123 can be written as the sum of two abundant
;numbers.

;However, this upper limit cannot be reduced any further by analysis even though it is known that the greatest number 
;that cannot be expressed as the sum of two abundant numbers is less than this limit.

;Find the sum of all the positive integers which cannot be written as the sum of two abundant numbers.

(unfinished )

(defn all-dec-in-sums "Find all the sums possibles that gives n with 2 integers"
  [n]
  (let [half-n (ceil (/ n 2))
        integers-s (range 1 (inc half-n))]
    (reduce conj #{}
            (map
             #(vec [% (- n %)])
             integers-s))))

(fact
  (all-dec-in-sums 10) => #{[1 9] [2 8] [3 7] [4 6] [5 5]}
  (all-dec-in-sums 11) => #{[6 5] [5 6] [4 7] [3 8] [2 9] [1 10]}
  )

(defn sum "Compute the sum of the vector of integers"
  [v]
  (reduce + v))

(fact
  (sum [1 2 4]) => 7)

(defn what-is? "Compute the divisors of a numbers and generate a matrix of result to dispatch its qualities (perfect, abundant, deficient)"
  [n]
  (let [sum-all-divisors (sum (all-divisors-bl n))]
    (cond
     (= n sum-all-divisors) {:perfect true :abundant false :deficient false}
     (< n sum-all-divisors) {:perfect false :abundant true :deficient false}
     (> n sum-all-divisors) {:perfect false :abundant false :deficient true}
     )
    )
  )

(fact
  (what-is? 10) => {:perfect true :abundant false :deficient false}
  (provided
    (all-divisors-bl 10) => [1 2 4]
    (sum [1 2 4]) => 10)
  ; it test
  (what-is? 10) => {:perfect false :abundant false :deficient true}
  (what-is? 28) => {:perfect true :abundant false :deficient false}
  (what-is? 120) => {:perfect false :abundant true :deficient false}
  )

(defn deficient? "Test if a mumber is deficient (sum of all its divisors inferior to itself)"
  [n]
  (let [result (what-is? n)]
    (= true (result :deficient) )
    )
  )

(fact
  (deficient? 10) => true
  (provided
    (what-is? 10) => {:deficient true})
  (deficient? 20) => false
  (provided
    (what-is? 20) => {:deficient false})
  ; it test
  (deficient? 10) => true
  (deficient? 28) => false
  )

(defn perfect? "Test if a mumber is a perfect number"
  [n]
  (let [result (what-is? n)]
    (= true (result :perfect) )
    )
  )

(fact
  (perfect? 10) => true
  (provided
    (what-is? 10) => {:perfect true})
  (perfect? 20) => false
  (provided
    (what-is? 20) => {:perfect false})
  ; it test
  (perfect? 10) => false
  (perfect? 28) => true
  )

(defn abundant? "Test if a number is abundant (sums of its divisors is superior to itself)"
  [n]
  (let [result (what-is? n)]
    (= true (result :abundant) )
    )
  )

(fact
  (abundant? 10) => true
  (provided
    (what-is? 10) => {:abundant true})
  (abundant? 20) => false
  (provided
    (what-is? 20) => {:abundant false})
  ; it test
  (abundant? 10) => false
  (abundant? 120) => true
  )

(defn sum-two-abundant? "Can the number be a sum of 2 abundants numbers?"
  [n]
  (let [all-sums (all-dec-in-sums n)]
    (some (fn [e] (= true e))
          (map #(and (abundant? (first %)) (abundant? (second %))) all-sums))))

(fact
  (sum-two-abundant? 10) => falsey
  (provided
    (all-dec-in-sums 10) => [[5 5] [9 1]]
    (abundant? 5) => false
    (abundant? 9) => false)
  )

(fact
  (sum-two-abundant? 10) => true
  (provided
    (all-dec-in-sums 10) => [[5 5] [9 1]]
    (abundant? 5) => true
    (abundant? 9) => false)
  )

; it test
(fact
    (sum-two-abundant? 240) => true)

(defn integers-numbers "Compute the set of integers from 1 to n"
  [n]
    {:pre [(pos? n)]}
    (set (range 1 (inc n))))

(fact
  (integers-numbers 5) => #{1 2 3 4 5}
  (integers-numbers -1) => (throws AssertionError)
  )

(defn abuntant-numbers
  [n]
  {:pre [(pos? n)]}
  (let [ints (integers-numbers n)]
    (set (filter #(not= nil %) (map (fn [num] (if (sum-two-abundant? num) num)) ints)))))

(fact
  (abuntant-numbers 50) => #{12 24}
  (provided
    (integers-numbers 50) => #{12 24 48}
    (sum-two-abundant? 12) => true
    (sum-two-abundant? 24) => true
    (sum-two-abundant? 48) => false
    )
  (abuntant-numbers -1) => (throws AssertionError)
  )

; it test
;.;. The reward of a thing well done is to have done it. -- Emerson
(fact (abuntant-numbers 50) => #{32 36 38 40 42 44 48 50 24 30})

(defn sum-all-positive-integer-euler-23 "Compute the sum of all positive integer which cannot be written as the sum of two abundant numbers in the interval 12 - n"
  [n]
  (reduce + (clojure.set/difference (integers-numbers n) (abuntant-numbers n)))
  )

(fact "sum of all positive integers which cannot be written as the sum of two abundant numbers."
  (sum-all-positive-integer-euler-23 28123) => 3
  (provided
    (integers-numbers 28123) => #{1 2 3}
    (abuntant-numbers 28123) => #{3}
    )
  (sum-all-positive-integer-euler-23 -1) => (throws AssertionError)
  )

