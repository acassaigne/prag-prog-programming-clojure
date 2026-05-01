
(defn hello [name]
  (str "Hello, " name))

;; different arity for a function

(defn greeting
  "this a documentation"
  ([] (greeting "world"))
  ([username] (str "Hello, " username)))

(defn date [person-1 person-2 & chaperones]
  (println person-1 "and" person-2
           "went out with" (count chaperones) "chaperones."))

(use '[clojure.string :as str :only (join split)])

(defn indexable-word? [word]
  (> (count word) 2))

(defn my-filter [phrase]
  (filter indexable-word? (str/split phrase #"\W+")))

;;(filter (fn [w] (> (count w) 2)) (str/split "hello to you" #"\W"))

;;(filter #(> (count %) 2 ) (str/split "bonjour hola h j to you" #"\W"))

(defn indexable-words [text]
  (let [indexable-word? (fn [w] (> (count w) 2))]
    (filter indexable-word? (str/split text #"\W+"))))

;; create dynamicalyy a function
(defn make-greeter [greeting-prefix]
  (fn [username] (str greeting-prefix ", " username)))

((make-greeter "Hello") "you")

(def hello-greeting (make-greeter "Hello"))

 ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
 ;; LET and Binding             ;;
 ;; (let [bindings*] exprs*)    ;;
 ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn square-corners [top left size]
  (let [right (+ left size)
        bottom (+ top size)]
    [[bottom left] [top left] [top right] [bottom right]]))

;; destructuring

(defn greet-author [author]
  (println "Hello, " (:firstname author)))

(defn greet-author-2 [{firstname :firstname}]
  (println "Hello, " firstname))

;; destructuring multiples keys

(defn greet-author-3 [{:keys [firstname lastname]}]
  (println "Hello, " firstname lastname))

;; example let destructuring

(let [[x y] [1 2 3]] [x y])

;; discard when destructuring

(let [[_ _ z] [1 2 3]] z)

;; destructuring less than present values

(let [[x y :as coords] [1 2 3 4 5 6]]
  (str "value of x=" x " value of y=" y " just 2 values of " (count coords) " counted"))

(require '[clojure.string :as str])

(defn ellipsize [words]
  (let [[w1 w2 w3] (str/split words #"s+")]
    (str/join " " [w1 w2 w3 "..."])))

(ellipsize "Hello you trying ellipsize")

;;(println "Success")

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; namespace & import java ;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(import [java.io InputStream File])

(.exists (File. "/tmp"))

;; (ns myapp                               ;;
;;   (:import [java.io InputStream File])) ;;

;; for using clojure split function inside clojure.string
(require '[clojure.string :refer [split]])
(split "My first sentence" #"\s+")

;; if you want get all function with a prefix
(require '[clojure.string :as str])

;; declare namespace with import and require
;; (ns examples.exploring
;;   (:require [clojure.string :as str])
;;   (:import [java.io File]))

;;;;;;;;;;;;;;;;;;
;; calling Java ;;
;;;;;;;;;;;;;;;;;;

(new java.util.Random)
(java.util.Random.) ;; shortcut for (new...)

(def rnd (new java.util.Random))

;; use the qualified constructor new
(map java.io.File/new ["a.txt" "b.txt"])

;; call method here on rnd def
(.nextInt rnd 10)
;; same thing with field

;; call static method
;; (Class/method & args)
(System/lineSeparator)

;;;;;;;;;;;;;;;;;;
;; Flow Control ;;
;;;;;;;;;;;;;;;;;;

(defn is-small [number]
  (if (< number 5) "yes" "no"))

;; cond multiples conditions

(defn is-small-2 [number]
  (cond
    (> number 1000) "big"
    (> number 500) "medium"
    :else "small"))

;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Introduce Side Effect ;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn is-small-side-effect [number]
  (if (< number 5) "small"
      (do (println "Saw a big number")
          "big")))

;;;;;;;;;;;;;;;;;;;
;; curr and loop ;;
;;;;;;;;;;;;;;;;;;;

(loop [result [], x 5]
  (if (zero? x)
    result
    (recur (conj result x) (dec x))))

(defn countdown [result x]
  (if (zero? x)
    result
    (recur (conj result x) (dec x))))

(defn countdown-2 [n]
  (loop [result [] x n]
    (if (zero? x)
      result
      (recur (conj result x) (dec x)))))

;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Where's My for Loop? ;;
;;;;;;;;;;;;;;;;;;;;;;;;;;

;;     public static int indexOfAny(String str, char[] searchChars) {
;;         if (isEmpty(str) || ArrayUtils.isEmpty(searchChars)) {
;;             return -1;
;;         }
;;         for (int i = 0; i < str.length(); i++) {
;;             char ch = str.charAt(i);
;;             for (int j = 0; j < searchChars.length; j++) {
;;                 if (searchChars[j] == ch) {
;;                     return i;
;;                 }
;;             }
;;         }
;;         return -1;
;;     }
;; rewrite it in clojure

;; get index and value from vector with `map-indexed`

(map-indexed vector "Hello")
;; return ([0 \H] [1 \e] [2 \l] [3 \l] [4 \o])

(defn try-loop []
  (for [val ["a" "b" "c"]] val))

;; comprehension loop with filtering on character a
;;  (for [letter ["a" "b" "a" "b" "z"] :when (= letter "a") ] letter)

(defn indexed [coll] (map-indexed vector coll))

(defn index-filter [search-chars coll]
  (for [[idx val] (indexed coll) :when (search-chars val)] idx))

(defn index-of-any [search-set-chars coll]
  (first (index-filter search-set-chars coll)))

;;;;;;;;;;;;;;;;
;; nampespace ;;
;;;;;;;;;;;;;;;;

;; (require 'my-app) ;; load the namespace
;;(in-ns my-app) ;; change the namespace

;; add lib and add it to classpath
;; (add-lib 'org.clojure/core.cache {:mvn/version "1.1.234"})

(add-lib 'org.clojure/core.cache)

;; if you want add a new dependecy to your project, the right way to do that is
;; {:deps {org.clojure/core.cache {:mvn/version "1.1.234"}}}

;; (sync-deps)
;; in this case the deps.edn file has been updated

;; after add a dependency this last one can be use with require

;; (require '[clojure.core.cache :as cache])

;; At the heart of every Clojure program is your
;; domain, represented as data, and a set of
;; functions that manipulate that data.

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; CHAPITRE 4 : Unifying Data with Sequences ;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; sequence is a logical consideration about vector, list, set and so on... it's not rely on specific implementation

;; function working on seq

(first [1 2 3])

(rest [1 2 3])

(cons 1 [2 3])

(seq "abc")

(seq [1 2 3])

(seq ()) ;; return nil
(seq []) ;; return nil

;; we can consider map as seq too
(first {:fname "Aaron" :lname "Bedra"})
(rest {:fname "Aaron" :lname "Bedra"})
(cons [:mname "James"] {:fname "Aaron" :lname "Bedra"})

(def my-map {:c "c" :b "b" :a "a"})

(into (sorted-map) my-map)
(sorted-map :b "b" :a "a")

;;;;;;;;;;;;;;;;;;;;;
;; Create sequence ;;
;;;;;;;;;;;;;;;;;;;;;

(def end-range 10)
(def start-range 1)

(range end-range)

(range start-range end-range)
(range start-range end-range 2)

(repeat 5 \a)

;; (iterate my-function start-value)

(take 10 (iterate inc 1))

(take 10 (cycle ["a" "b"]))

(interleave [1 2 3] ["a" "b" "c" "d"])

(def my-separator ",")
(interpose my-separator ["apples" "bananas" "grapes"])

(apply str (interpose my-separator ["apples" "bananas" "grapes"]))

;; equivalent to join
(require '[clojure.string :refer [join]])

(join \, ["apples" "bananas" "grapes"])

;; exist function to create vec, set, hash-set

(set [1 2 3])
(hash-set 1 2 3)
(vec (range 5))

(def whole-numbers (iterate inc 1))

(take 5 whole-numbers)

;;;;;;;;;;;;;;;;;;;;;;;;;
;; filtering Sequences ;;
;;;;;;;;;;;;;;;;;;;;;;;;;

;; (filter pred coll)

(defn multiple-three [n]
  (= (mod n 3) 0))

(filter multiple-three [0 1 2 3 4 5 6])

(def vowel? #{\a \i \e \o \u \y})
(take-while vowel? "eeollo")

(drop-while vowel? "eeollo")

(split-at 4 (range 6))
(split-at 4 [1 2 3 4 5 6])

(split-with #(<= % 8) [1 2 3 4 5 6 7 8 9 10])

;;;;;;;;;;;;;;;;;;;;;;;;;
;; Sequence Predicates ;;
;;;;;;;;;;;;;;;;;;;;;;;;;

(defn is-a [letter]
  (= \a letter))

(every? is-a "bb")
(every? is-a "aaa")

(some is-a [\g \h \a \b])
(some is-a [\g \h \b])

;; some is not a predicat, combine with identity it's return the first true

(some identity [nil false 4 "a"])
(some identity [nil false 0 4 "a"])
(not-every? is-a "ha bon?")
(not-every? is-a "aaa")

(not-any? is-a "Ha bon?")
(not-any? is-a "Hello")

;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Transforming sequence ;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;

(map #(format "<%s>" %) ["une" "valeur" "entre" "diamant" "signes"])

;; map with multiples collections
(map #(format "%s - %s" %1 %2) ["a" "b" "c"] ["one" "two" "three"])

;; (reduce f val? coll)
(reduce + 0 (range 1 11))
(sort [42 1 7 11])
(sort > [42 1 7 11])

(sort-by str [42 1 7 11])
(sort-by :grade > [{:grade 83} {:grade 90} {:grade 77}])

;; for list comprehension

;; (for [binding-form coll-expr filter-expr? ...] expr)

(for [word ["on" "two" "three"]] (format "<%s>" word))

(for [word ["on" "two" "three"] number [1 2 3]] (format "%d - %s" number word))

(for [letter "ha ah bon he beee vas-y" :when (is-a letter)] letter)
(for [letter "aaa b aa" :while (is-a letter)] letter)

;;;;;;;;;;;;;;;;;;;;;;;;;
;; Threadind functions ;;
;;;;;;;;;;;;;;;;;;;;;;;;;

(reduce + (map #(* % %) (filter odd? (range 100))))

(->> (range 100) (filter odd?) (map #(* % %)) (reduce +))

(->> '("one" "two" "three")
     (map #(format "<%s>" %1))
     (map #(format "%d - %s" %1 %2) [1 2 3]))

(defn format-with-number [[n s]]
  (format "%d - %s" n s))

(->> '("one" "two" "three")
     (map #(format "<%s>" %1))
     (map-indexed vector)
     (map format-with-number))

(defn my-test []
  (def x "test")
  (println x))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; ;; TODO project fonction sur les map set ;;
;; ;; page 87 to 95                         ;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Fibonacci
;; F(0) = 0
;; F(1) = 1
;; F(n) = F(n-1) + F(n-2)

(defn stack-consuming-fibo [n]
  (cond
    (= n 0) 0
    (= n 1) 1
    :else (+ (stack-consuming-fibo (- n 1))
             (stack-consuming-fibo (- n 2)))))

(defn tail-fibo [n]
  (letfn [(fib
            [current next n]
            (if (zero? n)
              current
              (fib next (+ current next) (dec n))))]
    (fib 0N 1N n)))

(defn recur-fibo [n]
  (letfn [(fib
            [current next n]
            (if (zero? n)
              current
              (recur next (+ current next) (dec n))))]
    (fib 0N 1N n)))

(defn lazy-seq-fibo
  ([] (concat [0 1] (lazy-seq-fibo 0N 1N)))
  ([a b]
   (let [n (+ a b)]
     (lazy-seq (cons n (lazy-seq-fibo b n))))))

;;(take 10 (lazy-seq-fibo))

;; limit repl
;; (set! *print-length* 10)

;;;;;;;;;;;;;;;;;;;;;;
;; Lazier Than Lazy ;;
;;;;;;;;;;;;;;;;;;;;;;

;; WIP pg 113

;; problem pair of :h :h toss head
;; [:h :h :h :t :h] => answerd 2
;; [:h :t :h :t :h] => answerd 0

(defn count-heads-pairs [coll]
  (loop [cnt 0 coll coll]
    (if (empty? coll)
      cnt
      (recur (if (= :h (first coll) (second coll))
               (inc cnt)
               cnt) (rest coll)))))

(defn inc-when-matching-heads [cnt coll]
  (if (= :h (first coll) (second coll))
    (inc cnt)
    cnt))

(defn count-heads-pairs-2 [coll]
  (loop [cnt 0 coll coll]
    (if (empty? coll)
      cnt
      (recur (inc-when-matching-heads cnt coll) (rest coll)))))

(defn is-head-pair? [pair]
  (= :h (first pair) (second pair)))

(defn count-heads-pairs-3 [coll]
  (->> (partition 2 1 coll) (filter is-head-pair?) (count)))



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; ;; other trampoline for combine recursion A call B and B call A ;;
;; ;; optimize recursion with Memoization                          ;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; ;; earger transformation ;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


;; Transducer optimize
