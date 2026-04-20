
(defn hello [name] 
  (str "Hello, " name))


;; different arity for a function

(defn greeting
 "this a documentation"
 ([] (greeting "world"))
 ([username] (str "Hello, " username))
 )

(defn date [person-1 person-2 & chaperones]
 (println person-1 "and" person-2
  "went out with" (count chaperones) "chaperones."))

(use '[clojure.string :as str :only (join split)])

(defn indexable-word? [word]
 (> (count word) 2) )

(defn my-filter [phrase]
 (filter indexable-word? (str/split phrase #"\W+")))


;;(filter (fn [w] (> (count w) 2)) (str/split "hello to you" #"\W"))

;;(filter #(> (count %) 2 ) (str/split "bonjour hola h j to you" #"\W"))

(defn indexable-words [text]
  (let [indexable-word? (fn [w] (> (count w) 2))]
    (filter indexable-word? (str/split text #"\W+")))
  )


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
    [[bottom left] [top left] [top right] [bottom right]])
  )

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
    (> number 500 ) "medium"
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
  (for [ val ["a" "b" "c"] ] val ))  

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
