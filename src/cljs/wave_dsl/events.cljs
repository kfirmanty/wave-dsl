(ns wave-dsl.events
  (:require
   [re-frame.core :as re-frame]
   [wave-dsl.db :as db]
   [wave-dsl.parser :as parser]))

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

(re-frame/reg-event-db
 ::parse-input
 (fn [db _]
   (let [program (db/input db)]
     (db/set-parser-output db (parser/parse program (rand-int 10000))))))

(re-frame/reg-event-db
 ::update-input
 (fn [db [_ input]]
   (db/set-input db input)))
