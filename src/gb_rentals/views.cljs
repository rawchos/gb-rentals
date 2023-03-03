(ns gb-rentals.views
  (:require [reagent.core :as reagent]
            [re-frame.core :as rf]
            [gb-rentals.subs :as subs]))

(defn api-key
  []
  (let [text-input (reagent/atom {:api-key ""})]
    (fn []
      (let [{:keys [api-key]} @text-input
            add-api-key (fn [event api-key]
                          (.preventDefault event)
                          (rf/dispatch [:add-api-key api-key]))]
        [:div.container.page
         [:div.row
          [:div.col-md-6.offset-md-3.col-xs-12
           [:form {:on-submit #(add-api-key % (:api-key @text-input))}
            [:fieldset.form-group
             [:input.form-control.form-control-lg {:type        "text"
                                                   :placeholder "Giant Bomb API Key"
                                                   :value       api-key
                                                   :on-change   #(swap! text-input assoc :api-key (-> % .-target .-value))
                                                   :disabled    false}]]
            [:button.btn.btn-lg.btn-primary.pull-xs-right "Add API Key"]]]]]))))

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
    :api-key [api-key]
    :search [search]
    [api-key]))

;; TODO: Probably add a header and footer for this
(defn main-panel []
  (let [active-page @(rf/subscribe [::subs/active-page])]
    [:div
     [pages active-page]]))
