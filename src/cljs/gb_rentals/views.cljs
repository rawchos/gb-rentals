(ns gb-rentals.views
  (:require [reagent.core :as reagent]
            [re-frame.core :as rf]
            [gb-rentals.subs :as subs]))

(defn search
  []
  (let [text-input (reagent/atom {:search-text ""})]
    (fn []
      (let [{:keys [search-text]} @text-input
            search-games (fn [event search-text]
                           (.preventDefault event)
                           (rf/dispatch [:search-games search-text]))]
        [:div.container.page
         [:div.row
          [:div.col-md-6.offset-md-3.col-xs-12
           [:form {:on-submit #(search-games % (:search-text @text-input))}
            [:fieldset.form-group
             [:input.form-control.form-control-lg {:type        "text"
                                                   :placeholder "Games"
                                                   :value       search-text
                                                   :on-change   #(swap! text-input assoc :search-text (-> % .-target .-value))
                                                   :disabled    false}]]
            [:button.btn.btn-lg.btn-primary.pull-xs-right "Search"]]]]]))))

(defn pages [page-name]
  (case page-name
    :search [search]
    [search]))

;; TODO: Probably add a header and footer for this
(defn main-panel []
  (let [active-page @(rf/subscribe [::subs/active-page])]
    [:div
     [pages active-page]]))
