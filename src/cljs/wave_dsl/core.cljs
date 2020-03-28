(ns wave-dsl.core
  (:require
   [reagent.core :as reagent]
   [re-frame.core :as re-frame]
   [wave-dsl.events :as events]
   [wave-dsl.views :as views]
   [wave-dsl.config :as config]
   ))


(defn dev-setup []
  (when config/debug?
    (println "dev mode")))

(defn ^:dev/after-load mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn init []
  (re-frame/dispatch-sync [::events/initialize-db])
  (dev-setup)
  (mount-root))
