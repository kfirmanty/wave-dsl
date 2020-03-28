(ns wave-dsl.db)

(def default-db
  {:name "re-frame"})

(defn set-parser-output [db parsed]
  (assoc db ::parsed parsed))

(defn parser-output [db]
  (::parsed db))

(defn set-input [db input]
  (assoc db ::input input))

(defn input [db]
  (::input db))
