(ns wave-dsl.parser
  (:require [instaparse.core :as insta]))

(def seed (atom 1))

(defn rint
  "Taken from stack overflow, unsafe, but good enough for dsl"
  [upper]
  (let [x (-> @seed Math/sin (* 10000))]
    (swap! seed inc)
    (Math/floor (* upper (- x (Math/floor x))))))

(def parser
  (insta/parser
   "
<PROGRAM> = <WS>* (CELL | REPETITION | <COMMENT>) (<WS>? (CELL | REPETITION | <COMMENT>))* <WS>*
COMMENT = <'#'> #'[A-Za-z0-9- ]+' <#'\\n+'>
REPETITION = <'['> (CELL | REPETITION) (<WS> (CELL | REPETITION))* <']*'>REPS
REPS = #'[0-9]+'
CELL = <'<'> QUALIFIER (<WS> FREQ)+ <'>'>
<FREQ> = #'[0-9]+\\.?[0-9]*'
<QUALIFIER> = 'single' | 'grain' | 'short' |  'medium' | 'long' | 'si' | 'g' | 's' | 'm' | 'l'
WS=#'\\s+' | #'\\n+'"))

(defn apply-qualifier [qualifier val] ;;TODO: version which takes into account the frequency of repeated val - for example lower frequencies would need less repetitions for long qualifier
  (let [n (case qualifier
            :single 1
            :grain (inc (rint 5))
            :short (+ 5 (rint 40))
            :medium (+ 45 (rint 200))
            :long (+ 250 (rint 1000)))]
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

(defn parse
  ([program]
   (parse program 1))
  ([program seed-val]
   (reset! seed seed-val)
   (let [parsed (parser program)]
     (flatten (resolve-parsed parsed)))))
