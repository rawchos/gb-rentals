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

(defn current-rentals []
  (let [rented-games @(rf/subscribe [::subs/rented-games])]
    [:div.current-rentals
     [:div.container
      [:div.row
       [:div.col-xs-12.col-md-10.offset-md-1
        [games-list (flatten (vals rented-games))]]]]]))

(defn search []
  (let [current-search @(rf/subscribe [::subs/search-text])
        text-input (reagent/atom {:search-text current-search})]
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

(defn header []
  (let [active-page @(rf/subscribe [::subs/active-page])
        search-results @(rf/subscribe [::subs/search-results])
        rented-games @(rf/subscribe [::subs/rented-games])]
    [:nav.navbar.navbar-light
     [:div.container
      [:ul.nav.navbar-nav.pull-xs-right
       [:li.nav-item
        [:a.nav-link {:on-click #(rf/dispatch [:set-active-page :search])
                      :class (when (= active-page :search) "active")} "Search"]]
       (when (seq search-results)
         [:li.nav-item
          [:a.nav-link {:on-click #(rf/dispatch [:set-active-page :search-results])
                        :class (when (= active-page :search-results) "active")} "Search Results"]])
       (when (seq rented-games)
         [:li.nav-item
          [:a.nav-link {:on-click #(rf/dispatch [:set-active-page :current-rentals])
                        :class (when (= active-page :current-rentals) "active")} "Current Rentals"]])]]]))

(defn pages [page-name]
  (case page-name
    :search [search]
    :search-results [search-results]
    :current-rentals [current-rentals]
    [search]))

(defn main-panel []
  (let [active-page @(rf/subscribe [::subs/active-page])]
    [:div
     [header]
     [pages active-page]]))
