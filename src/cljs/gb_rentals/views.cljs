(ns gb-rentals.views
  (:require [reagent.core :as reagent]
            [re-frame.core :as rf]
            [gb-rentals.subs :as subs]))

(defn game-preview
  [{:keys [name deck image guid]}]
  (let [rentals @(rf/subscribe [::subs/rented-games])
        image-url (:superUrl image)
        rented? (fn [guid]
                  (-> (keys rentals)
                      (set)
                      (contains? guid)))]
    [:div.game-container 
     [:div.game-header 
      [:img {:src image-url :alt "Game Image"}]
      [:button.btn.btn-primary.btn-sm.pull-xs-right {:on-click #(rf/dispatch [:toggle-rental guid])}
       (if (rented? guid) "Return" "Rent")]]
     [:div.game-description 
      [:h1 name]
      [:p deck]]]))

(defn games-list [games]
  [:div
   (for [game games]
     ^{:key (:guid game)} [game-preview game])])

(defn search-results []
  (let [results @(rf/subscribe [::subs/search-results])]
    [:div.search-results
     [:div.container
      [:div.row
       [:div.col-xs-12.col-md-10.offset-md-1
        [games-list results]]]]]))

(defn search []
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
    :search-results [search-results]
    [search]))

;; TODO: Probably add a header and footer for this
(defn main-panel []
  (let [active-page @(rf/subscribe [::subs/active-page])]
    [:div
     [pages active-page]]))
