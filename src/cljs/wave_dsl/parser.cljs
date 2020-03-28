(ns wave-dsl.parser
  (:require [instaparse.core :as insta]))

(def parser
  (insta/parser
   "
<PROGRAM> = (CELL | REPETITION | <COMMENT>) (<WS>? (CELL | REPETITION | <COMMENT>))* <WS>*
COMMENT = <'#'> #'[A-Za-z0-9-]+' <#'\\n+'>
REPETITION = <'['> (CELL | REPETITION) (<WS> (CELL | REPETITION))* <']*'>REPS
REPS = #'[0-9]+'
CELL = <'<'> QUALIFIER (<WS> FREQ)+ <'>'>
<FREQ> = #'[0-9]+\\.?[0-9]*'
<QUALIFIER> = 'single' | 'grain' | 'short' |  'medium' | 'long' | 'si' | 'g' | 's' | 'm' | 'l'
WS=#'\\s+' | #'\\n+'"))

(defn apply-qualifier [qualifier val] ;;TODO: version which takes into account the frequency of repeated val - for example lower frequencies would need less repetitions for long qualifier
  (let [n (case qualifier
            :single 1
            :grain (inc (rand-int 5))
            :short (+ 5 (rand-int 40))
            :medium (+ 45 (rand-int 200))
            :long (+ 250 (rand-int 1000)))]
    (repeat n val)))

(defn ->qualifier [q]
  (case q
    ("single" "si") :single
    ("grain" "g") :grain
    ("short" "s") :short
    ("medium" "m") :medium
    ("long" "l") :long))

(defn resolve-cell [[qualifier freq]]
  (apply-qualifier (->qualifier qualifier) freq))

(defn resolve-repetition [cells]
  (let [[_ reps] (last cells)
        cells (drop-last cells)
        cells (map (fn [[type & r]]
                     (if (= type :REPETITION)
                       (resolve-repetition r)
                       (resolve-cell r))) cells)]
    (repeat reps cells)))

(defn resolve-parsed [parsed]
  (map (fn [[type & r]]
                     (if (= type :REPETITION)
                       (resolve-repetition r)
                       (resolve-cell r))) parsed))

(defn parse [program]
  (let [parsed (parser program)]
    (flatten (resolve-parsed parsed))))
