(ns wave-dsl.views
  (:require
   [re-frame.core :as re-frame]
   [wave-dsl.subs :as subs]
   [wave-dsl.events :as events]))

(defn main-panel []
  (let [output (re-frame/subscribe [::subs/output])
        style {:font-family "et-book, Palatino, \"Palatino Linotype\", \"Palatino LT STD\", \"Book Antiqua\", Georgia, serif"
               :font-size "14px"}]
    [:article
     [:h1 "Wave dsl"]
     [:p "Custom dsl for " [:a {:href "https://github.com/lucdoebereiner/segmod"} "segmod"]]
     [:p "More information about usage can be found here: " [:a {:href "https://github.com/kfirmanty/wave-dsl/blob/master/README.md"} "README"]]
     [:section
      [:h2 "Program"]
      [:textarea {:cols 40 :rows 5
                  :style style
                  :on-change #(re-frame/dispatch [::events/update-input (-> % .-target .-value)])}]
      [:br]
      [:button {:on-click #(re-frame/dispatch [::events/parse-input])} [:b "CONVERT"]]
      [:br]
      [:h2 "Output"]
      [:textarea {:cols 40 :rows 5
                  :style style
                  :value (clojure.string/join " " @output)}]
      [:h2 "Example programs:"]
      [:div
       [:pre
        [:code "#repeat-1-only-once
<si 1>"]]]
      [:div
       [:pre
        [:code "#grain-1-and-grain-2-sequence-repeated-twice
[<g 1> <g 2>]*2"]]]
      [:div
       [:pre
        [:code "#nested-sequences-example
[[<si 1> <si 2>]*4 <si 3> <si 4>]*2"]]]]]))
