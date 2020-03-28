(ns wave-dsl.subs
  (:require
   [re-frame.core :as re-frame]
   [wave-dsl.db :as db]))

(re-frame/reg-sub
 ::name
 (fn [db]
   (:name db)))

(re-frame/reg-sub
 ::output
 (fn [db]
   (db/parser-output db)))
